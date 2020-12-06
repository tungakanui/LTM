/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;
import static view.Test.image_ID;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class fr_loadImage extends JPanel {
    private Image image;
    public fr_loadImage() {
         
        loadImage();
    }
 
    private void loadImage(){
        String id = image_ID + "";
         image = new ImageIcon("src/image/ex"+id+".jpg").getImage();
    }
     
 
    private void doDrawing(Graphics g) {
 
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image, 0, 0,300,300, null);
    }
 
    @Override
    public void paintComponent(Graphics g) {
 
        super.paintComponent(g);
        doDrawing(g);
    }
}
