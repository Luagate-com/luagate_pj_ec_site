#!/usr/bin/env bash
# ============================================================
# Cloud Run デプロイスクリプト
#
# 使い方:
#   ./deploy.sh                # サービス名 prod-luagate-pj-ec-demo（完成版）
#   ./deploy.sh starter        # サービス名 prod-luagate-pj-ec-starter（starter）
#
# 事前準備:
#   1. gcloud auth login で認証済みであること
#   2. プロジェクトが選択済みであること（gcloud config set project ...）
#   3. .env.deploy に DB_URL / DB_USER / DB_PASSWORD を書いておく（git ignore 済み）
# ============================================================
set -euo pipefail

VARIANT="${1:-demo}"
case "$VARIANT" in
  demo)    SERVICE_NAME="prod-luagate-pj-ec-demo" ;;
  starter) SERVICE_NAME="prod-luagate-pj-ec-starter" ;;
  *)
    echo "Usage: $0 [demo|starter]" >&2
    exit 1
    ;;
esac

REGION="asia-northeast1"

# DB 接続情報を .env.deploy から読み込み
if [ ! -f .env.deploy ]; then
  echo "ERROR: .env.deploy が見つかりません。DB_URL / DB_USER / DB_PASSWORD を書いてください。" >&2
  exit 1
fi
# shellcheck disable=SC1091
set -a; source .env.deploy; set +a

: "${DB_URL:?DB_URL が未設定}"
: "${DB_USER:?DB_USER が未設定}"
: "${DB_PASSWORD:?DB_PASSWORD が未設定}"

echo "==> Deploying $SERVICE_NAME to $REGION (max-instances=1)"

gcloud run deploy "$SERVICE_NAME" \
  --source . \
  --region "$REGION" \
  --platform managed \
  --allow-unauthenticated \
  --max-instances 1 \
  --min-instances 0 \
  --memory 1Gi \
  --cpu 1 \
  --port 8080 \
  --timeout 300 \
  --set-env-vars "DB_URL=${DB_URL},DB_USER=${DB_USER},DB_PASSWORD=${DB_PASSWORD}"

echo "==> Done"
gcloud run services describe "$SERVICE_NAME" --region "$REGION" --format='value(status.url)'
