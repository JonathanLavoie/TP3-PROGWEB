<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<img src="${pageContext.request.contextPath}/images/logo-twitface-tf.png" id="logo-twitface-tf" alt="Logo twitface" />
<h1 class="equivalent-image">twitface</h1>
<img src="${pageContext.request.contextPath}/images/oiseau-twitter.png" id="oiseau-twitface" alt="Oiseau twitface" />

<div id="connexion">
	<%-- Utilisateur connecté; on affiche de l'information sur l'utilisateur --%>
	<c:if test="${not empty sessionScope['connBean'] && sessionScope.connBean.getModeConn() != 'AUCUN' && empty sessionScope['msgErrConn']}">
		<%-- Affichage de la photo si c'est un membre (pas un administrateur) --%>
		<c:if test="${sessionScope['connBean'].getModeConn() == 'MEMBRE'}">
			<fmt:formatNumber var="noFormate" value="${sessionScope.connBean.getNoUtil()}" pattern="000" />
			<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" id="photo-membre-conn" alt="Photo de ${sessionScope['nom']}" />
		</c:if>

		<div id="info-util">
			<p>
				${sessionScope.connBean.getNom()} (${sessionScope.connBean.getNomUtil()})
				<c:if test="${sessionScope['connBean'].getModeConn() == 'ADMIN'}">
					<br><span>Administrateur du site web</span>
				</c:if>
			</p>
			<p>
				<a href="${pageContext.request.contextPath}/deconnexion">Déconnexion</a>
			</p>
		</div>
	</c:if>  <%-- Fin de utilisateur connecté --%>
	
	<c:if test="${empty sessionScope['connBean'] || sessionScope['connBean'].getModeConn() == 'AUCUN' && empty sessionScope['msgErrConn']}">
	<div id="form-connexion">
		<!-- Formulaire de connexion -->
		<form method="post" action="${pageContext.request.contextPath}/connexion">

			<p>
				<label for="nom-util">Nom d'utilisateur : </label>
				<input type="text" name="nom-util" id="nom-util" />
			</p>
			<p>
				<label for="mot-passe">Mot de passe : </label>
				<input type="password" name="mot-passe" id="mot-passe" />
			</p>
			<input type="image" id="img-soumettre-connexion" src="${pageContext.request.contextPath}/images/icone-connexion.png" alt="Se connecter" />

			<%-- Champ caché pour indiquer une tentative de connexion à partir de la recherche d'amis --%>
			<input type="hidden" name="source" value="rech-amis" />

			<c:if test="${not empty requestScope['msgErrConn']}">
				<p id="msg-err-conn"><c:out value="${requestScope['msgErrConn']}" /></p>
			</c:if> 
		</form>
	</div>  <!-- Fin de la division "form-connexion" -->
	</c:if>  <%-- Fin de utilisateur non connecté --%>


</div>  <!-- Fin de la division "connexion" -->
