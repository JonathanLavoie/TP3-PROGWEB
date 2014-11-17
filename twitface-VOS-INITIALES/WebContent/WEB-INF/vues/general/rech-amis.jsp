<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Rechercher de nouveaux amis</h2>

<div id="form-rechercher-amis">
	<!-- Formulaire de recherche d'amis -->
	<form method="get" action="${pageContext.request.contextPath}/rech-amis">
		<p>
			<label for="nom-ami">Nom : </label>
			<input type="text" name="nom-ami" id="nom-ami" value="${fn:trim(param['nom-ami'])}"/>

			<label for="ville-origine">Ville d'origine : </label>
			<input type="text" name="ville-origine" id="ville-origine" value="${fn:trim(param['ville-origine'])}"/>

			<label for="ville-actuelle">Ville actuelle : </label>
			<input type="text" name="ville-actuelle" id="ville-actuelle" value="${fn:trim(param['ville-actuelle'])}"/>

			<label for="sexe-m">Masculin </label>
			<input type="checkbox" name="sexe" id="sexe-m" value="M" 
			<c:if test="${(paramValues['sexe'][0] == 'M') || ((paramValues['sexe'][0] != 'M') && (paramValues['sexe'][0] != 'F'))}">
				checked
			</c:if> />
			<label for="sexe-f">Féminin </label>
			<input type="checkbox" name="sexe" id="sexe-f" value="F" 
			<c:if test="${((paramValues['sexe'][0] == 'F') || (paramValues['sexe'][1] == 'F')) || ((paramValues['sexe'][0] != 'M') && (paramValues['sexe'][0] != 'F'))}">
				checked
			</c:if> />
			<input type="image" name="rech-amis" id="img-soumettre-rech-amis" src="${pageContext.request.contextPath}/images/icone-recherche.png" alt="Rechercher des amis" />
		</p>
	</form>
</div>  <!-- Fin de la division "form-rechercher-amis" -->
<c:choose>

	<c:when test="${empty requestScope.modRechAmis.lstAmis}">
		<p>Aucune personne trouver</p>
	</c:when>

	<c:otherwise>

		<ul id="lst-RechAmis" class="lst-membres">
			<%-- Parcours et affichage des amis suggérés --%>
			<c:forEach var="sa" items="${requestScope.modRechAmis.lstAmis}">
				<li>
					<%-- Affichage de la photo de l'ami suggéré (dans une division) --%>
					<fmt:formatNumber var="noFormate" value="${sa.noAmi}" pattern="000" />
					<div class="div-img">
						<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${sa.nomAmi}" />
					</div>
					<p class="nom-ami-sugg">${sa.nomAmi}</p>
					<p class="nom-ami-sugg">${sa.villeActuelle}</p>
					<p class="nom-ami-sugg">(${sa.villeOrigine})</p>
					<c:if test="${not empty sessionScope['modeConn'] && sessionScope['modeConn'] != 'AUCUN'}">
				   		<p>
							<a href="${pageContext.request.contextPath}/membre/dem-ami?no-ami=${sa.noAmi}">Ajouter comme ami</a>
						</p>
				    </c:if>
				</li>
			</c:forEach>
		</ul>

	</c:otherwise>

</c:choose>
