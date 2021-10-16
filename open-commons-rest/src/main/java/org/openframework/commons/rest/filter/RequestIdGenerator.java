package org.openframework.commons.rest.filter;

import java.util.concurrent.atomic.AtomicLong;

public class RequestIdGenerator {

    private AtomicLong counter;

    private String prefix;

    public RequestIdGenerator() {
        counter = new AtomicLong( 0L );
        prefix = "";
    }

    public RequestIdGenerator( String prefix ) {
        this();
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix( String prefix ) {
        this.prefix = prefix;
    }

    public void resetCounter() {
        counter.set( 0 );
    }


    public String generate() {
        return new StringBuffer().append( prefix ).append( counter.incrementAndGet() ).toString();
    }

}
