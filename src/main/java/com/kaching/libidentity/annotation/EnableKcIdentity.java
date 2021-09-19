package com.kaching.libidentity.annotation;


import com.kaching.libidentity.config.KcIdentityLibConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Documented
@Import(KcIdentityLibConfig.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableKcIdentity {
}
