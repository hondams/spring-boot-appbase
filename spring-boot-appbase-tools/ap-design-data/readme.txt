・TSV形式のファイルは、拡張子「.tsv」とする。
・CSV形式のファイルは、拡張子「.csv」とする。


・最初の行で[[オブジェクト名]]を記載する。
・[[オブジェクト名]]の次の行で、複数個の[列名]を記載する。
・[列名]の次の行に、[列名]と同じ数の値を記載する。

・空行で、値の記載を終了する。

・空行をｎ行繰り返した後、次の[[オブジェクト名]]を記載できる。

・以下で、列名に補足情報を付加する。
　・@optionName=optionValue
　・@optionName=optionValue1;optionValue2
　・@optionName1=optionValue1@optionName2=optionValue2


・オプション
・「@key」を主キー列に付与する。
・「@name=<和名の列名>」で、「和名の列」を英名にする。


・オプション（自動定義列名）
・「@children=<子要素のオブジェクト名>」で、子要素として、キーの一致する「子要素のオブジェクト」を保持する。
・「@ref=<オブジェクト名.列名>」で、キーの一致する「オブジェクト」の「列」の値を保持する。
・「@dbDataType」
・「@javaDataType」
・「@javaValidations」


・[dataDics.dbDataType]
    - 真偽値：bool
    - 列挙値：char(n)
    - 文字列:char(n)、varchar(n)
    - 日付：date
    - タイムスタンプ：timestamptz
    - 小数：decimal(p,s)
    - 整数（9桁以下）：int
    - 整数（18桁以下）：int8
    - 整数（19桁以上）：numeric(p)

・[dataDics.javaDataType]
    - 真偽値：boolean
    - 列挙値：Enum
    - 文字列:String
    - 日付：LocalDate
    - タイムスタンプ：LocalDateTiem
    - 小数：BigDecimal
    - 整数（9桁以下）：int
    - 整数（18桁以下）：long
    - 整数（19桁以上）：BigInteger

・[dataDics.javaValidations]


