
package baitaplon.nhom4.client.model;

import baitaplon.nhom4.client.table.EventAction;
import javax.swing.Icon;

public class ModelProfile {
    Icon icon;
    String name;

    ModelProfile(Icon icon, String name) {
       this.icon = icon;
       this.name = name;
    }

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
