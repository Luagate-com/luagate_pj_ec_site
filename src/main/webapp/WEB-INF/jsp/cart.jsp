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
      <div class="error"><c:out value="${cartError}" /></div>
    </c:if>

    <div class="columns">
      <div class="items">
        <c:choose>
          <c:when test="${empty items}">
            <div class="item-card">カートに商品がありません。</div>
          </c:when>
          <c:otherwise>
            <c:forEach var="item" items="${items}">
              <div class="item-card">
                <div class="item-image">
                  <c:choose>
                    <c:when test="${not empty item.good.imageUrl}">
                      <img src="${pageContext.request.contextPath}${item.good.imageUrl}" alt="">
                    </c:when>
                    <c:otherwise>
                      <img src="${pageContext.request.contextPath}/assets/images/goods/placeholder.svg" alt="">
                    </c:otherwise>
                  </c:choose>
                </div>
                <div class="item-info">
                  <div class="item-name"><c:out value="${item.good.name}" /></div>
                  <div class="item-price">¥<fmt:formatNumber value="${item.good.price}" pattern="#,##0" /></div>
                  <div class="item-subtotal">小計 ¥<fmt:formatNumber value="${item.subtotal}" pattern="#,##0" /></div>
                </div>
                <div class="counter">
                  <form class="counter-form" action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="_method" value="PUT">
                    <input type="hidden" name="goodId" value="${item.good.id}">
                    <button type="button" data-action="minus" aria-label="減らす">-</button>
                    <input class="count" type="number" name="quantity" value="${item.quantity}" min="1" max="99">
                    <button type="button" data-action="plus" aria-label="増やす">+</button>
                  </form>
                </div>
                <div class="actions">
                  <form action="${pageContext.request.contextPath}/cart" method="post">
                    <input type="hidden" name="_method" value="DELETE">
                    <input type="hidden" name="goodId" value="${item.good.id}">
                    <button class="action-link" type="submit">削除</button>
                  </form>
                </div>
              </div>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </div>

      <div class="summary">
        <div class="summary-head">注文</div>
        <div class="summary-body">
          <div class="summary-row">
            <span>小計</span>
            <span>¥<fmt:formatNumber value="${total}" pattern="#,##0" /></span>
          </div>
          <div class="summary-row">
            <span>送料</span>
            <span>¥0</span>
          </div>
          <div class="summary-row">
            <span>合計</span>
            <span>¥<fmt:formatNumber value="${total}" pattern="#,##0" /></span>
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
