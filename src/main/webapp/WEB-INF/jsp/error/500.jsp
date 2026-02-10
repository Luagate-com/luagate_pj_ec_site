<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>システムエラー</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/base.css">
</head>
<body>
  <div style="max-width:720px;margin:64px auto;padding:24px;border:1px solid var(--border-main);border-radius:16px;background:var(--bg-main);">
    <h1 style="margin:0 0 12px 0;font-size:20px;">システムエラー</h1>
    <p style="margin:0 0 16px 0;color:var(--text-sub);">処理中にエラーが発生しました。時間をおいて再度お試しください。</p>
    <a href="${pageContext.request.contextPath}/goods" style="color:var(--accent-text);text-decoration:none;">商品一覧へ戻る</a>
  </div>
</body>
</html>
