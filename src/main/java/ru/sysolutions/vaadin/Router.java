package ru.sysolutions.vaadin;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;


public class Router {
    protected Navigator navigator;
    protected RoutedMenu menu;

    public Router(Navigator navigator) {
        this(navigator, new RoutedMenu());
    }

    public Router(Navigator navigator, RoutedMenu menu) {
        this.navigator = navigator;
        this.menu = menu;
        postInit();
    }

    public void registerPath(String title, String path) {
        registerPath(null, title, title, path, null);
    }

    public void registerPath(Resource icon, String title, String path) {
        registerPath(icon, title, title, path, null);
    }

    public void registerPath(Resource icon, String title, String desc, String path) {
        registerPath(icon, title, desc, path, null);
    }

    // todo: check permissions
    public void registerPath(Resource icon, String title, String desc, String path, String cssClass) {
        menu.addItem(icon, title, desc, path, cssClass, selectedItem -> {
            menu.setActiveItem(selectedItem);
            if (selectedItem instanceof RoutedMenu.RoutedMenuItem) {
                navigator.navigateTo(((RoutedMenu.RoutedMenuItem) selectedItem).getPath());
            }
        });
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public RoutedMenu getMenu() {
        return menu;
    }

    protected Router() {
    }

    protected void postInit() {
        navigator.addViewChangeListener(new ViewChangeListener() {

            // todo: add permission checking
            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                menu.setActiveItem(menu.getItemByPath(event.getNavigator().getState()));
            }
        });
    }
}
