# Spring Boot Applicationの作り方

## 業務部品
### DTO

### 業務部品（画面）
- RESTfulサービス
@Controller
- 画面（Thymeleaf）
- CSS（Bootstrap）
- JavaScript（jQuery）
- Form

### 業務部品（RESTfulサービス）
- RESTfulサービス
@RestController
- Request
- Response


### 業務部品（ドメイン）
- 共通サービス
@Service

- 個別サービス
@Service

- リポジトリ
@Repository
@Mapper

- エンティティ
- エンティティキー
- 検索条件（Criteria）
- ページング


## AP基盤機能

### 例外
- システムエラー
- 業務エラー

### 例外ハンドリング

- RESTfulサービス
	-  DefaultHandlerExceptionResolverを継承
- Web画面
	-  BasicErrorControllerを継承？


### バリデーション
- CodeEnum

### データアクセス
- DB
	- MyBatis
- キュー
	- SQS

### ファイルアクセス
- S3
- EFS

### ログ
- エラーログ
- トレースログ
- 業務ログ

### 通信クライアント
- RESTfulサービス

### 設定値管理
- application.properties拡張

### メッセージ管理
- ログメッセージ
- 応答メッセージ
	- 多言語（i18n）対応

### MyBatis拡張
- CodeEnumTypeHandler
- LocalDateTimeTypeHandler
- LocalDateTypeHandler
　
### MyBatis拡張
- Enum<=>コード 変換 CodeEnum

### ファイルアップロード

### ファイルダウンロード

### コードリスト
- Enum<=>コード 変換 CodeEnum
- 動的コード

### 日付操作
- 標準API
- システム時刻
- システム時刻（オフセット）

### 文字列操作
- commons-lang StringUtils
- commons-codec Hex

### ソースコード自動生成
- DBアクセスサービス
- 標準DBアクセス
- データクラス


### Enum

### MyBatisサポート

    - DTO
    	- privateフィールドを宣言し、@Dataで実装する。
    	- String、int、long、BigInteger、BigDecimal、LocalDateTime、LocalDate、Enum
    	- javax.validation.constraintsを利用
    		DecimalMax、DecimalMin、Max、Min、NotBlank、NotEmpty、NotNull、Pattern、Size
