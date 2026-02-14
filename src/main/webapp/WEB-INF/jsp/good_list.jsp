<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>商品一覧</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <div class="section-row">
      <div class="section-title">
        <h1>商品</h1>
        <span>全 <c:out value="${totalCount}" /> 件</span>
      </div>
      <div class="tabs">
        <a href="${pageContext.request.contextPath}/goods" class="${empty selectedCategory ? 'active' : ''}">すべて</a>
        <c:forEach var="category" items="${categories}">
          <a href="${pageContext.request.contextPath}/goods?category=${category}" class="${selectedCategory == category ? 'active' : ''}">
            <c:out value="${category}" />
          </a>
        </c:forEach>
      </div>
    </div>

    <div class="grid">
      <c:forEach var="good" items="${goods}">
        <a class="card" href="${pageContext.request.contextPath}/goods/detail?id=${good.id}">
          <c:choose>
            <c:when test="${not empty good.imageUrl}">
              <img src="${pageContext.request.contextPath}${good.imageUrl}" alt="">
            </c:when>
            <c:otherwise>
              <img src="${pageContext.request.contextPath}/assets/images/goods/${good.name}.jpg" alt="">
            </c:otherwise>
          </c:choose>
          <div class="card-body">
            <div class="card-title"><c:out value="${good.name}" /></div>
            <div class="card-category"><c:out value="${good.category}" /></div>
            <div class="card-price">¥<fmt:formatNumber value="${good.price}" pattern="#,##0" /></div>
          </div>
        </a>
      </c:forEach>
    </div>

    <div class="pager">
      <span class="count">
        <span class="current">01</span>
        <span>/</span>
        <span>01</span>
      </span>
    </div>
  </main>
</body>
</html>
