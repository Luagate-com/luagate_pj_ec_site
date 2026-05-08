# ec_site_practical_project (starter)

このブランチは **実装前の雛形（starter）** です。受講生はこのブランチを fork してから Codespaces で開き、各 Chapter の手順に従って Servlet / JSP / DAO を実装していきます。

完成版は `main` ブランチを参照してください（ただし、まずは自分で実装してみることを推奨）。

## 含まれているもの

- `db/schema.sql`, `db/seed.sql` — DB スキーマと初期データ（商品12件）
- `docs/` — ヒアリング / 企画書 / 要件定義 / 基本設計 / 詳細設計
- `src/main/java/com/northclout/ecsite/util/` — DB接続・パスワードハッシュ・トランザクション・バリデーションのユーティリティ
- `src/main/resources/db.properties` — DB接続設定（自分のNeon URLに書き換える）
- `src/main/webapp/WEB-INF/web.xml` — Servlet 設定
- `src/main/webapp/index.jsp` — welcome ページ
- `src/main/webapp/assets/css/` — CSS 雛形
- `src/main/webapp/assets/images/goods/` — 商品画像（12点）
- `pom.xml` — Maven ビルド設定

## 自分で書くもの

- `src/main/java/com/northclout/ecsite/dto/*.java`
- `src/main/java/com/northclout/ecsite/dao/*.java`
- `src/main/java/com/northclout/ecsite/service/*.java`
- `src/main/java/com/northclout/ecsite/controller/*.java`
- `src/main/webapp/WEB-INF/jsp/*.jsp`

## 進め方

[chotdekiru.com の実践プロジェクト#1](https://chotdekiru.com/luagate/practical-projects/1) を参照してください。

## 環境構築

`docs/environment_setup.md` を参照してください。Codespaces + Neon (PostgreSQL) で進めます。
