/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baitaplon.nhom4.client.model;

/**
 *
 * @author ADMIN
 */
public class ModelResult {
    private String myName;
    private String mScore;
    private String opponentName;
    private String oScore;
    private String result;

    public ModelResult(String myName, String mScore, String opponentName, String oScore, String result) {
        this.myName = myName;
        this.mScore = mScore;
        this.opponentName = opponentName;
        this.oScore = oScore;
        this.result = result;
    }
    
    
    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getOppointName() {
        return opponentName;
    }

    public void setOppointName(String opponentName) {
        this.opponentName = opponentName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getmScore() {
        return mScore;
    }

    public void setmScore(String mScore) {
        this.mScore = mScore;
    }

    public String getoScore() {
        return oScore;
    }

    public void setoScore(String oScore) {
        this.oScore = oScore;
    }
    
    
}
