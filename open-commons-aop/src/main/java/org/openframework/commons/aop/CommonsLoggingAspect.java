package org.openframework.commons.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CommonsLoggingAspect {

	/** Logger that is available to subclasses */
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * pointcut definition for all classes
	 */
	@Pointcut("within(org.openframework.commons..*)")
	public void logAllCommons() {
		/**
		 * pointcut definition for all classes
		 */
	}

	/**
	 * pointcut definition for all methods returning void.
	 */
	@Pointcut("execution(public void org.openframework.commons.*..*(..))")
	public void executeVoidMethod() {
		/**
		 * pointcut definition for all methods returning void.
		 */
	}

	/**
	 * pointcut definition to exclude before advice.
	 */
	@Pointcut("@annotation(org.openframework.commons.aop.annotations.ExcludeBeforeCommonsLogging) || @annotation(org.openframework.commons.aop.annotations.ExcludeAOPCommonsLogging)")
	public void excludeBeforeLogging() {
		/**
		 * pointcut definition for exclude before advice.
		 */
	}

	/**
	 * pointcut definition to exclude after returning advice.
	 */
	@Pointcut("@annotation(org.openframework.commons.aop.annotations.ExcludeAfterReturningCommonsLogging) || @annotation(org.openframework.commons.aop.annotations.ExcludeAOPCommonsLogging)")
	public void excludeAfterReturningLogging() {
		/**
		 * pointcut definition to exclude after returning advice.
		 */
	}

	/**
	 * pointcut definition for all methods except returning void.
	 */
	@Pointcut("logAllCommons() && !executeVoidMethod() && !excludeAfterReturningLogging()")
	public void logAllCommonsAndExcludeVoidMethod() {
		/**
		 * pointcut definition for all methods except returning void.
		 */
	}

	/**
	 * Before pointcut definition for all methods
	 * 
	 * @param joinPoint Join point for logging
	 */
	@Before("logAllCommons() && !excludeBeforeLogging()")
	//@Before("logAllCommons()")
	public void logBefore(JoinPoint joinPoint) {
		// Logging only for Info mode as this code is likely to throw
		// concurrentModificationexception.
	}

	/**
	 * After pointcut definition for all methods
	 * 
	 * @param joinPoint Join point for logging
	 */
	@After("logAllCommons() && !@annotation(org.openframework.commons.aop.annotations.ExcludeAOPCommonsLogging)")
	public void logAfter(JoinPoint joinPoint) {
		logger.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
				+ " ...Exit");
	}

	/**
	 * After pointcut definition for all methods which return some value
	 * 
	 * @param joinPoint Join point for logging
	 * @param result    Result object for logging
	 */
	@AfterReturning(pointcut = "logAllCommonsAndExcludeVoidMethod()", returning = "result")
	public void logAfterReturning(JoinPoint joinPoint, Object result) {
		// Logging only for Info mode as this code is likely to throw
		// concurrentModificationexception.
		logger.info(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName()
				+ " ...Returning --> " + result);
	}

	/**
	 * AfterThrowing pointcut definition for all methods that throw any
	 * exception/throwable
	 * 
	 * @param joinPoint Join point for logging
	 * @param error     Error to be caught
	 */
	@AfterThrowing(pointcut = "logAllCommons()", throwing = "error")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
		logger.error(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), error);
	}

}
