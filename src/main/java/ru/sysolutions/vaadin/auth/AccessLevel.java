package ru.sysolutions.vaadin.auth;

/**
 * Created by kenstavichusva on 16.03.16.
 * Access levels. Zero - is Admin, 100 - logged in User, 200 - Guest.
 * Your Roles Enum should implements this interface.
 * You can add custom access levels.
 */
public interface AccessLevel {
    int GUEST_LEVEL = 200;
    int USER_LEVEL = 100;
    int SUPERUSER_LEVEL = 0;

    int getLevelId();

}
