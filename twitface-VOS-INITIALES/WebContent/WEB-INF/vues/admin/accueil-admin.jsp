<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2> Liste des publications </h2>

<div id="publicationsMembres">
		<sql:query var="lesPub" dataSource="jdbc/twitface">
			SELECT PubTexte, MemNoCreateur, MemNom, PubDate, PubNo FROM publications,membres
			WHERE publications.MemNoCreateur = membres.MemNo ORDER BY PubDate ASC
		</sql:query>
		
		<ul id="lst-pub-babillard">
			<%-- On parcourt tous les publications. --%>
			<c:forEach var="pub" items="${lesPub.rows}">
				<%-- Affichage des publications --%>
				<li>
					<fmt:formatNumber var="noFormate" value="${pub.MemNoCreateur}"  pattern="000" />
					<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre"/>
					<span>Soumis par ${pub.MemNom} le ${pub.PubDate}</span>
					${pub.PubTexte}
					<a href="${pageContext.request.contextPath}/admin/supp-pub?no-pub${pub.PubNo}">
						<img src="${pageContext.request.contextPath}/images/detruire.png" class="lien-supp"/>
					</a>
				</li>
			</c:forEach>
		</ul>
</div>