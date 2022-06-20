# AP設計・実装ガイドライン

## AP設計ガイドライン

### 画面遷移設計

- ログイン画面遷移パターン
  - ログイン画面
    - SpringSecurityで実現
- メインメニュー画面遷移パターン
  - メインメニュー画面
- 検索条件入力_検索結果一覧画面遷移パターン
  - 検索条件入力_検索結果一覧画面
    - 検索条件、検索結果一覧は、同じ画面に表示する。
    - 検索条件は、必要に応じて、アコーディオンなどで、小さくできるとよい。
    - 検索条件入力なしで、検索結果一覧画面のみを出す場合もある。
- 単件詳細表示画面遷移パターン
  - 単件詳細表示画面
  - 単件詳細表示画面、単件更新編集画面、単件更新確認画面、単件更新完了画面
  - 単件詳細表示画面、単件削除完了画面
  - 単件登録画面、単件登録画面、単件登録完了画面

``` plantuml

state ログイン画面遷移パターン {

[*] -R-> ログイン画面
ログイン画面 -> ログイン画面 : ログイン失敗
ログイン画面 -D-> [*] : 閉じる

}

state メインメニュー画面遷移パターン {

ログイン画面 -D--> メインメニュー画面 : 【遷移】ログイン成功

}

state 検索条件入力_検索結果一覧画面遷移パターン {

メインメニュー画面 -D-> 検索条件入力_検索結果一覧画面 : 【ポップアップ】メニュー選択

検索条件入力_検索結果一覧画面 -> 検索条件入力_検索結果一覧画面 : 検索
検索条件入力_検索結果一覧画面 -> 検索条件入力_検索結果一覧画面 : 頁切替え
検索条件入力_検索結果一覧画面 -> 検索条件入力_検索結果一覧画面 : 複数選択＋一括削除
検索条件入力_検索結果一覧画面 -D-> [*] : 閉じる

}


state 単件詳細表示画面遷移パターン {

検索条件入力_検索結果一覧画面 -D--> 単件詳細表示画面 : 【ポップアップ】単件選択＋表示

単件詳細表示画面 -L-> [*] : 閉じる
単件詳細表示画面 -R-> 単件詳細表示画面 : ダイアログ確認＋削除＋完了メッセージつき再表示（データなし）



state 単件更新画面遷移 {

単件詳細表示画面 -D-> 単件更新編集画面 : 【遷移】編集
検索条件入力_検索結果一覧画面 -D-> 単件更新編集画面 : 【ポップアップ】単件選択＋編集

単件更新編集画面 -> 単件更新編集画面 : 入力チェックエラー
単件更新編集画面 -D-> 単件更新確認画面 : 確認
単件更新確認画面 -R-> 単件詳細表示画面 : 更新＋完了メッセージつき再表示
}


state 単件登録画面遷移 {

単件詳細表示画面 -[hidden]-> 単件登録編集画面
検索条件入力_検索結果一覧画面 -D--> 単件登録編集画面 : 【ポップアップ】新規登録

単件登録編集画面 -> 単件登録編集画面 : 入力チェックエラー
単件登録編集画面 -D-> 単件登録確認画面 : 確認
単件登録確認画面 -R-> 単件詳細表示画面 : 登録＋完了メッセージつき再表示
}



```

- 画面遷移のポイント
  - 単件更新確認画面では、新旧差分を強調表示できるとよい。
  - 単件登録確認画面は、単件更新確認画面とUXを合わせるために、表示する。
  - 単件更新確認画面、単件登録確認画面は、同じ画面レイアウトにする。
  - 単件更新編集画面、単件登録編集画面は、同じ画面レイアウトにする。
  - 単件詳細表示画面での削除は、ダイアログで確認の上、削除し、完了メッセージを表示する、データなしの単件詳細表示画面を表示する。

### 画面設計

- 日付入力は、カレンダー部品で入力できるとよい。
- 入力項目のフォーカスアウトで、入力チェックする。

### エンティティ設計

- 基本的に、第三正規形で、エンティティを定義する。
- トランザクション系エンティティ、マスタ系エンティティを区別して定義する。
- 主体となるエンティティと、それに従属するエンティティを整理する。

- 参考URL
  - https://qiita.com/nishina555/items/a79ece1b54faf7240fac

### セッション設計

- 業務利用しない
- 以下で限定利用
  - 認証情報管理
  - トランザクショントークン管理

### エラー設計

- システムエラー
  - AP基盤部品からスローする例外
  - Javaの検査例外をラップする例外
- 業務エラー
  - ビジネスロジックで、異常として、処理を中断する場合にスローする例外

### メッセージ設計

- ログメッセージ
  - システムの運用者向けのメッセージ
  - システムの運用者むけの言語で出力
  - エラー原因を特定に必要な情報を含める。
- 応答メッセージ
  - システムの利用者向けのメッセージ
  - システムの内部実装がわかってしまうエラーメッセージにしない。
  - 多言語対応が必要

  - UXを考慮したエラーメッセージにする。
    - 文章にする
    - ユーザーを非難しない
    - ユーザー目線のメッセージにする
    - 何が起きているかを説明する
    - どうすれば解決できるのかを伝える
  - 入力しフォーカスアウトしたときに、入力項目の下にエラーの内容を記載します。
  - そもそもエラーにならないほうがいい。
  - 想定されるユーザー側のエラーは全て考慮する。
  - ユーザーにわかりやすく、攻撃者に必要以上に情報を与えない。
  - ページ最上部か送信ボタン付近に表示する。 
    - 複数のエラーがあることを伝える場合や、各項目の下に表示するエラーメッセージに該当するものがない場合には、ページ最上部か送信ボタン付近にエラーメッセージを表示します。
  - それ以上の行動ができない場合に、別のページに遷移してエラーを表示する。
    - そうでなければ、エラー表示のために遷移すべきできない。

- 参考URL
  - https://qiita.com/castaneai/items/da6baec750370689e2fa
  - https://upwrite.jp/writing_uxes/uxw4081889724
  - https://blog.btrax.com/jp/ux-writing-basic/
  - https://blog.nijibox.jp/article/practices_uxwriting/
  - https://zenn.dev/never_be_a_pm/scraps/758dc13aa81512
  - https://idealump.com/service/lab/44
  - https://uxmilk.jp/62956
  - https://baigie.me/officialblog/2022/04/21/error_design/
  - https://u-site.jp/alertbox/errors-forms-design-guidelines

### URI設計

- Web画面のURI
  - ログイン画面（表示）：GET /login
  - ログイン画面（ログイン）：POST /login
  - メインメニュー画面（表示）：GET /menu
  - 検索条件入力_検索結果一覧画面（表示）：GET /<list-name or entities>
    - クエリ文字列がなければ、検索条件なしで表示する。
  - 検索条件入力_検索結果一覧画面（検索）：GET /<list-name or entities>?aaaa=aaa&sort=bbb
    - 検索条件：item=a
    - ソート（昇順）：sort=item
    - ソート（降順）：sort=-item
  - 検索条件入力_検索結果一覧画面（頁切替え）：GET /<list-name or entities>?aaaa=aaa&sort=bbb&page
    - ページ番号：page=1
    - ページ表示件数 ：limit=10
  - 検索条件入力_検索結果一覧画面（削除）：POST /<list-name or entities>?aaaa=aaa&sort=bbb&page{action=remove-all}
    - クエリ文字列は、検索条件を指定し、完了後の再表示に利用する。
    - ポストデータ：action=remove-allがある場合
    - 削除後、リダイレクトで、削除時の検索条件で、1ページ目の検索条件入力_検索結果一覧画面を表示する。
  - 単件詳細表示画面（表示）：GET /<entities>/<entity-id>
    - データなしの場合は、データなしを表す画面として表示する。
  - 単件詳細表示画面（削除）：POST /<entities>/<entity-id>{action=remove}
    - 削除後後、リダイレクトで、削除したデータの単件詳細表示画面（表示）を、データなしの完了メッセージつきで表示する。
  - 単件更新編集画面（表示）：POST /<entities>/<entity-id>{action=update-input}
    - GETの場合、単件詳細表示画面（表示）となるが、リソースをあらわすURIとしては、適切なので、この仕組みとする。
    - このページをブックマークした場合は、単件詳細表示画面（表示）を表示する。
  - 単件更新編集画面（確認）：POST /<entities>/<entity-id>{action=update-confirm}
    - このページをブックマークした場合は、単件詳細表示画面（表示）を表示する。
    - 編集による新旧差分を強調表示する。
  - 単件更新確認画面（更新）：POST /<entities>/<entity-id>{action=update}
    - 更新後後、リダイレクトで、更新したデータの単件詳細表示画面（表示）を、完了メッセージつきで表示する。
  - 単件登録編集画面（表示）：POST /<entities>{action=register-input}
    - GETの場合、検索条件入力_検索結果一覧画面（表示）となるが、リソースをあらわすURIとしては、適切なので、この仕組みとする。
    - このページをブックマークした場合は、検索条件入力_検索結果一覧画面を表示する。
  - 単件登録編集画面（確認）：POST /<entities>action=register-confirm}
    - このページをブックマークした場合は、検索条件入力_検索結果一覧画面を表示する。
  - 単件登録確認画面（登録）：POST /<entities>action=register}
    - 登録後後、リダイレクトで、登録したデータの単件詳細表示画面（表示）を、完了メッセージつきで表示する。

- Web画面のURIの考慮ポイント
  - サーバサイドで、入力エラーが発生する場合、入力エラーつきで遷移元の画面を再表示するので、URIを変更しないようにする。
  - 検索条件は、クエリ文字列で指定する。
  - 参照系は、HTTP GETで表示し、ブックマーク可能にする。
  - 参照画面は、主キーをURIパスで指定する。

- RESTfulサービスURI
  - 参照 GET /api/v1/<entities>/<entity-id>.json
  - 新規作成 POST /api/v1/<entities>
  - 更新 PUT /api/v1/<entities>/<entity-id>
  - 削除 DELETE /api/v1/<entities>/<entity-id>
  - 検索 GET /api/v1/<entities>?items=&sort=&page=&limit=
  - コマンド実行 POST /api/v1/<command-name>

- RESTfulサービスURIの考慮ポイント
  - URIに、api、バージョンを入れる。

- RESTfulサービスURIの応答HTTPステータス
  - 200系
    - 200 OK：処理が成功したとき
    - 201 Created：POSTでデータを作成したとき
    - 202 Accepted：バッチ処理を受け付けたとき
  - 400系
    - 401 Unauthorized：認証が必要なのに未認証
    - 403 Forbidden：利用権限がない
    - 404 Not Found：指定したリソースがない
    - 409 Conflict：排他制御などで競合が発生した
    - 422 Unprocessable Entity：入力チェックエラー
    - 429 Too Many Requests：同一ユーザーからのリクエスト過多
    - 400 Bad Request：それ以外のクライアント起因によるエラー（業務エラー）
  - 500系
    - 503 Service Unavailable：閉塞中
    - 500 Internal Server Error：それ以外のサーバ内エラー（システムエラー）

- 参考URL
  - https://qiita.com/NagaokaKenichi/items/6298eb8960570c7ad2e9
  - https://qiita.com/mserizawa/items/b833e407d89abd21ee72
  - HTTPステータスコードの使いわけ
    - https://postd.cc/choosing-an-http-status-code/
  - https://qiita.com/ryo88c/items/0a3c7861015861026e00
  - https://www.itmanage.co.jp/column/http-www-request-response-statuscode/
  - https://cio.go.jp/guides
    - APIテクニカルガイドブック

### ログ設計

- トレースログ
  - プログラムの各所で、プログラムのふるまいを解析するログ
- アクセスログ
  - リクエスト処理、バッチ処理の出口で、処理時間と処理結果を記録するログ
- 監査ログ
  - システムの利用状況を監査するために必要な情報を記録するするログ
- エラー監視ログ
  - エラーが発生したことをあらわす、システム監視で検知可能なキーワードを記録するログ
- エラーログ
  - エラーの解析に必要な情報（エラー箇所を特定するためのスタックなど）を記録するログ

## AP実装ガイドライン

### 業務部品の種類

- Web画面向けController
  - 入力：○○Form、出力：○○Form、エンティティなど
  - 実装単位：画面遷移グループ単位に１つ。
- RESTfulサービス向けController
  - 入力：○○Request、出力：○○Response
  - 実装単位：エンティティ単位に１つ。
- Service
  - 入力・出力：エンティティなど
  - 実装単位：エンティティ単位に１つ。
- SharedService
  - 入力・出力：エンティティなど
  - 実装単位：エンティティ単位に１つ。
- Repository
  - 入力・出力：<Entity>、<Entity>Id、Page、Pageable、Criteria
- DAO
  - 入力・出力：<Entity>、<Entity>Id、Page、Pageable、Criteria

### Springアノテーションの使い分け

- org.springframework.stereotype.Service
  - Service、SharedService
- org.springframework.stereotype.Repository
  - Repository
- org.springframework.stereotype.Controller
  - Web画面向けController
- org.springframework.web.bind.annotation.RestController
  - RESTfulサービス向けController
- org.apache.ibatis.annotations.Mapper
  - DAO
  - SpringのComponentではないが、MapperScanの対象となる。
- org.springframework.stereotype.Component
  - 上記以外の部品

### データクラス実装ガイドライン

- 利用可能なデータ型
  - 真偽値：boolean
  - 列挙値：Enum（CodeEnumを実装）
  - 文字列：String
  - 日付：LocalDate
  - タイムスタンプ：LocalDateTime
  - 小数：BigDecimal
  - 整数（9桁以下）：int
  - 整数（18桁以下）：long
  - 整数（19桁以上）：BigInteger
  - 整数（19桁以上）：BigInteger
  - 繰り返し：List<T>
    - 実装には、ArrayList<T>を利用する。
    - デフォルト値には、ArrayList<T>を定義し、nullにならないようにする。

- 利用可能なバリデーション
  - 真偽値：boolean
    - AssertFalse
    - AssertTrue
  - 列挙値：Enum（CodeEnumを実装）
    - なし
  - 文字列：String
    - StringSize（独自）
    - StringPattern（独自）
  - 日付：LocalDate
    - なし
  - タイムスタンプ：LocalDateTime
    - なし
  - 小数：BigDecimal
    - Digit
    - DecimalMin
    - DecimalMax
  - 整数（9桁以下）：int
    - Min
    - Max
  - 整数（18桁以下）：long
    - Min
    - Max
  - 整数（19桁以上）：BigInteger
    - Min
    - Max
  - 繰り返し：List<T>
    - Size

  - 必須チェック
    - 文字列：String
      - NotEmpty
    - 繰り返し：List<T>
      - なし
        - 常に、NotNullとするので、チェックしない。
    - それ以外
      - NotNull
  - グループを利用する場合に利用
    - Null
  - 相関チェック
    - Correlation（独自）
  - 標準のメッセージを利用しない
    - AssertFalse
    - AssertTrue

- 実装のポイント
  - lombokの@Dataをクラスに付与し、private フィールドを定義する。
  - 共用しないネストしたデータは、public static な内部クラスとして定義する。
  - Serializableを実装する。
  - 基本的に継承を利用しない。
  - 必要に応じて、Jackson、JAXBなどのアノテーションを付与する。
  - バリデーションする場合の整数は、オーバフローしないように、常に、BigIntegerを利用する。

### データクラス（エンティティ）実装ガイドライン

- 実装のポイント
  - Entityクラスは、テーブル毎に作成する。
  - 主体となるエンティティは、それに従属するエンティティを、プロパティとして保持する。
  - 主体となるエンティティに従属するエンティティでない関連は、関連するエンティティの主キーのみをプロパティとして保持する。

### DBテーブル実装ガイドライン

- 利用可能なデータ型（PostgreSQL）
  - 真偽値：bool
  - 列挙値：char(n)
  - 最小文字数・最大文字数が同じ文字列：char(n)
  - 最小文字数・最大文字数が異なる文字列：varchar(n)
  - 日付：date
  - タイムスタンプ：timestamptz
  - 小数：decimal(p,s)
  - 整数（9桁以下）：int
  - 整数（18桁以下）：int8
  - 整数（19桁以上）：numeric(p,s)

- 実装のポイント
  - 主体となるエンティティに、更新カウンタ、更新時刻、作成時刻、更新者、作成者を記録する。
  - 主体となるエンティティは、更新カウンタによる楽観的排他を制御しながら更新する。
  - 主体となるエンティティに従属するエンティティは、主体となるエンティティを、楽観的排他排他制御で更新した後に変更する。
  - マスタ系エンティティは、複数の主体となるエンティティを同時に更新しない。
  - トランザクション系エンティティとマスタ系エンティティは、同時に更新しない。
  - トランザクション系エンティティは、更新順序を一意に決めて、遵守する。
  - トランザクション分離レベルは、ReadCommitedを想定。
  - 更新データの参照は、更新ロックを獲得して更新する。

### リポジトリ実装ガイドライン

- リポジトリAPI
  - 標準API
    - count：全件数のカウントのメソッド
    - save：1件更新のメソッド
    - saveAll：全件更新のメソッド
    - delete：1件削除のメソッド
    - deleteAll：全件削除のメソッド
    - findOneById：主キー検索のメソッド
    - findAll：全件検索のメソッド
    - findAllByIds：主キーリスト検索のメソッド
  - 拡張API（うまく、標準として定義できるとよい）
    - findAllBy：複数件検索系のメソッド
    - findPageBy：複数件ページ検索系のメソッド
    - countBy：件数のカウント系のメソッド
    - deleteBy：検索条件による削除メソッド
    - updateBy：検索条件による更新メソッド

- DAO API
  - insert(List<Entity>)
  - updateOneExclusively(Entity)
  - deleteByIds(List<EntityId>)
  - countAll():int
  - selectAll():List<Entity>
  - selectByIds(List<EntityId>):List<Entity>
  - selectByIdsForUpdate(List<EntityId>):List<Entity>
  - selectUpdateCountByIds(List<EntityId>):List<EntityUpdateCount>
  - selectUpdateCountByIdsForUpdate(List<EntityId>):List<EntityUpdateCount>

- 実装のポイント（MyBatis）
  - 主体となるEntityごとに、リポジトリを実装する。
  - 楽観的排他排他の実装は、リポジトリ内で実装する。
  - DBアクセスは、MyBatisで実装する。
  - DAOをMyBatisで実装し、リポジトリからDAOを利用する。
    - リポジトリは、DBアクセスのAPIをそのまま利用するだけでは実現できない。
  - Entity、EntityId以外のRepositoryで使用するDTO（検索条件など）は、Repositoryインタフェースと同じパッケージに配置する。

- 実装のポイント（見直し：Spring Data JDBC）
  - 主体となるEntityごとに、リポジトリを実装する。
  - 楽観的排他排他の実装は、リポジトリ内で実装する。
  - INSERTは、複数レコードを1回のSQLで登録する。
  - 主キーは複数カラムでもよい
  - 登録者、更新者、登録時刻、更新時刻を記録できる。
  - 削除時は、ルートレコードをロックしてから、これコードを削除し、ルートレコードを削除する。
  - Header-Detailも実現可能、Detailは、複数種類でも可能。
  - ページングはSQLで絞り込む。
  - カスタマイズSQLも利用可能。

- 参考URL
  - リポジトリのAPI
    - https://spring.pleiades.io/spring-data/commons/docs/current/api/org/springframework/data/repository/PagingAndSortingRepository.html
    - https://spring.pleiades.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html

  - DBアクセスは、MyBatisで実装する。
    - http://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/DataAccessDetail/DataAccessMyBatis3.html#dataaccessmybatis3appendixaboutmappermechanism
  - Spring Data REST
    - https://pppurple.hatenablog.com/entry/2017/03/06/221334

### コードリスト実装ガイドライン

- 静的な列挙値は、Enumで実現する。
- 動的な列挙値は、コードテーブルで実現する。

### Web画面向けController実装ガイドライン

- 実装のポイント
  - 画面遷移グループごとにクラスを定義し、URIごとにメソッドを実装する。

### RESTfulサービス向けController実装ガイドライン

- 実装のポイント
  - 主となるEntityごとにクラスを定義し、CRUDのメソッドを実装する。

## [3.2. ドメイン層の実装](http://terasolunaorg.github.io/guideline/current/ja/ImplementationAtEachLayer/DomainLayer.html)

### 3.2.3. Entityの実装

### 3.2.4. Repositoryの実装

- Serviceに対して、Entityのライフサイクルを制御するための操作（Repositoryインタフェース）を提供する。
- Entityを永続化する処理(Repositoryインタフェースの実装クラス)を提供する。
- Entityの永続先は、リレーショナルデータベースになることが多いが、NoSQLデータベース、キャッシュサーバ、外部システム、ファイル（共有ディスク）などになることもある。



## 業務部品






## 参考URL
- SpringBoot
https://cs27.org/wiki/kobespiral2021/?SpringBoot

- DAOとRepositoryの違い
https://blog.fukuchiharuki.me/entry/use-repository-and-dao-according-to-the-purpose

- Repositoryについて
https://qiita.com/mikesorae/items/ff8192fb9cf106262dbf

- TERASOLUNA ガイドライン
http://terasolunaorg.github.io/guideline/

- 画面遷移パターン
https://document.intra-mart.jp/library/iap/public/im_ui/im_design_guideline_pc/texts/screen_transition/index.html

- Macchinetta ガイドライン
https://macchinetta.github.io/server-guideline-thymeleaf/current/ja/
