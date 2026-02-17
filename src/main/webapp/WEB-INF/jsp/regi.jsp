<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ご注文手続き</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/regi.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <div class="title-block">
      <div class="title">ご注文手続き</div>
    </div>

    <c:if test="${not empty regiError}">
      <!-- regiError は RegiViewServlet / OrderCompleteServlet が設定する失敗メッセージ。 -->
      <div class="error">
        <div>!</div>
        <div>
          <div class="error-title">在庫状況の更新により、注文を確定できませんでした</div>
          <div class="error-text">確定処理中に在庫状況が変わりました。お手数ですが数量を調整してもう一度お試しください。</div>
          <!-- items はレジ表示中のカート内容（RegiViewServlet で作成）。 -->
          <c:forEach var="item" items="${items}">
            <div class="error-item" style="margin-top:16px;">
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
              </div>
              <div class="item-qty">数量 <c:out value="${item.quantity}" /></div>
            </div>
          </c:forEach>
        </div>
      </div>
    </c:if>

    <div class="columns">
      <div class="main">
        <div class="card">
          <div class="card-head">配送先</div>
          <div class="card-body">
            <!-- user は RegiViewServlet が session.userId から取得して設定する。 -->
            <div class="field">
              <div class="label">氏名</div>
              <input class="input" type="text"
                     value="${empty user.lastName and empty user.firstName ? '-' : user.lastName} ${user.firstName}"
                     readonly>
            </div>
            <div class="field">
              <div class="label">住所</div>
              <input class="input" type="text" value="${empty user.address ? '-' : user.address}" readonly>
            </div>
            <div class="field">
              <div class="label">電話</div>
              <input class="input" type="text" value="-" readonly>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-head">お支払い</div>
          <div class="card-body">
            <div class="field">
              <div class="label">カード</div>
              <c:choose>
                <c:when test="${not empty user.cardBrand and not empty user.cardNumberLast4}">
                  <input class="input" type="text" value="${user.cardBrand} **** ${user.cardNumberLast4}" readonly>
                </c:when>
                <c:otherwise>
                  <input class="input" type="text" value="-" readonly>
                </c:otherwise>
              </c:choose>
            </div>
            <div class="field">
              <div class="label">名義</div>
              <input class="input" type="text" value="${empty user.cardName ? '-' : user.cardName}" readonly>
            </div>
            <div class="field">
              <div class="label">有効期限</div>
              <c:choose>
                <c:when test="${not empty user.cardExpMonth and not empty user.cardExpYear}">
                  <input class="input" type="text" value="${user.cardExpMonth}/${user.cardExpYear}" readonly>
                </c:when>
                <c:otherwise>
                  <input class="input" type="text" value="-" readonly>
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-head">注文内容</div>
          <div class="card-body">
            <div class="items">
              <!-- items の各要素は CartItemViewDTO。 -->
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
                  </div>
                  <div class="item-qty">数量 <c:out value="${item.quantity}" /></div>
                </div>
              </c:forEach>
            </div>
            <div class="summary-row" style="margin-top:16px;">
              <span>小計</span>
              <!-- total は RegiViewServlet が算出した合計金額。 -->
              <span>¥<fmt:formatNumber value="${total}" pattern="#,##0" /></span>
            </div>
          </div>
        </div>
      </div>

      <div class="summary">
        <div class="summary-head">注文サマリー</div>
        <div class="summary-body">
          <div class="summary-row">
            <span>小計</span>
            <!-- total は注文サマリーでも同じ値を表示する。 -->
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
          <form action="${pageContext.request.contextPath}/order/complete" method="post">
            <button class="button-primary" type="submit">注文を確定</button>
          </form>
        </div>
      </div>
    </div>
  </main>
</body>
</html>
