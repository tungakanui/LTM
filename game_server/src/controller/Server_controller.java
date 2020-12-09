/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DAO.RelaDAO;
import DAO.UserDAO;
import view.fr_server;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import model.Client;
import model.Data_socket;
import model.Friend;
import model.Relationship;
import model.Server;
import model.User;

public class Server_controller extends Thread {

    private int totalOpponent;
    private Boolean hasOpponent = false;
    private Socket socket = null;
    private int ID;
    private int int_status;
    private int image_ID;
    private String opponents = "";
    ObjectInputStream in = null;
    ObjectOutputStream out = null;
    ArrayList<Friend> arr_fr = new ArrayList<>();
    Boolean is_running = true;
    private UserDAO userDAO = new UserDAO();
    private RelaDAO relaDAO = new RelaDAO();

    public Server_controller(Socket socket) {
        this.socket = socket;
        try {
            this.out = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Data_socket msg = null;
//        try {
        while (is_running) {
            try {
                in = new ObjectInputStream(this.socket.getInputStream());
                msg = (Data_socket) in.readObject(); // ép về dtsk
                System.out.println(msg.action);
                switch (msg.action) {
                    case "login":
                        this.login(msg.data[0], msg.data[1]);
                        break; // this.login(msg)
                    case "logout":
                        this.user_disconnect(Integer.parseInt(msg.data[0]));
                        break;
                    case "loadonline":
                        this.loadOnline();
                        break;
                    case "reg":
                        this.reg(msg.data[0], msg.data[1], msg.data[2]);
                        break;
                    case "challenge":
                        this.challenge(Integer.parseInt(msg.data[0]), Integer.parseInt(msg.data[2]), msg.data[1]);
                        break;
                    case "repchallenge":
                        this.repChallenge(Integer.parseInt(msg.data[0]), Integer.parseInt(msg.data[1]), msg.data[2], msg.data[3]);
                        break;
                    case "repMultiChallenge":
                        this.repMultiChallenge(Integer.parseInt(msg.data[0]), Integer.parseInt(msg.data[1]), msg.data[2], msg.data[3]);
                        break;
                    case "emitLost":
                        this.onLost(Integer.parseInt(msg.data[0]), Integer.parseInt(msg.data[1]), Double.parseDouble(msg.data[2]));
                        break;
                    case "emitWin":
                        this.onSubmit(msg);
                        break;
                    case "emitPause":
                        this.submitPause(msg);
                        break;
                    case "emitResume":
                        this.submitResume(msg);
                        break;
                    case "emitMultiChallenge":
                        this.multiChallenge(Integer.parseInt(msg.data[0]), msg.data[1]);
                        break;
                    case "daw":
                        this.onDaw(msg);
                        break;
                    case "updateranktable":
                        this.updateRankTable(Integer.parseInt(msg.data[0]), Integer.parseInt(msg.data[1]));
                        break;
                    case "onwin":
                        this.onWin(msg);
                        break;
                    case "onlosechallenge":
                        this.onLoseChallenge(msg);
                        break;

                    case "onPause":
                        this.onPause(Integer.parseInt(msg.data[0]), msg.data[2]);
                        break;
                    case "onResume":
                        this.onResume(Integer.parseInt(msg.data[0]), msg.data[2]);
                        break;
                    default:
                        System.out.println("unknow action");
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    //public void emitPause(int senderID, String fullName)
    public void onResume(int senderID, String fullName) {
        Data_socket dtsk = new Data_socket();
        String[] data = new String[2];
        dtsk.action = "onResume";
        data[0] = senderID + "";
        data[1] = fullName;
        dtsk.data = data;
        for (int i = 0; i < Server.arr_client.size(); i++) {
            try {
                Server.arr_client.get(i).dout.writeObject(dtsk);
                Server.arr_client.get(i).dout.flush();
                return;
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onPause(int senderID, String fullName) {
        Data_socket dtsk = new Data_socket();
        String[] data = new String[2];
        dtsk.action = "onPause";
        data[0] = senderID + "";
        data[1] = fullName;
        dtsk.data = data;
        for (int i = 0; i < Server.arr_client.size(); i++) {
            try {
                Server.arr_client.get(i).dout.writeObject(dtsk);
                Server.arr_client.get(i).dout.flush();
                return;
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void loadOnline() {
        Data_socket dtsk = new Data_socket();
        dtsk.action = "loadonline";
        dtsk.listOnline = new ArrayList<>();
        dtsk.listOnline.addAll(Server.listOnline);
        for (int i = 0; i < Server.arr_client.size(); i++) {
            try {
                Server.arr_client.get(i).dout.writeObject(dtsk);
                Server.arr_client.get(i).dout.flush();
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void multiChallenge(int senderID, String fullName) {
        Data_socket dtsk = new Data_socket();
        String[] data = new String[2];
        dtsk.action = "multiChallenge";
        data[0] = senderID + "";
        data[1] = fullName;
        dtsk.data = data;
        totalOpponent = Server.arr_client.size();
        System.out.println("SIZE");
        System.out.println(Server.arr_client.size() + "");
        for (int i = 0; i < Server.arr_client.size(); i++) {
            if (Server.arr_client.get(i).ID != senderID) {
                try {
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();

                } catch (IOException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return;
    }

    public void challenge(int senderID, int receiverID, String fullName) {
        Data_socket dtsk = new Data_socket();
        String[] data = new String[2];
        dtsk.action = "challenge";
        data[0] = senderID + "";
        data[1] = fullName;
        dtsk.data = data;
        for (int i = 0; i < Server.arr_client.size(); i++) {
            if (Server.arr_client.get(i).ID == receiverID) {
                try {
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                    return;
                } catch (IOException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public void repMultiChallenge(int senderID, int receiverID, String fullName, String decision) {
        totalOpponent = totalOpponent - 1;
        System.out.println("TOTALOPPONENT");
        System.out.println(totalOpponent + "");
        if (decision.equals("yes")) {
            opponents += receiverID + "";
            opponents += " ";
            hasOpponent = true;
            ArrayList<Relationship> listRela = new ArrayList<>();
            listRela.addAll(relaDAO.getAllRela()); // load ra list quan hệ
            int checkRela = 1;
            for (Relationship r : listRela) {
                if ((senderID == r.getId1() && receiverID == r.getId2()) || (senderID == r.getId2() && receiverID == r.getId1())) {
                    checkRela = 0;
                    break;
                }
            }
            if (checkRela != 0) {
                relaDAO.addRela(new Relationship(senderID, receiverID)); // thêm quan hệ vào csdl
            }
            // handle invite
            for (int i = 0; i < Server.listOnline.size(); i++) { // set trạng thái cho 2 thằng thành bận
                User u = Server.listOnline.get(i);
                int id = u.getID();
                if (id == receiverID) {
                    Server.listOnline.get(i).setIsOnline(3); // bận
                }
            }
            
            Data_socket dtsk = new Data_socket();
            String[] data = new String[6];
            data[0] = fullName;
            data[1] = "yes";
            data[2] = senderID + "";
            data[3] = receiverID + "";
            image_ID = (int) Math.round(Math.random() * 10);
            data[4] = image_ID + ""; // đề bài
            data[5] = hasOpponent + "";
            dtsk.data = data;
            for (int i = 0; i < Server.arr_client.size(); i++) {
                if (Server.arr_client.get(i).ID == receiverID) { //dem het so nguoi di
                    try {
                        dtsk.action = "repMultiChallenge";
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    dtsk.action = "updatetobusy";
                    try {
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } else {
            Data_socket dtsk = new Data_socket();
            String[] data = new String[2];
            data[0] = fullName;
            data[1] = "no";
            dtsk.data = data;
            dtsk.action = "repMultiChallenge";
            for (int i = 0; i < Server.arr_client.size(); i++) {
                if (Server.arr_client.get(i).ID == senderID) {
                    try {
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        if (totalOpponent == 1){
            Data_socket dtsk = new Data_socket();
            String[] data = new String[2];
            data[0] = opponents;
            data[1] = image_ID + "";
            dtsk.data = data;
            dtsk.action = "hasOpponent";
            for (int i = 0; i < Server.arr_client.size(); i++) {
                if (Server.arr_client.get(i).ID == senderID) {
                    try {
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void repChallenge(int senderID, int receiverID, String fullName, String decision) {

        if (decision.equals("yes")) { // đồng ý chơi
            // Check relationship
            ArrayList<Relationship> listRela = new ArrayList<>();
            listRela.addAll(relaDAO.getAllRela()); // load ra list quan hệ
            int checkRela = 1;
            for (Relationship r : listRela) {
                if ((senderID == r.getId1() && receiverID == r.getId2()) || (senderID == r.getId2() && receiverID == r.getId1())) {
                    checkRela = 0;
                    break;
                }
            }
            if (checkRela != 0) {
                relaDAO.addRela(new Relationship(senderID, receiverID)); // thêm quan hệ vào csdl
            }

            // handle invite
            for (int i = 0; i < Server.listOnline.size(); i++) { // set trạng thái cho 2 thằng thành bận
                User u = Server.listOnline.get(i);
                int id = u.getID();
                if (id == senderID || id == receiverID) {
                    Server.listOnline.get(i).setIsOnline(3); // bận
                }
            }
            Data_socket dtsk = new Data_socket();
            String[] data = new String[5];
            data[0] = fullName;
            data[1] = "yes";
            data[2] = senderID + "";
            data[3] = receiverID + "";
            int image_ID = (int) Math.round(Math.random() * 10);
            data[4] = image_ID + ""; // đề bài
            dtsk.data = data;
            for (int i = 0; i < Server.arr_client.size(); i++) {
                if (Server.arr_client.get(i).ID == senderID || Server.arr_client.get(i).ID == receiverID) {
                    try {
                        dtsk.action = "repchallenge";
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    dtsk.action = "updatetobusy";
                    try {
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        } else {
            Data_socket dtsk = new Data_socket();
            String[] data = new String[2];
            data[0] = fullName;
            data[1] = "no";
            dtsk.data = data;
            dtsk.action = "repchallenge";
            for (int i = 0; i < Server.arr_client.size(); i++) {
                if (Server.arr_client.get(i).ID == senderID) {
                    try {
                        Server.arr_client.get(i).dout.writeObject(dtsk);
                        Server.arr_client.get(i).dout.flush();
                        return;
                    } catch (IOException ex) {
                        Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    public void updateToOnline(int id1, int id2) {
        for (int i = 0; i < Server.listOnline.size(); i++) {
            User u = Server.listOnline.get(i);
            int id = u.getID();
            if (id == id1 || id == id2) {
                Server.listOnline.get(i).setIsOnline(1);
                System.out.println(Server.listOnline.get(i).getIsOnline());
            }
        }
    }

    public void onDaw(Data_socket dtsk) {
        int idEnemy = Integer.parseInt(dtsk.data[0]);
        int idMe = Integer.parseInt(dtsk.data[0]);
        double totalTime = Double.parseDouble(dtsk.data[3]);
        try {
            userDAO.updateUser(idMe, 0.5, 0, 1, 0); // loser
            userDAO.updateUser(idEnemy, 0.5, 0, 1, 0); // winner
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Server.arr_client.size(); i++) {
            if (Server.arr_client.get(i).ID == idEnemy) {
                try {
                    Data_socket dtsk1 = new Data_socket();
                    dtsk1.action = "ondaw";
                    String[] data = new String[2];
                    data[0] = dtsk.data[0];
                    data[1] = dtsk.data[2];
                    dtsk1.data = data;
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    public void onWin(Data_socket dtsk) {
        int idMe = Integer.parseInt(dtsk.data[0]);
        int idEnemy = Integer.parseInt(dtsk.data[1]);
        // core, time , game. wins
        double totalTime = Double.parseDouble(dtsk.data[3]);
        try {
            userDAO.updateUser(idMe, 0, 0, 1, 0); // loser
            userDAO.updateUser(idEnemy, 1, totalTime, 1, 1); // winner
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Server.arr_client.size(); i++) {
            if (Server.arr_client.get(i).ID == idEnemy) {
                try {
                    Data_socket dtsk1 = new Data_socket();
                    dtsk1.action = "onwin";
                    String[] data = new String[2];
                    data[0] = dtsk.data[0];
                    data[1] = dtsk.data[2];
                    dtsk1.data = data;
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    public void onLoseChallenge(Data_socket dtsk) {
        int idMe = Integer.parseInt(dtsk.data[0]);
        int idEnemy = Integer.parseInt(dtsk.data[1]);
        // core, time , game. wins
        double totalTime = Double.parseDouble(dtsk.data[3]);
        try {
            userDAO.updateUser(idMe, 0, 0, 1, 0); // loser
            userDAO.updateUser(idEnemy, 1, totalTime, 1, 1); // winner
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < Server.arr_client.size(); i++) {
            if (Server.arr_client.get(i).ID == idEnemy) {
                try {
                    Data_socket dtsk1 = new Data_socket();
                    dtsk1.action = "onlosechallenge";

                    dtsk1.data = dtsk.data;
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
    }

    public void submitPause(Data_socket dtsk) {
        System.out.println("Submit Pause");
        int idMe = Integer.parseInt(dtsk.data[0]);
        for (int i = 0; i < Server.listOnline.size(); i++) {
            try {
                if (Server.listOnline.get(i).getID() != idMe) {

                    dtsk.action = "opponentPause";
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void submitResume(Data_socket dtsk) {
        System.out.println("Submit Resume");
        int idMe = Integer.parseInt(dtsk.data[0]);
        for (int i = 0; i < Server.listOnline.size(); i++) {
            try {
                if (Server.listOnline.get(i).getID() != idMe) {

                    dtsk.action = "opponentResume";
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onSubmit(Data_socket dtsk) {
        int idMe = Integer.parseInt(dtsk.data[0]);
        int idEnemy = Integer.parseInt(dtsk.data[1]);

        updateToOnline(idMe, idEnemy);

        Data_socket dtsk1 = new Data_socket();
        dtsk1.data = dtsk.data;
        for (int i = 0; i < Server.listOnline.size(); i++) {
            try {
                if (Server.listOnline.get(i).getID() == idEnemy) {

                    dtsk.action = "enemysubmit";
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                } else {
                    dtsk.action = "updatetoonline";
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void onLost(int idMe, int idEnemy, double totalTime) {
        updateToOnline(idMe, idEnemy);
        // core, time , game. wins
        try {
            userDAO.updateUser(idMe, 0, 0, 1, 0); // loser
            userDAO.updateUser(idEnemy, 1, totalTime, 1, 1); // winner
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] data = new String[3];
        data[0] = idMe + "";
        data[1] = idEnemy + "";
        for (int i = 0; i < Server.listOnline.size(); i++) {
            if (Server.listOnline.get(i).getID() == idMe) {
                data[2] = Server.listOnline.get(i).getFullname();
                break;
            }
        }
        Data_socket dtsk = new Data_socket();
        dtsk.data = data;
        for (int i = 0; i < Server.listOnline.size(); i++) {
            try {
                if (Server.listOnline.get(i).getID() == idEnemy) {

                    dtsk.action = "youwin";
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                } else {
                    dtsk.action = "updatetoonline";
                    Server.arr_client.get(i).dout.writeObject(dtsk);
                    Server.arr_client.get(i).dout.flush();
                }
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public User createUser(ResultSet rs, User u) throws SQLException {
        u.setID(rs.getInt("ID"));
        u.setUser_name(rs.getString("user_name"));
        u.setPass(rs.getString("pass"));
        u.setFullname(rs.getString("fullname"));
        u.setTotalCore(rs.getDouble("totalCore"));
        u.setTotalTimePlayed(rs.getDouble("totalTimePlayed"));
        u.setTotalGames(rs.getInt("totalGames"));
        u.setTotalWins(rs.getInt("totalWins"));
        u.setStatus(rs.getString("status"));
        u.setIsOnline(1);
        return u;
    }

    public boolean isOnline(User user) {
        for (User u : Server.listOnline) {
            if (u.getUser_name().equals(user.getUser_name())) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<User> getListEnemyOrderByAV(ArrayList<User> list) {
        Collections.sort(list, new Comparator<User>() {
            @Override
            public int compare(User u1, User u2) {
                double avCore1 = u1.getTotalCore() / (double) u1.getTotalGames();
                double avCore2 = u2.getTotalCore() / (double) u2.getTotalGames();
                int rs;
                if (avCore2 > avCore1) {
                    return 1;
                } else if (avCore2 < avCore1) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        return list;
    }

    public void updateRankTable(int indexSeclected, int clientID) {
        Data_socket dtsk = new Data_socket();
        String[] data = new String[1];
        data[0] = indexSeclected + "";
        dtsk.data = data;
        dtsk.rankList = new ArrayList<>();
        dtsk.action = "updateranktable";
        switch (indexSeclected) {
            case 0: {
                try {
                    dtsk.rankList.addAll(userDAO.getListUserOrderByCore());
                    for (int i = 0; i < Server.arr_client.size(); i++) {
                        if (Server.arr_client.get(i).ID == clientID) {
                            Server.arr_client.get(i).dout.writeObject(dtsk);
                            Server.arr_client.get(i).dout.flush();
                            break;
                        }
                    }
                    break;
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case 2: {
                try {
                    dtsk.rankList.addAll(userDAO.getListUserOrderByTime());
                    for (int i = 0; i < Server.arr_client.size(); i++) {
                        if (Server.arr_client.get(i).ID == clientID) {
                            Server.arr_client.get(i).dout.writeObject(dtsk);
                            Server.arr_client.get(i).dout.flush();
                            break;
                        }
                    }
                    break;
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            case 1: {
                try {
                    ArrayList<User> listEnemy = new ArrayList<>();
                    ArrayList<Relationship> listRela = new ArrayList<>();
                    listRela.addAll(relaDAO.getAllRelaById(clientID));
                    for (Relationship r : listRela) {
                        int id = (r.getId1() == clientID) ? r.getId2() : r.getId1();
                        listEnemy.add(userDAO.getUserById(id));
                    }
                    dtsk.rankList.addAll(getListEnemyOrderByAV(listEnemy));
                    for (int i = 0; i < Server.arr_client.size(); i++) {
                        if (Server.arr_client.get(i).ID == clientID) {
                            Server.arr_client.get(i).dout.writeObject(dtsk);
                            Server.arr_client.get(i).dout.flush();
                            break;
                        }
                    }
                    break;
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            default:
                break;
        }
    }

    public boolean login(String name, String pass) {
        String query = "SELECT * FROM `info_user` WHERE `user_name`= ? AND `pass` = ?";
        ResultSet result = null;
        String[] data = new String[3];
        Data_socket dtsk = new Data_socket();
        try {
            PreparedStatement prepstmt = Server.connect.prepareStatement(query);
            prepstmt.setString(1, name);
            prepstmt.setString(2, pass);
            result = prepstmt.executeQuery();
            result.last();
            dtsk.action = "login";
            if (result.getRow() != 0) {
                User user = createUser(result, new User());
                if (!isOnline(user)) {
                    data[0] = "true";
                    data[1] = user.getID() + "";
                    data[2] = user.getTotalCore() + "";
                    Server.listOnline.add(user);
                    System.out.println(Server.listOnline);
                    System.out.println(user.getID());
                    BufferedImage img = null;
                    String avt = user.getID() + "";
                    try {
                        File file = new File("src/image/user" + avt + ".jpg");
                        img = ImageIO.read(file);
                    } catch (IOException e) {
                        File file = new File("src/image/avt.jpg");
                        img = ImageIO.read(file);
                    }
                    ImageIcon icon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
                    Friend me = new Friend(result.getInt("ID"), result.getString("fullname"), 1, icon, result.getString("status"));
                    ArrayList<Friend> ar = new ArrayList<>();
                    ar.add(me);
                    dtsk.list_fr = ar;
                    dtsk.listOnline = Server.listOnline;

                    this.user_login(user.getID());
                    System.out.println("dd" + Server.arr_client);
                    this.ID = user.getID();
                    this.int_status = 1;

                    append_txt("client: " + this.socket.getPort() + " Login");
                } else {
                    append_txt("client: " + this.socket.getPort() + " is Online, cant connect!");
                    data[0] = "online";
                }
            } else {
                append_txt("client: " + this.socket.getPort() + " try to login");
                data[0] = "false";
            }
            dtsk.data = data;
            dtsk.rankList = new ArrayList<>();
            dtsk.rankList.addAll(userDAO.getListUserOrderByCore());
            out.writeObject(dtsk);
            out.flush();
            return true;
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void reg(String username, String full_name, String pass) {
        String[] data = new String[1];
        Data_socket dtsk = new Data_socket();
        dtsk.action = "reg";
        String query_check = "SELECT `user_name` FROM `info_user` WHERE `user_name` = ?";
        String query = "INSERT INTO `info_user` (`user_name`, `pass`, `fullname`) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement1 = Server.connect.prepareStatement(query_check);
            preparedStatement1.setString(1, username);
            System.out.println(preparedStatement1.toString());
            ResultSet rs = preparedStatement1.executeQuery();
            rs.last();
            if (rs.getRow() != 0) {
                //user_name exist;
                data[0] = "false";
                dtsk.data = data;
                out.writeObject(dtsk);
                return;
            }
            PreparedStatement preparedStatement2 = Server.connect.prepareStatement(query);
            preparedStatement2.setString(1, username);
            preparedStatement2.setString(2, pass);
            preparedStatement2.setString(3, full_name);
            preparedStatement2.executeUpdate();
            data[0] = "true";
            dtsk.data = data;

            out.writeObject(dtsk);
        } catch (SQLException | IOException ex) {
            Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void user_login(int ID) {
        int size = Server.arr_client.size();
        this.int_status = 1; // 1 = online
        Client cl = new Client(socket, ID);
        cl.dout = out;
        cl.int_status = 1;
        Server.arr_client.add(cl);
    }

    public void user_disconnect(int ID) {
        Data_socket dtsk = new Data_socket();
        for (int i = 0; i < Server.arr_client.size(); i++) {
            if (Server.arr_client.get(i).ID == ID) {
                Server.arr_client.remove(i); // remove form online list
                Server.listOnline.remove(i);
                dtsk.listOnline = new ArrayList<>();
                dtsk.listOnline.addAll(Server.listOnline);
                dtsk.action = "loadonline";
                break;
            }
        }
        for (int i = 0; i < Server.arr_client.size(); i++) {
            try {
                Server.arr_client.get(i).dout.writeObject(dtsk);
                Server.arr_client.get(i).dout.flush();
            } catch (IOException ex) {
                Logger.getLogger(Server_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.is_running = false; // terminal thread
    }

    public void append_txt(String msg) {
        fr_server.txt_log.append(msg + "\n");
        fr_server.txt_log.setCaretPosition(fr_server.txt_log.getDocument().getLength());
    }

}
