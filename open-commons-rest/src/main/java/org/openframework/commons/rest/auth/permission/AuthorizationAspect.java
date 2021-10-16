package org.openframework.commons.rest.auth.permission;

import java.util.Arrays;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.openframework.commons.domain.exceptions.PermissionDeniedException;
import org.openframework.commons.rest.argumentresolver.AbstractUserProfileHandlerMethodArgumentResolver;
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

		System.out.println("securedPermissions.value(): "+securedPermissions.value());

        final Object details = AbstractUserProfileHandlerMethodArgumentResolver.getUserProfile();
        if ( !( details instanceof UserVO ) ) {
            return pjp.proceed();
        }

        final List<String> permissions = Arrays.asList( securedPermissions.value() );
        logger.trace( "Permissions: [{}]", permissions );

        final List<String> roles = retrieveRoles();
        logger.trace( "Roles: [{}]", roles );

        final UserVO profile = ( UserVO ) details;
        logger.trace( "Profile: [{}]", profile.getEmail() );

        for ( String permission : permissions ) {
            if ( roles.contains(permission ) ) {
                logger.trace( "User has permission [{}]", permission );
                return pjp.proceed();
            }
        }
        throw new PermissionDeniedException( "Permission to resource is denied for roles [" + roles + "]" );
    }


    private List<String> retrieveRoles() {
        return AbstractUserProfileHandlerMethodArgumentResolver.getUserAccess();
    }

}
