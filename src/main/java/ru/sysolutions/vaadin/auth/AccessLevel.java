package ru.sysolutions.vaadin.auth;

/**
 * Created by kenstavichusva on 16.03.16.
 */
public interface AccessLevel {
    int GUEST_LEVEL = 200;
    int USER_LEVEL = 100;
    int SUPERUSER_LEVEL = 0;

    int getLevelId();

}
