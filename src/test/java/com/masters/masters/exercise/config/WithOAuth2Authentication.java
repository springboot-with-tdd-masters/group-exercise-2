/**
 * 
 */
package com.masters.masters.exercise.config;

import java.lang.annotation.*;

import org.springframework.security.test.context.support.WithSecurityContext;
/**
 * @author michaeldelacruz
 *
 */

@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = WithOAuth2AuthenticationSecurityContextFactory.class)
public @interface WithOAuth2Authentication {

	String clientId() default "devglan-client";

	String username() default "Zaldy";
	
}
