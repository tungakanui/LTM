/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    public Socket socket = null;
    public int ID;
    public ObjectOutputStream dout = null;
    public int int_status;

    public Client(Socket sk,int id) {
        this.socket = sk;
        this.ID = id;
    }
    
}
