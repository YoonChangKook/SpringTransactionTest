package com.navercorp.example.transactiontest.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션 컨텍스트 설정
 *
 * @author 국윤창
 */
@Configuration
@ComponentScan(basePackages = {"com.navercorp.example.transactiontest"})
public class AppConfig {

}
