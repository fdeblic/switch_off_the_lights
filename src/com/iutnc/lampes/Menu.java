package com.iutnc.lampes;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

@SuppressWarnings("serial")
public class Menu extends JPanel {
	
	private GButton btnPlay;
	private GButton btnConfig;
	private GButton btnRandom;
	private GButton btnQuit;
	private JLabel lblCount;
	private JeuLampes controller;
	
	/**
	 * Constructeur
	 */
	public Menu(JeuLampes controller) {
		this.controller = controller;
		this.setOpaque(false);
		Font font = new Font("Tuffy", Font.BOLD, 16);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0};
		gridBagLayout.columnWeights = new double[]{0.0};
		setLayout(gridBagLayout);
		Dimension dimension = new Dimension(260, 445);
		setMinimumSize(dimension);
		setPreferredSize(new Dimension(260, 328));
		
		// PLAY
		btnPlay = new GButton("Jouer");
		GridBagConstraints gbc_btnPlay = new GridBagConstraints();
		gbc_btnPlay.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnPlay.insets = new Insets(0, 0, 5, 0);
		gbc_btnPlay.gridx = 0;
		gbc_btnPlay.gridy = 0;
		add(btnPlay, gbc_btnPlay);
		btnPlay.setFont(font);
		btnPlay.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.play();
			}
		});
		
		
		// CONFIG
		btnConfig = new GButton("Éditer");
		GridBagConstraints gbc_btnConfig = new GridBagConstraints();
		gbc_btnConfig.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnConfig.insets = new Insets(0, 0, 5, 0);
		gbc_btnConfig.gridx = 0;
		gbc_btnConfig.gridy = 1;
		add(btnConfig, gbc_btnConfig);
		btnConfig.setToolTipText("Changez les tiles une à une pour créer votre niveau");
		btnConfig.setFont(font);
		btnConfig.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.configure();
			}
		});
		
		
		// RANDOM
		btnRandom = new GButton("Générer");
		GridBagConstraints gbc_btnRandom = new GridBagConstraints();
		gbc_btnRandom.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRandom.insets = new Insets(0, 0, 5, 0);
		gbc_btnRandom.gridx = 0;
		gbc_btnRandom.gridy = 2;
		add(btnRandom, gbc_btnRandom);
		btnRandom.setFont(font);
		btnRandom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.random();
			}
		});
		
		
		// QUIT
		btnQuit = new GButton("Arrêter");
		GridBagConstraints gbc_btnQuit = new GridBagConstraints();
		gbc_btnQuit.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnQuit.insets = new Insets(0, 0, 5, 0);
		gbc_btnQuit.gridx = 0;
		gbc_btnQuit.gridy = 3;
		add(btnQuit, gbc_btnQuit);
		btnQuit.setEnabled(false);
		btnQuit.setFont(font);
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.quit();
			}
		});
		GridBagConstraints gc1 = new GridBagConstraints();
		gc1.insets = new Insets(0, 0, 5, 0);
		gc1.gridx = 0;
		gc1.gridy = 4;
		
		// COMPTEUR
		lblCount = new JLabel("");
		lblCount.setForeground(Color.RED);
		GridBagConstraints gbc_lblCount = new GridBagConstraints();
		gbc_lblCount.gridx = 0;
		gbc_lblCount.gridy = 4;
		add(lblCount, gbc_lblCount);
		lblCount.setFont(font);
	}


	/**
	 * Met à jour l'accessibilité des boutons en fonction de l'état
	 * @param state
	 */
	public void updateButtons() {
		if (controller == null) return;
		switch (controller.getState()) {
			case CONFIG:
				btnQuit.setEnabled(true);
				btnPlay.setEnabled(true);
				btnRandom.setEnabled(true);
				btnConfig.setEnabled(false);
				break;
			case FINISH:
				btnQuit.setEnabled(true);
				btnPlay.setEnabled(false);
				btnRandom.setEnabled(false);
				btnConfig.setEnabled(false);
				break;
			case PLAY:
				btnQuit.setEnabled(true);
				btnPlay.setEnabled(false);
				btnRandom.setEnabled(false);
				btnConfig.setEnabled(false);
				break;
			case IDLE:
			default:
				btnQuit.setEnabled(false);
				btnPlay.setEnabled(true);
				btnRandom.setEnabled(true);
				btnConfig.setEnabled(true);
				break;
		
		}
	}
}
