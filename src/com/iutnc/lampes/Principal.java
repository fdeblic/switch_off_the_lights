package com.iutnc.lampes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

public class Principal {
	
	public static void main(String[] args) {
		JFrame f = new JFrame("Eteins la lumière !");
		JeuLampes jeu = new JeuLampes();
		JPanel conteneur = new JPanel();
		conteneur.setBackground(Color.decode("#123456"));
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.rowHeights = new int[]{0, 56, 0, 0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 1.0};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		conteneur.setLayout(gridBagLayout);
		
		Menu menu = new Menu(jeu);
		GridBagConstraints gbc_menu = new GridBagConstraints();
		gbc_menu.anchor = GridBagConstraints.NORTHEAST;
		gbc_menu.gridx = 0;
		gbc_menu.gridy = 1;
		gbc_menu.insets = new Insets(0, 0, 5, 30);
		conteneur.add(menu, gbc_menu);
		
		JLabel lblTitre = new JLabel("Eteins la lumiere !");
		lblTitre.setFont(new Font("Fake Receipt", Font.PLAIN, 54));
		lblTitre.setForeground(Color.LIGHT_GRAY);
		GridBagConstraints gbc_lblTitre = new GridBagConstraints();
		gbc_lblTitre.gridwidth = 2;
		gbc_lblTitre.anchor = GridBagConstraints.SOUTH;
		gbc_lblTitre.insets = new Insets(10, 30, 5, 0);
		gbc_lblTitre.gridx = 0;
		gbc_lblTitre.gridy = 0;
		conteneur.add(lblTitre, gbc_lblTitre);

		Plateau plateau = new Plateau(jeu, 5, 5);
		plateau.placerLumieresHasard();
		
		
		GridBagConstraints gbc_plateau = new GridBagConstraints();
		gbc_plateau.fill = GridBagConstraints.BOTH;
		gbc_plateau.gridheight = 2;
		gbc_plateau.insets = new Insets(0, 0, 5, 50);
		gbc_plateau.gridx = 1;
		gbc_plateau.gridy = 1;
		conteneur.add(plateau, gbc_plateau);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(conteneur);
		
		JLabel lblCompteur = new JLabel("Compteur");
		lblCompteur.setVerticalAlignment(SwingConstants.TOP);
		lblCompteur.setHorizontalAlignment(SwingConstants.CENTER);
		lblCompteur.setForeground(Color.LIGHT_GRAY);
		lblCompteur.setFont(new Font("Andika", Font.BOLD, 24));
		Dimension dimensionCompteur = new Dimension(250, 100);
		lblCompteur.setMinimumSize(dimensionCompteur);
		lblCompteur.setPreferredSize(dimensionCompteur);
		GridBagConstraints gbc_lblCompteur = new GridBagConstraints();
		gbc_lblCompteur.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblCompteur.insets = new Insets(0, 0, 5, 40);
		gbc_lblCompteur.gridx = 0;
		gbc_lblCompteur.gridy = 2;
		conteneur.add(lblCompteur, gbc_lblCompteur);
		
		
		
		jeu.setComponents(plateau, menu, lblCompteur);
		f.pack();
		f.setMinimumSize(new Dimension(750, 550));
		f.setVisible(true);
	}
}
