package com.iutnc.lampes;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;

public class GButton extends JButton implements MouseListener{
    /**
	 * GameButton
	 */
	private static final long serialVersionUID = -5892960484439127294L;
	Font defaultFont = new Font("Should've Known",Font.PLAIN,24);
    enum State {NORMAL, HOVER, ACTIVE, DISABLED}
    
    final Color COL_PLAIN = Color.decode("#bbbbbb");
    final Color COL_HOVER = Color.decode("#4b9788");
    final Color COL_DISAB = Color.decode("#333333");
    Color textColor = COL_PLAIN;
    State currentState = State.NORMAL;
    
    BufferedImage bgImage;
    
    
    public GButton(String s) {
    	try {
    		bgImage = ImageIO.read(new File("bg_btn.png"));
		} catch (IOException e) {
			System.out.println("Impossible d'ouvrir l'image 'bg_btn.png'");
			e.printStackTrace();
		}
    	
    	s = s.toUpperCase();
        this.setFocusPainted(false);
        this.setText(s);
        this.setBorder(null);
        this.setForeground(textColor);
        this.setFont(defaultFont);
        this.setOpaque(false);
        this.addMouseListener(this);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setMinimumSize(new Dimension(200, 75));
        this.setPreferredSize(new Dimension(250, 75));
    }
    
    @Override
    protected void paintComponent(Graphics g) {
    	// Antialiasing
		Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    	
    	BufferedImage img;
    	switch (currentState) {
			case NORMAL:
			default:
				textColor = COL_PLAIN;
				img = bgImage.getSubimage(0, 0, 474, 170);
			break;
			case HOVER:
				textColor = COL_HOVER;
				img = bgImage.getSubimage(0, 170, 474, 170);
				break;
			case ACTIVE:
				textColor = COL_HOVER;
				img = bgImage.getSubimage(0, 2*170, 474, 170);
				break;
			case DISABLED:
				textColor = COL_DISAB;
				img = bgImage.getSubimage(0, 3*170, 474, 170);
				break;
    	}
    	
    	g.drawImage(img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_DEFAULT), 0, 0, null);
		
		g.setFont(defaultFont);
		
		FontMetrics metrics = g.getFontMetrics(defaultFont);
		int x = 0 + (getWidth() - metrics.stringWidth(getText())) / 2;
		int y = 0 + ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		g.setColor(textColor);
		g.drawString(getText(), x, y);
    }
    
    @Override
    public void setEnabled(boolean b) {
    	super.setEnabled(b);
    	if (b) {
    		textColor = COL_PLAIN;
    	} else {
    		textColor = COL_DISAB;
    	}
    	
    	currentState = b ? State.NORMAL : State.DISABLED;
    	//this.setForeground(textColor);
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {
    }
    
    @Override
    public void mouseReleased(MouseEvent me) {
    	if (me.getSource()==this && isEnabled()) { 
        	currentState = State.NORMAL;
            repaint();
        }
    }
    @Override
    public void mousePressed(MouseEvent me) {
    	if (me.getSource()==this && isEnabled()) { 
        	currentState = State.ACTIVE;
            repaint();
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e) { 
        if (e.getSource()==this && isEnabled()) {
        	currentState = State.HOVER;
        	repaint();
        }
    }
    @Override
    public void mouseExited(MouseEvent e) { 
        if (e.getSource()==this && isEnabled()) { 
        	currentState = State.NORMAL;
            repaint();
        }
    }
}