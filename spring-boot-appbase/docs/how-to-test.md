# テストの実施方法

## テストコードの記載方法

### 基本的なテストメソッド

```
    @Test
    @DisplayName（”日本語のテスト内容”）
    void testAssertTrue() {
        // テストコード
    }
```

- テストクラス、テストメソッドは、パッケージスコープ（アクセス制御修飾子なし）とする。

### テストのアサーション

JUnit5の標準的なアサーションを利用する。

- 実行されないことをチェックする。

```
if (fail) {
    fail();
}
```

- true（boolean）であることをチェックする。

```
boolean testingCondition = true;
assertTrue(testingCondition);
```

- false（boolean）であることをチェックする。

```
boolean testingCondition = false;
assertFalse(testingCondition);
```

- nullであることをチェックする。

```
Object testingValue = null;
assertNull(testingValue);
```

- nullでないことをチェックする。

```
Object testingValue = new Object();
assertNotNull(testingValue);
```

- 同じ値であることをチェックする。

```
String testingValue = "A";
assertEquals("A", testingValue);
```

- 同じ値でないことをチェックする。

```
String testingValue = "not A";
assertNotEquals("A", testingValue);
```

- 同じインスタンスであることをチェックする。

```
Object testingValue = new Object();
Object expectedValue = testingValue;
assertSame(expectedValue, testingValue);
```

- 同じインスタンスでないことをチェックする。

```
Object testingValue = new Object();
Object expectedValue = new Object();
assertNotSame(expectedValue, testingValue);
```

- 指定した例外が発生することをチェックする。（
	- assertThrowsは、指定した例外、または、そのサブクラスの例外が発生することをチェックするので、を利用しない。

```
assertThrowsExactly(SomeException.class, () -> {
    throw new SomeException("error message.");
});
```

- 同じ配列であることをチェックする。

```
String[] testingValue = {"value1", "value2", "value3"};
assertArrayEquals(new String[] {"value1", "value2", "value3"}, testingValue);
```

- 同じコレクションであることをチェックする。
	- 同じコレクションであることをチェックする。

```
Collection<String> testingValue = List.of("value1", "value2", "value3");
assertIterableEquals(List.of("value1", "value2", "value3"), testingValue);
```

- 文字列リストをチェックする。
    - 文字列の同値、正規表現のマッチをチェックできる。
    - 「>> >>」は、次のデータがマッチするまでスキップする。
    - 「>> コメント >>」のように、「>>」の間に文字列を書いた場合は、「>> >>」と同じように、次のデータがマッチするまでスキップする。
    - 「>> 3 >>」のように、「>>」の間に数値を書いた場合は、その行数分スキップする。
	
```
List<String> values = List.of(//
        "line1", "line2", "line3", "line4", "line5", //
        "line6", "line7", "line8", "line9", "line10");
assertLinesMatch(
        List.of("line1", ">> 2 >>", "line4", "l.\\w.\\d", "line6", ">> skip >>", "line10"),
        values);
```

- 指定時間以内に処理が完了することをチェックする。
    - 指定時間を超過した場合、処理の完了を待たずに、assertエラーをスローするので、基本的には、assertTimeoutPreemptivelyを利用する。
    - assertTimeoutは、指定時間を超過した場合、処理の完了を待つので、テストの対象の処理の並列実行が問題になる場合に限り、利用を検討する。

```
assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
    /* 2秒以内の処理 */});
```

- true（boolean）を返却するメソッドでチェックする。

```
Collection<String> testingValue = List.of("value1", "value2", "value3");
assertTrue(testingValue.contains("value1"));
```


### テストの初期処理・終了処理

```
@BeforeAll
static void setUpTestClass() {
    // テストクラス単位の初期処理
}

@BeforeEach
void setUpTestMethod() {
    // テストメソッド単位の初期処理
}

@AfterEach
void tearDownTestMethod() {
    // テストメソッド単位の終了処理
}

@AfterAll
static void tearDownTestClass() {
    // テストクラス単位の終了処理
}
```

### private フィールド、メソッドのテスト

- クラスフィールドの値の取得

```
ReflectionTestUtils.getField(SomeClass.class, "someClassField");
```

- クラスフィールドの値の設定

```
ReflectionTestUtils.setField(SomeClass.class, "someClassField", someValue);
```

- インスタンスフィールドの値の取得

```
ReflectionTestUtils.getField(someInstance, "someInstanceField");
```

- インスタンスフィールドの値の設定

```
ReflectionTestUtils.setField(someInstance, "someInstanceField", someValue);
```

- クラスメソッドの実行

```
ReflectionTestUtils.invokeMethod(SomeClass.class, "someClassMethod", someArg);
```

- インスタンスメソッドの実行

```
ReflectionTestUtils.invokeMethod(someInstance, "someInstanceMethod", someArg);
```

### Logのテスト方法

- テストクラスにアノテーションを付与

```
@ExtendWith(LoggingEventExtension.class)
```

- LoggingEventを取得

```
List<ILoggingEvent> loggingEvents = LoggingEventExtension.getLoggingEvent();
```

- LoggingEventのアサーションの例

```
List<ILoggingEvent> loggingEvents = LoggingEventExtension.getLoggingEvent();

assertEquals(1, loggingEvents.size());
assertEquals(Level.INFO, loggingEvents.get(0).getLevel());
assertEquals("test message.", loggingEvents.get(0).getMessage());
assertEquals(LoggingEventExtensionTest.class.getName(),
        loggingEvents.get(0).getLoggerName());
```

### Springの初期化を利用したテスト方法

#### @SpringBootApplicationを付与したクラスがある場合のテスト方法の準備

- 特になし

#### @SpringBootApplicationを付与したクラスがない場合のテスト方法の準備

- src/test/javaに、@SpringBootApplicationを付与したクラスを、テスト対象のクラスをすべて含むパッケージに配置する。
- src/test/javaに、application.propertiesを配置する。

### MyBatisのテスト

### Dbunit
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)


### MockMvcのテスト

### RESTfulサービスのテスト

TODO
https://spring.pleiades.io/guides/gs/testing-web/

```
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
```


### モックのテスト方法

#### モックテスト方法

- 基本方針
    - コンポーネントは、コンストラクタインジェクションで初期化する。
    - テスト対象インスタンス生成は、以下を利用する。
        - テスト対象インスタンスは、テストクラスに、＠InjectMockを付与した未初期化フィールドとして宣言し、初期化する。
        - テスト対象インスタンスが利用するインスタンスは、以下のいずれかで、初期化する。
            - 実メソッドを呼び出さないインスタンスは、テストクラスに、＠Mockを付与した未初期化フィールドとして宣言し、初期化する。
            - インターフェイス、abstractクラス、実メソッドを呼び出す可能性のある、デフォルトコンストラクタのあるインスタンスは、テストクラスに、@Spyを付与した未初期化フィールドとして宣言し、初期化する。
            - 実メソッドを呼び出す可能性のある、デフォルトコンストラクタのないインスタンスは、テストクラスに、@Spyを付与した初期値ありフィールドとして宣言し、初期化する。
    - 引数は、以下の、いずれかを利用する。
        - 引数をチェックする場合は、ArgumentCaptorを、テストメソッド内で、ArgumentCaptor.forClass(Arg.class)で初期化し、capture()を引数とする。
        - 引数をチェックしない場合は、any()を引数とする。

    - インスタンスメソッドのふるまいの登録には、以下を利用する。
        - 戻り値なしメソッドは、doNothing().when(this.someMock).someMethod(someArg)で登録する。
        - 戻り値ありメソッドは、doReturn(someReturn).when(this.someMock).someMethod(someArg)で登録する。
        - 実メソッドの呼出は、doCallRealMethod().when(this.someMock).someMethod(someArg)で登録する。インターフェイス、abstractクラスであっても、実装があれば、呼出可能であるが、利用すべきではない。
        - 例外スローは、doThrow(new SomeException("some message.")).when(this.someMock).someMethod(someArg)で登録する。
        - 動的な戻り値は、doAnswer(invocation -> /* 動的な戻り値 */).when(this.someMock).someMethod(someArg)で登録する。
        - doNothing、doReturn、doCallRealMethod、doThrow、doAnswerは、続けて、複数登録し、メソッドの呼出た順に登録した振る舞いを実行する。
        - Mockを登録しない場合、@Mockのときは、なにも実行せず、nullを返却。@Spyのときは、実メソッドを実行する。

- 実装方針
    - DTO
    	- privateフィールドを宣言し、@Dataで実装する。
    	- String、int、long、BigInteger、BigDecimal、LocalDateTime、LocalDate、Enum
    	- javax.validation.constraintsを利用
    		DecimalMax、DecimalMin、Max、Min、NotBlank、NotEmpty、NotNull、Pattern、Size
    		
    	
    - Controlerの入力
    	- すべて、文字列にすると、Validationで処理できる。
    - MyBatis
    	- TypeHandler
    
- 実装方針
    - コンポーネントは、コンストラクタインジェクションで利用するコンポーネントを設定する。
- テスト方針
    - コンポーネントは、コンストラクタインジェクションで利用するコンポーネントを設定する。

- Mockitoを利用したテストの基本的な実装方法
    - テストクラスに、@ExtendWith(MockitoExtension.class)を付与する。
    - テストクラスのフィールドとして、以下を定義する。
        - インスタンスメソッドにモックを適用する場合、以下のいずれかで、フィールドを定義する。
            - ＠Mockを付与したSomeClassインスタンのスフィールドを初期値なしで定義する。
                - Mockを登録しない時、なにも実行せず、nullを返却する。
            - デフォルトコンストラクタがある場合、@Spyを付与したSomeClassインスタンのスフィールドを初期値なしで定義できる。
                - Mockを登録しない時、実メソッドを実行する。
            - デフォルトコンストラクタがない場合、@Spyを付与したSomeClassインスタンのスフィールドを初期値ありで定義する。
                - Mockを登録しない時、実メソッドを実行する。
        - クラスメソッドにモックを適用する場合、＠Mockを付与したMockedStatic<SomeClass>インスタンのスフィールドを初期値なしで定義する。
            - Mockを登録しない時、なにも実行せず、nullを返却する。
        - コンストラクタにモックを適用する場合、＠Mockを付与したMockedConstruction<SomeClass>インスタンスのフィールドを初期値なしで定義する。
        - テスト対象インスタンスにモックを設定する場合、テスト対象をコンストラクタインジェクションで実装し、＠InjectMocksを付与したSomeClassインスタンのスフィールドを初期値なしで定義する。
            - テスト対象インスタンスに、テストクラスのフィールドで定義したモックが設定される。
        
    - 引数は、以下の、いずれかを利用する。
        - 引数をチェックする場合は、ArgumentCaptorを、テストメソッド内で、ArgumentCaptor.forClass(Arg.class)で初期化し、capture()を引数とし、実行後に、実行時の引数の内容をアサーションする。
        - 引数をチェックしない場合は、any()を引数とする。

    - インスタンスメソッドのふるまいの登録には、以下を利用する。
        - 戻り値なしメソッドは、doNothing().when(this.someInstanceMock).someMethod(someArg)で登録する。
        - 戻り値ありメソッドは、doReturn(someReturn).when(this.someInstanceMock).someMethod(someArg)で登録する。
        - 実メソッドの呼出は、doCallRealMethod().when(this.someInstanceMock).someMethod(someArg)で登録する。インターフェイス、abstractクラスであっても、実装があれば、呼出可能であるが、利用すべきではない。
        - 例外スローは、doThrow(new SomeException("some message.")).when(this.someInstanceMock).someMethod(someArg)で登録する。
        - 動的な戻り値は、doAnswer(invocation -> /* 動的な戻り値 */).when(this.someMock).someMethod(someArg)で登録する。
        - doNothing、doReturn、doCallRealMethod、doThrow、doAnswerは、続けて、複数登録でき、メソッドの呼出た順に登録した振る舞いを実行する。
        - Mockを登録しない場合、@Mockのときは、なにも実行せず、nullを返却する。@Spyのときは、実メソッドを実行する。

    - クラスメソッドのふるまいの登録には、以下を利用する。
        - 戻り値なしメソッドは、this.someStaticMock.when(() -> SomeClass.someMethod(someArg)).then(invocation -> null)で登録する。
        - 戻り値ありメソッドは、this.someStaticMock.when(() -> SomeClass.someMethod(someArg)).thenReturn(someReturn)で登録する。
        - 実メソッドの呼出は、this.someStaticMock.when(() -> SomeClass.someMethod(someArg)).thenCallRealMethod()で登録する。
        - 例外スローは、this.someStaticMock.when(() -> SomeClass.someMethod(someArg)).thenThrow(new SomeException("some message."))で登録する。
        - 動的な戻り値は、this.someStaticMock.when(() -> SomeClass.someMethod(someArg)).thenAnswer(invocation -> /* 動的な戻り値 */)で登録する。
        - thenReturn、thenCallRealMethod、thenThrow、thenAnswerは、続けて、複数登録でき、メソッドの呼出た順に登録した振る舞いを実行する。
        - Mockを登録しない場合、@Mockのときは、なにも実行せず、nullを返却する。


#### インスタンスメソッドのMock、Spy

#### クラスメソッドのMock

- クラスメソッドのMockの準備

```
try (MockedStatic<StaticMethods> staticMethodsMock =
        mockStatic(StaticMethods.class)) {
    // この中でのみ、StaticメソッドをMock化する。
}
```

- クラスメソッドのMockの登録
    - てｓｔ

```
```

    - てｓｔ
  
- クラスメソッドのMockの実行回数チェック

#### モックテスト方法

## cording style
https://google.github.io/styleguide/

## checkstyle
https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml

## formatter
https://github.com/google/styleguide/blob/gh-pages/eclipse-java-google-style.xml