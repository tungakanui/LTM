package model;

import java.io.Serializable;
import java.util.ArrayList;


public class Data_socket implements Serializable{
    public String action = null;
    public String[] data;
    public ArrayList<String[]> data_arr;
    public ArrayList<Friend> list_fr;
    public ArrayList<User> listOnline;
    public ArrayList<User> rankList;
    public boolean accept = false;
    public Data_socket(){
        
    }
    public Data_socket(String action,String[] data, ArrayList<String[]> data_arr) {
        this.action = action;
        this.data = data;
        this.data_arr = data_arr;
    }
    
}
