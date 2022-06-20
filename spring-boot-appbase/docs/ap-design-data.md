# AP設計情報

## アクター定義

## 業務フロー定義

## 画面定義

## 機能定義

## 外部インターフェイス定義

## テーブル定義

- 【主キー】テーブル和名
- 【自動】テーブル名

## テーブルカラム定義
- 【主キー】テーブル和名
- 【主キー】テーブルカラム和名
- 【自動】テーブルカラム名
- データ辞書名
- 【自動】DBデータ型

## バッチ処理定義

- バッチ種別
    - 一括
    - チャンク
- 入力データ種別
    - DB
    - ファイル
- 入力データ定義
- 処理定義

## リクエスト処理定義

- 要求データ定義
- 応答データ定義
- 処理定義

## データ定義

- 【主キー】データ和名
- 【自動】データ名

## データ項目定義
- 【主キー】データ和名
- 【主キー】データ項目和名
- 【自動】データ項目名
- データ辞書名
- 【自動】Javaデータ型
- 【自動】入力チェック

## データ辞書定義

- 【主キー】データ辞書名
- データ値種別
    - 真偽値：bool
    - 列挙値：char(n)
    - 文字列
    - 日付：date
    - タイムスタンプ：timestamptz
    - 小数：decimal(p,s)
    - 整数（9桁以下）：int
    - 整数（18桁以下）：int8
    - 整数（19桁以上）：numeric(p)
- 【自動】DBデータ型
- 【自動】Javaデータ型

## 文字列データ辞書定義

- 【主キー】データ辞書名
- 最小文字数
- 最大文字数
- 文字種別
    - 数字
    - 半角大文字英字
    - 半角小文字英字
    - 半角大文字英数字
    - 半角小文字英数字
    - 半角英数字
    - 半角文字
    - 全角大文字英字
    - 全角小文字英字
    - 全角大文字英数字
    - 全角小文字英数字
    - 全角英数字
    - 全角文字
    - 全半角文字
    - パターン
- 文字列パターン

## 数値データ辞書定義

- 【主キー】データ辞書名
- 最小値
- 最大値
- 小数点以下桁数

## 列挙値データ辞書定義

- 【主キー】データ辞書名
- 【主キー】列挙値
- 列挙値名

## 和名定義

- 英名
- 和名

## Javaクラス名定義

- 【主キー】Javaクラス名
- Javaプロジェクト名
- AP設計情報種別
- AP設計情報名


## DBデータ型変換ルール
- PostgreSQL
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

## Javaデータ型変換ルール

- 列挙値は、Enum。パッケージは、datatype
- 小数は、BigDecimal
- 整数（9桁以下）は、int
- 整数（18桁以下）は、long
- 整数（19桁以上）は、BigInteger

## Javaパッケージ名候補

- valuetype：
- entity：
- model：
- service：
- controller：
- repository：
- util：
- logging：
- test：