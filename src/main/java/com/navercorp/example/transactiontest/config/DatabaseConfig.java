package com.navercorp.example.transactiontest.config;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import ch.vorburger.exec.ManagedProcessException;
import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;

/**
 * 데이터베이스 설정
 *
 * @author 국윤창
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.navercorp.example.transactiontest.dao")
@PropertySource("classpath:/database/database.properties")
public class DatabaseConfig {
	@Bean
	public DataSource dataSource(@Value("${test.datasource.driver-class-name}")String driverClassName,
								@Value("${test.datasource.database-name}")String databaseName,
								@Value("${test.datasource.username}")String username,
								@Value("${test.datasource.password}")String password) throws ManagedProcessException {
		DBConfigurationBuilder config = DBConfigurationBuilder.newBuilder();
		config.setPort(0);
		DB db = DB.newEmbeddedDB(config.build());
		db.start();
		db.createDB(databaseName);
		db.source("database/user.sql", username, password, databaseName);
		db.source("database/insert-users.sql", username, password, databaseName);

		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl(config.getURL(databaseName));
		dataSource.setDriverClassName(driverClassName);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean
	public SqlSessionFactory sqlSessionFactoryBean(DataSource dataSource) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

		sqlSessionFactoryBean.setDataSource(dataSource);
		// mapper matching
		PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver = new PathMatchingResourcePatternResolver();
		sqlSessionFactoryBean.setTypeAliasesPackage("com.navercorp.example.trasactiontest.dto");
		sqlSessionFactoryBean.setMapperLocations(pathMatchingResourcePatternResolver.getResources("classpath:/mapper/**/*.xml"));
		// mybatis configuration
		org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
		configuration.setMapUnderscoreToCamelCase(true);
		configuration.setUseGeneratedKeys(true);
		sqlSessionFactoryBean.setConfiguration(configuration);

		return sqlSessionFactoryBean.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
