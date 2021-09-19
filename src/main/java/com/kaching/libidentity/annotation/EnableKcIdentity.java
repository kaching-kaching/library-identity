package com.kaching.libidentity.annotation;


import com.kaching.libidentity.aspect.IdentityVerificationAspect;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(IdentityVerificationAspect.class)
@ComponentScan(basePackages = "com.kaching.libidentity")
public @interface EnableKcIdentity {
}
