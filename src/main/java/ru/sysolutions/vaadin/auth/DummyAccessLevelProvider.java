package ru.sysolutions.vaadin.auth;

/**
 * Created by kenstavichusva on 16.03.16.
 */
public class DummyAccessLevelProvider implements CurrentAccessLevelProvider {
    @Override
    public AccessLevel getCurrentAccessLevel() {
        return () -> AccessLevel.USER_LEVEL;
    }
}
