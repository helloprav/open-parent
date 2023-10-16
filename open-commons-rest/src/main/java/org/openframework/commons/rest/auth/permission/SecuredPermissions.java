package org.openframework.commons.rest.auth.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention( RetentionPolicy.RUNTIME )
@Target( { ElementType.METHOD, ElementType.TYPE } )
public @interface SecuredPermissions {

	/**
	 * group = "PARIKSHA_ADMIN".
	 *
	 * @return String
	 */
	String group() default "";

	/**
	 * role = "TEACHER".
	 *
	 * @return String
	 */
	String role() default "";

	/**
	 * value = {"list_user","add_user"}.
	 *
	 * @return String[]
	 */
    public String[] value() default "";

}
