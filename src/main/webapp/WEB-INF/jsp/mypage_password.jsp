<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>パスワード変更</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/auth.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <form class="card" action="${pageContext.request.contextPath}/mypage/password" method="post">
      <div class="title">パスワード変更</div>

      <%-- TODO Ch7-7: passwordError がセットされていればエラーメッセージとして表示する
           ヒント:
             <c:if test="${not empty passwordError}">
               <div class="error"><c:out value="${passwordError}" /></div>
             </c:if>
           passwordError は MypagePasswordServlet が session 経由で渡し、
           doGet 側で req.setAttribute に詰め替えている。 --%>

      <div class="field">
        <label class="label" for="currentPassword">現在のパスワード</label>
        <input class="input" id="currentPassword" name="currentPassword" type="password" placeholder="・・・・・・・・">
      </div>

      <div class="field">
        <label class="label" for="newPassword">新しいパスワード</label>
        <input class="input" id="newPassword" name="newPassword" type="password" placeholder="・・・・・・・・">
      </div>

      <div class="field">
        <label class="label" for="newPasswordConfirm">新しいパスワード（確認用）</label>
        <input class="input" id="newPasswordConfirm" name="newPasswordConfirm" type="password" placeholder="・・・・・・・・">
      </div>

      <div class="actions">
        <a class="link-button" href="${pageContext.request.contextPath}/mypage">戻る</a>
        <button class="button-primary" type="submit">更新</button>
      </div>
    </form>
  </main>
</body>
</html>
