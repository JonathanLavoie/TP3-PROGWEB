<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<h2>Vos demandes d'amitié</h2>

<c:set var="noUtil" value="${sessionScope.connBean.getNoUtil()}" />

<c:if test="${not empty noUtil}">

	<sql:query var="resDemAmis" dataSource="jdbc/twitface">
			SELECT MemNoDemandeur, DemAmiDate, MemNom 
			FROM demandes_amis INNER JOIN membres
			ON MemNoDemandeur = MemNo
			WHERE MemNoInvite = ?
			ORDER BY DemAmiDate
			<sql:param value="${noUtil}" />
	</sql:query>
	<c:choose>

			<c:when test="${empty resDemAmis.rows}">
				<p class="aucun"><strong>Aucune demande d'ami trouver</strong></p>
			</c:when>

			<c:otherwise>
				<p><strong>${resDemAmis.rowCount} Demande(s) d'ami(s)</strong></p>
				<ul class="lst-membres">
					<c:forEach var="Amis" items="${resDemAmis.rows}">
						<li>
							<fmt:formatNumber var="noFormate" value="${Amis.MemNoDemandeur}" pattern="000" />
							<div class="div-img">
								<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${Amis.MemNom}" />
							</div>
							<p class="nom-ami-sugg">${Amis.MemNom}</p>
							<p>Date de la demande: <br/> ${Amis.DemAmiDate}</p>
							<a href="${pageContext.request.contextPath}/membre/accept-dem-ami?no-ami=${Amis.MemNoDemandeur}">Accepter comme ami</a>
						</li>
					</c:forEach>
				</ul>
			</c:otherwise>
	</c:choose>
</c:if>
<c:choose>

	<c:when test="${requestScope.modAcceptDemAmis.getExiste() == false}">
		<p id="msg-err-accept-dem-ami">${requestScope.modAcceptDemAmis.getMessage()}</p>
	</c:when>

	<c:otherwise>
		<p id="msg-conf-accept-dem-ami">${requestScope.modAcceptDemAmis.getMessage()}</p>
	</c:otherwise>

</c:choose>

<h2>Vos amis</h2>

<c:if test="${not empty noUtil}">

	<sql:query var="resAmis" dataSource="jdbc/twitface">
			SELECT MemNo, MemNom 
			FROM amis
			INNER JOIN membres
			ON MemNo1 = MemNo
			WHERE MemNo2 = ?
			UNION SELECT MemNo, MemNom 
			FROM amis
			INNER JOIN membres
			ON MemNo2 = MemNo
			WHERE MemNo1 = ?
			ORDER BY MemNom
			<sql:param value="${noUtil}" />
			<sql:param value="${noUtil}" />
	</sql:query>
	<c:choose>

			<c:when test="${empty resAmis.rows}">
				<p class="aucun"><strong>Vous avez aucun ami</strong></p>
			</c:when>

			<c:otherwise>
				<p><strong>${resAmis.rowCount} Amis</strong></p>
				<ul class="lst-membres">
					<c:forEach var="Amis" items="${resAmis.rows}">
						<li>
							<fmt:formatNumber var="noFormate" value="${Amis.MemNo}" pattern="000" />
							<div class="div-img">
								<img src="${pageContext.request.contextPath}/images/photos/membre-${noFormate}.jpg" class="photo-membre" alt="Photo de ${Amis.MemNom}" />
							</div>
							<p class="nom-ami-sugg">${Amis.MemNom}</p>
							<a href="${pageContext.request.contextPath}/membre/supp-ami?no-ami=${Amis.MemNo}">Supprimer cet ami</a>
						</li>
					</c:forEach>
				</ul>
			</c:otherwise>
	</c:choose>
</c:if>