package ru.sysolutions.vaadin;

import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;

import javax.validation.constraints.NotNull;

public class RoutedMenu extends MenuBar {

    public MenuItem addItem(Resource icon, String title, String description,
                            String path, String styleName, MenuBar.Command command) {

        MenuBar.MenuItem item = addItem(title == null ? "" : title, icon, command, path);

        if (styleName != null) {
            item.setStyleName(styleName);
        }

        item.setDescription(description == null ? "" : description);
        item.setCheckable(true);

        return item;
    }

    public void setActiveItem(MenuItem item) {
        if (item != null && item instanceof RoutedMenuItem) {
            MenuBar.MenuItem selectedItem = null;

            for (MenuItem el : this.getItems()) {
                el.setChecked(false);
                if (item.equals(el)) {
                    selectedItem = el;
                }
            }

            while (selectedItem != null && !selectedItem.isChecked()) {
                selectedItem.setChecked(true);
                selectedItem = selectedItem.getParent();
            }
        }
    }

    public MenuBar.MenuItem getItemByPath(@NotNull String path) {
        for (MenuItem el : this.getItems()) {
            if (el instanceof RoutedMenuItem && path.equals(((RoutedMenuItem) el).getPath())) {
                return el;
            }
        }
        return null;
    }

    private MenuBar.MenuItem addItem(String caption, Resource icon, MenuBar.Command command, String path) {
        if (caption == null) {
            throw new IllegalArgumentException("caption cannot be null");
        } else {
            MenuBar.MenuItem newItem = new RoutedMenuItem(caption, icon, command, path);
            getItems().add(newItem);
            this.markAsDirty();
            return newItem;
        }
    }

    public class RoutedMenuItem extends MenuBar.MenuItem {

        private final String path;

        public RoutedMenuItem(String caption, Resource icon, Command command, String path) {
            super(caption, icon, command);
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
