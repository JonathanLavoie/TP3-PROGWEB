package com.dinfogarneau.cours526.twitface.modeles;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.dinfogarneau.cours526.twitface.classes.Ami;
import com.dinfogarneau.cours526.util.ReqPrepBdUtil;

public class ModeleRechAmis {
	
	/**
	 * Liste d'amis.
	 */
	private ArrayList<Ami> lstAmis;
	/**
	 * Nom de la recherche d'ami
	 */
	private String nomAmi;
	/**
	 * La ville d'origine 
	 */
	private String villeOrigine;
	/**
	 * La ville Actuelle
	 */
	private String villeActuelle;
	/**
	 * le sexe de la personne
	 */
	private String sexe;
	/**
	 * Nomero Utilisateur
	 */
	private String noUtil;
	
	public ModeleRechAmis() {
		
		this.lstAmis = null;
		this.nomAmi = "";
		this.villeActuelle = "";
		this.villeOrigine = "";
		this.sexe = "";
	}

	public ArrayList<Ami> getLstAmis() {
		return lstAmis;
	}

	public void setNomAmi(String nomAmi) {
		this.nomAmi = nomAmi;
	}

	public void setVilleOrigine(String villeOrigine) {
		this.villeOrigine = villeOrigine;
	}

	public void setVilleActuelle(String villeActuelle) {
		this.villeActuelle = villeActuelle;
	}

	public void setSexe(String sexe) {
		this.sexe = sexe;
	}
	public void setnoUtil(String noUtil){
		this.noUtil = noUtil;
	}
	
	public void rechercheAmis() throws NamingException, SQLException {
		
		// Liste des condition
		ArrayList<String> lstCondition = new ArrayList<String>();
		// Parametres SQL Preparer
		ArrayList<String> lstParametres = new ArrayList<String>();
		if (this.noUtil != "")
		{
			// String Condition SQL
			String str = " MemNo <> ?"
					+ " AND MemNo NOT IN ("
					+ " SELECT MemNo1 FROM amis WHERE MemNo2 = ? UNION"
					+ "	SELECT MemNo2 FROM amis WHERE MemNo1 = ?)"
					+ " AND MemNo NOT IN ("
					+ "	SELECT MemNoDemandeur FROM demandes_amis WHERE MemNoInvite = ? UNION"
					+ "	SELECT MemNoInvite FROM demandes_amis WHERE MemNoDemandeur = ?)";
			lstCondition.add(str);
			lstParametres.add("%" + this.noUtil + "%");
			lstParametres.add("%" + this.noUtil + "%");
			lstParametres.add("%" + this.noUtil + "%");
			lstParametres.add("%" + this.noUtil + "%");
			lstParametres.add("%" + this.noUtil + "%");
		}
		if (this.nomAmi != "")
		{
			lstCondition.add(" MemNom LIKE ?");
			lstParametres.add("%" + this.nomAmi + "%");
		}
		if (this.villeOrigine != "")
		{
			lstCondition.add(" MemVilleOrigine LIKE ?");
			lstParametres.add("%" + this.villeOrigine + "%");
		}
		if (this.villeActuelle != "")
		{
			lstCondition.add(" MemVilleActuelle LIKE ?");
			lstParametres.add("%" + this.villeActuelle + "%");
		}
		if (this.sexe != "")
		{
			lstCondition.add(" MemSexe LIKE ?");
			lstParametres.add("%" + this.sexe + "%");
		}
		// Source de données (JNDI).
		String nomDataSource = "jdbc/twitface";
		
		// Création de l'objet pour l'accès à la BD.
		ReqPrepBdUtil utilBd = new ReqPrepBdUtil(nomDataSource);
		
		// Connexion à la BD.
		utilBd.ouvrirConnexion();
		
		// Requête SQL permettant de chercher des amis en fonction du formulaire.
		String reqSQLAmis =
				"SELECT MemNom, MemNo, MemVilleOrigine, MemVilleActuelle"
				+ " FROM"
				+ " membres";
		for(int i = 0; i < lstCondition.size(); i++)
		{
			if (i == 0)
				reqSQLAmis += " WHERE";
			else
				reqSQLAmis += " AND";
			reqSQLAmis += lstCondition.get(i);
		}
		reqSQLAmis += " ORDER BY MemNom;";
		
		// Préparation de la requête SQL.
		utilBd.preparerRequete(reqSQLAmis, false);
		
		// Exécution de la requête
		ResultSet rs = utilBd.executerRequeteSelect(lstParametres.toArray());
		
		// Création de liste de suggestions d'amis.
		this.lstAmis = new ArrayList<Ami>();
		// Objet pour conserver une suggestion d'ami.
		Ami ami;
		// Parcours des amis
		while (rs.next()) {
			// Création de l'objet "Ami".
			ami = new Ami(rs.getString("memNom"), rs.getInt("memNo"), rs.getString("memvilleOrigine"), rs.getString("memvilleActuelle"));
			// Ajout de l'ami dans la liste.
			this.lstAmis.add(ami);
		}
		
		// Fermeture de la connexion à la BD.
		utilBd.fermerConnexion();
		
	}
}
