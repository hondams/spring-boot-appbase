- 

- 参考URL
  - https://github.com/spring-projects/spring-data-examples
  - https://github.com/spring-projects/spring-data-relational/tree/main/spring-data-jdbc
  - https://spring.pleiades.io/projects/spring-data-jdbc
  - https://github.com/boostchicken/spring-data-dynamodb
	- AWS SDKのバージョンが古い。
  - https://docs.spring.io/spring-data/data-document/docs/current/reference/html/
  - https://docs.spring.io/spring-data/jdbc/docs/current/reference/html/

  
●CrudRepository<T, ID>の初期化？
org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>

●CrudRepositoryの実体
org.springframework.data.jdbc.repository.support.SimpleJdbcRepository<T, ID>

org.springframework.data.jdbc.core.mapping.JdbcMappingContext

saveAllは、saveを1件ずつ実行。
deleteAllByIdは、deleteを1件ずつ実行。
実体は、JdbcAggregateOperations（JdbcAggregateTemplate）に移譲。


●JdbcMappingContextは、以下で初期化
org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration.jdbcMappingContext(java.util.Optional<org.springframework.data.relational.core.mapping.NamingStrategy>, org.springframework.data.jdbc.core.convert.JdbcCustomConversions)

・NamingStrategyは、Bean定義されていれば、それを使用。Bean定義されていなければ、NamingStrategy.INSTANCEを利用。

●RelationalPersistentEntityImplの初期化
ClassTypeInformation.from(type)
RelationalPersistentEntityImpl<T> entity = new RelationalPersistentEntityImpl<>(typeInformation,
		this.namingStrategy);

※ JdbcMappingContextに、setInitialEntitySetで、エンティティクラスを設定して、afterPropertiesSet()またはinitialize()を実行する。
getPersistentEntities()で取得する。

→エンティティの解析情報


MyBatis用のCrudRepositoryは、基本的に、JdbcAggregateOperationsに処理を移譲。
JdbcAggregateOperationsから、JdbcOperationsを利用。

JdbcAggregateOperationsの実装
  org.springframework.data.jdbc.core.JdbcAggregateTemplate

※ IDは、1つプロパティしか、対応していない。
※ INSERTは、一括登録できない。


●MyBatisの仕組み解析
org.mybatis.spring.SqlSessionFactoryBean
で
org.apache.ibatis.session.Configuration
を構築し
org.apache.ibatis.session.defaults.DefaultSqlSessionFactory
を生成。

org.apache.ibatis.session.defaults.DefaultSqlSessionFactory
は
org.apache.ibatis.session.defaults.DefaultSqlSession(org.apache.ibatis.session.Configuration, org.apache.ibatis.executor.Executor, boolean)
を生成。


org.apache.ibatis.session.defaults.DefaultSqlSession
の中で、
      MappedStatement ms = configuration.getMappedStatement(statement);
で、MappedStatementを特定し、

以下のExecutorを使って、
    if (ExecutorType.BATCH == executorType) {
      executor = new BatchExecutor(this, transaction);
    } else if (ExecutorType.REUSE == executorType) {
      executor = new ReuseExecutor(this, transaction);
    } else {
      executor = new SimpleExecutor(this, transaction);
    }
    if (cacheEnabled) {
      executor = new CachingExecutor(executor);
    }

クエリを実行する。



org.apache.ibatis.session.Configuration
には
org.apache.ibatis.builder.MapperBuilderAssistant.addMappedStatement(java.lang.String, org.apache.ibatis.mapping.SqlSource, org.apache.ibatis.mapping.StatementType, org.apache.ibatis.mapping.SqlCommandType, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Class<?>, java.lang.String, java.lang.Class<?>, org.apache.ibatis.mapping.ResultSetType, boolean, boolean, boolean, org.apache.ibatis.executor.keygen.KeyGenerator, java.lang.String, java.lang.String, java.lang.String, org.apache.ibatis.scripting.LanguageDriver, java.lang.String)
で、登録する。


org.apache.ibatis.builder.annotation.MapperAnnotationBuilder
で、
Annotationを解析して登録。

org.apache.ibatis.builder.xml.XMLStatementBuilder
XMLを解析して登録。



org.mybatis.spring.mapper.MapperFactoryBean<T>
から
org.apache.ibatis.session.Configuration.addMapper(java.lang.Class<T>)
経由で、
Mapper


org.mybatis.spring.annotation.MapperScannerRegistrar
で、
org.mybatis.spring.annotation.MapperScan
を検索し
org.mybatis.spring.mapper.MapperScannerConfigurer
のBeanを生成する。



●Mapperのインターフェイスの実態。
org.apache.ibatis.binding.MapperProxy<T>
を
org.apache.ibatis.binding.MapperProxyFactory<T>
で生成する。



●ConfigurationへのMappedStatement
SqlSessionFactoryBean
では、ＸＭＬ設定ファイルから初期化する。

MapperScan、MapperScannerRegistrarで、
アノテーションから、MappedStatementを登録する。


●
org.mybatis.spring.SqlSessionFactoryBean.setDefaultEnumTypeHandler(java.lang.Class<? extends org.apache.ibatis.type.TypeHandler>)
で、１つのEnumをコンストラクタ引数とする、TypeHandlerのクラスを指定する。
CodeEnumTypeHandlerで対応可能。


●SimpleJdbcRepositoryの仕組み
JdbcRepositoryFactory
で、
SimpleJdbcRepository
をリフレクションで初期化する。

JdbcRepositoryFactory
は
JdbcRepositoryFactoryBeanで
初期化する。



JdbcRepositoriesAutoConfiguration
　JdbcRepositoriesRegistrar　　ImportBeanDefinitionRegistrarとして、JdbcRepositoryFactoryBeanをBean登録。

拡張方法
JdbcRepositoryConfigExtension
を継承して、
getRepositoryFactoryBeanClassNameで、JdbcRepositoryFactoryBeanの代替クラスを指定し、
Bean登録


JdbcRepositoryFactoryBeanのコピー実装用意して、
org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean.doCreateRepositoryFactory()
の実装を置き換え、
JdbcRepositoryFactoryの継承クラスを返却できるようにする。


SimpleJdbcRepositoryを差し替えないと、Inesrtを一括実行できない。


JdbcAggregateOperationsを拡張しないと、
SimpleJdbcRepositoryの実装も、変更できない。


MyBatisJdbcConfigurationでは、DataAccessStrategyの実装をCompositeにして、置き換える。

