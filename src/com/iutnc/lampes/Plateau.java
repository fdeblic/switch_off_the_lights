package com.iutnc.lampes;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Plateau extends JPanel implements MouseListener, MouseMotionListener {
	private int tailleCase = 64;
	private int nbCaseAllumeesDepart = 8;
	private boolean lamps[][];
	private float progress;
	private boolean animationRuning = false;
	private int hoverX = -1;
	private int hoverY = -1;
	private JeuLampes c;

	BufferedImage imgLamp = null;
	
	/**
	 * Constructeur
	 * @param x
	 * @param y
	 */
	public Plateau(JeuLampes controller, int x, int y) {
		this.c = controller;
		if (x < 5) x = 5;
		if (y < 5) y = 5;
		lamps = new boolean[x][y];
		addMouseListener(this);
		addMouseMotionListener(this);
		Dimension dimension = new Dimension(400, 400);
        this.setMinimumSize(dimension);
        this.setPreferredSize(dimension);
        setOpaque(true);
        setBackground(Color.decode("#123456"));
        
        // Charge l'image
 		try {
 			imgLamp = ImageIO.read(new File("light.png"));
 		} catch (IOException e) {
 			System.out.println("Erreur : impossible d'ouvrir l'image 'light.png'");
 		}
	}
	
	/**
	 * Dessine le plateau
	 */
	@Override
	protected void paintComponent(Graphics g) {
		if (!animationRuning) super.paintComponent(g);
		
		// Variables
		Graphics2D g2d = (Graphics2D) g;
		
		Image imgOn = null; 
		Image imgOff = null;

		if (c.getState() == State.CONFIG)
			imgOn = imgLamp.getSubimage(128, 0, 64, 64);
		else
			imgOn = imgLamp.getSubimage(64, 0, 64, 64);
		
		tailleCase = getHeight() > getWidth() ? getWidth()/lamps.length : getHeight()/lamps[0].length;
		
		imgOn = imgOn.getScaledInstance(tailleCase, tailleCase, Image.SCALE_DEFAULT);
		imgOff = imgLamp.getSubimage(0, 0, 64, 64).getScaledInstance(tailleCase, tailleCase, Image.SCALE_DEFAULT);
		
		// Antialiasing
		g2d.setStroke(new BasicStroke(3));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (animationRuning)
        	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, progress));
		
		for (int y = 0 ; y < lamps[0].length ; y++) {
			for (int x = 0 ; x < lamps.length ; x++) {
				
				if (lamps[x][y]) {
					g.drawImage(imgOn, tailleCase*x, tailleCase*y, null);
				} else {
					g.drawImage(imgOff, tailleCase*x, tailleCase*y, null);
				}
				g.setColor(new Color(255, 255, 255, 30));
				
				if ((c.getState() == State.PLAY || c.getState() == State.CONFIG) && x == hoverX && y == hoverY) 
					g.fillRect(tailleCase*x, tailleCase*y, tailleCase, tailleCase);
			}
		}
	}
	
	/**
	 * Eteint tout et allume des lampes au hasard
	 * selon l'attribut nbCaseAllumeesDepart
	 */
	public void placerLumieresHasard() {
		reset();
		// Place les premières lumières au hasard
		for (int i = 0 ; i < nbCaseAllumeesDepart ; i ++) {
			int rx, ry;
			do {
				rx = (int) Math.floor(Math.random()*lamps.length);
				ry = (int) Math.floor(Math.random()*lamps[0].length);
			} while (lamps[rx][ry] == true);
			lamps[rx][ry] = true;
		}
		animateTransition();
	}
	
	/**
	 * Allume / éteint une lampe
	 * @param x coordonnée X de la lampe
	 * @param y coordonnée Y de la lampe
	 * @return l'état final de la lampe
	 */
	protected boolean toggleLamp(int x, int y) {
		if (x < 0 || x >= lamps.length) return false;
		if (y < 0 || y >= lamps[x].length) return false;
		return lamps[x][y] = !lamps[x][y];
	}
	
	
	/**
	 * Clic du joueur sur une lampe lors d'une partie
	 * @param x coordonnée X de la lampe
	 * @param y coordonnée Y de la lampe
	 */
	protected void tap(int x, int y) {
		if (x < 0 || x >= lamps.length) return;
		if (y < 0 || y >= lamps[x].length) return;
		
		if (c.getState() == State.PLAY) {
			c.incNbCoups();// nbCoups++;
		}
		toggleLamp(x, y);
		toggleLamp(x+1, y);
		toggleLamp(x-1, y);
		toggleLamp(x, y+1);
		toggleLamp(x, y-1);
		animateTransition();
	}
	
	
	/**
	 *  Vérifie que toutes les lampes sont éteintes
	 * @return true si oui, false sinon
	 */
	protected boolean isFinished() {
		for (int x = 0 ; x < lamps.length ; x++) {
			for (int y = 0 ; y < lamps[x].length ; y++) {
				if (lamps[x][y] == true) return false;
			}
		}
		return true;
	}
	
	/**
	 * Eteint toutes les lampes
	 */
	protected void reset() {
		for (int x = 0 ; x < lamps.length ; x++) {
			for (int y = 0 ; y < lamps[x].length ; y++) {
				lamps[x][y] = false;
			}
		}
		c.resetNbCoup();
	}
	
	/**
	 * Fait la transition animée entre deux affichages
	 */
	public void animateTransition() {
        final int animationTime = 300;
        int framesPerSecond = 25;
        int delay = 1000 / framesPerSecond;
        final long start = System.currentTimeMillis();
        final Timer timer = new Timer(delay, null);
        progress = 0;
        animationRuning = true;
        
        timer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final long now = System.currentTimeMillis();
                final long elapsed = now - start;
                progress = (float)((float) elapsed / animationTime);
                if (progress>1) progress = 1;
                if (progress<0) progress = 0;
                repaint();
            	if (elapsed >= animationTime) {
            		progress = 0;
            		animationRuning = false;
            		timer.stop();
                }
			}
		});
        timer.start();
    }
	
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	
	// ÉVÈNEMENTS
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (c.getState() == State.IDLE || c.getState() == State.FINISH)
			return;
		
		int x = e.getX() / tailleCase;
		int y = e.getY() / tailleCase;
		if (c.getState() == State.CONFIG)
			toggleLamp(x, y);
		else {
			tap(x, y);
			if (c.getState() == State.PLAY && isFinished()) {
				c.finish();
			}
		}
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
		hoverX = -1;
		hoverY = -1;
		repaint();
	}


	
	
	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		hoverX = e.getX() / tailleCase;
		hoverY = e.getY() / tailleCase;
		if (hoverX >= lamps.length || hoverX < 0) hoverX = -1;
		if (hoverY >= lamps[0].length || hoverY < 0) hoverY = -1;
		if ((c.getState() == State.PLAY || c.getState() == State.CONFIG) && hoverX != -1 && hoverY != -1) setCursor(new Cursor(Cursor.HAND_CURSOR));
		else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		repaint();
	}
	
	public void setNbCaseAleatoire(int nbCase) {
		if (nbCase < 1 || nbCase > lamps.length * lamps[0].length)
			nbCase = 8;
		nbCaseAllumeesDepart = nbCase;
	}
	
	public int getNbLamp() {
		return lamps.length * lamps[0].length;
	}
}
