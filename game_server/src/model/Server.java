package model;

import view.fr_server;
import java.net.ServerSocket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Server {

    public ServerSocket serversocket = null;
    public static ArrayList<Client> arr_client = new ArrayList<>(); // list người dùng  --- chứa thông tin về client như: 
                                                                    // ID, socket của người dùng đấy
    public static ArrayList<User> listOnline = new ArrayList<>(); // list người online -- chính là User ( acc,pass,đi
    public static Connection connect = null; 
    public static fr_server fr_server = null;
    private static final String hostname = "localhost";
    private static final String databasename = "testjava";
    private static final String databaseusername = "root";
    private static final String databasepassword = "";
    public Server() {
        try{  
            Class.forName("com.mysql.jdbc.Driver");
            Server.connect = DriverManager.getConnection("jdbc:mysql://"+ hostname +":3306/"+ databasename +"?useUnicode=true&characterEncoding=UTF-8",databaseusername,databasepassword);
                System.out.println("connected mysql");
            }catch(ClassNotFoundException | SQLException e){ 
                JOptionPane.showMessageDialog(null, "Không tìm thấy CSDL!");
                return;
        }
        fr_server = new fr_server();
        fr_server.setVisible(true);
    }
}
