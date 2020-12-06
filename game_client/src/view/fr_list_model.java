
package view;

import model.User;
import model.Friend;
import java.awt.Component;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


public  class fr_list_model extends javax.swing.JPanel implements ListCellRenderer<User>{


    public fr_list_model() {
        initComponents();
    }
   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lb_avatar = new javax.swing.JLabel();
        lb_name = new javax.swing.JLabel();
        lb_status = new javax.swing.JLabel();
        txt_status = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(500, 104));

        lb_avatar.setBackground(new java.awt.Color(51, 51, 255));
        lb_avatar.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lb_avatar.setText("avartar");
        lb_avatar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 102)));

        lb_name.setFont(new java.awt.Font("Times New Roman", 3, 13)); // NOI18N
        lb_name.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lb_name.setText("jLabel2");
        lb_name.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        txt_status.setFont(new java.awt.Font("Tahoma", 2, 13)); // NOI18N
        txt_status.setText("jLabel1");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(118, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_status, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_status)
                            .addComponent(lb_name, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47))))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(126, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lb_status, javax.swing.GroupLayout.DEFAULT_SIZE, 21, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_name)
                .addGap(18, 18, 18)
                .addComponent(txt_status, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lb_avatar, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addContainerGap()))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lb_avatar;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_status;
    private javax.swing.JLabel txt_status;
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getListCellRendererComponent(JList<? extends User> jlist, User e, int i, boolean bln, boolean bln1) {
        /*
        1 on
        3 busy
        
        
        */
        
        ImageIcon icon = null;

        switch (e.getIsOnline()) {
            case 1:
                icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("image/online.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                break;
            case 2:
                icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("image/offline.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                break;
            default:
                icon = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource("image/busy.png")).getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
                break;
        }
        BufferedImage img = null;
        try {
            File file = new File("src/image/user"+e.getID()+".jpg");
            img = ImageIO.read(file);
        } catch (IOException ex) {
            try {
                File file = new File("src/image/avt.jpg");
                img = ImageIO.read(file);
            } catch (IOException ex1) {
                Logger.getLogger(fr_list_model.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        lb_status.setIcon(icon);
        lb_avatar.setIcon(new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(70, 70, Image.SCALE_DEFAULT)));
        lb_name.setText(e.getFullname());
        txt_status.setText(e.getStatus());
        //lb_status.setIcon(icon_status);
        return this;
    }
}
