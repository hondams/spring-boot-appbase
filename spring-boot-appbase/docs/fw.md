boot app web



# ap基盤機能

## 例外

- システムエラー
- 業務エラー

## 例外ハンドリング

- RESTfulサービス
	- @RestControllerAdviceを付与し、以下を継承し、拡張する。
		- org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
		- https://qiita.com/shotana/items/f3627e45feb912946c7c
- web画面
	- 以下を参考にする。
		- org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController
	- 以下で、HTTPステータスごとのエラー画面を表示する。
		- /<templates>/error/400.html
		- /<templates>/error/4xx.html
		- /<templates>/error.html

## バリデーション

- メッセージファイルの読み込み順序
	- （MessageSource）messages_ja.properties
	- （MessageSource）messages.properties
	- ValidationMessages_ja.properties
	- ValidationMessages.properties
	- 【ポイント】（MessageSource）のみを利用する。

- 標準のふるまい
	- Object名の決定
		- org.springframework.web.method.annotation.ModelFactory.getNameForParameter(org.springframework.core.MethodParameter)
		- ModelAttributeのvalue()、または、データクラスのSimpleClassNameのlowerCamelCase
	- 項目名の決定
		- org.springframework.validation.SmartValidator.validate(java.lang.Object, org.springframework.validation.Errors, java.lang.Object[])では、決定しない。この時点では、{0}のままとなっている。
		- 画面に表示するときに、MessageSourceResolvableからメッセージを組み立てる。
		- MessageSource（デフォルトでは、messages_ja.propertiesなど）から、「Object名.プロパティ名」で、表示項目名を取得する。

``` java
if (result.hasErrors()) {
    return "user/createForm";
}
```

- 推奨実装
	- 入力チェック対象のパラメータに、ModelAttributeは利用しない。
	- messages_ja.propertiesに、以下を定義する。
		- <オブジェクト名>.<プロパティ名>=<表示項目名>
		- <validationのAnnotationのクラス名>.message
			- {0} 表示項目名
			- ${validatedValue} 入力値
			- {min}、{max}など Annotationの値
		- typeMismatch.<データ方>
			- {0} 表示項目名

- 拡張機能
	- CorrelationのAnnotationで、相関チェックを定義。
		- ConstraintValidator<Correlation, <データクラス名>>を実装した、<データクラス名Correlation>Validatorを実装する。
		- 以下を参考に実装する。
			- https://terasolunaorg.github.io/guideline/1.0.1.RELEASE/ja/ArchitectureInDetail/Validation.html#id13

- バリデーション
	- 利用するバリデーション
		- @Valid
		- @AssertFalse
		- @AssertTrue
		- @Digits
		- @Size
		- @DecimalMax
		- @DecimalMin
		- @Max
		- @Min
		- @Pattern
		- @Null
		- @NotBlank
		- @NotNull
		- @NotEmpty
	- 参考URL
		- https://macchinetta.github.io/server-guideline-thymeleaf/current/ja/ArchitectureInDetail/WebApplicationDetail/Validation.html#id24


## データアクセス
- DB
- キュー

## ログ

- トレースログ
	- クラスに@Slf4jを付与し、logフィールドを利用しログを出力する。

- アクセスログ
	- 以下のように、Loggerを定義したクラスから出力する。
``` java
    @Getter
    private static final Logger accessLog = LoggerFactory.getLogger("ACCESS_LOG");
```

- 通信ログ

## 通信クライアント

- RESTfulサービス

## 通信再送管理

## 設定値管理

- SpringBoot標準機能
	- https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config.typesafe-configuration-properties

## メッセージ管理

- ログメッセージ
	- code.<Enumフルクラス名>.<Enum値>=<表示コードラベル>

- 応答メッセージ

http://terasolunaorg.github.io/guideline/current/ja/ArchitectureInDetail/WebApplicationDetail/MessageManagement.html

## ソースコード自動生成

- DBアクセスサービス
- 標準DBアクセス
- データクラス

## ファイルアップロード

## ファイルダウンロード

## コードリスト

- 動的なコードリスト管理
	- DB管理
- DIは、1コンポーネント
- Javaコード利用
	- ラベル取得
	- 値リスト取得
	- 値存在判定
- Thymleaf利用
- 入力チェック

## Thymleaf部品

- ラジオボタン
- チェックボックス
- テキスト
- 非表示
- テキストエリア
- セレクト

## 日付操作
- システム日付
	- 固定
	- オフセット
- 日付演算
	-　Java標準機能

## 文字列操作
- 文字列加工
	-　Java標準機能
	-　commons-lang
-　文字種変換・判定

## enum管理
- JacksonのEnum変換
- MyBatisのEnum変換

## ラベル管理
- enumのラベル名取得
	- code.<Enumフルクラス名>.<Enum値>=<表示コードラベル>
- データクラスプロパティのラベル名取得
	- <データクラスのパッケージなしLowerCamelシンプル名名>.<プロパティ>=<表示項目名>

- 多言語対応できるように、MessageSourceで、ラベル名を管理する。

## 多言語対応
- spring.messages.basename
	- デフォルトでは、messages.properties
	- 日本語の場合、messages_ja.properties。英語の場合、messages_en.propertiesを利用。
- ThymeleafのテンプレートHTML実装例
	- th:text="#{キー名}"
	- <span th:text="#{i.xx.yy.0001}"></span>

- ロケールの切り替え
https://blog.okazuki.jp/entry/2015/07/13/203328



## トランザクショントークン

## hiddenデータ引き継ぎ

## ページング

- SQLで、レコードを絞り込む。

## データコピー
- Dozerを利用
	- https://macchinetta.github.io/server-guideline-thymeleaf/current/ja/ArchitectureInDetail/GeneralFuncDetail/Dozer.html


## セッション管理
- 業務でのセッション利用禁止
- 以下で限定利用
	- 認証情報管理
	- トランザクショントークン管理

## JavaScript+HTML5入力チェック


●業務部品



controler  implのみ

restcontrole implのみ

service if impl

sharedservice if impl

repository ifのみ





●データクラスの使い分け



form

dto

controler

    form

    



 

restcontroler

    request

    response

service if impl

    input

    output

repository ifのみ

   key

   entity

　creteria



●repository
　＋find,save,remove

 
 BasicErrorController


## 参考資料
### Spring JDBC
https://github.com/spring-projects-experimental/spring-native/tree/main/samples/data-jdbc 


### @ImportでのBean登録
ImportBeanDefinitionRegistrar