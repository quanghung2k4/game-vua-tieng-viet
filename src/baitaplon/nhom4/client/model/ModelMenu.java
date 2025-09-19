
package baitaplon.nhom4.client.model;

import javax.swing.Icon;

/**
 *
 * @author ADMIN
 */
public class ModelMenu {
    Icon icon;
    String menuName;
    public ModelMenu(Icon icon, String menuName) {
        this.icon = icon;
        this.menuName = menuName;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    
}
