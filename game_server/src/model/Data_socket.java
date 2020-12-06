package model;

import java.io.Serializable;
import java.util.ArrayList;


public class Data_socket implements Serializable{
    public String action = null; // sự kiện 1 socket tới: thách đấu, mời, online,thua, thắng, hòa.....v.v.
    public String[] data; // String có thể convert đc cả int, double..VD: mình muốn gửi ID của mình, ID của đối thủ, điểm......
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
