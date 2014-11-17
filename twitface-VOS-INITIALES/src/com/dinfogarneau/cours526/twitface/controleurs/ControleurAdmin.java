package com.dinfogarneau.cours526.twitface.controleurs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dinfogarneau.cours526.twitface.beans.ConnexionBean;
import com.dinfogarneau.cours526.twitface.classes.ConnexionMode;

/**
 * Contrôleur pour les ressources des admins.
 * @author Stéphane Lapointe
 * @author VOS NOMS COMPLETS ICI
 */
public class ControleurAdmin extends HttpServlet {
	private static final long serialVersionUID = 1L;

		
	// Attributs
	// =========
	/**
	* URI sans le context path.
	*/
	protected String uri;
		
	/**
	* Vue à afficher (chemin du fichier sur le serveur).
	*/
	protected String vue;
		
	/**
	* Fragment de la vue (chemin du fichier sur le serveur) à charger
	* dans la zone de contenu si la vue est créée à partir du gabarit.
	*/
	protected String vueContenu;
		
	/**
	* Sous-titre de la vue si la vue est créée à partir du gabarit.
	*/
	protected String vueSousTitre;
		
	/**
	 * Bean de connexion
	*/
	protected ConnexionBean sessionConnBean;
	/**
	* Permet d'effectuer les traitements avant de gérer la ressource demandée.
	* @param request La requête HTTP.
	* @param response La réponse HTTP.
	* @return true si les opérations se sont bien déroulées; false, autrement.
	* @throws IOException 
	*/
	protected boolean preTraitement(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Récupération de l'URI sans le context path.
		this.uri = request.getRequestURI().substring(request.getContextPath().length());
		this.vue = null;
		this.vueContenu = null;
		this.vueSousTitre = null;
			
		// Expiration de la cache pour les pages de cette section.		
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");  // HTTP 1.1.
		response.setHeader("Pragma", "no-cache");  // HTTP 1.0.
		response.setDateHeader("Expires", 0);  // Proxies.
		
		sessionConnBean = (ConnexionBean) request.getSession().getAttribute("connBean");
			
		// Récupération du mode de connexion dans la session utilisateur.
		// *** À MODIFIER (UTILISATION DU BEAN DE CONNEXION) ***
		ConnexionMode modeConn = (ConnexionMode) sessionConnBean.getModeConn();

		// Contrôle d'accès à la section pour les admins
		if (modeConn == null || modeConn != ConnexionMode.ADMIN) {
			// Non connecté en tant qu'admin; on retourne une code d'erreur
			// HTTP 401 qui sera intercepté par la page d'erreur "erreur-401.jsp".
			response.sendError(401);
			return false;
		}
		else
			return true;
		}
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControleurAdmin() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(preTraitement(request,response))
		{
			if(uri.equals("/admin") || uri.equals("/admin/"))
			{
				// Paramètres pour la vue créée à partir du gabarit.
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/admin/accueil-admin.jsp";
				
				
				String nom = (String) sessionConnBean.getNom();
				String nomUtil = (String) sessionConnBean.getNomUtil();
				vueSousTitre = "Page d'administration de " + nom + " (" + nomUtil + ")";
			} else if(uri.equals("/admin/supp-pub")) {
				vue = "/WEB-INF/vues/gabarit-vues.jsp";
				vueContenu = "/WEB-INF/vues/admin/supp-pub.jsp";
			}
			postTraitement(request,response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendError(405);
	}
	protected void postTraitement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Doit-on transférer le contrôle vers une vue ?
		if (this.vue != null) {
			// Doit-on conserver les informations pour la production d'une vue à partir du gabarit ?
			if (this.vueContenu != null || this.vueSousTitre != null) {
				// On conserve le chemin du fichier du fragment de la vue ainsi que le
				// sous-titre de la vue dans les attributs de la requête;
				// ces informations serviront à générer la vue à partir du gabarit.
				request.setAttribute("vueContenu", this.vueContenu);
				request.setAttribute("vueSousTitre", this.vueSousTitre);
			}
			// Transfert du contrôle de l'exécution à la vue.
			request.getRequestDispatcher(this.vue).forward(request, response);
		}		
	}	
}
