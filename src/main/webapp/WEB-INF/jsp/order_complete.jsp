<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%--
  Ch7-6 注文完了画面 (order_complete.jsp)

  OrderCompleteServlet#doGet がセットする属性
    - orderNumber : 注文番号文字列（例 "ORD-12"）

  PRG パターンで遷移してくる前提なので、orderNumber は1度だけ表示される。
  完了文言・ボタン・イラストの枠はそのまま使う。${orderNumber} の埋め込みが課題。
--%>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>注文完了</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/order_complete.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <img class="illustration" src="${pageContext.request.contextPath}/assets/images/order_complete.svg" alt="">
    <div class="title">ご注文ありがとうございました</div>

    <%--
      TODO Ch7-6: 注文番号を表示する。
        - 表示フォーマット: 「注文番号: #ORD-12」のように "#" + orderNumber を出す
        - <c:out value="${orderNumber}" /> を使うとXSSエスケープも入って安全
        - 既存のクラス class="order-number" を使う
    --%>
    <div class="order-number">注文番号: #</div>

    <div class="message">
      <p style="margin:0;">ご注文内容はマイページからご確認いただけます。</p>
      <p style="margin:0;">配送状況はメールでご連絡します。</p>
    </div>
    <div class="actions">
      <a class="button-secondary" href="${pageContext.request.contextPath}/mypage">マイページへ</a>
      <a class="button-primary" href="${pageContext.request.contextPath}/goods">買い物を続ける</a>
    </div>
  </main>
</body>
</html>
