# プロジェクト関係

- spring-boot-appbase-modules
- spring-boot-appbase-dependencies
- spring-boot-appbase
- spring-boot-appbase-parent
- spring-boot-appbase-test
- spring-boot-appbase-tools
- spring-boot-appbase-sample-app

## プロジェクト継承関係

```plantuml

class spring-boot-appbase-modules <<pom>>
class spring-boot-starter-parent <<external-pom>>
class spring-boot-appbase-dependencies <<pom>>
class spring-boot-appbase-parent <<pom>>
class spring-boot-appbase <<lib>>
class spring-boot-appbase-test <<test-lib>>
class spring-boot-appbase-tools <<boot-ap>>
class spring-boot-appbase-sample-app <<boot-ap>>
class spring-boot-appbase-lib <<lib>>

"spring-boot-starter-parent" <|-- "spring-boot-appbase-dependencies"

"spring-boot-appbase-dependencies" <|-- "spring-boot-appbase-parent"

"spring-boot-appbase-dependencies" <|-- "spring-boot-appbase-test"
"spring-boot-appbase-dependencies" <|-- "spring-boot-appbase-lib"

"spring-boot-appbase-parent" <|-- "spring-boot-appbase"
"spring-boot-appbase-parent" <|-- "spring-boot-appbase-tools"
"spring-boot-appbase-parent" <|-- "spring-boot-appbase-sample-app"

hide members
hide circle

```

## プロジェクト依存関係

```plantuml

class spring-boot-appbase-tools <<boot-ap>>
class spring-boot-appbase-parent <<pom>>
class spring-boot-appbase-test <<test-lib>>
class spring-boot-appbase-sample-app <<boot-ap>>
class spring-boot-appbase <<lib>>
class spring-boot-appbase-lib <<lib>>


"spring-boot-appbase-parent" <|--- "spring-boot-appbase-tools"
"spring-boot-appbase-parent" <|--- "spring-boot-appbase-sample-app"
"spring-boot-appbase-parent" <|- "spring-boot-appbase"

"spring-boot-appbase-sample-app" -R- "spring-boot-appbase" : use >

"spring-boot-appbase-parent" -R- "spring-boot-appbase-test" : use >

"spring-boot-appbase-test" -R- "spring-boot-appbase-lib" : use >
"spring-boot-appbase" -R- "spring-boot-appbase-lib" : use >

"spring-boot-appbase-tools" -[hidden]R- "spring-boot-appbase-sample-app"

hide members
hide circle

```

## 参考資料

### DynamoDBのテスト利用

[yo1000.com/dynamo-local-test](https://www.yo1000.com/dynamo-local-test)

- Mavenで実行しないとだめ。
- Eclipseで実行するには、Mavenで一度実行し、実行時に、「java.library.path」の指定が必要。

### MapStructの設定

https://mapstruct.org/documentation/ide-support/

### Mavenのバージョン変更

https://www.mojohaus.org/versions-maven-plugin/index.html

### 「Plugin execution not covered by lifecycle configuration」の抑止
https://qiita.com/tkatochin/items/4909d329e62562e76af8

- 支障があるわけではないので、何もしない。

### AWS SDK for Java 1.x と 2.x の相違点

https://docs.aws.amazon.com/ja_jp/sdk-for-java/latest/developer-guide/migration-whats-different.html

- 高レベルのライブラリは、バージョン 2.x でまだ利用可能でない
- 両方の SDK の並行使用も可能
