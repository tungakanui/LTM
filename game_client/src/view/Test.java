/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import static view.fr_play.IS_RUN;
import model.Data_socket;
import java.awt.Color;
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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class Test extends javax.swing.JDialog implements Runnable {

    public static int tics = 0, secs = 0, mins = 0, hours = 0, timePause = 60, tics2 = 0, secs2 = 0;
    public static boolean IS_RUN = false, CAN_PAUSE = true, IS_PAUSING = false;
    public static String image_ID = "1";
    private Thread timeCounter, timePauseCounter;
    private PuzzleGame puzzle;
//    public  static String idMe ="";
//    public  static String idEnemy = "";

    /**
     * Creates new form Test
     */
    public Test(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setUndecorated(false);
        initComponents();

        new PuzzleGame();
        IS_RUN = true;
        timeCounter = new Thread(this);
        timeCounter.start();
    }

    //Code Game
    public class PuzzleGame {

        private JPanel panel_2;
        private BufferedImage source;
        private BufferedImage resized;
        private Image image;
        private MyButton lastButton;
        private int width, height;

        private List<MyButton> buttons;
        private List<Point> solution;

        private final int NUMBER_OF_BUTTONS = 9;
        private final int DESIRED_WIDTH = 300;

        public PuzzleGame() {
            initUI();
        }

        private void initUI() {
            jTextField2.setText(timePause + "");
            solution = new ArrayList<>();

            solution.add(new Point(0, 0));
            solution.add(new Point(0, 1));
            solution.add(new Point(0, 2));
//        solution.add(new Point(0, 3));
//        solution.add(new Point(0, 4));
            solution.add(new Point(1, 0));
            solution.add(new Point(1, 1));
            solution.add(new Point(1, 2));
//        solution.add(new Point(1, 3));
//        solution.add(new Point(1, 4));
            solution.add(new Point(2, 0));
            solution.add(new Point(2, 1));
            solution.add(new Point(2, 2));
//        solution.add(new Point(2, 3));
//        solution.add(new Point(2, 4));
//        solution.add(new Point(3, 0));
//        solution.add(new Point(3, 1));
//        solution.add(new Point(3, 2));
//        solution.add(new Point(3, 3));
//        solution.add(new Point(3, 4));
//        solution.add(new Point(4, 0));
//        solution.add(new Point(4, 1));
//        solution.add(new Point(4, 2));
//        solution.add(new Point(4, 3));
//        solution.add(new Point(4, 4));

            buttons = new ArrayList<>();

            panel.setBorder(BorderFactory.createLineBorder(Color.gray));
            panel.setLayout(new GridLayout(3, 3, 0, 0));

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

            for (int i = 0; i < 3; i++) {

                for (int j = 0; j < 3; j++) {

                    image = createImage(new FilteredImageSource(resized.getSource(),
                            new CropImageFilter(j * width / 3, i * height / 3,
                                    (width / 3), height / 3)));

                    MyButton button = new MyButton(image);
                    button.putClientProperty("position", new Point(i, j));

                    if (i == 2 && j == 2) {
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
            String id = image_ID + "";
            BufferedImage bimg = ImageIO.read(new File("src/image/ex" + id + ".jpg"));

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
                        || (bidx - 3 == lidx) || (bidx + 3 == lidx)) {
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
                main.done = true;
                IS_RUN = false;
                emitWin();
//            double totalTime = hours*3600 + mins*60 + secs + (double)(tics/100);
//            JOptionPane.showMessageDialog(panel, "Finished",
//                    "Congratulation! You win with " + totalTime + " seconds" , JOptionPane.INFORMATION_MESSAGE);
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
    //End Code Game

    @Override
    public void run() {
        try {
            while (IS_RUN) {
                tics++;
                if (tics == 100) {
                    secs += 1;
                    tics = 0;
                }
                if (secs == 60) {
                    mins += 1;
                    secs = 0;
                }
                if (mins == 60) {
                    hours += 1;
                    mins = 0;
                }
                txtTimes.setText(hours + " : " + mins + " : " + secs + " : " + tics);
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
        jLabel2 = new javax.swing.JLabel();
        txtTimes = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        panel = new javax.swing.JPanel();
        fr_loadImage2 = new view.fr_loadImage();
        jButton2 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("GAMING AREA");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Ảnh mẫu");

        jLabel2.setText("Thời gian");

        txtTimes.setEditable(false);
        txtTimes.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        txtTimes.setForeground(new java.awt.Color(255, 51, 51));
        txtTimes.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtTimes.setText("jTextField1");

        jButton1.setText("Thoát");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout fr_loadImage2Layout = new javax.swing.GroupLayout(fr_loadImage2);
        fr_loadImage2.setLayout(fr_loadImage2Layout);
        fr_loadImage2Layout.setHorizontalGroup(
            fr_loadImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 321, Short.MAX_VALUE)
        );
        fr_loadImage2Layout.setVerticalGroup(
            fr_loadImage2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 295, Short.MAX_VALUE)
        );

        jButton2.setText("Pause");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField2.setEditable(false);
        jTextField2.setText("jTextField2");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(fr_loadImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(188, 188, 188)
                        .addComponent(jLabel2)
                        .addGap(51, 51, 51)
                        .addComponent(txtTimes, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel1)
                        .addGap(119, 119, 119)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jButton2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(23, 23, 23)
                        .addComponent(fr_loadImage2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(80, 80, 80)
                        .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(txtTimes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int confirm = JOptionPane.showConfirmDialog(this, "Chấp nhận thua game này !?", "Ok?", JOptionPane.YES_NO_OPTION);
        switch (confirm) {
            case JOptionPane.YES_OPTION: {
                IS_RUN = false;
                emitLost();
                JOptionPane.showMessageDialog(this, "Thua ở time " + hours + " : " + mins + " : " + secs + " : " + tics + "");
                this.dispose();
                break;
            }
            default:
                break;
        }
    }//GEN-LAST:event_jButton1ActionPerformed
    private void emitWin() {
        Data_socket dtsk = new Data_socket();
        double totalTime = hours * 3600 + mins * 60 + secs + (double) (tics / 100);
        dtsk.action = "emitWin";
        String[] data = new String[4];
        data[0] = main.my_ID + "";
        data[1] = main.my_EnemyID + "";

        data[2] = main.full_name;
        data[3] = totalTime + "";
        dtsk.data = data;
        try {
            ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void emitPause() {
        Data_socket dtsk = new Data_socket();
        dtsk.action = "onPause";
        String[] data = new String[3];
        data[0] = main.my_ID + "";
        data[1] = "";
        data[2] = main.full_name + "";
        dtsk.data = data;
        try {
            ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void emitResume() {
        Data_socket dtsk = new Data_socket();
        dtsk.action = "onResume";
        String[] data = new String[3];
        data[0] = main.my_ID + "";
        data[1] = "";
        data[2] = main.full_name + "";
        dtsk.data = data;
        try {
            ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void emitLost() {
        Data_socket dtsk = new Data_socket();
        double totalTime = hours * 3600 + mins * 60 + secs + (double) (tics / 100);
        dtsk.action = "emitLost";
        String[] data = new String[3];
        data[0] = main.my_ID + "";
        data[1] = main.my_EnemyID + "";
        data[2] = totalTime + "";
        dtsk.data = data;
        try {
            ObjectOutputStream dout = new ObjectOutputStream(main.socket.getOutputStream());
            dout.writeObject(dtsk);
            dout.flush();
        } catch (IOException ex) {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed

    }//GEN-LAST:event_formWindowClosed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int confirm = JOptionPane.showConfirmDialog(this, "Chấp nhận thua game này !?", "Ok?", JOptionPane.YES_NO_OPTION);
        switch (confirm) {
            case JOptionPane.YES_OPTION: {
                IS_RUN = false;
                emitLost();
                JOptionPane.showMessageDialog(this, "Thua ở time " + hours + " : " + mins + " : " + secs + " : " + tics + "");
                this.dispose();
                break;
            }
            default:
                break;
        }
    }//GEN-LAST:event_formWindowClosing

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {

            IS_PAUSING = !IS_PAUSING;
            if (IS_PAUSING) {
                jButton2.setText("Resume");
            } else {
                jButton2.setText("Pause");
            }

            while (IS_PAUSING && timePause > 0) {
                MyTimer myTimer = new MyTimer(timePause);
                myTimer.run();
                jTextField2.setText(timePause + "");
                if (timePause == 0) {
                    jButton2.setText("Pause");
                    CAN_PAUSE = false;
                    jButton2.setEnabled(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Test.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                Test dialog = new Test(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private view.fr_loadImage fr_loadImage2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JPanel panel;
    private javax.swing.JTextField txtTimes;
    // End of variables declaration//GEN-END:variables
}

