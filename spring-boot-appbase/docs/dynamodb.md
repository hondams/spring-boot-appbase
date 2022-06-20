#

## v1
-  https://docs.aws.amazon.com/ja_jp/amazondynamodb/latest/developerguide/Programming.html

  - 低レベルインターフェイス（Low-Level Interfaces）
  	- com.amazonaws.services.dynamodbv2.AmazonDynamoDB
  - ドキュメントインターフェイス（Document Interfaces）
  	- com.amazonaws.services.dynamodbv2.document.DynamoDB
  	- https://docs.aws.amazon.com/ja_jp/amazondynamodb/latest/developerguide/DynamoDB_API.html
  	  - 項目と属性を操作する
  	    - PutItem — 項目を作成します。
  	    - GetItem — 項目を読み込みます。
  	    - UpdateItem — 項目を更新します。
  	    - DeleteItem — 項目を削除します。
  	    - BatchGetItem — 1 つ以上のテーブルから最大 100 個の項目を読み込みます。
  	    - BatchWriteItem — 1 つ以上のテーブルから最大 25 個の項目を作成または削除します。
  	    
  	  - テーブルの操作
  	    - テーブルの作成
  	    - テーブルの更新
  	    - テーブルの削除
  	    - テーブルの一覧表示
  	  - 項目の操作
  	    - 項目の置換
  	    - 項目の取得
  	    - バッチ書き込み: 複数の項目の書き込みおよび削除
  	    - バッチ取得: 複数の項目の取得
  	    - 項目の更新
  	    - 項目の削除
  	    - 例: AWS SDK for Java ドキュメント API を使用した CRUD オペレーション
  	    - 例: AWS SDK for Java ドキュメント API を使用したバッチオペレーション
  	    - 例: AWS SDK for Java ドキュメント API を使用したバイナリタイプ属性の処理
  	  - 項目コレクションの操作
  	    - テーブルおよびインデックスにクエリを実行 : Java
  	      - QuerySpec
  	  - DynamoDB でのスキャンの操作
  	    - テーブルおよびインデックスにスキャン: Java
  	      - ScanRequest 

```java
AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
DynamoDB docClient = new DynamoDB(client);
```
  - オブジェクト永続性インターフェイス（Object Persistence Interface）
  	- com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
```java
AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
DynamoDBMapper mapper = new DynamoDBMapper(client);
```
  - DynamoDB の上位レベルのプログラミングインターフェイス（Higher-Level Programming Interfaces for DynamoDB）
  	- DynamoDBMapper



- https://docs.aws.amazon.com/ja_jp/sdk-for-java/v1/developer-guide/examples-dynamodb.html
  - テーブルを作成する
  - テーブルの一覧表示
  - テーブルの説明 (テーブルに関する情報の取得)
  - テーブルの変更 (更新)
  - テーブルの削除
  - テーブルからの項目の取り出し (取得)
  - テーブルへの新しい項目の追加
  - テーブルの既存の項目の更新
  - DynamoDBMapper クラスの使用

## v2


- https://docs.aws.amazon.com/ja_jp/sdk-for-java/latest/developer-guide/examples-dynamodb-enhanced.html
  - DynamoDB でのテーブルの操作
    - software.amazon.awssdk.services.dynamodb.DynamoDbClient
    - テーブルの作成
      - シンプルプライマリキーを使用してテーブルを作成する
      - 複合プライマリキーを使用してテーブルを作成する
    - テーブルの一覧表示
    - テーブルの説明 (テーブルに関する情報の取得)
    - テーブルの変更 (更新)
    - テーブルの削除
  - DynamoDB での項目の操作
    - software.amazon.awssdk.services.dynamodb.DynamoDbClient
    - テーブルからの項目の取り出し (取得)
    - 非同期クライアントを使用したテーブルからの項目の取り出し (取得)
    - テーブルへの新しい項目の追加
    - テーブルの既存の項目の更新
    - テーブルの既存の項目の削除
  - 拡張クライアントを使用したテーブルの作成
    - software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
    - テーブルからの項目の取り出し (取得)
    - 項目のバッチ作成 (put) と削除
    - フィルタされたクエリを使用してテーブルから項目を取得する
    - テーブルからすべての項目を取得する (get)


```java
DynamoDbClient ddb = DynamoDbClient.builder()
        .region(region)
        .build();

DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
        .dynamoDbClient(ddb)
        .build();

```

  
  


https://docs.aws.amazon.com/ja_jp/sdk-for-java/latest/developer-guide/home.html


## DynamoDB

### DynamoDB データ型記述子

S – 文字列
N – 数値
B – バイナリ
BOOL – ブール
NULL – Null
M – マップ
L – リスト
SS – 文字列セット
NS – 数値セット
BS – バイナリセット

### エラー

https://docs.aws.amazon.com/ja_jp/amazondynamodb/latest/developerguide/Programming.Errors.html

