
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Cette fentre presente 2 onglets , un onglet "ajout et mise à jour" qui permet la consultation 
 * et la mise °jour(modification et supression) de la liste d'étudiants.
 * Un autre onglet "Resultat recherche" de recherche par mot clé d'un ètudiant.
 * 
 */
public class MaApplication extends JFrame {
	private JMenuBar barreMenus;
	private JMenu etudiant, couleurs;
	private JMenuItem recherche, quitter, cin, nom, rouge, bleu, rose;
	private JButton mod, sup, suiv, prec, ajt, anl, ok, anlMotCle;;
	private JTabbedPane tabPan;
	protected JPanel ajout, result, ajoutNord, ajoutCenter, ajoutEst, resultCenter, resultNord, resultEst;
	protected JLabel cle, CIN1, Nom1, Prenom1, sexelbl1, datelbl1, obligatoire;
	protected JTextField txtcinNord, txtnomNord, txtprenomNord, txtcle, texteNord, txtcinCenter, txtnomCenter,
			txtprenomCenter, txtcleCenter, texteCenter;
	protected JFormattedTextField datetxtNord, datetxtCenter;
	private JComboBox<String> sexeBoxNord, sexeBoxCenter;
	protected JTable tab;
	private SimpleDateFormat formater;
	private Font font = new Font("Garamond", Font.PLAIN, 20);
	private Font fontItalic = new Font("Serif", Font.ITALIC, 17);
	private Connection connex;
	private ResultSet myRs;

	private Statement myStmt;
	private Tableau tableauPan;
	private ResultSetTableModel rtm;
	private int rech;

	public MaApplication() {
		super();
	connex = null;
		try {
			connex = newConn();
			/** 2.Create a statement */
			myStmt = connex.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			/** 3.Execute SQL query */
			myRs = myStmt.executeQuery("select * from etudiants");
		} catch (SQLException e) {
			System.out.println("SQLState: " + e.getSQLState());
			System.out.println("SQLException: " + e.getMessage());
			System.out.println("Error : " + e.getErrorCode());
		}
		;

		/** construction de la fenètre */

		setTitle("Gestion Etudiant");
		setSize(800, 800);
		setLocation(100, 100);
		setLayout(new FlowLayout());
		barreMenus = new JMenuBar();
		setJMenuBar(barreMenus);
		etudiant = new JMenu("Etudiant");
		font = new Font("Garamond", Font.PLAIN, 20);
		etudiant.setFont(font);
		couleurs = new JMenu("Couleur");
		couleurs.setFont(font);
		barreMenus.add(etudiant);
		JMenuItem recherche = new JMenu("Recherche");

		cin = new JMenuItem("Cin");
		recherche.add(cin);
		cin.addActionListener(new mesActions());
		nom = new JMenuItem("Nom");
		recherche.add(nom);
		nom.addActionListener(new mesActions());

		etudiant.add(recherche);
		etudiant.addSeparator();
		quitter = new JMenuItem("Quitter");
		etudiant.add(quitter);
		quitter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		barreMenus.add(couleurs);
		rouge = new JMenuItem("Rouge");
		couleurs.add(rouge);
		couleurs.addSeparator();
		rouge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutNord.setBackground(Color.red);
				resultNord.setBackground(Color.red);
			}
		});

		bleu = new JMenuItem("Bleu");
		couleurs.add(bleu);
		couleurs.addSeparator();
		bleu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutNord.setBackground(Color.blue);
				resultNord.setBackground(Color.blue);
			}
		});

		rose = new JMenuItem("Rose");
		couleurs.add(rose);
		rose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutNord.setBackground(Color.pink);
				resultNord.setBackground(Color.pink);
			}
		});

		tabPan = new JTabbedPane();
		setContentPane(tabPan);

		/**
		 * Onglet 1: Ajout et mise à jour
		 */
		ajout = new JPanel();
		ajout.setLayout(new BorderLayout());
		tabPan.addTab("Ajout et mise à jour", ajout);

		ajoutNord = new JPanel();
		ajoutCenter = new JPanel();
		ajoutEst = new JPanel();
		ajoutNord.setBackground(Color.pink);
		ajoutEst.setBackground(Color.white);
		ajout.add(ajoutNord, "North");
		ajout.add(ajoutCenter, "Center");
		ajout.add(ajoutEst, "East");
		//ajoutNord.setPreferredSize(new Dimension(400,100));
		ajoutEst.setPreferredSize(new Dimension(100, 200));

		/**
		 * Panneau Nord
		 */

		ajoutNord.setLayout(new GridBagLayout());
		GridBagConstraints cons = new GridBagConstraints();

		txtcinNord = new JTextField("", 8);
		txtcinNord.setEditable(false); // On a mis cette instruction pour
										// bloquer la modification de CIN
										// puisque il'est unique et non
										// modifiable.
		txtnomNord = new JTextField("", 15);
		txtprenomNord = new JTextField("", 15);
		SimpleDateFormat formater = new SimpleDateFormat("YYYY-MM-DD");
		datetxtNord = new JFormattedTextField(formater.format(new java.util.Date()));
		//datetxtNord.setColumns(12);

		String[] choix = { "Masculin", "Feminin" };
		sexeBoxNord = new JComboBox<String>(choix);

		mod = new JButton("Modifier");
		mod.addActionListener(new mesActions());
		sup = new JButton("Suprimer");
		sup.addActionListener(new mesActions());
		prec = new JButton("<");
		prec.addActionListener(new mesActions());
		suiv = new JButton(">");
		suiv.addActionListener(new mesActions());

		cons.gridx = 0;
		cons.gridy = 0;
		ajoutNord.add(txtcinNord, cons);
		cons.gridx = 1;
		cons.gridy = 0;
		ajoutNord.add(txtnomNord, cons);
		cons.gridx = 2;
		cons.gridy = 0;
		ajoutNord.add(txtprenomNord, cons);
		cons.gridx = 3;
		cons.gridy = 0;
		ajoutNord.add(datetxtNord, cons);
		cons.gridx = 4;
		cons.gridy = 0;
		ajoutNord.add(sexeBoxNord, cons);
		cons.gridx = 5;
		cons.gridy = 0;
		ajoutNord.add(mod, cons);
		cons.gridx = 6;
		cons.gridy = 0;
		ajoutNord.add(sup, cons);
		cons.gridx = 2;
		cons.gridy = 1;
		ajoutNord.add(prec, cons);
		cons.gridx = 4;
		cons.gridy = 1;
		ajoutNord.add(suiv, cons);

		/**
		 * connexion et Initialisation des champs de l'ajout sur la derniére
		 * affectation de la base
		 */

		try {
			if (myRs.next()) {
				txtcinNord.setText(myRs.getString(1));
				txtnomNord.setText(myRs.getString(2));
				txtprenomNord.setText(myRs.getString(3));
				datetxtNord.setText(myRs.getString(4));
				sexeBoxNord.setSelectedItem(myRs.getString(5));
			}
		} catch (SQLException ex) {
			System.err.println("ex");
		}
		catch (NullPointerException e1) {
			System.out.println(" Activer MySQL Database ");
		}
		
		/*
		 finally{ if(connex != null) try {connex.close();
		  System.out.println("connexion fermée "); } catch (SQLException
		  ignore){ System.out.println("impossible de fermer la connexion"); } ;
		  
		  };
		 */

		/**
		 * Panneau Centre
		 */

		CIN1 = new JLabel("CIN");
		Nom1 = new JLabel("Nom");
		Prenom1 = new JLabel("Prenom");
		sexelbl1 = new JLabel("Sexe");
		sexeBoxCenter = new JComboBox<String>(choix);
		datelbl1 = new JLabel("Date de naissance");
		obligatoire = new JLabel("<html><font color = #FF0000 >* Champs obligatoires</font></html>");

		CIN1.setFont(fontItalic);
		Prenom1.setFont(fontItalic);
		sexelbl1.setFont(fontItalic);
		sexeBoxCenter.setFont(fontItalic);
		datelbl1.setFont(fontItalic);
		obligatoire.setFont(fontItalic);

		txtcinCenter = new JTextField(10);
		txtcinCenter.setEditable(true);
		txtnomCenter = new JTextField(10);
		txtprenomCenter = new JTextField(10);
		SimpleDateFormat formater1 = new SimpleDateFormat("YYYY-MM-dd");
		datetxtCenter = new JFormattedTextField(formater1.format(new java.util.Date()));
		//datetxtCenter.setColumns(10);

		ajoutCenter.setLayout(new GridBagLayout());
		GridBagConstraints con = new GridBagConstraints();
		con.anchor = GridBagConstraints.FIRST_LINE_START;
		con.insets = new Insets(15, 15, 0, 0);
		con.gridx = 0;
		con.gridy = 0;
		ajoutCenter.add(CIN1, con);
		con.gridx = 1;
		con.gridy = 0;
		ajoutCenter.add(txtcinCenter, con);
		con.gridx = 2;
		con.gridy = 0;
		ajoutCenter.add(sexelbl1, con);
		con.gridx = 3;
		con.gridy = 0;
		ajoutCenter.add(sexeBoxCenter, con);
		con.gridx = 0;
		con.gridy = 1;
		ajoutCenter.add(Nom1, con);
		con.gridx = 1;
		con.gridy = 1;
		ajoutCenter.add(txtnomCenter, con);
		con.gridx = 2;
		con.gridy = 1;
		ajoutCenter.add(Prenom1, con);
		con.gridx = 3;
		con.gridy = 1;
		ajoutCenter.add(txtprenomCenter, con);
		con.gridx = 0;
		con.gridy = 3;
		ajoutCenter.add(obligatoire, con);
		con.gridx = 0;
		con.gridy = 2;
		ajoutCenter.add(datelbl1, con);
		con.gridx = 1;
		con.gridy = 2;
		String datelbl1 = formater1.format(new java.util.Date());
		datetxtCenter.setText(datelbl1);
		ajoutCenter.add(datetxtCenter, con);
		con.gridx = 0;
		con.gridy = 4;
		ajoutCenter.add(obligatoire, con);
		
		
		
		/**
		 * Panneaux East
		 */

		ajt = new JButton("Ajouter");
		ajoutEst.add(ajt);
		ajt.addActionListener(new mesActions());
		anl = new JButton("Annuler");
		ajoutEst.add(anl);
		anl.addActionListener(new mesActions());

		/**
		 * Onglet 2 : RÈsultat et recherche
		 */

		result = new JPanel();
		result.setLayout(new BorderLayout());
		tabPan.addTab("Resultats Recherche", result);
		resultNord = new JPanel();
		resultCenter = new JPanel();
		resultEst = new JPanel();
		resultNord.setBackground(Color.pink);
		result.add(resultNord, "North");
		resultNord.setPreferredSize(new Dimension(400, 100));
		cle = new JLabel("mot clé");
		resultNord.add(cle);
		txtcle = new JTextField(10);
		datetxtNord.setColumns(10);
		resultNord.add(txtcle);
		ok = new JButton("Ok");
		resultNord.add(ok);
		ok.addActionListener(new mesActions());
		anlMotCle = new JButton("Annuler");
		resultNord.add(anlMotCle);
		anlMotCle.addActionListener(new mesActions());
		result.add(resultCenter, "Center");
		JTable tab = new JTable();
		resultCenter.add(tab);
		result.add(resultEst, "East");
		resultEst.setPreferredSize(new Dimension(100, 200));

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Connexion à la base de donnée
	 */
	
	Connection newConn() throws SQLException {
		final String url = "jdbc:mysql://localhost/db_etude";
		Connection myConn = DriverManager.getConnection(url, "root", "");
		System.out.println("congratulation! la base est connectée");
		return myConn;
	}

	/**
	 * Méthode pour l'ajout d'un nouveau étudiant
	 * 
	 */
	
	private void ajoutEtud(String cin, String nom, String prenom, String datNais, String sexe) throws SQLException {
		if (connex != null) {
			myStmt.executeUpdate("INSERT INTO etudiants(CIN,NOM,PRENOM,DAT_NAIS,SEXE) values('" + cin + " ',' " + nom
					+ "','" + prenom + "','" + datNais + "','" + sexe + "')");
		}
	}

	/**
	 * Méthode pour la modification d'un champs inséré
	 */
	private void modEtud(String cin, String nom, String prenom, String datNais, String sexe) throws SQLException {
		if (connex != null) {
			String sql = "UPDATE etudiants SET NOM=? , PRENOM = ? ,DAT_NAIS = ?, SEXE=?" + "WHERE CIN = ?";
			PreparedStatement myStmt = connex.prepareStatement(sql);

			myStmt.setString(1, nom);
			myStmt.setString(2, prenom);
			myStmt.setString(3, datNais);
			myStmt.setString(4, sexe);
			myStmt.setString(5, cin);
			myStmt.executeUpdate();
			myStmt.close();
		}
	}

	/**
	 * méthode des champs vides
	 */
	
	public void vider() {
		txtcinCenter.setText("");
		txtcinCenter.setBackground(Color.white);
		txtnomCenter.setText("");
		txtprenomCenter.setText("");
		datetxtCenter.setText("YYYY-MM-DD");
		SimpleDateFormat formater1 = new SimpleDateFormat("YYYY-MM-dd");
		datetxtCenter = new JFormattedTextField(formater1.format(new java.util.Date()));
	}

	/**
	 * méthode pour mettre à jour le panneau Nord
	 */
	private void misAjour() {
		try {
			myRs = myStmt.executeQuery("SELECT * FROM etudiants");
			myRs.absolute(0);
			if (myRs.next()) {
				txtcinNord.setText(myRs.getString(1));
				txtnomNord.setText(myRs.getString(2));
				txtprenomNord.setText(myRs.getString(3));
				datetxtNord.setText(myRs.getString(4));
				sexeBoxNord.setSelectedItem(myRs.getString(5));}} catch (SQLException e) {
		}
		catch (NullPointerException e1) {
				System.out.println(" erreur ");
			}
	}

	/**
	 * la sous classe des actions
	 * 
	 */
	class mesActions implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Object boutton = e.getSource();
			String getcinNord = txtcinNord.getText();
			String getnomNord = txtnomNord.getText();
			String getprenomNord = txtprenomNord.getText();
			String getdateNord = datetxtNord.getText();
			String getsexeNord = String.valueOf(sexeBoxNord.getSelectedItem());

			String getcinCenter = txtcinCenter.getText();
			String getnomCenter = txtnomCenter.getText();
			String getprenomCenter = txtprenomCenter.getText();
			String getdateCenter = datetxtCenter.getText();
			String getsexeCenter = String.valueOf(sexeBoxCenter.getSelectedItem());
			String getCle = txtcle.getText();

			/**
			 * les actions precedent
			 */
			if (boutton == prec) {

				try {
					myRs.absolute(0);
					while (myRs.next()) {
						if (txtcinNord.getText().equals(myRs.getString(1))) {
							myRs.previous();
							txtcinNord.setText(myRs.getString(1));
							txtnomNord.setText(myRs.getString(2));
							txtprenomNord.setText(myRs.getString(3));
							datetxtNord.setText(myRs.getString(4));
							sexeBoxNord.setSelectedItem(myRs.getString(5));
							if (myRs.isFirst())
								prec.setEnabled(false);
							else
								prec.setEnabled(true);
							if (myRs.isLast())
								suiv.setEnabled(false);
							else
								suiv.setEnabled(true);
						}
					}
					
				} catch (SQLException ex) {
				}
			}

			/**
			 * les actions de bouton suivant
			 */
			else if (boutton == suiv) {
				try {
					myRs.absolute(0);
					while (myRs.next()) {
						if ((txtcinNord.getText().equals(myRs.getString(1)))) {
							myRs.next();
							txtcinNord.setText(myRs.getString(1));
							txtnomNord.setText(myRs.getString(2));
							txtprenomNord.setText(myRs.getString(3));
							datetxtNord.setText(myRs.getString(4));
							sexeBoxNord.setSelectedItem(myRs.getString(5));
							if (myRs.isFirst())
								prec.setEnabled(false);
							else
								prec.setEnabled(true);
							if (myRs.isLast())
								suiv.setEnabled(false);
							else
								suiv.setEnabled(true);
						}

					}
					
				} catch (SQLException ex) {
				}
			}

			/**
			 * L'action de boutton NOM
			 */

			else if (boutton == nom) {
				tabPan.setSelectedIndex(1);
			}
			
			/**
			 * L'action de boutton CIN
			 */
			
			else if (boutton == cin) {
				String retour = JOptionPane.showInputDialog(null, "Saisir un numero de CIN", "Recherche avec CIN",
						JOptionPane.INFORMATION_MESSAGE);
				int rech = 0;
				try{try {
					rech = Integer.parseInt(retour);
				}catch (NumberFormatException errore) {
					retour = JOptionPane.showInputDialog(null, "Veuillez resaisir CIN", "Attention",
							JOptionPane.ERROR_MESSAGE);
				}
				
					while ((retour != null) && (retour.isEmpty() || retour.length() != 8)) {
						retour = JOptionPane.showInputDialog(null, "Veuillez resaisir CIN", "Attention",
								JOptionPane.ERROR_MESSAGE);
					}
					myRs.absolute(0);
					while (myRs.next()) {
						if (retour.equals(myRs.getString(1))) {
							String s = String.format("%08d", myRs.getInt(1));
							txtcinNord.setText(s);
							txtnomNord.setText(myRs.getString(2));
							txtprenomNord.setText(myRs.getString(3));
							datetxtNord.setValue(myRs.getDate(4));
							sexeBoxNord.setEditable(true);
							sexeBoxNord.setSelectedItem(myRs.getString(5));
						} }
					 if (myRs.isAfterLast())
						JOptionPane.showMessageDialog(null, "Aucun etudiant avec ce CIN !");

				}  catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NullPointerException e1) {
					System.out.println(" erreur d'appel null ");
				}
			}

			/**
			 * L'action de boutton modifier
			 */

			else if (boutton == mod) {
				try {

					int warning = JOptionPane.showConfirmDialog(null,
							" etes-vous  sur de vouloir modifier cet etudiant", "Attention",
							JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (warning == JOptionPane.OK_OPTION) {
						modEtud(getcinNord, getnomNord, getprenomNord, getdateNord, getsexeNord);
						JOptionPane.showMessageDialog(null, "la modification effectuée avec sucées!");
						misAjour();
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Erreur de modification \n" + e1.getMessage());
					misAjour();
				}
			}

			/**
			 *les actions du bouton supprimer */
			
			else if (boutton == sup) {
				try {
					int retour = JOptionPane.showConfirmDialog(null, "vous etes sur de vouloir supprimer cet etudiant?",
							"Attention", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
					if (retour == JOptionPane.OK_OPTION) {
						String sql = "DELETE FROM etudiants where CIN = ?";
						PreparedStatement st = connex.prepareStatement(sql);
						st.setString(1, getcinNord);
						st.executeUpdate();
						JOptionPane.showMessageDialog(null, "la suppression terminer avec succé !");
						misAjour();
					}
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog(null, "Erreur de suppression \n" + e1.getMessage());
					misAjour();
				}

			}
			/**
			 *les actions du bouton Ajouter */
			else if (boutton == ajt) {
				try {
					try {
						myRs = myStmt.executeQuery("SELECT * FROM etudiants where CIN='" + getcinCenter + "'");
						if (myRs.next()) {
							JOptionPane.showMessageDialog(null, "CIN existe déja");
							vider();
						}
					}
					catch (Exception e1) {}
					if (getcinCenter.length() == 0 || getnomCenter.length() == 0 || getprenomCenter.length() == 0) {
						JOptionPane.showMessageDialog(null, "Veuillez completer tous les champs obligatoires",
								"Données manquantes", 1);
						if (getcinCenter.length() == 0) {
							txtcinCenter.setBackground(Color.red);
						}
					} else if (!getcinCenter.matches("[0-9]+")) {
						JOptionPane.showMessageDialog(null, "Le CIN doit contenir uniquement des chiffres", "Attention",
								1);
						txtcinCenter.setText("");
						txtcinCenter.setBackground(Color.red);
					} else if (getcinCenter.length() != 8) {
						JOptionPane.showMessageDialog(null, "Le CIN doit contenir 8 chiffres", "Données manquantes", 1);
						txtcinCenter.setBackground(Color.red);
					} else if (!getnomCenter.matches("[a-zA-Z]+")) {
						JOptionPane.showMessageDialog(null, "Le nom doit contenir uniquement des lettres", "Attention",
								1);
						txtnomCenter.setText("");
					} else if (!getprenomCenter.matches("[a-zA-Z]+")) {
						JOptionPane.showMessageDialog(null, "Le prénom doit contenir uniquement des lettres",
								"Attention", 1);
						txtprenomCenter.setText("");
					} else {
						ajoutEtud(getcinCenter, getnomCenter, getprenomCenter, getdateCenter, getsexeCenter);
						JOptionPane.showMessageDialog(null, "la saisie était ajoutée avec succées");
						vider();
					}
				} catch (SQLException e1) {
					System.out.println(" Errore d'insertion donne");
					e1.printStackTrace();
				}
				misAjour();

			} else if (boutton == anl) {
				try {
					JOptionPane.showMessageDialog(null, "l'ajout de l'etudiant(e) sera annulè");
					vider();
				} catch (Exception e1) {
				}
			}

			/**
			 * Activation bouton OK
			 */
			
			else if (boutton == ok) {

				try {
					myRs = myStmt.executeQuery("select * from etudiants where NOM like '%" + getCle + "%'");
					rtm = new ResultSetTableModel(myRs);

					/** Verifiant si le mot clé ne contient pas de chiffre */

					if (getCle.matches(".*\\d+.*"))
						JOptionPane.showMessageDialog(null, "Pas de chiffre dans le mot clé du nom !");
					else {
						if (myRs.next()) {
							tableauPan = new Tableau(rtm);
							resultCenter.removeAll();
							resultCenter.repaint();
							resultCenter.revalidate();

							resultCenter.add(tableauPan);
						} else {
							JOptionPane.showMessageDialog(null,
									"Votre mot clé n'existe pas dans la base! Veuillez vérifier", "WARNING!",
									JOptionPane.WARNING_MESSAGE);
						}
					}
				} catch (SQLException exceptionTab) {
					System.out.println("exceptionSup " + exceptionTab.getMessage());
					JOptionPane.showMessageDialog(null, "<html><i>Vérifiez votre base ou votre mot clé !</i>",
							"WARNING!", JOptionPane.WARNING_MESSAGE);
				}
				resultCenter.repaint();
				resultCenter.revalidate();

			} 
			
			/**
			 *  Activation Bouton Annuler panneau Resultat recherche */
			else if (boutton == anlMotCle) {
				txtcle.setText("");
				resultCenter.removeAll();
				resultCenter.repaint();
			} 

		}
	}

}
/** Fin  MaApplication */
