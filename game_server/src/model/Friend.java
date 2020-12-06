/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.swing.ImageIcon;


public class Friend implements Serializable{
    private final int ID;
    private final String full_name;
    private int online;
    private final ImageIcon getImageIcon;
    private String pass;
    private String status;

    public Friend(int ID, String full_name, int online, ImageIcon icon,String status) {
        this.ID = ID;
        this.full_name = full_name;
        this.online = online;
        this.getImageIcon = icon;
        this.status = status;
    }
    public void setStatus(String data){
        this.status = data;
    }
    public void setPass(String pass){
        this.pass = pass;
    }
    public void setonline(int online){
        this.online = online;
    }
    public String getPass(){
        return this.pass;
    }
    
    public int getID() {
        return ID;
    }

    public String getFull_name() {
        return full_name;
    }

    public int getonline() {
        return online;
    }
    public ImageIcon getImageIcon(){
        return getImageIcon;
    }
    public String getStatus(){
        return this.status;
    }
    
}
