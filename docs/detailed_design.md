**雑貨ECサイト 詳細設計書**  
株式会社​​luagate  
システム開発部 アプリケーションエンジニア 佐藤 恒一

## **1\. 文書情報**

* 文書名：雑貨ECサイト 詳細設計書  
* 版：v0.1（ドラフト）  
* 作成者：システム開発部 アプリケーションエンジニア 佐藤 恒一  
* 対象：弊社開発チーム

---

## **2\. 詳細設計書の位置付け**

本書は、基本設計書で定義した画面・URL・データ・主要処理フローを前提として、実装に必要となる内部仕様を確定する。

---

## **3\. 前提**

* 技術：Java 17 / Servlet / JSP / MySQL  
* 対象端末：PC・スマートフォン・タブレット（レスポンシブ対応を行う）  
* 認証：あり（ログイン／ログアウト／新規会員登録／マイページ）  
* 決済：なし  
* 商品・在庫：seed投入済み（管理画面なし）  
* ログ：基本設計書に記載された方針に従う

---

## **4\. ディレクトリ構成**

/src  
  /controller        // Servlet群（リクエスト受付、遷移制御）  
  /service           // 業務処理（注文確定など）  
  /dao               // DBアクセス  
  /dto               // 画面・層間のデータ受け渡し  
  /util              // 共通（DB接続、トランザクション、バリデーション等）  
/webapp  
  /WEB-INF/jsp       // JSP（画面描画のみ）  
  /assets            // CSS/JS/画像（レスポンシブCSS含む）  
    /images  
      /goods   //商品画像（パスはDBで管理）

責務分離方針（再掲）

* JSP：描画のみ（業務判断・DBアクセス禁止）  
* Servlet：入力受付、バリデーション呼び出し、Service呼び出し、画面遷移制御  
* Service：複数DAOを束ねる業務処理、トランザクション境界  
* DAO：SQL実行、DTOへマッピング

---

## **5\. URL・Servlet・JSP対応表**

| 画面ID | URL | Method | 処理Servlet | 表示JSP |
| :---- | :---- | :---- | :---- | :---- |
| G-01 商品一覧 | /goods | GET | GoodListServlet | good\_list.jsp |
| G-01 商品一覧（絞り込み） | /goods?category=x | GET | GoodListServlet | good\_list.jsp |
| G-02 商品詳細 | /goods/detail?id={id} | GET | GoodDetailServlet | good\_detail.jsp |
| G-03 カート | /cart | GET | CartViewServlet | cart.jsp |
| カート追加 | /cart | POST | CartAddServlet | /cartへリダイレクト |
| カート更新 | /cart | PUT or PATCH | CartUpdateServlet | /cartへリダイレクト |
| カート削除 | /cart | DELETE | CartRemoveServlet | /cartへリダイレクト |
| G-04 レジ | /regi | GET | RegiViewServlet | regi.jsp |
| G-05 注文完了 | /order/complete | GET | OrderCompleteViewServlet | order\_complete.jsp |
| 注文確定（成功） | /order/complete | POST | OrderCompleteServlet | redirect:/order/complete |
| 注文確定（失敗） | /order/complete | POST | OrderCompleteServlet | forward: regi.jsp |
| G-06 ログイン | /login | GET | LoginViewServlet | login.jsp |
| ログイン処理 | /login | POST | LoginServlet | 成功 redirect:/goods / 失敗 forward: login.jsp |
| G-07 ログアウト | /logout | GET | LogoutViewServlet | logout.jsp |
| ログアウト処理 | /logout | POST | LogoutServlet | redirect:/goods |
| G-08 新規会員登録 | /signup | GET | SignupViewServlet | signup.jsp |
| 会員会員登録処理 | /signup | POST | SignupServlet | 成功 redirect:/goods / 失敗 forward: signup.jsp |
| G-09 マイページ | /mypage | GET | MypageViewServlet | mypage.jsp |
| マイページ更新 | /mypage | PUT or PATCH | MypageUpdateServlet | 成功 redirect:/mypage / 失敗 forward: mypage.jsp |

遷移方針

* POST後は原則 redirect（二重送信防止）  
  * 例：/order/complete 成功 → forward: order\_complete.jsp  
  * 例：/order/complete 失敗（在庫不足） → forward: regi.jsp（またはredirect:/regi）

---

## **6\. セッション設計（DTO詳細）**

### **6.1 セッションキー**

* session.cart：List\<CartItem\>（CartItemDTOのリスト）  
* session.userId : Long（ログイン済みユーザーID）

### **6.2 DTO定義**

#### CartItemDTO

* goodId : long  
* quantity : int

※ goodName/price は保持しない。（表示時にDB参照）

注文作成時の unit\_price は DBの goods.price を参照し order\_items に保存。

---

## **7\. DAO／Service／Controller 詳細**

### **7.1 DAO一覧**

#### GoodDAO

* List\<GoodDTO\> findAll()  
* List\<GoodDTO\> findByCategory(String category)  
* Optional\<GoodDTO\> findById(long id)  
* List\<GoodDTO\> findByIds(List\<Long\> ids)

#### StockDAO

* Optional\<StockDTO\> findByGoodId(long goodId)  
* StockDTO findByGoodIdForUpdate(long goodId) ※注文確定で使用  
* int updateQuantity(long goodId, int newQuantity)

#### OrderDAO

* long insertOrder(long userId, LocalDateTime orderedAt, int totalAmount)  
* int insertOrderItem(long orderId, long goodId, int unitPrice, int quantity)

#### UserDAO

* Optional\<UserDTO\> findById(long userId)  
* Optional\<UserDTO\> findByEmail(String email)（ログイン用）  
* long insertUser(...)（会員登録用）  
* int updateUser(...)（マイページ更新用）

### **7.2 Service一覧**

#### OrderService

* OrderResult completeOrder(long userId, List\<CartItemDTO\> cartItems)  
  * 戻り値に「成功/失敗」「エラーメッセージ」「注文ID（任意）」を含める

### **7.3 Controller（Servlet）一覧と責務**

* GoodListServlet：categoryの検証→DAOで一覧取得→JSPへ  
* GoodDetailServlet：idの検証→存在チェック→JSPへ（無ければ404方針へ）  
* CartAddServlet：数量検証→セッションカートへ追加→redirect:/cart  
* CartUpdateServlet：数量検証→セッションカート更新→redirect:/cart  
* CartRemoveServlet：セッションカートから削除→redirect:/cart  
* OrderCompleteServlet：session.userId取得（未ログインなら/loginへredirect）→カート取得→OrderService呼出（userId付き）→成功/失敗で遷移  
* RegiViewServlet：userId確認→cart取得→goods取得→user取得→regi.jspへ

---

## **8\. DBカラム定義**

## **8.1 方針**

* DBは MySQL を前提とする  
* 本フェーズの要件に必要な最小限のカラムのみ定義する  
* 商品・在庫は seed 投入済みを前提とする  
* 金額は円の整数（INT）で管理する  
* 日時は DATETIME で管理する

### **8.2 テーブル一覧**

* goods（商品）  
* stocks（在庫）  
* orders（注文）  
* order\_items（注文明細）  
* users（ユーザー）

### **8.3 テーブル定義**

#### A) goods（商品）

テーブル概要

* 商品情報を管理する  
* 商品は「1商品 \= 1販売単位」とし、SKU（色・サイズ等）のバリエーションは持たない

| No | カラム名 | 型 | NULL | KEY | デフォルト | 説明 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| 1 | id | BIGINT | NOT NULL | PK |  | 商品ID（採番） |
| 2 | code | VARCHAR(32) | NOT NULL | UNIQUE |  | 商品コード（例：ZAK-001） |
| 3 | name | VARCHAR(255) | NOT NULL |  |  | 商品名 |
| 4 | description | TEXT | NOT NULL |  |  | 商品説明 |
| 5 | price | INT | NOT NULL |  |  | 販売価格（税込・円） |
| 6 | category | VARCHAR(64) | NOT NULL |  |  | カテゴリ名 |
| 7 | image\_url | VARCHAR(1024) | NULL |  | NULL | 商品画像URL／参照パス |
| 8 | created\_at | DATETIME | NOT NULL |  |  | 作成日時 |
| 9 | updated\_at | DATETIME | NOT NULL |  |  | 更新日時 |

備考

* category は固定マスタは持たず、文字列で管理する  
* image\_url は固定URL参照／ローカル参照のいずれかを想定する

#### 

#### B) stocks（在庫）

テーブル概要

* 商品単位で在庫数を管理する  
* 注文確定時に減算更新する

| No | カラム名 | 型 | NULL | KEY | デフォルト | 説明 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| 1 | good\_id | BIGINT | NOT NULL | PK / FK |  | 商品ID（goods.id） |
| 2 | quantity | INT | NOT NULL |  |  | 在庫数 |
| 3 | updated\_at | DATETIME | NOT NULL |  |  | 更新日時 |

備考

* 在庫は商品単位で管理する  
* 在庫履歴テーブルは本フェーズでは作成しない

#### C) orders（注文）

* 注文確定により作成される注文データを管理する

| No | カラム名 | 型 | NULL | KEY | デフォルト | 説明 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| 1 | id | BIGINT | NOT NULL | PK |  | 注文ID（採番） |
| 2 | user\_id | BIGINT | NOT NULL | FK |  | ユーザーID |
| 3 | ordered\_at | DATETIME | NOT NULL |  |  | 注文日時 |
| 4 | total\_amount | INT | NOT NULL |  |  | 合計金額（税込・円） |
| 5 | created\_at | DATETIME | NOT NULL |  |  | 作成日時 |

**備考**

* 合計金額は order\_items の合算値を保存する

#### D) order\_items（注文明細）

テーブル概要

* 注文に含まれる商品の明細を管理する  
* 注文時点の単価を保持する

| No | カラム名 | 型 | NULL | KEY | デフォルト | 説明 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| 1 | id | BIGINT | NOT NULL | PK |  | 注文明細ID |
| 2 | order\_id | BIGINT | NOT NULL | FK |  | 注文ID（orders.id） |
| 3 | good\_id | BIGINT | NOT NULL | FK |  | 商品ID（goods.id） |
| 4 | unit\_price | INT | NOT NULL |  |  | 注文時単価（税込・円） |
| 5 | quantity | INT | NOT NULL |  |  | 購入数量 |
| 6 | created\_at | DATETIME | NOT NULL |  |  | 作成日時 |

**備考**

* unit\_price は注文確定時の goods.price を保存する

#### E) users（ユーザー）

テーブル概要

* ユーザー情報を管理する

| No | カラム名 | 型 | NULL | KEY | デフォルト | 説明 |
| :---- | :---- | :---- | :---- | :---- | :---- | :---- |
| 1 | id | BIGINT | NOT NULL | PK |  | ユーザーID |
| 2 | email | VARCHAR(255) | NOT NULL |  |  | メールアドレス |
| 3 | password\_hash | VARCHAR(255) | NOT NULL |  |  | パスワードハッシュ |
| 4 | last\_name | VARCHAR(32) | NOT NULL |  |  | 名字 |
| 5 | first\_name | VARCHAR(32) | NOT NULL |  |  | 名前 |
| 6 | address | VARCHAR(255) | NOT NULL |  |  | 住所 |
| 7 | card\_number\_last4 | VARCHAR(4) | NULL |  |  | カード番号下４桁 |
| 8 | card\_brand | VARCHAR(32) | NULL |  |  | カードブランド |
| 9 | card\_exp\_month | TINYINT | NULL |  |  | カード有効期限 |
| 10 | card\_name | VARCHAR(255) | NULL |  |  | カード名 |
| 11 | created\_at | DATETIME | NOT NULL |  |  | 作成日時 |
| 12 | updated\_at | DATETIME | NOT NULL |  |  | 更新日時 |

### **8.4 制約・インデックス**

主キー・外部キー

* goods.id：PK  
* goods.code：UNIQUE  
* stocks.good\_id：PK、FK → goods.id  
* orders.id：PK  
* orders.user\_id：FK → users.id  
* order\_items.id：PK  
* order\_items.order\_id：FK → orders.id  
* order\_items.good\_id：FK → goods.id

インデックス

* goods(category)  
* order\_items(order\_id)  
* order\_items(good\_id)

---

## **9\. SQL設計**

## **9.1 商品一覧**

全件

| SELECT id, code, name, description, price, category, image\_url FROM goods ORDER BY id; |
| :---- |

カテゴリ絞り込み

| SELECT id, code, name, description, price, category, image\_url FROM goods WHERE category \= ? ORDER BY id; |
| :---- |

### **9.2 商品詳細**

| SELECT id, code, name, description, price, category, image\_url FROM goods WHERE id \= ?; |
| :---- |

### **9.3 注文確定**

在庫取得

| SELECT good\_id, quantity FROM stocks WHERE good\_id \= ? FOR UPDATE; |
| :---- |

在庫更新

| UPDATE stocks SET quantity \= ?, updated\_at \= NOW() WHERE good\_id \= ?; |
| :---- |

注文作成

| INSERT INTO orders (user\_id, ordered\_at, total\_amount, created\_at) VALUES (?, ?, ?, NOW()); |
| :---- |

注文明細作成

| INSERT INTO order\_items (order\_id, good\_id, unit\_price, quantity, created\_at) VALUES (?, ?, ?, ?, NOW()); |
| :---- |

---

## **10\. トランザクション設計（注文確定）**

### **10.1 トランザクション境界**

* OrderService.completeOrder() をトランザクション境界とする  
  * トランザクションは Connection\#setAutoCommit(false) で開始し、成功時 commit / 失敗時 rollback を util.TransactionManager で共通化する  
* 途中で在庫不足や例外が発生した場合は **ロールバック**する

### **10.2 分離レベル**

* MySQLを前提に、在庫行に対して SELECT ... FOR UPDATE を利用する  
* これにより同一商品の同時注文時に在庫の整合性を担保する

---

## **11\. 主要処理シーケンス**

### **11.1 商品一覧（GET /goods）**

1. GoodListServlet が category（任意）を受領  
2. category が指定されていれば GoodDAO.findByCategory()  
3. 未指定なら GoodDAO.findAll()  
4. JSPに goods を渡して一覧表示

### **11.2 カート追加（POST /cart）**

1. CartAddServlet が goodId, quantity を受領  
2. quantity を検証（1以上の整数）  
3. セッション session.cart を取得（なければ新規）  
4. 既に同一goodIdがあれば quantity を加算  
5. redirect:/cart

### **11.3 レジ表示（GET /regi）**

1. RegiViewServlet が session.userId を取得（未ログイン→redirect:/login）  
2. session.cart を取得（空→redirect:/cart）  
3. GoodDAOでカート内商品の情報取得（findByIdsなど）  
4. UserDAOでユーザー情報取得（配送先・カード表示）  
5. forward: regi.jsp

### **11.4 注文確定（POST /order/complete）**

1. OrderCompleteServlet が session.userId を取得（未ログイン→redirect:/login）  
2. session.cart を取得（空→forward: regi.jsp で「カートが空」）  
3. OrderService.completeOrder(userId, cartItems)  
4. ServiceでTX開始  
5. stocks FOR UPDATE → 在庫不足判定  
6. 不足→rollback→失敗（forward: regi.jsp で在庫不足表示）  
7. 合計計算 → orders(user\_id含む) → order\_items → stocks減算  
8. commit  
9. セッションカートクリア  
10. forward: order\_complete.jsp

---

## **12\. バリデーション詳細**

* 数量：1 \<= quantity \<= 99（上限99で固定）  
* 商品ID：数値、かつ goods に存在すること  
* category：goods.category の定義済み値のみ（未定義値は全件扱い）  
* ログイン  
  * email：必須、形式（@含む）  
  * password：必須  
* 会員登録  
  * email：必須、形式、重複不可  
  * password：必須、確認一致  
  * 氏名：必須  
  * 住所：任意（基本設計のとおりなら）  
  * カード：保持する項目だけ（下4桁/ブランド/期限/名義）  
* マイページ  
  * 氏名/住所：任意  
  * カード情報：任意（保持する範囲内）

---

## **13\. エラー設計**

### **13.1 画面エラー**

* 入力エラー：同一画面にメッセージ表示（HTTP 200）  
* 在庫不足：レジ画面（G-04）に表示（HTTP200, forward）  
* カートが空：レジ画面に表示（基本設計の例に合わせる）

### **13.2 404相当**

* 存在しない商品IDの詳細アクセス：共通エラー画面（404）

### **13.3 想定外例外**

* 共通エラー画面へ（500相当）  
   ※ 実装は Filter または error-page（web.xml）で集約（どちらでも可）

---

## **14\. ログ設計（任意）**

* 例外ログ：スタックトレースを出力（任意）  
* 注文確定：注文ID、商品ID、数量、在庫更新結果を出力（任意）

---

## **15\. 画面（JSP）実装方針**

* JSTL / EL で出力（XSS対策の基本としてエスケープを前提）  
* 共通ヘッダー等が必要なら jsp:include を使用  
* レスポンシブCSSは提供物を適用

---

