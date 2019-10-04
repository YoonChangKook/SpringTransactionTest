# Spring Transaction Test
* JDK 1.8
* Spring 5.1.4
* H2 Database 1.4.197
* Mybatis 3.5.0
* Maven 3

### Embedded Maria Database
본 프로젝트에서 트랜잭션 테스트를 위한 데이터베이스는 Embedded Maria DB를 사용한다.

Embedded Maria DB를 사용하기 위해 MariaDB4j를 사용한다.

	<dependency>
		<groupId>org.mariadb.jdbc</groupId>
		<artifactId>mariadb-java-client</artifactId>
		<version>2.4.0</version>
	</dependency>
	<dependency>
		<groupId>ch.vorburger.mariaDB4j</groupId>
		<artifactId>mariaDB4j</artifactId>
		<version>2.3.0</version>
	</dependency>

* 참고: https://github.com/vorburger/MariaDB4j

### Proxy Bean Test
스프링의 AOP는 프록시 패턴을 이용하여 적용되는데, @Transactional 애노테이션을 이용하여 트랜잭션을 적용하는 방법도 마찬가지이다.

프록시 객체 내에서 자신의 트랜잭션 메서드를 호출했을 때 일어나는 문제를 자세히 테스트한다.

### Transaction Isolation Test
트랜잭션의 격리수준에 따라서 동시성과 정합성에 미치는 영향을 테스트한다.

### Transaction Propagation Test
트랜잭션 메서드 내에서 다른 객체의 트랜잭션 메서드를 호출했을 때, 트랜잭션 전파 설정에 따라 달라지는 부분을 테스트한다.