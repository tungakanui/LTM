/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import model.User;
import model.Data_socket;
import model.Friend;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class fr_client extends javax.swing.JFrame{

    ObjectOutputStream dout = null;
    ObjectInputStream din = null;
    
    DefaultTableModel model_1;
    public static ArrayList<User> listRank;
    
    public fr_client() {
        listRank = new ArrayList<>();
        model_1 = new DefaultTableModel();
        
        
        initComponents();
        this.lb_avatar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() ==2){
                    fr_change_info fr = new fr_change_info();
                    fr.lb_avatar.setIcon(main.icon);
                    fr.txt_name.setText(main.full_name);
                    fr.setVisible(true);
                }
            }
        });
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width-this.getSize().width-50, 50);
        list_online.requestFocus();
        
        model_1 = (DefaultTableModel) rankTable.getModel();
        
    }
    
    public void setListRank(ArrayList<User> list){
        listRank.clear();
        listRank.addAll(list);
    }
    
    public void UDCTB (ArrayList<User> list){ // rank follow core
        setListRank(list);
        model_1.setRowCount(0);
        rankTable.removeAll();
        int stt = 1;
        for(User u : list){
            Object[] os = new Object[]{stt, u.getFullname(), u.getTotalCore()};
            model_1.addRow(os);
            stt++;
        }
        rankTable.setModel(model_1);
    }
    
    public void UDTTB (ArrayList<User> list){ // rank follow time win
        setListRank(list);
        model_1.setRowCount(0);
        rankTable.removeAll();
        int stt = 1;
        for(User u : list){
            Object[] os = new Object[]{stt, u.getFullname(), u.getTotalTimePlayed()};
            model_1.addRow(os);
            stt++;
        }
        rankTable.setModel(model_1);
    }
    
    public void UDWTB (ArrayList<User> list){ // rank follow enemy
        setListRank(list);
        model_1.setRowCount(0);
        rankTable.removeAll();
        int stt = 1;
        for(User u : list){
            if(u.getTotalGames() != 0){
                Object[] os = new Object[]{stt, u.getFullname(), (double)Math.round((u.getTotalCore()/(double)u.getTotalGames())*10)/10};
                model_1.addRow(os);
                stt++;
            }
            
        }
        rankTable.setModel(model_1);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        list_online = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        lb_avatar = new javax.swing.JLabel();
        lb_display_name = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        rankTable = new javax.swing.JTable();
        jComboBox2 = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lbl_MyTotalCore = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");
        jPopupMenu1.add(jMenuItem1);

        jMenuItem2.setText("jMenuItem2");
        jPopupMenu1.add(jMenuItem2);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(204, 204, 204));
        setLocation(new java.awt.Point(0, 0));
        setResizable(false);
        setSize(new java.awt.Dimension(500, 780));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        list_online.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_onlineMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(list_online);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel1.setText("DANH SÁCH NGƯỜI CHƠI");

        lb_avatar.setBorder(BorderFactory.createLineBorder(new Color(0,255,0),2));

        lb_display_name.setBackground(new java.awt.Color(204, 204, 204));
        lb_display_name.setFont(new java.awt.Font("Times New Roman", 1, 20)); // NOI18N
        lb_display_name.setForeground(new java.awt.Color(0, 204, 204));
        lb_display_name.setText("Họ tên");
        lb_display_name.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 12)); // NOI18N
        jLabel2.setText("BẢNG XẾP HẠNG");

        rankTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rank", "Tên", "Chỉ số"
            }
        ));
        jScrollPane2.setViewportView(rankTable);

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Total Core", "Total Core ( Enemy)", "AV Times" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel3.setText("Xếp theo");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Tổng điểm của bạn:");

        lbl_MyTotalCore.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbl_MyTotalCore.setForeground(new java.awt.Color(255, 51, 51));
        lbl_MyTotalCore.setText("ĐIỂM");

        jButton1.setText("Chiến hết");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lb_display_name, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(4, 4, 4))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lbl_MyTotalCore)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 157, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(65, 65, 65))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(lb_display_name, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(lbl_MyTotalCore))))
                .addGap(26, 26, 26)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addGap(11, 11, 11)
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void list_onlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_onlineMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
            User selectedUser = list_online.getSelectedValue();
            if(selectedUser.getIsOnline() == 1){
                int confirm = JOptionPane.showConfirmDialog(null, "Thách đấu với: "+selectedUser.getFullname()+" (ID: "+ selectedUser.getID() + ")");
                switch (confirm) {
                    case JOptionPane.YES_OPTION:
                        System.out.println("Chấp nhận thách đấu với "+selectedUser.getID());
                        Data_socket dtsk = new Data_socket();
                        dtsk.action = "challenge";
                        String[] data = new String[3];
                        data[0] = main.my_ID +""; // sender id
                        data[1] = main.full_name; // sender name
                        data[2] = selectedUser.getID() + ""; // receiver id
                        
                        dtsk.data = data;

                        {
                            try {
                                ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
                                dout.writeObject(dtsk);
                                dout.flush();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                        break;
                    case JOptionPane.NO_OPTION:
                        System.out.println("no "+selectedUser.getFullname());
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        System.out.println("cancel "+selectedUser.getFullname());
                        break;
                    default:
                        break;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Người chơi đang bận! Không thể thách đấu!");
            }
        }
    }//GEN-LAST:event_list_onlineMouseClicked

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            Data_socket dtsk = new Data_socket();
            dtsk.action = "logout";
            String[] data = new String[1];
            data[0] = main.my_ID + "";
            dtsk.data = data;
            dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
            this.dispose();
            
        } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_formWindowClosing

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        try {
            int indexSelected = jComboBox2.getSelectedIndex();
            Data_socket dtsk = new Data_socket();
            String[] data = new String[2];
            data[0] = indexSelected + "";
            data[1] = main.my_ID + "";
            dtsk.data = data;
            dtsk.action = "updateranktable";
            dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        emitMultiChallenge();
    }//GEN-LAST:event_jButton1ActionPerformed
// 
//    public void chat(int index){
//        Friend fr = Chat_client.list_friend.get(index);
//        if(!Chat_client.list_fr_chat.isEmpty()){
//            for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
//                if(Chat_client.list_fr_chat.get(i).fr_ID == fr.getID()){
//                    Chat_client.list_fr_chat.get(i).setVisible(true);
//                    return;
//                }
//            }
//        }
//        fr_chat chat = new fr_chat();
//        chat.fr_ID = fr.getID();
//        chat.setTitle("Nhắn tin với: "+fr.getFull_name()+fr.getID());
//        chat.fr_name = fr.getFull_name();
//        chat.lb_avartar_me.setIcon(Chat_client.icon);
//        chat.icon_fr = new ImageIcon(Chat_client.list_friend.get(index).getImageIcon().getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
//        chat.lb_avaratar_fr.setIcon(chat.icon_fr);
//        if(fr.getonline() == 1 ){
//            chat.btn_call.setEnabled(true);
//        }else{
//            chat.btn_call.setEnabled(false);
//        }
//        chat.load_history();
//        chat.setVisible(true);
//        Chat_client.list_fr_chat.add(chat);
//    }// 
//    public void chat(int index){
//        Friend fr = Chat_client.list_friend.get(index);
//        if(!Chat_client.list_fr_chat.isEmpty()){
//            for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
//                if(Chat_client.list_fr_chat.get(i).fr_ID == fr.getID()){
//                    Chat_client.list_fr_chat.get(i).setVisible(true);
//                    return;
//                }
//            }
//        }
//        fr_chat chat = new fr_chat();
//        chat.fr_ID = fr.getID();
//        chat.setTitle("Nhắn tin với: "+fr.getFull_name()+fr.getID());
//        chat.fr_name = fr.getFull_name();
//        chat.lb_avartar_me.setIcon(Chat_client.icon);
//        chat.icon_fr = new ImageIcon(Chat_client.list_friend.get(index).getImageIcon().getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
//        chat.lb_avaratar_fr.setIcon(chat.icon_fr);
//        if(fr.getonline() == 1 ){
//            chat.btn_call.setEnabled(true);
//        }else{
//            chat.btn_call.setEnabled(false);
//        }
//        chat.load_history();
//        chat.setVisible(true);
//        Chat_client.list_fr_chat.add(chat);
//    }// 
//    public void chat(int index){
//        Friend fr = Chat_client.list_friend.get(index);
//        if(!Chat_client.list_fr_chat.isEmpty()){
//            for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
//                if(Chat_client.list_fr_chat.get(i).fr_ID == fr.getID()){
//                    Chat_client.list_fr_chat.get(i).setVisible(true);
//                    return;
//                }
//            }
//        }
//        fr_chat chat = new fr_chat();
//        chat.fr_ID = fr.getID();
//        chat.setTitle("Nhắn tin với: "+fr.getFull_name()+fr.getID());
//        chat.fr_name = fr.getFull_name();
//        chat.lb_avartar_me.setIcon(Chat_client.icon);
//        chat.icon_fr = new ImageIcon(Chat_client.list_friend.get(index).getImageIcon().getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
//        chat.lb_avaratar_fr.setIcon(chat.icon_fr);
//        if(fr.getonline() == 1 ){
//            chat.btn_call.setEnabled(true);
//        }else{
//            chat.btn_call.setEnabled(false);
//        }
//        chat.load_history();
//        chat.setVisible(true);
//        Chat_client.list_fr_chat.add(chat);
//    }// 
//    public void chat(int index){
//        Friend fr = Chat_client.list_friend.get(index);
//        if(!Chat_client.list_fr_chat.isEmpty()){
//            for(int i = 0; i< Chat_client.list_fr_chat.size();i++){
//                if(Chat_client.list_fr_chat.get(i).fr_ID == fr.getID()){
//                    Chat_client.list_fr_chat.get(i).setVisible(true);
//                    return;
//                }
//            }
//        }
//        fr_chat chat = new fr_chat();
//        chat.fr_ID = fr.getID();
//        chat.setTitle("Nhắn tin với: "+fr.getFull_name()+fr.getID());
//        chat.fr_name = fr.getFull_name();
//        chat.lb_avartar_me.setIcon(Chat_client.icon);
//        chat.icon_fr = new ImageIcon(Chat_client.list_friend.get(index).getImageIcon().getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT));
//        chat.lb_avaratar_fr.setIcon(chat.icon_fr);
//        if(fr.getonline() == 1 ){
//            chat.btn_call.setEnabled(true);
//        }else{
//            chat.btn_call.setEnabled(false);
//        }
//        chat.load_history();
//        chat.setVisible(true);
//        Chat_client.list_fr_chat.add(chat);
//    }
    
   public void emitMultiChallenge(){
       try {
           Data_socket dtsk = new Data_socket();
           dtsk.action = "emitMultiChallenge";
           String[] data = new String[2];
           data[0] = String.valueOf(main.my_ID);
           data[1] = main.full_name;
           dtsk.data = data;
           dout = new ObjectOutputStream(main.socket.getOutputStream());
           dout.writeObject(dtsk);
       } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
   } 
    
   public void load_friend(int status){
        try {
            Data_socket dtsk = new Data_socket();
            dtsk.action = "loadfriend";
            String[] data = new String[1];
            data[0] = String.valueOf(status);
            dtsk.data = data;
            dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            
        } catch (IOException ex) {
            Logger.getLogger(fr_client.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fr_client.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new fr_client().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JLabel lb_avatar;
    public javax.swing.JLabel lb_display_name;
    public javax.swing.JLabel lbl_MyTotalCore;
    public static javax.swing.JList<User> list_online;
    private javax.swing.JTable rankTable;
    // End of variables declaration//GEN-END:variables


}
