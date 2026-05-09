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
        <!-- totalCount は GoodListServlet が設定する表示件数。 -->
        <h1>商品</h1>
        <span>全 <c:out value="${totalCount}" /> 件</span>
      </div>
      <div class="tabs">
        <!-- selectedCategory / categories は GoodListServlet から受け取る。 -->
        <a href="${pageContext.request.contextPath}/goods" class="${empty selectedCategory ? 'active' : ''}">すべて</a>
        <c:forEach var="category" items="${categories}">
          <a href="${pageContext.request.contextPath}/goods?category=${category}" class="${selectedCategory == category ? 'active' : ''}">
            <c:out value="${category}" />
          </a>
        </c:forEach>
      </div>
    </div>

    <div class="grid">
      <!-- goods は GoodListServlet が DB から取得した商品一覧。 -->
      <!-- TODO Ch7-3: goods を c:forEach で展開し、各商品のカードを描画する
           ヒント:
             - <c:forEach var="good" items="${goods}"> で 1 件ずつ取り出す
             - リンク先は ${pageContext.request.contextPath}/goods/detail?id=${good.id}
             - good.imageUrl があればそれを、無ければ /assets/images/goods/${good.name}.jpg を表示
             - good.name / good.category / good.price をカードに表示する
             - 価格は <fmt:formatNumber value="${good.price}" pattern="#,##0" /> で 3 桁区切りに -->
      <c:if test="${empty goods}">
        <p>商品が見つかりませんでした。</p>
      </c:if>
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
