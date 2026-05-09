<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%--
  Ch7-6 レジ画面 (regi.jsp)

  RegiViewServlet がセットする属性
    - user      : UserDTO（配送先・カード情報）
    - items     : List<CartItemViewDTO>（カート内訳。item.good / item.quantity / item.subtotal）
    - total     : 合計金額
    - regiError : 直前の注文確定で失敗した場合のメッセージ（任意）

  カラムレイアウトと枠（card / summary）はそのまま使う。
  EL（${user.*} や c:forEach var="item"）の部分を埋めるのが課題。
--%>
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
      <%-- regiError がある＝直前の確定で失敗（在庫不足等）。エラー枠＋カート内容を再表示する。 --%>
      <div class="error">
        <div>!</div>
        <div>
          <div class="error-title">在庫状況の更新により、注文を確定できませんでした</div>
          <div class="error-text">確定処理中に在庫状況が変わりました。お手数ですが数量を調整してもう一度お試しください。</div>

          <%--
            TODO Ch7-6: items を c:forEach で回して、各カート行を表示する。
              - var="item" items="${items}"
              - 各 item は CartItemViewDTO で、item.good (GoodDTO) / item.quantity を持つ
              - 既存の構造（class="error-item" の中に item-image / item-info / item-qty）に合わせる
              - 商品画像は item.good.imageUrl が空の時のフォールバックを忘れずに（placeholder.svg）
              - 価格表示は <fmt:formatNumber value="${item.good.price}" pattern="#,##0" /> を使う
          --%>

        </div>
      </div>
    </c:if>

    <div class="columns">
      <div class="main">
        <div class="card">
          <div class="card-head">配送先</div>
          <div class="card-body">
            <%--
              TODO Ch7-6: user の情報を表示する。
                - 氏名: ${user.lastName} ${user.firstName}（両方空なら "-" を出す等のフォールバックも考える）
                - 住所: ${user.address}（空なら "-"）
                - 電話: 今回は固定で "-" でOK
              既存のフィールド構造（class="field" の中に label / input.readonly）に合わせること。
            --%>
            <div class="field">
              <div class="label">氏名</div>
              <input class="input" type="text" value="-" readonly>
            </div>
            <div class="field">
              <div class="label">住所</div>
              <input class="input" type="text" value="-" readonly>
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
            <%--
              TODO Ch7-6: user のカード情報を表示する。
                - カード: "${user.cardBrand} **** ${user.cardNumberLast4}"。空なら "-"
                - 名義  : ${user.cardName}。空なら "-"
                - 有効期限: "${user.cardExpMonth}/${user.cardExpYear}"。どちらか空なら "-"
              c:choose / c:when / c:otherwise を使うとフォールバック分岐が書きやすい。
            --%>
            <div class="field">
              <div class="label">カード</div>
              <input class="input" type="text" value="-" readonly>
            </div>
            <div class="field">
              <div class="label">名義</div>
              <input class="input" type="text" value="-" readonly>
            </div>
            <div class="field">
              <div class="label">有効期限</div>
              <input class="input" type="text" value="-" readonly>
            </div>
          </div>
        </div>

        <div class="card">
          <div class="card-head">注文内容</div>
          <div class="card-body">
            <div class="items">
              <%--
                TODO Ch7-6: items を c:forEach で回してカート内訳を表示する。
                  - var="item" items="${items}"
                  - 構造: <div class="item-card"> の中に
                            item-image (img タグ。imageUrl が空ならplaceholder.svg)
                            item-info  (item.good.name / item.good.price)
                            item-qty   ("数量 ${item.quantity}")
                  - 価格は <fmt:formatNumber value="${item.good.price}" pattern="#,##0" />
              --%>
            </div>
            <div class="summary-row" style="margin-top:16px;">
              <span>小計</span>
              <%-- total は RegiViewServlet が算出した合計金額。 --%>
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
          <%--
            「注文を確定」ボタンは /order/complete に POST する。
            これが OrderCompleteServlet#doPost を叩いて、トランザクション処理を実行する。
          --%>
          <form action="${pageContext.request.contextPath}/order/complete" method="post">
            <button class="button-primary" type="submit">注文を確定</button>
          </form>
        </div>
      </div>
    </div>
  </main>
</body>
</html>
