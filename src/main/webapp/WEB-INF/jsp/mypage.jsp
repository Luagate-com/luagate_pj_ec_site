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

    <%-- TODO Ch7-7: mypageError がセットされていればエラーメッセージとして表示する
         ヒント: <c:if test="${not empty mypageError}"> ... <c:out value="${mypageError}" /> ... </c:if> --%>

    <div class="columns">
      <div class="card" style="flex:1;">
        <div class="card-head">会員情報</div>
        <div class="card-body">
          <%-- user は MypageServlet が session.userId から取得して setAttribute で渡す。 --%>
          <div class="row">
            <div class="label">氏名</div>
            <%-- TODO Ch7-7: user.lastName / user.firstName を表示する --%>
            <div></div>
          </div>
          <div class="row">
            <div class="label">メール</div>
            <%-- TODO Ch7-7: user.email を表示する --%>
            <div></div>
          </div>
          <div class="row">
            <div class="label">住所</div>
            <%-- TODO Ch7-7: user.address を表示する --%>
            <div></div>
          </div>

          <form class="edit-form" action="${pageContext.request.contextPath}/mypage" method="post">
            <%-- _method=PUT で更新リクエストとして扱う。 --%>
            <input type="hidden" name="_method" value="PUT">
            <div class="field">
              <label class="label">氏名</label>
              <div class="name-fields">
                <%-- TODO Ch7-7: user の各フィールドを value 属性で表示する（lastName / firstName） --%>
                <input class="input" type="text" name="lastName" value="" placeholder="姓">
                <input class="input" type="text" name="firstName" value="" placeholder="名">
              </div>
            </div>
            <div class="field">
              <label class="label">住所</label>
              <%-- TODO Ch7-7: user.address を value にセット --%>
              <input class="input" type="text" name="address" value="" placeholder="住所">
            </div>
            <div class="field">
              <label class="label">カード名義</label>
              <%-- TODO Ch7-7: user.cardName を value にセット --%>
              <input class="input" type="text" name="cardName" value="" placeholder="名義">
            </div>
            <div class="field">
              <label class="label">カード番号（下4桁）</label>
              <%-- TODO Ch7-7: user.cardNumberLast4 を value にセット --%>
              <input class="input" type="text" name="cardLast4" value="" placeholder="0000">
            </div>
            <div class="field">
              <label class="label">有効期限（月）</label>
              <%-- TODO Ch7-7: user.cardExpMonth を value にセット --%>
              <input class="input" type="text" name="cardExpMonth" value="" placeholder="12">
            </div>
            <div class="field">
              <label class="label">有効期限（年）</label>
              <%-- TODO Ch7-7: user.cardExpYear を value にセット --%>
              <input class="input" type="text" name="cardExpYear" value="" placeholder="30">
            </div>
            <div class="field">
              <label class="label">カードブランド</label>
              <%-- TODO Ch7-7: user.cardBrand を value にセット --%>
              <input class="input" type="text" name="cardBrand" value="" placeholder="VISA">
            </div>
            <div class="actions">
              <button class="button-secondary" type="submit">更新</button>
            </div>
          </form>
        </div>
      </div>

      <div class="card settings">
        <div class="card-head" style="padding:0;">設定</div>
        <a class="settings-link" href="${pageContext.request.contextPath}/mypage/password">パスワード変更</a>
        <div>支払い方法</div>
        <a class="settings-link muted" href="${pageContext.request.contextPath}/logout">ログアウト</a>
      </div>
    </div>
  </main>
</body>
</html>
