package com.dinfogarneau.cours526.twitface.modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.dinfogarneau.cours526.twitface.classes.Ami;
import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;
import com.dinfogarneau.cours526.util.ReqPrepBdUtil;
import com.dinfogarneau.cours526.twitface.beans.ConnexionBean;
/**
 * Modele pour la connextion
 * @author Main Nain
 *
 */
public class ModeleConnexion {
	
	/**
	 * Nom de l'utilisateur
	 */
	private String nomUtil;
	/**
	 * Mot De Passe de l'utilisateur
	 */
	private String motPasse;
	/**
	 * La connexion Bean
	 */
	private ConnexionBean connBean;
	/**
	 * Message d'erreur
	 */
	private String msgErreur;
	
	/**
	 * Constructeur 
	 * @param Nom de Utilisateur
	 * @param Mot De Passe
	 */
	public ModeleConnexion(String nomUtilisateur,String mdp)
	{
		this.nomUtil = nomUtilisateur;
		this.motPasse = mdp;		
		this.connBean = null;
		this.msgErreur = null;
	}
	
	/**
	 * Retourne la connextion bean
	 * @return Connextion Bean
	 */
	public ConnexionBean getConnexionBean()
	{
		return this.connBean;
	}
	
	/**
	 * Retourne un message d'erreur
	 * @return String
	 */
	public String getMsgErreur()
	{
		return this.msgErreur;
	}
	
	/**
	 * Methode pour se connecter
	 * @throws NamingException
	 * @throws SQLException
	 */
	public void seConnecter() throws NamingException, SQLException
	{
		if(this.nomUtil != "" ||  this.motPasse != "")
		{
			// Source de données (JNDI).
			String nomDataSource = "jdbc/twitface";

			// Création de l'objet pour l'accès à la BD.
			ReqPrepBdUtil utilBd = new ReqPrepBdUtil(nomDataSource);

			// Obtention de la connexion à la BD.
			utilBd.ouvrirConnexion();
			String reqSqlConnexionMembre = "SELECT MemNo, MemNom FROM membres WHERE MemNomUtil like ? AND MemMotPasse like SHA2(?,256) ;";
		
			// Préparation de la requête SQL.
			utilBd.preparerRequete(reqSqlConnexionMembre, false);
				
			// Exécution de la requête tout en lui passant les paramètres pour l'exécution.
			ResultSet rs = utilBd.executerRequeteSelect(this.nomUtil,this.motPasse);
				
			if(rs.next())
			{
				this.connBean = new ConnexionBean();
				this.connBean.setNom(rs.getString("MemNom"));
				this.connBean.setNomUtil(this.nomUtil);
				this.connBean.setNoUtil(rs.getInt("MemNo"));
				this.connBean.setModeConn(ConnexionMode.MEMBRE );
			}
			else
			{
				String reqSqlConnexionAdmin = "SELECT AdminNo, AdminNom FROM Administrateurs WHERE AdminNomUtil like ? AND AdminMotPasse like SHA2(?,256);";
				utilBd.preparerRequete(reqSqlConnexionAdmin, false);
				
				rs = utilBd.executerRequeteSelect(this.nomUtil,this.motPasse);
				
				if(rs.next())
				{					
					this.connBean = new ConnexionBean();
					this.connBean.setNom(rs.getString("AdminNom"));
					this.connBean.setNomUtil(this.nomUtil);
					this.connBean.setNoUtil(rs.getInt("AdminNo"));
					this.connBean.setModeConn(ConnexionMode.ADMIN );
				}
				else
				{
					this.msgErreur = "Les informations saisies ne correspondent pas";
				}
					
			}
				
			// Fermeture de la connexion à la BD.
			utilBd.fermerConnexion();			
		}
		else
		{
			this.msgErreur = "Tous les champs doivent être remplis";
		}
	}
	

}
