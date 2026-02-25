# 環境構築手順

## 前提
- macOS（Homebrew 利用）
- Java 17
- Maven
- Tomcat 10
- PostgreSQL（ローカル or Neon 接続）

## 0. Homebrew の確認
未インストールの場合は先に Homebrew を導入する。

```bash
brew --version
```

## 1. Java 17 のインストールと確認
インストール:

```bash
brew install openjdk@17
```

PATH と JAVA_HOME を設定:

```bash
echo 'export PATH="/opt/homebrew/opt/openjdk@17/bin:$PATH"' >> ~/.zshrc
echo 'export JAVA_HOME="/opt/homebrew/opt/openjdk@17"' >> ~/.zshrc
source ~/.zshrc
```

確認（`17` が表示されること）:

```bash
java -version
```

## 2. Maven のインストールと確認
インストール:

```bash
brew install maven
```

確認:

```bash
mvn -version
```

## 3. Tomcat 10 の準備
インストール:

```bash
brew install tomcat@10
```

`CATALINA_HOME` を設定:

```bash
echo 'export CATALINA_HOME="/opt/homebrew/opt/tomcat@10/libexec"' >> ~/.zshrc
echo 'export PATH="$CATALINA_HOME/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

確認:

```bash
catalina version
```

## 4. PostgreSQL（ローカル）の準備
インストール:

```bash
brew install postgresql@16
```

起動:

```bash
brew services start postgresql@16
```

PATH 反映:

```bash
echo 'export PATH="/opt/homebrew/opt/postgresql@16/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

バージョン確認:

```bash
psql --version
```

### 4.1 DB とテーブルの作成
`postgres` ユーザーで接続:

```bash
psql postgres
```

DB 作成と接続:

```sql
CREATE DATABASE ec_site;
\c ec_site
```

テーブル作成:

```sql
\i db/schema.sql
```

### 4.2 初期データ投入

```sql
\i db/seed.sql
```

### 4.2.1 seed投入後の確認

```sql
\dt
SELECT COUNT(*) FROM goods;
SELECT COUNT(*) FROM stocks;
SELECT id, code, name, image_url FROM goods ORDER BY id;
```

### 4.3 アプリ用ユーザーの作成

```sql
CREATE USER ec_user WITH PASSWORD 'ec_password';
GRANT CONNECT ON DATABASE ec_site TO ec_user;
\c ec_site
GRANT USAGE ON SCHEMA public TO ec_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO ec_user;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO ec_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO ec_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
  GRANT USAGE, SELECT ON SEQUENCES TO ec_user;
\q
```

## 5. Neon 接続を使う場合
### 5.1 Neon Local（Docker）を使う
Neon Local コンテナを起動する。

```bash
docker run --rm \
  --name neon-local-db \
  -p 5432:5432 \
  -e NEON_API_KEY=<your_neon_api_key> \
  -e NEON_PROJECT_ID=<your_neon_project_id> \
  -e DRIVER=postgres \
  neondatabase/neon_local:latest
```

`src/main/resources/db.properties` の設定例:

```properties
db.url=jdbc:postgresql://localhost:5432/neondb
db.user=neon
db.password=npg
```

### 5.2 Neon Managed（クラウド）を使う
Neon Local を使わず直接接続する場合は、Neon管理画面の接続情報を設定する。

```properties
db.url=jdbc:postgresql://<host>/<database>?sslmode=require
db.user=<user>
db.password=<password>
```

## 6. DB接続設定
`src/main/resources/db.properties` をローカル環境に合わせて更新する。

ローカル開発では `db.properties` の値を利用する。  
本番環境では `DB_URL` / `DB_USER` / `DB_PASSWORD` の環境変数を設定し、
環境変数の値を優先して接続する。

例（本番・Neon Managed）:

```bash
export DB_URL='jdbc:postgresql://<host>/<database>?sslmode=require'
export DB_USER='<user>'
export DB_PASSWORD='<password>'
```

## 7. ビルド
以下で WAR を生成する。

```bash
mvn clean package
```

## 8. 配置と起動
`target/ROOT.war` を Tomcat の `webapps/` に配置して Tomcat を起動する。

例:

```bash
cp target/ROOT.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh
```

## 9. 動作確認
- `http://localhost:8080/` にアクセス
- `/goods` にリダイレクトされることを確認
