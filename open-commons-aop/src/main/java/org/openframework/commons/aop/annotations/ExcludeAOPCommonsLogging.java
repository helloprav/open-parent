package org.openframework.commons.aop.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to exclude from AOP logging.
 * 
 * This interface is used to exclude aop logging for methods containing secure
 * info
 * 
 * @author :)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeAOPCommonsLogging {

}
