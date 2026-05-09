<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ログイン</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <form class="card" action="${pageContext.request.contextPath}/login" method="post">
      <div class="title">ログイン</div>

      <!--
        TODO Ch7-5: ログインエラーメッセージを表示
        ヒント:
          - LoginServlet が認証失敗時に req.setAttribute("loginError", "...") でメッセージを設定する
          - JSTL の <c:if test="${not empty loginError}"> でメッセージが存在するときだけ表示
          - 値の出力には XSS 対策として <c:out value="${loginError}" /> を使う
          - 表示用の class は "error" を使うとスタイルが当たる (assets/css/auth.css)
        例:
          <c:if test="${not empty loginError}">
            <div class="error"><c:out value="${loginError}" /></div>
          </c:if>
      -->

      <div class="field">
        <label class="label" for="email">メールアドレス</label>
        <!-- value は入力エラー時に param 経由で再表示する。 -->
        <input class="input" id="email" name="email" type="email" value="${param.email}" placeholder="example@northclout.jp">
      </div>

      <div class="field">
        <label class="label" for="password">パスワード</label>
        <input class="input" id="password" name="password" type="password" placeholder="・・・・・・・・">
        <button class="helper" type="button">パスワードを忘れた方</button>
      </div>

      <div class="actions">
        <a class="link-button" href="${pageContext.request.contextPath}/signup">新規会員登録</a>
        <button class="button-primary" type="submit">ログイン</button>
      </div>
    </form>
  </main>
</body>
</html>
