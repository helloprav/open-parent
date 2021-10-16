package org.openframework.commons.rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class TracerFilter implements Filter {

    public static final String TRACER_REQUEST = "request";
    public static final String TRACER_SESSION = "session";
    public static final String TRACER_CLIENT = "client";
    public static final String TRACER_USER = "userId";
    public static final String TRACER_ORGANIZATION = "organization";
    

	protected static Logger logger = LoggerFactory.getLogger(TracerFilter.class);

    private RequestIdGenerator generator;

    public void init( FilterConfig filterConfig ) throws ServletException {
        generator = new RequestIdGenerator( "tx" );
    }

    public void doFilter( ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain ) throws IOException, ServletException {

        final long start = System.currentTimeMillis();

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        MDC.put( TRACER_REQUEST, getTracerRequest( request ) );
        MDC.put( TRACER_CLIENT, getTracerAddress( request ) );
        MDC.put( TRACER_SESSION, getTracerSession( request ) );
        MDC.put( TRACER_USER, getTracerUser( request ) );
        MDC.put( TRACER_ORGANIZATION, getTracerUser( request ) );

        try {
            filterChain.doFilter( servletRequest, servletResponse );
        } finally {
            traceTime( start, servletRequest );
            MDC.remove( TRACER_REQUEST );
            MDC.remove( TRACER_CLIENT );
            MDC.remove( TRACER_SESSION );
            MDC.remove( TRACER_USER );
            MDC.remove( TRACER_ORGANIZATION );
        }
    }

    protected void traceTime( final long start, final ServletRequest servletRequest ) {
    }

    protected String getTracerUser( final HttpServletRequest request ) {
        return null;
    }


    protected String getTracerRequest( HttpServletRequest request ) {
        return generator.generate();
    }

    protected String getTracerAddress( HttpServletRequest request ) {
        final String xforwardedForHeader = request.getHeader( "X-Forwarded-For" );
        return xforwardedForHeader != null ? xforwardedForHeader : request.getRemoteAddr();
	}

	protected String getTracerSession(HttpServletRequest request) {
		String id = null;

		if (request.getSession() != null)
			id = request.getSession().getId();
		return id;
	}
    
    public void destroy() {
    }

    public void setGenerator( final RequestIdGenerator generator ) {
        this.generator = generator;
    }

}
