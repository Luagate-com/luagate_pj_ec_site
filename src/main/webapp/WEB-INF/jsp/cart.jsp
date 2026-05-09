<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>カート</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/cart.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <div class="title">カート</div>

    <c:if test="${not empty cartError}">
      <!-- cartError は CartViewServlet がセッション経由で渡すエラーメッセージ。 -->
      <div class="error"><c:out value="${cartError}" /></div>
    </c:if>

    <div class="columns">
      <div class="items">
        <c:choose>
          <c:when test="${empty items}">
            <div class="item-card">カートに商品がありません。</div>
          </c:when>
          <c:otherwise>
            <%-- TODO Ch7-4: items を <c:forEach var="item" items="${items}"> で展開する --%>
            <%--   ループの中で表示すべき項目:                                          --%>
            <%--     - 商品画像 (item.good.imageUrl が空ならプレースホルダー)            --%>
            <%--     - 商品名 (item.good.name)                                          --%>
            <%--     - 単価 (item.good.price) と 小計 (item.subtotal) を fmt:formatNumber で整形 --%>
            <%--     - 数量変更フォーム: _method=PUT, goodId, quantity を持つ POST フォーム  --%>
            <%--     - 削除フォーム:    _method=DELETE, goodId を持つ POST フォーム         --%>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="summary">
        <div class="summary-head">注文</div>
        <div class="summary-body">
          <div class="summary-row">
            <span>小計</span>
            <%-- TODO Ch7-4: CartViewServlet が req にセットした total を fmt:formatNumber で整形して表示 --%>
            <span>¥0</span>
          </div>
          <div class="summary-row">
            <span>送料</span>
            <span>¥0</span>
          </div>
          <div class="summary-row">
            <span>合計</span>
            <!-- TODO Ch7-4: 合計欄も同じ total を表示する（送料は今回の章では一律0円扱い） -->
            <span>¥0</span>
          </div>
          <div class="summary-note">確定後はキャンセルできません。</div>
        </div>
        <div class="summary-action">
          <form action="${pageContext.request.contextPath}/regi" method="get">
            <button class="button-primary" type="submit">レジへ進む</button>
          </form>
          <a class="continue-link" href="${pageContext.request.contextPath}/goods">買い物を続ける</a>
        </div>
      </div>
    </div>
  </main>
  <script>
    // カウンターの+/-は即時送信する
    document.querySelectorAll('.counter-form').forEach(function(form) {
      var input = form.querySelector('input[name="quantity"]');
      form.querySelectorAll('button[data-action]').forEach(function(btn) {
        btn.addEventListener('click', function() {
          var value = parseInt(input.value, 10) || 1;
          if (btn.dataset.action === 'minus') {
            value = Math.max(1, value - 1);
          } else {
            value = Math.min(99, value + 1);
          }
          input.value = value;
          form.submit();
        });
      });
    });
  </script>
</body>
</html>
