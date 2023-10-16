/**
 * 
 */
package org.openframework.commons.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hello
 *
 */
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class CommonsSpringConfig {

	static {
		System.out.println("CommonsSpringConfig.static{} ");
	}

}
