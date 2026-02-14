<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>商品詳細</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/good_detail.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <div class="crumbs">
      <span>ホーム</span>
      <span class="divider">/</span>
      <span>商品詳細</span>
    </div>

    <div class="detail">
      <div class="image">
        <c:choose>
          <c:when test="${not empty good.imageUrl}">
            <img src="${pageContext.request.contextPath}${good.imageUrl}" alt="">
          </c:when>
          <c:otherwise>
            <img src="${pageContext.request.contextPath}/assets/images/goods/${good.name}.jpg" alt="">
          </c:otherwise>
        </c:choose>
      </div>

      <div class="info">
        <div class="name"><c:out value="${good.name}" /></div>
        <div class="price">¥<fmt:formatNumber value="${good.price}" pattern="#,##0" /></div>
        <div class="desc"><c:out value="${good.description}" /></div>

        <div class="qty-row">
          <span>数量</span>
          <div class="counter">
            <button type="button" aria-label="減らす">-</button>
            <input class="value-input" type="number" name="quantity" value="1" min="1" max="99" form="addToCartForm">
            <button type="button" aria-label="増やす">+</button>
          </div>
        </div>

        <div class="actions">
          <form id="addToCartForm" action="${pageContext.request.contextPath}/cart" method="post" style="display:inline-flex;gap:10px;">
            <input type="hidden" name="goodId" value="${good.id}">
            <button class="button-primary" type="submit">カートに追加</button>
          </form>
          <a class="button-secondary" href="${pageContext.request.contextPath}/regi">購入手続きへ</a>
        </div>
      </div>
    </div>
  </main>
</body>
</html>
