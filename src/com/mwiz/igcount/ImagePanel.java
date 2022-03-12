package com.mwiz.igcount;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image = null;

    public ImagePanel(BufferedImage image) {
       this.image = image;
    }
    
    public void setImage(BufferedImage image) {
    	this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null)
        	g.drawImage(image, 0, 0, this);          
    }

}