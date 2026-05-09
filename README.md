# luagate_pj_ec_site

LuaGate 実践プロジェクト「ECサイト」のリポジトリ。

## リンク

- **完成版デモ** — https://prod-luagate-pj-ec-demo-v3bbmayaea-an.a.run.app
- **starter デモ** — https://prod-luagate-pj-ec-demo-starter-v3bbmayaea-an.a.run.app
- **教材ページ** — https://chotdekiru.com/luagate/practical-projects/1
- **Figma** — https://www.figma.com/design/5LLAPdI03tsb3z0ufTofP6/

## ブランチ

- `main` — 完成版（学習中は見ないでください）
- `starter` — 受講生の出発点（このブランチを clone して TODO を埋めていく）

```bash
git clone -b starter https://github.com/Luagate-com/luagate_pj_ec_site.git
```

## 開発環境

- **Java**: 25 (LTS, 2025年9月リリース)
- **Maven**: 3.9+
- **Tomcat**: 10.1

### GitHub Codespaces で動かす

`.devcontainer/devcontainer.json` を同梱しているので、Codespaces を起動すれば Java 25 + Maven が入った状態で立ち上がる。

Codespaces 標準ベースイメージは複数の JDK が `update-alternatives` で管理されているため、Java 25 がデフォルトでない場合は次のコマンドで切り替える。

```bash
./scripts/use-java25.sh
```

