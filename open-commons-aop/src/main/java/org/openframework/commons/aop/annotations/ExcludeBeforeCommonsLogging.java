package org.openframework.commons.aop.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This annotation is used to exclude input parameters of a method from logging.
 * 
 * This interface is used to exclude before logging for methods containing
 * secure info
 * 
 * @author :)
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcludeBeforeCommonsLogging {

}
