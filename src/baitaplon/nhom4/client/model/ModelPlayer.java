
package baitaplon.nhom4.client.model;

import baitaplon.nhom4.client.table.EventAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ModelPlayer {
    int id;
    String username;
    Icon icon;
    String name;
    int score;
    long numOfVictory;
    int rank;
    String status;
    

    public ModelPlayer() {
    }

    public ModelPlayer(String username,Icon icon, String name, int score, String status) {
        this.username = username;
        this.icon = icon;
        this.name = name;
        this.score = score;
        this.status = status;
    }

    public ModelPlayer(Icon icon, String name,long numOfVictory,int rank) {

        this.icon = icon;
        this.name = name;
        this.numOfVictory = numOfVictory;
        this.rank = rank;
    }
    
    public ModelPlayer(Icon icon, String name, int score, int rank) {
        this.icon = icon;
        this.name = name;
        this.score = score;
        this.rank = rank;
    }
    
    
    
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(ImageIcon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Object toRowTable1(EventAction event){
        return new Object[]{username,new ModelProfile(icon,name),score,status};
    }
    public Object toRowTable2(EventAction event){
        return new Object[]{new ModelProfile(icon,name),numOfVictory,rank};
    }
    public Object toRowTable3(EventAction event){
        return new Object[]{new ModelProfile(icon,name),score,rank};
    }
}
