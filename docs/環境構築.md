# 環境構築手順

## 前提
- macOS（Homebrew 利用）
- Java 17
- Maven
- Tomcat 10
- MySQL（ローカル）

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

## 4. MySQL（ローカル）の準備
MySQL 8.4（LTS）を利用する。

インストール:

```bash
brew install mysql@8.4
```

起動:

```bash
brew services start mysql@8.4
```

PATH 反映:

```bash
echo 'export PATH="/opt/homebrew/opt/mysql@8.4/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc
```

バージョン確認:

```bash
mysql --version
```

### 4.1 DB とテーブルの作成
root で接続できることを確認:

```bash
mysql -u root
```

DB とテーブルを作成:

```sql
source db/schema.sql;
```

### 4.2 初期データ投入

```sql
source db/seed.sql;
```

### 4.3 アプリ用ユーザーの作成

```sql
CREATE USER 'ec_user'@'localhost' IDENTIFIED BY 'ec_password';
GRANT ALL PRIVILEGES ON ec_site.* TO 'ec_user'@'localhost';
FLUSH PRIVILEGES;
exit;
```

## 5. DB接続設定
`src/main/resources/db.properties` をローカル環境に合わせて更新する。

## 6. ビルド
以下で WAR を生成する。

```bash
mvn clean package
```

## 7. 配置と起動
`target/ROOT.war` を Tomcat の `webapps/` に配置して Tomcat を起動する。

例:

```bash
cp target/ROOT.war $CATALINA_HOME/webapps/
$CATALINA_HOME/bin/startup.sh
```

## 8. 動作確認
- `http://localhost:8080/` にアクセス
- `/goods` にリダイレクトされることを確認

## 補足: MySQL 起動失敗時の対処
MySQL 9.x への自動アップグレードで起動できない場合は、8.4（LTS）へ戻す。

```bash
brew services stop mysql
mv /opt/homebrew/var/mysql /opt/homebrew/var/mysql_backup_$(date +%Y%m%d_%H%M%S)
brew install mysql@8.4
brew services start mysql@8.4
```
