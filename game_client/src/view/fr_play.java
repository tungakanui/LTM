/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class fr_play extends javax.swing.JFrame
        implements Runnable{

    /**
     * Creates new form fr_play
     */
    private static int tics = 0, secs = 0, mins = 0, hours = 0, timePause = 60;
     public static boolean IS_RUN = false, IS_EXPIRED = false;
    private Thread timeCounter;
    private PuzzleGame puzzle;
    public fr_play() {

        initComponents();
        new PuzzleGame();
        IS_RUN = true;
        timeCounter = new Thread (this);
        timeCounter.start();
        
    }
    
    public class PuzzleGame{

    private JPanel panel_2;
    private BufferedImage source;
    private BufferedImage resized;    
    private Image image;
    private MyButton lastButton;
    private int width, height;   
    
    private List<MyButton> buttons;
    private List<Point> solution;

    private final int NUMBER_OF_BUTTONS = 25;
    private final int DESIRED_WIDTH = 300;
    public PuzzleGame() {
        initUI();
    }

    private void initUI() {

        solution = new ArrayList<>();
        
        solution.add(new Point(0, 0));
        solution.add(new Point(0, 1));
        solution.add(new Point(0, 2));
        solution.add(new Point(0, 3));
        solution.add(new Point(0, 4));
        solution.add(new Point(1, 0));
        solution.add(new Point(1, 1));
        solution.add(new Point(1, 2));
        solution.add(new Point(1, 3));
        solution.add(new Point(1, 4));
        solution.add(new Point(2, 0));
        solution.add(new Point(2, 1));
        solution.add(new Point(2, 2));
        solution.add(new Point(2, 3));
        solution.add(new Point(2, 4));
        solution.add(new Point(3, 0));
        solution.add(new Point(3, 1));
        solution.add(new Point(3, 2));
        solution.add(new Point(3, 3));
        solution.add(new Point(3, 4));
        solution.add(new Point(4, 0));
        solution.add(new Point(4, 1));
        solution.add(new Point(4, 2));
        solution.add(new Point(4, 3));
        solution.add(new Point(4, 4));

        buttons = new ArrayList<>();

//        panel = new JPanel();
        panel.setBorder(BorderFactory.createLineBorder(Color.gray));
        panel.setLayout(new GridLayout(5, 5, 0, 0));

        try {
            source = loadImage();
            int h = getNewHeight(source.getWidth(), source.getHeight());
            resized = resizeImage(source, DESIRED_WIDTH, h,
                    BufferedImage.TYPE_INT_ARGB);

        } catch (IOException ex) {
            Logger.getLogger(PuzzleGame.class.getName()).log(
                    Level.SEVERE, null, ex);
        }

        width = resized.getWidth(null);
        height = resized.getHeight(null);
//        add(panel, BorderLayout.CENTER);
        for (int i = 0; i < 5; i++) {

            for (int j = 0; j < 5; j++) {

                image = createImage(new FilteredImageSource(resized.getSource(),
                        new CropImageFilter(j * width / 5, i * height / 5,
                                (width / 5), height / 5)));
                
                MyButton button = new MyButton(image);
                button.putClientProperty("position", new Point(i, j));

                if (i == 4 && j == 4) {
                    lastButton = new MyButton();
                    lastButton.setBorderPainted(false);
                    lastButton.setContentAreaFilled(false);
                    lastButton.setLastButton();
                    lastButton.putClientProperty("position", new Point(i, j));
                } else {
                    buttons.add(button);
                }
            }
        }

        Collections.shuffle(buttons);
        buttons.add(lastButton);

        for (int i = 0; i < NUMBER_OF_BUTTONS; i++) {

            MyButton btn = buttons.get(i);
            panel.add(btn);
            btn.setBorder(BorderFactory.createLineBorder(Color.gray));
            btn.addActionListener(new ClickAction());
        }
//      
    }

    private int getNewHeight(int w, int h) {

        double ratio = DESIRED_WIDTH / (double) w;
        int newHeight = (int) (h * ratio);
        return newHeight;
    }

    private BufferedImage loadImage() throws IOException {

        BufferedImage bimg = ImageIO.read(new File("src/image/th.jpg"));

        return bimg;
    }

    private BufferedImage resizeImage(BufferedImage originalImage, int width,
            int height, int type) throws IOException {

        BufferedImage resizedImage = new BufferedImage(width, height, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, width, height, null);
        g.dispose();

        return resizedImage;
    }

    private class ClickAction extends AbstractAction {

        @Override
        public void actionPerformed(ActionEvent e) {

            checkButton(e);
            checkSolution();
        }

        private void checkButton(ActionEvent e) {

            int lidx = 0;
            
            for (MyButton button : buttons) {
                if (button.isLastButton()) {
                    lidx = buttons.indexOf(button);
                }
            }

            JButton button = (JButton) e.getSource();
            int bidx = buttons.indexOf(button);

            if ((bidx - 1 == lidx) || (bidx + 1 == lidx)
                    || (bidx - 5 == lidx) || (bidx + 5 == lidx)) {
                Collections.swap(buttons, bidx, lidx);
                updateButtons();
            }
        }

        private void updateButtons() {

            panel.removeAll();

            for (JComponent btn : buttons) {

                panel.add(btn);
            }

            panel.validate();
        }
                 
        
    }

    private void checkSolution() {

        List<Point> current = new ArrayList<>();

        for (JComponent btn : buttons) {
            current.add((Point) btn.getClientProperty("position"));
        }

        if (compareList(solution, current)) {
            JOptionPane.showMessageDialog(panel, "Finished",
                    "Congratulation", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public boolean compareList(List ls1, List ls2) {
        
        return ls1.toString().contentEquals(ls2.toString());
    }
    
    

}
    class MyButton extends JButton {

    private boolean isLastButton;

    public MyButton() {

        super();

        initUI();
    }

    public MyButton(Image image) {

        super(new ImageIcon(image));

        initUI();
    }

    private void initUI() {

        isLastButton = false;
        BorderFactory.createLineBorder(Color.gray);

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.yellow));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(BorderFactory.createLineBorder(Color.gray));
            }
        });
    }

    public void setLastButton() {
        
        isLastButton = true;
    }

    public boolean isLastButton() {

        return isLastButton;
    }
}
    @Override
    public void run(){
        try {
            while(IS_RUN){
                tics++;
                if(tics== 100){
                    secs+=1;
                    tics = 0;
                }
                if(secs== 60){
                    mins+=1;
                    secs = 0;
                }
                if(mins== 60){
                    hours+=1;
                    mins= 0;
                }
                jTextField1.setText(hours + " : " + mins +" : " + secs + " : " + tics);
                txtTimes.setText(hours + " : " + mins +" : " + secs + " : " + tics);
                Thread.sleep(10);
            }   
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
   
    
  



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTimes = new javax.swing.JTextField();
        fr_loadImage1 = new view.fr_loadImage();
        panel = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setText("Time");

        jButton1.setText("Thoát");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Ảnh mẫu");

        txtTimes.setEditable(false);
        txtTimes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTimes.setForeground(new java.awt.Color(255, 0, 0));
        txtTimes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTimes.setText("Thời gian đã chơi");
        txtTimes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fr_loadImage1Layout = new javax.swing.GroupLayout(fr_loadImage1);
        fr_loadImage1.setLayout(fr_loadImage1Layout);
        fr_loadImage1Layout.setHorizontalGroup(
            fr_loadImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 305, Short.MAX_VALUE)
        );
        fr_loadImage1Layout.setVerticalGroup(
            fr_loadImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 301, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton2.setText("Pause");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setEditable(false);
        jTextField1.setText("jTextField1");
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fr_loadImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59))
            .addGroup(layout.createSequentialGroup()
                .addGap(130, 130, 130)
                .addComponent(jLabel2)
                .addGap(120, 120, 120)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(114, 114, 114))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(154, 154, 154))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(68, 68, 68)
                        .addComponent(txtTimes, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(jButton1)
                        .addGap(146, 146, 146))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 364, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addGap(26, 26, 26))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap(14, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addGap(30, 30, 30))
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton2)
                                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(fr_loadImage1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtTimes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Chấp nhận thua game này !?"," " , JOptionPane.YES_NO_OPTION);
        switch(confirm){
            case JOptionPane.YES_OPTION:{
                IS_RUN = false;
                JOptionPane.showMessageDialog(this, "Thua ở " + hours + " : " + mins +" : " + secs + " : " + tics +" !");
                this.dispose();
//                this.puzzle.dispose();
                break;
            }
            default: break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
       int confirm = JOptionPane.showConfirmDialog(this, "Chấp nhận thua game này !?"," " , JOptionPane.YES_NO_OPTION);
        switch(confirm){
            case JOptionPane.YES_OPTION:{
                IS_RUN = false;
                JOptionPane.showMessageDialog(this, "Thua ở " + hours + " : " + mins +" : " + secs + " : " + tics +" ! Nhục vkl!");
                this.dispose();
                break;
            }
            default: break;
        }
    }//GEN-LAST:event_formWindowClosing

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        JOptionPane.showMessageDialog(null, "Thua ở time " + hours + " : " + mins +" : " + secs + " : " + tics +" ! Nhục vkl!");
    }//GEN-LAST:event_formWindowClosed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void txtTimesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimesActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        while (IS_EXPIRED) { 
            try {
                jTextField1.setText(String.valueOf(timePause));
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(fr_play.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(fr_play.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(fr_play.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(fr_play.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(fr_play.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new fr_play().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private view.fr_loadImage fr_loadImage1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txtTimes;
    // End of variables declaration//GEN-END:variables
}
