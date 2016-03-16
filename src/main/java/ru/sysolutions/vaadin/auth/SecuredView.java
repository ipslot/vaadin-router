package ru.sysolutions.vaadin.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by kenstavichusva on 24.11.15.
 * Annotation for view required authorization.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SecuredView {
    int[] allowed() default {AccessLevel.USER_LEVEL, AccessLevel.SUPERUSER_LEVEL};
}
