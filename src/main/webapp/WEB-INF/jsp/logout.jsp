<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログアウト</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <form class="card" action="${pageContext.request.contextPath}/logout/execute" method="post">
      <div class="title">ログアウト</div>
      <div class="field">
        <div class="label">ログアウトしますか？</div>
      </div>
      <div class="actions">
        <a class="link-button" href="${pageContext.request.contextPath}/mypage">戻る</a>
        <button class="button-primary" type="submit">ログアウト</button>
      </div>
    </form>
  </main>
</body>
</html>
