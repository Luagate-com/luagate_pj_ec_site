<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <title>ECサイト実践プロジェクト (starter)</title>
  <style>
    body { font-family: system-ui, sans-serif; max-width: 720px; margin: 40px auto; padding: 0 20px; line-height: 1.7; }
    h1 { border-bottom: 2px solid #06A85F; padding-bottom: 8px; }
    code { background: #f5f5f5; padding: 2px 6px; border-radius: 4px; font-size: 0.9em; }
    .note { background: #fff8e1; border-left: 4px solid #f59e0b; padding: 12px 16px; margin: 16px 0; }
    a { color: #06A85F; }
  </style>
</head>
<body>
  <h1>ECサイト 実践プロジェクト (starter)</h1>
  <p>このページは <strong>starter ブランチ</strong> の初期画面です。各 Chapter の手順に従って Servlet / JSP / DAO を実装していくと、商品一覧 → カート → 認証 → レジ → マイページ が動くようになります。</p>

  <div class="note">
    <strong>Tomcat が起動できているこの画面が見えていれば OK。</strong><br>
    次は <code>Ch7-2: DB準備とプロジェクト雛形</code> に進み、<code>db/schema.sql</code> と <code>db/seed.sql</code> を Neon に流し込みます。
  </div>

  <h2>用意されているもの</h2>
  <ul>
    <li><code>db/schema.sql</code> / <code>db/seed.sql</code> — DBスキーマと初期データ（商品12件）</li>
    <li><code>docs/</code> — ヒアリング・企画書・要件定義・基本設計・詳細設計</li>
    <li><code>src/main/java/com/northclout/ecsite/util/</code> — DB接続・パスワードハッシュ・トランザクション・バリデーションのユーティリティ</li>
    <li><code>src/main/webapp/assets/</code> — CSS雛形・商品画像</li>
    <li><code>src/main/resources/db.properties</code> — DB接続設定テンプレ（自分のNeon URLに書き換える）</li>
  </ul>

  <h2>これから実装するもの</h2>
  <ul>
    <li>DTO (<code>GoodDTO</code>, <code>UserDTO</code> など)</li>
    <li>DAO (<code>GoodDAO</code>, <code>UserDAO</code>, <code>OrderDAO</code> など)</li>
    <li>Service (<code>OrderService</code>)</li>
    <li>Controller / Servlet (<code>GoodListServlet</code>, <code>CartViewServlet</code> など)</li>
    <li>JSP (<code>good_list.jsp</code>, <code>cart.jsp</code> など)</li>
  </ul>

  <p>進め方は <a href="https://chotdekiru.com/luagate/practical-projects/1">chotdekiru.com の実践プロジェクト#1</a> を参照してください。</p>
</body>
</html>
