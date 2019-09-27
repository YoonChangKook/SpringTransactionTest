# Spring Transaction Test
* JDK 1.8
* Spring 5.1.4
* H2 Database 1.4.197
* Mybatis 3.5.0
* Maven 3

### Embedded H2 Database
본 프로젝트에서 트랜잭션 테스트를 위한 데이터베이스는 Embedded H2를 사용한다.

DataSource 구성 시 EmbeddedDatabaseBuilder 클래스를 이용한다.

* 참고: https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#jdbc-embedded-database-support

### Proxy Bean Test
스프링의 AOP는 프록시 패턴을 이용하여 적용되는데, @Transactional 애노테이션을 이용하여 트랜잭션을 적용하는 방법도 마찬가지이다.

프록시 객체 내에서 자신의 트랜잭션 메서드를 호출했을 때 일어나는 문제를 자세히 테스트한다.

### Transaction Isolation Test
트랜잭션의 고립성에 따라서 동시성과 정합성에 미치는 영향을 테스트한다.

### Transaction Propagation Test
트랜잭션 메서드 내에서 다른 객체의 트랜잭션 메서드를 호출했을 때, 트랜잭션 전파 설정에 따라 달라지는 부분을 테스트한다.