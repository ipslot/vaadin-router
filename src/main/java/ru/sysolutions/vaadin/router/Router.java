package ru.sysolutions.vaadin.router;

import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import ru.sysolutions.vaadin.auth.AccessLevel;
import ru.sysolutions.vaadin.auth.AccessLevelView;
import ru.sysolutions.vaadin.auth.CurrentAccessLevelProvider;
import ru.sysolutions.vaadin.auth.DummyAccessLevelProvider;
import ru.sysolutions.vaadin.auth.SecuredView;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * Created by kenstavichusva on 16.03.16.
 * Use registerPath methods to add menu items.
 * Restricted for current user items makes hidden automatically.
 */
public class Router {
    protected Navigator navigator;
    protected RoutedMenu menu;
    protected CurrentAccessLevelProvider accessLevelProvider;
    protected Consumer<Component> onAccessDenied;

    public Router(Navigator navigator) {
        this(navigator, new RoutedMenu(), new DummyAccessLevelProvider());
    }

    public Router(Navigator navigator, CurrentAccessLevelProvider accessLevelProvider) {
        this(navigator, new RoutedMenu(), accessLevelProvider);
    }

    public Router(Navigator navigator, RoutedMenu menu, CurrentAccessLevelProvider accessLevelProvider) {
        this.navigator = navigator;
        this.menu = menu;
        this.accessLevelProvider = accessLevelProvider;
        postInit();
    }

    public void registerPath(String title, String path) {
        registerPath(null, null, title, title, path, null, null);
    }

    public void registerPath(Class<? extends AccessLevelView> viewClass, Resource icon, String title, String path) {
        registerPath(viewClass, icon, title, title, path, null, null);
    }

    public void registerPath(Class<? extends AccessLevelView> viewClass, Resource icon, String title, String desc, String path) {
        registerPath(viewClass, icon, title, desc, path, null, null);
    }

    public void registerPath(Class<? extends AccessLevelView> viewClass, Resource icon, String title, String desc, String path, String cssClass) {
        registerPath(viewClass, icon, title, desc, path, cssClass, null);
    }

    public void registerPath(Resource icon, String title, String desc, String path, String cssClass) {
        registerPath(null, icon, title, desc, path, cssClass, null);
    }

    public void registerPath(Class<? extends AccessLevelView> viewClass,
                             Resource icon,
                             String title, String desc, String path, String cssClass,
                             BooleanSupplier visibleCondition) {
        MenuBar.MenuItem item =  menu.addItem(icon, title, desc, path, cssClass, selectedItem -> {
            if (selectedItem instanceof RoutedMenu.RoutedMenuItem) {
                selectedItem.setChecked(false);
                navigator.navigateTo(((RoutedMenu.RoutedMenuItem) selectedItem).getPath());
            } else {
                menu.setActiveItem(selectedItem);
            }
        });
        item.setVisible((visibleCondition == null || visibleCondition.getAsBoolean()) &&
                (viewClass == null || AccessLevelView.hasAccessLevel(viewClass, accessLevelProvider.getCurrentAccessLevel())));
    }

    public Navigator getNavigator() {
        return navigator;
    }

    public RoutedMenu getMenu() {
        return menu;
    }

    public void setOnAccessDenied(Consumer<Component> onAccessDenied) {
        this.onAccessDenied = onAccessDenied;
    }

    protected Router() {
    }

    protected void postInit() {
        navigator.addViewChangeListener(new ViewChangeListener() {

            @Override
            public boolean beforeViewChange(ViewChangeEvent event) {
                if (event.getNewView() instanceof AccessLevelView) {
                    AccessLevelView view = ((AccessLevelView) event.getNewView());
                    AccessLevel level = accessLevelProvider.getCurrentAccessLevel();
                    if (level.getLevelId() == AccessLevel.SUPERUSER_LEVEL || view.hasAccessLevel(level)) {
                        return true;
                    } else {
                        if (onAccessDenied != null) {
                            if (view instanceof Component) {
                                onAccessDenied.accept((Component) view);
                            } else {
                                onAccessDenied.accept(null);
                            }
                        }
                        return false;
                    }
                } else if (event.getNewView().getClass().isAnnotationPresent(SecuredView.class)) {
                    throw new UnsupportedOperationException("@SecuredView annotated not access leveled view class");
                }
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent event) {
                menu.setActiveItem(menu.getItemByPath(event.getNavigator().getState()));
            }
        });
    }
}
