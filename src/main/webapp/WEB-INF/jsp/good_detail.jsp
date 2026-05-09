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
        <!-- good は GoodDetailServlet が id 指定で取得した1件データ。 -->
        <!-- TODO Ch7-3: good の各フィールドを EL で展開
             ヒント:
               - good.imageUrl があれば ${pageContext.request.contextPath}${good.imageUrl} を img.src に
               - 無ければ /assets/images/goods/${good.name}.jpg をフォールバックに使う -->
      </div>

      <div class="info">
        <!-- TODO Ch7-3: good の各フィールドを EL で展開
             ヒント:
               - 商品名: <c:out value="${good.name}" />
               - 価格: ¥<fmt:formatNumber value="${good.price}" pattern="#,##0" />
               - 説明: <c:out value="${good.description}" /> -->

        <div class="qty-row">
          <span>数量</span>
          <div class="counter">
            <button type="button" aria-label="減らす">-</button>
            <input class="value-input" type="number" name="quantity" value="1" min="1" max="99" form="addToCartForm">
            <button type="button" aria-label="増やす">+</button>
          </div>
        </div>

        <div class="actions">
          <!-- good.id を hidden で渡して CartViewServlet 側で追加処理する。 -->
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
