/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;


public class User implements Serializable{
    
    private int ID, totalGames, totalWins, isOnline;
    private String user_name, pass, fullname, status;
    private double totalCore, totalTimePlayed;
    
    public User(){}

    public User(int ID, int totalGames, int totalWins, int isOnline, String user_name, String pass, String fullname, double totalCore, double totalTimePlayed) {
        this.ID = ID;
        this.totalGames = totalGames;
        this.totalWins = totalWins;
        this.isOnline = isOnline;
        this.user_name = user_name;
        this.pass = pass;
        this.fullname = fullname;
        this.totalCore = totalCore;
        this.totalTimePlayed = totalTimePlayed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public double getTotalCore() {
        return totalCore;
    }

    public void setTotalCore(double totalCore) {
        this.totalCore = totalCore;
    }

    public double getTotalTimePlayed() {
        return totalTimePlayed;
    }

    public void setTotalTimePlayed(double totalTimePlayed) {
        this.totalTimePlayed = totalTimePlayed;
    }
    
    
}
