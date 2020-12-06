/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.fr_server;
import java.awt.Component;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Listen_connect extends Thread{
    
    public ServerSocket server_socket = null;
    public Socket socket = null;
    public int port = 6556;
    
    public fr_server fr_server;
    // mở server
    @Override
    public void run (){
            try {
                    server_socket = new ServerSocket(port);
                    fr_server.txt_log.append("Server running in port " + port + " \n");
                    System.out.println(fr_server.txt_log.getText());
                    fr_server.start_btn.setEnabled(false);
            } catch (IOException ex) {
                    Logger.getLogger(Listen_connect.class.getName()).log(Level.SEVERE, null, ex);
                    fr_server.txt_log.append("Lỗi server \n");
            }
            while(true){
                try {
                        this.socket = server_socket.accept();
                        if(socket!=null){
                            fr_server.txt_log.append("new client: " + this.socket.getPort() + "\n");
                        }
                        System.out.println(fr_server.txt_log.getText());
                        Server_controller rc = new Server_controller(this.socket);
                        rc.start();
                } catch (SecurityException | IOException ex) {
                        Logger.getLogger(Listen_connect.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    }
}
