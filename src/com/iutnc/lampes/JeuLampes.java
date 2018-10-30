package com.iutnc.lampes;

import javax.swing.JLabel;

public class JeuLampes {
	private Plateau plateau;
	private Menu menu;
	private JLabel lblInfo;
	private State state = State.IDLE;
	
	private int nbCoups;

	public JeuLampes() {
		
	}
	
	public JeuLampes(int x, int y) {
		menu = new Menu(this);
		plateau = new Plateau(this, x, y);
		lblInfo = new JLabel("compteur");
	}

	public void setComponents(Plateau p, Menu m, JLabel info) {
		menu = m;
		plateau = p;
		lblInfo = info;
		printInfo("");
	}
	
	private void setState(State st) {
		String s = "";
		switch (st) {
			case PLAY:
				s = "Eteins-les\ntoutes !";
				//s = "<html>################<br>################<br>################";
				//s = "<html>###########<br>###########<br>###########";
				break;
			case CONFIG:
				s = "- Mode édition -";
				break;
			case FINISH:
				s = "Gagné en " + nbCoups + " coups !";
				break;
			case IDLE:
				break;
			default:
				break;
		}
		
		if (!s.equals(""))
			printInfo(convertToMultiline(s));

		state = st;
	}
	
	
	/**
	 * Lance la partie
	 */
	protected void play() {
		if (plateau.isFinished())
			plateau.placerLumieresHasard();
		
		setState(State.PLAY);
		plateau.animateTransition();
		menu.updateButtons();
	}

	/**
	 * Ouvre l'éditeur
	 */
	public void configure() {
		setState(State.CONFIG);
		plateau.animateTransition();
		menu.updateButtons();
	}

	/**
	 * Quitte la partie ou l'éditeur
	 */
	public void quit() {
		resetNbCoup();
		if (state == State.PLAY)
			printInfo("Abandon...");
		setState(State.IDLE);
		plateau.animateTransition();
		menu.updateButtons();
	}
	
	/**
	 * Allume aléatoirement les lampes
	 */
	public void random() {
		int nbLamp = 8;
		plateau.setNbCaseAleatoire(nbLamp);
		plateau.placerLumieresHasard();
	}

	public void finish() {
		setState(State.FINISH);
		menu.updateButtons();
	}

	public void printInfo(String info) {
		if (lblInfo != null)
			lblInfo.setText(info);
	}

	private void printNbCoups(int nb) {
		if (lblInfo != null)
			lblInfo.setText("Essais : " + nb);
	}
	
	private String convertToMultiline(String s) {
		return "<html><body style='text-align: center'>" + s.replaceAll("\n", "<br>");
	}
	
	
	// GETTER - SETTER
	

	public Plateau getPlateau() {
		return plateau;
	}

	public Menu getMenu() {
		return menu;
	}

	public JLabel getLblCompteur() {
		return lblInfo;
	}
	
	public State getState() {
		return state;
	}

	public void incNbCoups() {
		printNbCoups(++nbCoups);
	}

	public void resetNbCoup() {
		nbCoups = 0;
	}
}
