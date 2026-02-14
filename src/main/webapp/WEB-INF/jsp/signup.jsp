<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>新規会員登録</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <form class="card" action="${pageContext.request.contextPath}/signup" method="post">
      <div class="title">新規会員登録</div>

      <c:if test="${not empty signupError}">
        <div class="error"><c:out value="${signupError}" /></div>
      </c:if>

      <div class="field">
        <label class="label" for="name">氏名</label>
        <input class="input" id="name" name="name" type="text" value="${param.name}" placeholder="山田 花子">
      </div>

      <div class="field">
        <label class="label" for="email">メールアドレス</label>
        <input class="input" id="email" name="email" type="email" value="${param.email}" placeholder="example@northclout.jp">
      </div>

      <div class="field">
        <label class="label" for="password">パスワード</label>
        <input class="input" id="password" name="password" type="password" placeholder="・・・・・・・・">
      </div>

      <div class="field">
        <label class="label" for="postal">郵便番号</label>
        <input class="input" id="postal" name="postal" type="text" value="${param.postal}" placeholder="123-4567">
      </div>

      <div class="field">
        <label class="label" for="prefecture">都道府県</label>
        <input class="input" id="prefecture" name="prefecture" type="text" value="${param.prefecture}" placeholder="東京都">
      </div>

      <div class="field">
        <label class="label" for="city">市区町村・番地</label>
        <input class="input" id="city" name="city" type="text" value="${param.city}" placeholder="渋谷区神宮前1-2-3">
      </div>

      <div class="field">
        <label class="label" for="building">建物名・部屋番号</label>
        <input class="input" id="building" name="building" type="text" value="${param.building}" placeholder="渋谷マンション101">
      </div>

      <label class="checkbox">
        <input class="checkbox-input" type="checkbox" name="agree" value="1" ${param.agree == '1' ? 'checked' : ''}>
        <span class="checkbox-box" aria-hidden="true"></span>
        <span class="checkbox-text">利用規約に同意する</span>
      </label>

      <div class="actions">
        <button class="button-primary" type="submit">登録する</button>
      </div>
    </form>
  </main>
</body>
</html>
