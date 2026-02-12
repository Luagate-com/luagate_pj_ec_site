<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>マイページ</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/goods.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/mypage.css">
</head>
<body class="page">
  <jsp:include page="/WEB-INF/jsp/partials/header.jsp" />

  <main class="container">
    <div class="title">マイページ</div>

    <c:if test="${not empty mypageError}">
      <div class="error"><c:out value="${mypageError}" /></div>
    </c:if>

    <div class="columns">
      <div class="card" style="flex:1;">
        <div class="card-head">会員情報</div>
        <div class="card-body">
          <div class="row">
            <div class="label">氏名</div>
            <div><c:out value="${user.lastName}" /> <c:out value="${user.firstName}" /></div>
          </div>
          <div class="row">
            <div class="label">メール</div>
            <div><c:out value="${user.email}" /></div>
          </div>
          <div class="row">
            <div class="label">住所</div>
            <div><c:out value="${user.address}" /></div>
          </div>

          <form class="edit-form" action="${pageContext.request.contextPath}/mypage" method="post">
            <input type="hidden" name="_method" value="PUT">
            <input type="text" name="lastName" value="${user.lastName}" placeholder="姓">
            <input type="text" name="firstName" value="${user.firstName}" placeholder="名">
            <input class="full" type="text" name="address" value="${user.address}" placeholder="住所">
            <input type="text" name="cardBrand" value="${user.cardBrand}" placeholder="カードブランド">
            <input type="text" name="cardLast4" value="${user.cardNumberLast4}" placeholder="カード下4桁">
            <input type="text" name="cardExpMonth" value="${user.cardExpMonth}" placeholder="有効期限(月)">
            <input type="text" name="cardExpYear" value="${user.cardExpYear}" placeholder="有効期限(年)">
            <input class="full" type="text" name="cardName" value="${user.cardName}" placeholder="名義">
            <div class="actions full">
              <button class="button-secondary" type="submit">編集する</button>
            </div>
          </form>
        </div>
      </div>

      <div class="card settings">
        <div class="card-head" style="padding:0;">設定</div>
        <div>パスワード変更</div>
        <div>支払い方法</div>
        <div class="muted">ログアウト</div>
        <form action="${pageContext.request.contextPath}/logout" method="post" style="margin-top:8px;">
          <button class="button-secondary" type="submit">ログアウト</button>
        </form>
      </div>
    </div>
  </main>
</body>
</html>
