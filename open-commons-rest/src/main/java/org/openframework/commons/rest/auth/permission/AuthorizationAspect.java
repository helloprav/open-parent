package org.openframework.commons.rest.auth.permission;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openframework.commons.domain.exceptions.PermissionDeniedException;
import org.openframework.commons.rest.interceptor.AbstractSecurityInterceptor;
import org.openframework.commons.rest.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * https://marcin-chwedczuk.github.io/overview-of-spring-annotation-driven-aop
 * @author Java Developer
 *
 */
@Aspect
@Component
public class AuthorizationAspect {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Pointcut("execution(@org.openframework.commons.rest.auth.permission.SecuredPermissions * *.*(..))")
	protected void useAdviceOnThisMethodAnnotation() { }

	@Around("useAdviceOnThisMethodAnnotation() && @annotation(securedPermissions)")
	public Object manageAnnotation(ProceedingJoinPoint pjp, SecuredPermissions securedPermissions) throws Throwable {

        final Object details = AbstractSecurityInterceptor.getUserProfile();
        if ( !( details instanceof UserVO ) ) {
            return pjp.proceed();
        }

        final List<String> permissions = Arrays.asList( securedPermissions.value() );
        logger.trace( "Permissions: [{}]", permissions );

        final List<String> userAccesList = AbstractSecurityInterceptor.getUserAccess();
        logger.trace( "Roles: [{}]", userAccesList );

        final UserVO profile = ( UserVO ) details;
        logger.trace( "Profile: [{}]", profile.getEmail() );

        // Support for resource access from permission/function names
        for ( String permission : permissions ) {
            if ( userAccesList.contains(permission ) ) {
                logger.trace( "User has permission [{}]", permission );
                return pjp.proceed();
            }
        }

        // Support for resource access from permission/function names
        // @TODO add support for resource access from user's role if configured in SecuredPermission

        // Support for resource access from permission/function names
        // @TODO add support for resource access from user's group

        throw new PermissionDeniedException( "Permission to resource is denied for roles [" + userAccesList + "]" );
    }

}
