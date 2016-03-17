package ru.sysolutions.vaadin.auth;

import java.util.Arrays;

/**
 * Created by kenstavichusva on 16.03.16.
 * Your Vaadin views should implement this interface, if you wants use @SecuredView.
 */
public interface AccessLevelView {
    default boolean hasAccessLevel(AccessLevel level) {
        return hasAccessLevel(this.getClass(), level);
    }

    static boolean hasAccessLevel(Class<? extends AccessLevelView> viewClass, AccessLevel level) {
        if (viewClass.isAnnotationPresent(SecuredView.class)) {
            SecuredView annotation = viewClass.getAnnotation(SecuredView.class);
            return Arrays.stream(annotation.allowed()).
                    filter(el -> el == level.getLevelId()).
                    findFirst().isPresent();
        }
        return false;
    }
}
