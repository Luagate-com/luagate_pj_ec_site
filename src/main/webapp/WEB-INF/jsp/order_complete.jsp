<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
    <div class="order-number">注文番号: #<c:out value="${orderNumber}" /></div>
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
