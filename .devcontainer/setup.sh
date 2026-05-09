#!/usr/bin/env bash
# ============================================================
# Codespaces 初回セットアップ
# - Java 25 / Maven の確認
# - Apache Tomcat 10.1 のダウンロード & 配置
# - CATALINA_HOME 等の環境変数を ~/.bashrc に追記
# ============================================================
set -euo pipefail

TOMCAT_VERSION="10.1.36"
TOMCAT_DIR="$HOME/apache-tomcat-${TOMCAT_VERSION}"
TOMCAT_TARBALL="apache-tomcat-${TOMCAT_VERSION}.tar.gz"
TOMCAT_URL="https://dlcdn.apache.org/tomcat/tomcat-10/v${TOMCAT_VERSION}/bin/${TOMCAT_TARBALL}"

echo "==> Java / Maven バージョン確認"
java -version
mvn -version

if [ ! -d "$TOMCAT_DIR" ]; then
  echo "==> Apache Tomcat ${TOMCAT_VERSION} をダウンロード"
  cd "$HOME"
  curl -fsSL "$TOMCAT_URL" -o "$TOMCAT_TARBALL"
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
