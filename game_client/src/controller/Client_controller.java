package controller;

import view.main;
import view.Test;
import view.fr_client;
import view.fr_list_model;
import view.fr_play;
import java.awt.Frame;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import model.User;
import model.Data_socket;

public class Client_controller  extends Thread{
    Socket socket = null;
    ObjectInputStream din = null;
    Data_socket respon = null;
    Clip clip;
    public Client_controller(Socket sk){
        this.socket = sk;
    }
    @Override
    public void run(){
        try {
            
            din = new ObjectInputStream(this.socket.getInputStream());
            while(true){
                respon = (Data_socket)din.readObject();
                switch(respon.action){
                    case "login"             : this.check_login(); break;
                    case "loadonline"        :{
                        this.resetListOnline(respon.listOnline);
                        this.loadOnline(respon.listOnline);
                        break;
                    }
                    case "updateranktable"   : this.updateRankTable(Integer.parseInt(respon.data[0]));break;
                    case "challenge"         : this.challenge(Integer.parseInt(respon.data[0]), respon.data[1]);break;
                    case "repchallenge"      : this.repChallenge(respon);break;
                    case "updatetobusy"      : this.updateToBusy(respon);break;
                    case "updatetoonline"    : this.updateToOnline(respon);break;
                    case "youwin"            : this.onWin(respon);break;
                    case "enemysubmit"       : this.onSubmit(respon);break;
                    case "ondaw"             : this.onDaw(respon);break;
                    case "reg"               : this.respon_reg(respon);break;
                    case "onwin"             : this.enemyLose(respon);break;
                    case "onlosechallenge"   : this.enemyLoseAndChallenge(respon);break;
//                    case "onPause": this.onPause(); break;
//                    case "multiChallenge": this.multiChallenge();break;
                    default                  : System.out.println("unknow action");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            System.out.println(ex.toString());
            System.out.println("Mất kết nối");
            JOptionPane.showMessageDialog(null, "Mất kết nối máy chủ, trương trình sẽ tự thoát");
            System.exit(0);
        }
    } 
    
    
    public void challenge(int senderID, String fullName){
        // check lúc hòa , nếu xủ lý xong thì resset...
        if(main.done){
            main.done = false;
        }
        // handle
         int confirm = JOptionPane.showConfirmDialog(null, "Người chơi "+ fullName + " có ID "+senderID+" thách đấu bạn! Đồng ý !?", "Thư thách đấu", JOptionPane.YES_NO_OPTION);
         Data_socket dtsk = new Data_socket();
         String[] data = new String[4];
         data[0] = senderID + "";
         data[1] = main.my_ID + "";
         data[2] = main.full_name;
         dtsk.action = "repchallenge";
         ObjectOutputStream dout;
         switch(confirm){
             case JOptionPane.YES_OPTION:{
                 data[3] = "yes";
                 dtsk.data = data;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                 break;
             }
             case JOptionPane.NO_OPTION: {
                 data[3] = "no";
                 dtsk.data = data;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                 break;
             }
             default:{
                 data[3] = "no";
                 dtsk.data = data;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                 break;
             }
         }
    }
    
    public void updateToBusy(Data_socket dtsk){
        String id1 = dtsk.data[2];
        String id2 = dtsk.data[3];
        for (int i = 0; i < main.listOnline.size(); i++) {
            int id = main.listOnline.get(i).getID();
            if(id1.equalsIgnoreCase(id+"") || id2.equalsIgnoreCase(id+"")){
                main.listOnline.get(i).setIsOnline(3);
                System.out.println(main.listOnline.get(i).getIsOnline());
            }
        }
        loadOnline(main.listOnline);
    }
    public void updateToOnline(Data_socket dtsk){
        String id1 = dtsk.data[0];
        String id2 = dtsk.data[1];
        for (int i = 0; i < main.listOnline.size(); i++) {
            int id = main.listOnline.get(i).getID();
            if(id1.equalsIgnoreCase(id+"") || id2.equalsIgnoreCase(id+"")){
                main.listOnline.get(i).setIsOnline(1);
                System.out.println(main.listOnline.get(i).getIsOnline());
            }
        }
        loadOnline(main.listOnline);
    }
    
    public void repChallenge(Data_socket dtsk){
        if(dtsk.data[1].equals("yes")){
            updateToBusy(dtsk);
            int myID = main.my_ID ;
            int id1 = Integer.parseInt(dtsk.data[2]);
            int id2 = Integer.parseInt(dtsk.data[3]);
            main.my_EnemyID = (myID==id1)? id2 : id1;
            
            main.test = new Test(main.fr_client, false);
            main.test.image_ID = dtsk.data[4]; // đề bài
            main.test.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, dtsk.data[0] + " từ chối lời mời của bạn!");
        }
    }
    
    public void onPause(Data_socket dtsk){
        main.done = false;
        dtsk.action = "onPause";
        String[] data = new String[3];
        data[0] = main.my_ID + "";
        data[1] = "";
        data[2] = main.full_name;
        
        dtsk.data = data;
        try {
            ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    
    public void onResume(Data_socket dtsk){
        main.done = false;
        dtsk.action = "onResume";
        String[] data = new String[3];
        data[0] = main.my_ID + "";
        data[1] = "";
        data[2] = main.full_name;
        
        dtsk.data = data;
        try {
            ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    
    public void onDaw(Data_socket dtsk){
        main.done = false;
        int confirm = JOptionPane.showConfirmDialog(main.fr_client, dtsk.data[2] + " đã HÒA với bạn! Thách đấu lại !?" , "Tiếp tục chiến !?", JOptionPane.YES_NO_OPTION);
        main.test.IS_RUN = false;
        main.test.dispose();
        
        switch(confirm){
            case JOptionPane.YES_OPTION:{
                
                Data_socket dtsk1 = new Data_socket();
                dtsk1.action = "challenge";
                String[] data = new String[3];
                data[0] = main.my_ID +""; // sender id
                data[1] = main.full_name; // sender name
                data[2] = dtsk.data[0]; // receiver id

                dtsk1.data = data;
                {
                    try {
                        ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
                        dout.writeObject(dtsk1);
                        dout.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
            case JOptionPane.NO_OPTION:{
                Data_socket dtsk2 = new Data_socket();
                dtsk2.action = "loadonline";
                ObjectOutputStream dout;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk2);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            default: break;
        }
    }
    public void onSubmit (Data_socket dtsk){
        updateToOnline(dtsk);
        if(main.done){
            main.done = false;
            main.test.IS_RUN = false;
            main.test.dispose();
            double currentCore = Double.parseDouble(main.fr_client.lbl_MyTotalCore.getText());
            double newCore = currentCore + 0.5;
            main.fr_client.lbl_MyTotalCore.setText(newCore+"");
            main.done = false;
            Data_socket dtsk1 = new Data_socket();
            String[] data = new String [4];
            dtsk1.action = "daw";
            data[0] = dtsk.data[1]; // thằng nhận submit
            data[1] = dtsk.data[0]; // thằng submit
            data[2] = main.full_name; // thời gian chơi xong
            data[3] = dtsk.data[3];
            dtsk1.data = data;
            try {
                ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
                dout.writeObject(dtsk1);
                dout.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(main.fr_client, dtsk.data[2] + " và bạn đã HÒA với "+dtsk.data[3]+" giây!");
        }
        else {
            main.test.IS_RUN = false;
            main.test.dispose();
            Data_socket dtsk1 = new Data_socket();
            String[] data = new String [4];
            dtsk1.action = "onwin";
            data[0] = dtsk.data[1]; // thằng nhận submit
            data[1] = dtsk.data[0]; // thằng submit
            data[2] = main.full_name; // thời gian chơi xong
            data[3] = dtsk.data[3];
            dtsk1.data = data;
            try {
                ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
                dout.writeObject(dtsk1);
                dout.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(main.fr_client, dtsk.data[2] + " đã hạ gục bạn với "+dtsk.data[3]+" giây!");
        }
        
    }
    public void enemyLoseAndChallenge(Data_socket dtsk){
         main.done = false;
        String currentCore = main.fr_client.lbl_MyTotalCore.getText();
        double newCore = Double.parseDouble(currentCore) + 1;
        main.fr_client.lbl_MyTotalCore.setText(newCore+"");
        updateToOnline(dtsk);
        main.test.IS_RUN = false;
        int confirm = JOptionPane.showConfirmDialog(null,"Bạn đã thắng với " + dtsk.data[3] + " giây!" + dtsk.data[2] + " thách đấu bạn! Chấp nhận!?", "Thư thách đấu", JOptionPane.YES_NO_OPTION);
         Data_socket dtsk1 = new Data_socket();
         String[] data = new String[4];
         data[0] = main.my_ID + "";
         data[1] = dtsk.data[0];
         data[2] = main.full_name;
         dtsk.action = "repchallenge";
         ObjectOutputStream dout;
         switch(confirm){
             case JOptionPane.YES_OPTION:{
                 data[3] = "yes";
                 dtsk.data = data;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                 break;
             }
             case JOptionPane.NO_OPTION: {
                 data[3] = "no";
                 dtsk.data = data;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                 break;
             }
             default:{
                 data[3] = "no";
                 dtsk.data = data;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                 break;
             }
         }
        
    }
    public void enemyLose(Data_socket dtsk){
        main.done = false;
        String currentCore = main.fr_client.lbl_MyTotalCore.getText();
        double newCore = Double.parseDouble(currentCore) + 1;
        main.fr_client.lbl_MyTotalCore.setText(newCore+"");
        updateToOnline(dtsk);
        main.test.IS_RUN = false;
        main.test.dispose();
        int confirm = JOptionPane.showConfirmDialog(main.fr_client, dtsk.data[2] + " đã thua! Bạn thắng! Thách đấu lại !?" , "Tiếp tục chiến !?", JOptionPane.YES_NO_OPTION);
        
        
        switch(confirm){
            case JOptionPane.YES_OPTION:{
                
                Data_socket dtsk1 = new Data_socket();
                dtsk1.action = "challenge";
                String[] data = new String[3];
                data[0] = main.my_ID +""; // sender id
                data[1] = main.full_name; // sender name
                data[2] = dtsk.data[0]; // receiver id

                dtsk1.data = data;
                {
                    try {
                        ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
                        dout.writeObject(dtsk1);
                        dout.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
            case JOptionPane.NO_OPTION:{
                Data_socket dtsk2 = new Data_socket();
                dtsk2.action = "loadonline";
                ObjectOutputStream dout;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk2);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            default: break;
        }
    }
    public void onWin (Data_socket dtsk){
        String currentCore = main.fr_client.lbl_MyTotalCore.getText();
        double newCore = Double.parseDouble(currentCore) + 1;
        main.fr_client.lbl_MyTotalCore.setText(newCore+"");
        updateToOnline(dtsk);
        main.test.IS_RUN = false;
        int confirm = JOptionPane.showConfirmDialog(main.fr_client, dtsk.data[2] + " đã đầu hàng! Bạn thắng! Thách đấu lại !?" , "Tiếp tục chiến !?", JOptionPane.YES_NO_OPTION);
        main.test.dispose();
        
        switch(confirm){
            case JOptionPane.YES_OPTION:{
                
                Data_socket dtsk1 = new Data_socket();
                dtsk1.action = "challenge";
                String[] data = new String[3];
                data[0] = main.my_ID +""; // sender id
                data[1] = main.full_name; // sender name
                data[2] = dtsk.data[0]; // receiver id

                dtsk1.data = data;
                {
                    try {
                        ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
                        dout.writeObject(dtsk1);
                        dout.flush();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                break;
            }
            case JOptionPane.NO_OPTION:{
                Data_socket dtsk2 = new Data_socket();
                dtsk2.action = "loadonline";
                ObjectOutputStream dout;
                try {
                    dout = new ObjectOutputStream(main.socket.getOutputStream());
                    dout.writeObject(dtsk2);
                    dout.flush();
                } catch (IOException ex) {
                    Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            default: break;
        }
    }
    public void updateRankTable(int indexSelected){
        switch(indexSelected){
            case 0: {
                main.fr_client.UDCTB(respon.rankList);
                break;
            }
            case 2: {
                main.fr_client.UDTTB(respon.rankList);
                break;
            }
            case 1: {
                main.fr_client.UDWTB(respon.rankList);
                break;
            }
            default: break;
        }
    }
    public void check_login(){
        if(respon.data[0].equalsIgnoreCase("true")){
            Data_socket dtsk = new Data_socket();
            ArrayList<User> listOnline = respon.listOnline;
            ArrayList<User> rankList = respon.rankList;
            resetListOnline(listOnline);
            main.my_ID = Integer.parseInt(respon.data[1]);
            main.fr_client.setVisible(true);
            main.fr_client.setTitle("Demo chat java");
            main.full_name = listOnline.get(listOnline.size()-1).getFullname();
            main.fr_client.lb_display_name.setText(main.full_name);
            String totalCore = respon.data[2];
            main.fr_client.lbl_MyTotalCore.setText(totalCore);
            main.fr_client.lb_avatar.setIcon(respon.list_fr.get(0).getImageIcon());
            main.fr_client.UDCTB(rankList);
            main.fr_login.setVisible(false);
            dtsk.action = "loadonline";
            ObjectOutputStream dout;
            try {
                dout = new ObjectOutputStream(main.socket.getOutputStream());
                dout.writeObject(dtsk);
            } catch (IOException ex) {
                Logger.getLogger(Client_controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        else if(respon.data[0].equalsIgnoreCase("online")){
            JOptionPane.showMessageDialog(null, "This account is online, cant login!");
        }
        else{
            JOptionPane.showMessageDialog(null, "username or password is not correct");
        }
    }
    public void respon_reg(Data_socket respon){
        if(respon.data[0].equalsIgnoreCase("true")){
            JOptionPane.showMessageDialog(null, "Đăng ký thành công");
            main.fr_reg.setVisible(false);
            main.fr_login.setVisible(true);
        }else{
            JOptionPane.showMessageDialog(null, "Tên người dùng đã tồn tại");
        }
    }
    public void resetListOnline(ArrayList<User> list){
        main.listOnline.clear();
        main.listOnline.addAll(list);
    }
    public void loadOnline(ArrayList<User> list){
        int size = list.size();
        int ID = main.my_ID;
        DefaultListModel<User> defaultListModel = new DefaultListModel<>();
        for(User u : list){
            //Chat_client.list_friend.add(data.get(i));
            System.out.println(u.getIsOnline());
           if(u.getID() != ID){
               defaultListModel.addElement(u);
           }
        }
        System.out.println(list);
        fr_client.list_online.setModel(defaultListModel);
        fr_client.list_online.setCellRenderer(new fr_list_model());
    }
}
