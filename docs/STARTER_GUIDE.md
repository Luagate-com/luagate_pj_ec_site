# starter ブランチ ガイド

このブランチは **受講生がゼロから実装を埋めて完成版に追いつく** ためのスタート地点です。

## このブランチの状態

完成版 (`main`) から以下を削った状態にしてあります：

- **SKELETON 化されたファイル**: メソッドのシグネチャ・SQL 定数・JSP の枠組みは残っていますが、本体（ロジック）は `// TODO Ch7-X: ...` コメントになっています
- **そのまま残っているファイル**: 共通基盤 (`util/`)、DTO (`dto/`)、エラー画面、CSS、画像、DB スキーマ、ドキュメント

受講生がゼロから書くべき分量は **約 720 行**（完成版 3,166 行のうち約 23%）です。残り 77% は「読んで理解する」教材として配布されています。

## 章対応マップ

| 章 | テーマ | 触るファイル |
|---|---|---|
| Ch7-1 | 環境構築・Hello Servlet | （SKELETON 化なし。動作確認のみ） |
| Ch7-2 | DTO とパッケージ構成 | DTO 群を読むだけ |
| Ch7-3 | 商品一覧/詳細（DAO 入門） | `GoodDAO`, `GoodListServlet`, `GoodDetailServlet`, `good_list.jsp`, `good_detail.jsp` |
| Ch7-4 | カート（セッション） | `CartViewServlet`, `cart.jsp` |
| Ch7-5 | 認証（ログイン/会員登録） | `UserDAO`, `LoginServlet`, `SignupServlet`, `login.jsp`, `signup.jsp` |
| Ch7-6 | レジ・注文確定（トランザクション） | `OrderService`, `OrderDAO`, `StockDAO`, `RegiViewServlet`, `OrderCompleteServlet`, `regi.jsp`, `order_complete.jsp` |
| Ch7-7 | マイページ | `MypageServlet`, `MypagePasswordServlet`, `mypage.jsp`, `mypage_password.jsp` |
| Ch7-8 | 仕上げ・自由課題 | エラーハンドリング強化、CSS 微調整、追加検証 |

## 触ってはいけないもの

| 種類 | 理由 |
|---|---|
| `db/schema.sql` `db/seed.sql` | カラム名を変えると DAO が連鎖死亡 |
| `util/*.java` | 共通基盤。書き換えるとセキュリティ事故・挙動ズレ |
| `dto/*.java` | getter/setter 100 行を書く時間は学習効果が薄い |
| `pom.xml` | 依存関係は完成形。触るとビルド不能 |
| `assets/css/*` `assets/images/*` | デザインを書かせるとスコープ爆発 |
| `WEB-INF/web.xml` | エラーページ設定を壊さないため |

## 始め方

1. 環境構築（Java 17 + Maven + Tomcat 10 + PostgreSQL）は `docs/environment_setup.md` に従って完了させる
2. `db/schema.sql` を流して、`db/seed.sql` で商品データを投入
3. `mvn clean package -DskipTests` でビルドして `target/ROOT.war` を Tomcat の `webapps/` に配置
4. `http://localhost:8080/` で空骨格が起動することを確認（商品一覧は空、ログインもできない状態）
5. 教材の Ch7-3 から順に TODO を埋めていく

## ビルドが落ちないこと

starter ブランチは clone 直後の状態で `mvn clean package -DskipTests` が **必ず通る** ように調整してあります。`return Optional.empty()` などの最小実装を入れてあるため、起動はするが画面に何も出ない、というのが正常な初期状態です。

ビルドが落ちる場合は `pom.xml` を触っていないか、Java 17 を使っているかを確認してください。

## Cloud Run でデプロイしたい

starter ブランチは完成版と同じ `Dockerfile` と `deploy.sh` を含んでいるので、自分の環境で実装を埋めながら Cloud Run で動かすことも可能です。

```bash
cp .env.deploy.example .env.deploy   # DB 接続情報を書く
./deploy.sh starter                  # サービス名 prod-luagate-pj-ec-starter で公開
```

詳細は `docs/cloud_run_deploy.md` を参照してください。
