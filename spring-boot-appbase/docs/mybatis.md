# アノテーションを使ったSQLの登録

org.apache.ibatis.builder.annotation.MapperAnnotationBuilder.parseStatement(java.lang.reflect.Method)
で、アノテーションを解析し、そのメソッド内で、
org.apache.ibatis.builder.MapperBuilderAssistant.addMappedStatement(java.lang.String, org.apache.ibatis.mapping.SqlSource, org.apache.ibatis.mapping.StatementType, org.apache.ibatis.mapping.SqlCommandType, java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.Class<?>, java.lang.String, java.lang.Class<?>, org.apache.ibatis.mapping.ResultSetType, boolean, boolean, boolean, org.apache.ibatis.executor.keygen.KeyGenerator, java.lang.String, java.lang.String, java.lang.String, org.apache.ibatis.scripting.LanguageDriver, java.lang.String)
を利用し、
SQLを登録している。


# SQLの構築

org.apache.ibatis.builder.annotation.ProviderSqlSource(org.apache.ibatis.session.Configuration, java.lang.annotation.Annotation, java.lang.Class<?>, java.lang.reflect.Method)

で
最終的には、以下で、SQLを構築。
org.apache.ibatis.scripting.LanguageDriver.createSqlSource(org.apache.ibatis.session.Configuration, java.lang.String, java.lang.Class<?>)

# デフォルトのLanguageDriver
XMLLanguageDriver

※ ほかにあも、実装はあるが、基本的に、デフォルトでいい。
※ 基本的なSQLは実装したくないので、Langで、SQLを自動構築できないかを考える。
※ ページングにも使えないか？


# MapperでのLanguageDriverの指定
org.apache.ibatis.annotations.Lang

XMLLanguageDriverを内包して、一部委譲することで、Lang指定も不要にできる？


# 実装方法
確かに、最小コードで、標準CRUDを実装できるのはうれしいが、
難易度が高く、いずれにせよ、エンティティクラス、DDLなどは、自動生成が必要なので、
MappingXMLも自動生成とする。

## 用意するAPI

- insertAll
- update
- updateExclusively
- selectAll
- selectByIds
- selectByIdsForUpdate
- selectIdsByIds
- deleteAll
- deleteByIds
- countAll

## エンティティのAPI

<エンティティ名>
	- id
	- recordId
	- version
	- createdTime
	- createdUser
	- updatedTime
	- updatedUser

<エンティティ名>Id
<エンティティ名>RecordId
	- id
	- parentRecordId


## 参考にするするAPI

- CrudRepository<T, ID>
	count()
	delete(T)
	deleteAll()
	deleteAll(Iterable<? extends T>)
	deleteAllById(Iterable<? extends ID>)
	deleteById(ID)
	existsById(ID)
	findAll()
	findAllById(Iterable<ID>)
	findById(ID)
	save(S)
	saveAll(Iterable<S>)

- PagingAndSortingRepository<T, ID>
	findAll(Pageable)
	findAll(Sort)

※ RepositoryのAPIなので、参考程度とする。
- メソッド名は、全部、別にする必要がある。
- saveは、insert、updateを組合せないと実現できない。



# Mapperの構築方法

- Beanの登録
org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration.AutoConfiguredMapperScannerRegistrar.registerBeanDefinitions(org.springframework.core.type.AnnotationMetadata, org.springframework.beans.factory.support.BeanDefinitionRegistry)


- Beanの登録
org.mybatis.spring.mapper.ClassPathMapperScanner.processBeanDefinitions(java.util.Set<org.springframework.beans.factory.config.BeanDefinitionHolder>)



- Mapperの中身
org.mybatis.spring.SqlSessionTemplate.getMapper(java.lang.Class<T>)
org.apache.ibatis.session.Configuration.getMapper(java.lang.Class<T>, org.apache.ibatis.session.SqlSession)
org.apache.ibatis.binding.MapperRegistry.getMapper(java.lang.Class<T>, org.apache.ibatis.session.SqlSession)
org.apache.ibatis.binding.MapperProxyFactory.newInstance(org.apache.ibatis.session.SqlSession)
java.lang.reflect.Proxy.newProxyInstance(java.lang.ClassLoader, java.lang.Class<?>[], java.lang.reflect.InvocationHandler)

で、Proxyを作成する。

org.apache.ibatis.binding.MapperProxy<T>

が、Proxyの中身となっている
