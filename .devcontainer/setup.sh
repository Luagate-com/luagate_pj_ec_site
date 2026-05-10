#!/usr/bin/env bash
# ============================================================
# Codespaces 初回セットアップ
# - Java 25 / Maven の確認
# - Apache Tomcat 10.1 のダウンロード & 配置
# - CATALINA_HOME 等の環境変数を ~/.bashrc に追記
# ============================================================
set -euo pipefail

TOMCAT_VERSION="10.1.54"
TOMCAT_DIR="$HOME/apache-tomcat-${TOMCAT_VERSION}"
TOMCAT_TARBALL="apache-tomcat-${TOMCAT_VERSION}.tar.gz"
# 最新リリースは dlcdn.apache.org、過去版は archive.apache.org にある。両方試す
TOMCAT_URL_MIRROR="https://dlcdn.apache.org/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/${TOMCAT_TARBALL}"
TOMCAT_URL_ARCHIVE="https://archive.apache.org/dist/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/${TOMCAT_TARBALL}"

echo "==> PostgreSQL クライアント (psql) をインストール"
sudo apt-get update -qq && sudo apt-get install -y -qq postgresql-client

echo "==> Java / Maven バージョン確認"
java -version
mvn -version

if [ ! -d "$TOMCAT_DIR" ]; then
  echo "==> Apache Tomcat ${TOMCAT_VERSION} をダウンロード"
  cd "$HOME"
  if curl -fsSL "$TOMCAT_URL_MIRROR" -o "$TOMCAT_TARBALL"; then
    echo "    (dlcdn から取得)"
  elif curl -fsSL "$TOMCAT_URL_ARCHIVE" -o "$TOMCAT_TARBALL"; then
    echo "    (archive から取得)"
  else
    echo "ERROR: Tomcat ${TOMCAT_VERSION} のダウンロードに失敗" >&2
    exit 1
  fi
  tar -xzf "$TOMCAT_TARBALL"
  rm -f "$TOMCAT_TARBALL"
  chmod +x "$TOMCAT_DIR/bin/"*.sh
  echo "==> Tomcat 展開完了: $TOMCAT_DIR"
else
  echo "==> Tomcat は既に存在: $TOMCAT_DIR"
fi

# ~/.bashrc に CATALINA_HOME / PATH を1度だけ追記
if ! grep -q 'CATALINA_HOME=' "$HOME/.bashrc" 2>/dev/null; then
  cat >> "$HOME/.bashrc" <<EOF

# --- luagate_pj_ec_site Tomcat ---
export CATALINA_HOME="$TOMCAT_DIR"
export PATH="\$CATALINA_HOME/bin:\$PATH"
EOF
  echo "==> ~/.bashrc に CATALINA_HOME を追記"
fi

echo ""
echo "==> セットアップ完了"
echo "    新しいターミナルを開くと CATALINA_HOME が反映されます。"
echo "    現在のシェルで使うには: source ~/.bashrc"
echo ""
echo "    Tomcat 起動: \$CATALINA_HOME/bin/startup.sh"
echo "    Tomcat 停止: \$CATALINA_HOME/bin/shutdown.sh"
