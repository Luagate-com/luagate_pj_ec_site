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
        <!-- signupError は SignupServlet の入力/重複チェック失敗時に設定される。 -->
        <div class="error"><c:out value="${signupError}" /></div>
      </c:if>

      <div class="field">
        <label class="label">氏名</label>
        <!-- 入力値は param から再表示し、エラー時の再入力コストを下げる。 -->
        <div class="name-fields">
          <input class="input" name="lastName" type="text" value="${param.lastName}" placeholder="山田">
          <input class="input" name="firstName" type="text" value="${param.firstName}" placeholder="花子">
        </div>
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
        <label class="label" for="passwordConfirm">パスワード（確認用）</label>
        <input class="input" id="passwordConfirm" name="passwordConfirm" type="password" placeholder="・・・・・・・・">
      </div>

      <label class="checkbox">
        <input class="checkbox-input" type="checkbox" name="agree" value="1" ${param.agree == '1' ? 'checked' : ''}>
        <span class="checkbox-box" aria-hidden="true"></span>
        <span class="checkbox-text">利用規約に同意する</span>
      </label>

      <div class="actions">
        <a class="link-button" href="${pageContext.request.contextPath}/login">ログインへ戻る</a>
        <button class="button-primary" type="submit">登録する</button>
      </div>
    </form>
  </main>
</body>
</html>
