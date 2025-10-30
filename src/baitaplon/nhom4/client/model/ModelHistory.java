/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baitaplon.nhom4.client.model;

import baitaplon.nhom4.client.table.EventAction;
import javax.swing.Icon;

/**
 *
 * @author ADMIN
 */
public class ModelHistory {
    Icon icon;
    String name;
    String time;
    String result;

    public ModelHistory(Icon icon, String name, String time, String reusult) {
        this.icon = icon;
        this.name = name;
        this.time = time;
        this.result = reusult;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String reusult) {
        this.result = reusult;
    }
    public Object toRowTable(EventAction event){
        return new Object[]{new ModelProfile(icon,name),time,null,result};
    }
}
