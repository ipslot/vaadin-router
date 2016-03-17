package ru.sysolutions.vaadin.auth;

/**
 * Created by kenstavichusva on 16.03.16.
 * This service provide current user access level.
 */
public interface CurrentAccessLevelProvider {

    AccessLevel getCurrentAccessLevel();

}
