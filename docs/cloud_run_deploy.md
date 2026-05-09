# Cloud Run デプロイ手順

このプロジェクトを **Google Cloud Run** にデプロイする手順。完成版（main）と starter ブランチの両方で同じ仕組みを使う。

## アーキテクチャ

```
┌─────────────────┐     ┌──────────────────────┐     ┌──────────────────┐
│ ブラウザ         │ ──> │ Cloud Run            │ ──> │ Neon Postgres     │
│ (受講生・講師)   │     │ Tomcat 10 + JSP      │     │ (managed)         │
│                 │ <── │ max-instances=1      │ <── │                  │
└─────────────────┘     └──────────────────────┘     └──────────────────┘
                              ↑
                              │ gcloud run deploy --source .
                              │
                        ┌─────────────┐
                        │ Dockerfile  │
                        │ (multi-stage)│
                        └─────────────┘
```

## 前提

- Google Cloud プロジェクトが作成済み
- `gcloud` CLI がインストール済み
- Cloud Run / Cloud Build / Artifact Registry API が有効化済み
- Neon（または別の Postgres）が起動済みで、外部からの接続情報が分かっている

## 1. gcloud 認証 & プロジェクト選択

```bash
gcloud auth login
gcloud config set project <YOUR_PROJECT_ID>
```

## 2. DB 接続情報を `.env.deploy` に書く

`.env.deploy.example` をコピーして実値を書く。

```bash
cp .env.deploy.example .env.deploy
# エディタで開いて DB_URL / DB_USER / DB_PASSWORD を埋める
```

`.env.deploy` は `.gitignore` 済みなのでコミットされない。

## 3. デプロイ

```bash
# 完成版（demo）をデプロイ → サービス名 prod-luagate-pj-ec-demo
./deploy.sh

# starter 版をデプロイ → サービス名 prod-luagate-pj-ec-starter
./deploy.sh starter
```

スクリプトは内部で以下を実行している：

```bash
gcloud run deploy <SERVICE_NAME> \
  --source .                  # Dockerfile を使ってビルド
  --region asia-northeast1
  --max-instances 1           # コスト固定のため最大1インスタンス
  --min-instances 0           # 無アクセス時はゼロまでスケールダウン
  --memory 1Gi
  --cpu 1
  --port 8080
  --allow-unauthenticated     # 受講生・講師が直接アクセスできるよう公開
  --set-env-vars "DB_URL=...,DB_USER=...,DB_PASSWORD=..."
```

## 4. 動作確認

デプロイ後に出力される URL（例: `https://prod-luagate-pj-ec-demo-xxxx-an.a.run.app`）を開いて

1. トップ → 商品一覧 が表示される
2. 商品詳細 → カート追加 が動く
3. 新規登録 → ログイン → マイページ が動く

## 5. 構成詳細

### Dockerfile（multi-stage）

- **build stage**: `maven:3.9-eclipse-temurin-17` で `mvn clean package` → `target/ROOT.war`
- **runtime stage**: `tomcat:10.1-jdk17-temurin` の `webapps/ROOT.war` に配置
- 起動時に `PORT` 環境変数の値を `server.xml` の Connector ポートに反映

### max-instances=1 の意図

- 学習用デモなので同時アクセスはほぼ無い
- セッション情報が JVM 内メモリに乗っているため、複数インスタンスではログイン状態が壊れる可能性がある
- コスト上限を明確化（1 インスタンス × アイドル時 0 = ほぼ無料）

### Cold Start について

- min-instances=0 のため、無アクセス時はインスタンスが落ちる
- 次のリクエスト時に Tomcat 起動 + WAR デプロイで 10〜20 秒かかる
- 体験を良くしたい場合は `--min-instances 1` に変えると常時稼働（コスト発生）

## 6. ログ確認

```bash
gcloud run services logs read prod-luagate-pj-ec-demo --region asia-northeast1 --limit 100
```

## 7. デプロイ削除（不要になったとき）

```bash
gcloud run services delete prod-luagate-pj-ec-demo --region asia-northeast1
gcloud run services delete prod-luagate-pj-ec-starter --region asia-northeast1
```
