/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.Client_controller;
import view.Test;
import view.fr_client;
import view.fr_reg;
import view.fr_Login;
import view.fr_play;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import model.User;


public class main {
    public static int socket_port = 6556;
    public static String ip_server = "127.0.0.1";
    public static Socket socket = null;
    public static boolean login = false;
    public static int my_ID;
    public static int my_EnemyID;
    public static ImageIcon icon = null;
    public static String full_name = null;
    public static boolean connected = false;
    public static fr_client fr_client = null;
    public static fr_Login fr_login = null;
    public static fr_reg fr_reg = null;
    public static fr_play fr_play = null;
    public static Test test = null;
    public static ArrayList<User> listOnline = new ArrayList<>();
    public static boolean done = false;
    
    public static void main(String[] args) {
        main chat_client = new main();
        chat_client.init();
    }
    public void init(){
        main.fr_client = new fr_client();
        main.fr_reg = new fr_reg();
        main.fr_login = new fr_Login();
        
        main.fr_login.setVisible(true);
        try {
            main.socket = new Socket(main.ip_server,main.socket_port);
            Thread receive = new Client_controller(main.socket);
            receive.start();
            main.connected = true;
            System.out.println("conected");
        } catch (IOException ex) {
            Logger.getLogger(fr_Login.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "socket error");
        }
        
    }
}
