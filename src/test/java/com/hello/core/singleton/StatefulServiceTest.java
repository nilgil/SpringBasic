package com.hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;

class StatefulServiceTest {
    @Test
    void statefulServiceSingleton() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //ThreadA: 사용자A 10000원 주문
        statefulService1.order("userA", 10000);
        //ThreadB: 사용자B 20000원 주문
        statefulService2.order("userB", 20000);

        //ThreadA: 사용자A 주문 금액 조회
        int price = statefulService1.getPrice();
        System.out.println("price = " + price);

        assertThat(statefulService1.getPrice()).isEqualTo(20000);
    }

    @Test
    void statefulServiceSingletonFix() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);
        StatefulServiceFix statefulService1 = ac.getBean(StatefulServiceFix.class);
        StatefulServiceFix statefulService2 = ac.getBean(StatefulServiceFix.class);

        //ThreadA: 사용자A 10000원 주문
        int userAPrice = statefulService1.order("userA", 10000);
        //ThreadB: 사용자B 20000원 주문
        int userBPrice = statefulService2.order("userB", 20000);

        //ThreadA: 사용자A,B 주문 금액 조회
        System.out.println("userAPrice = " + userAPrice + ", userBPrice = " + userBPrice);

        assertThat(userAPrice).isEqualTo(10000);
        assertThat(userBPrice).isEqualTo(20000);
    }

    static class TestConfig {
        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }

        @Bean
        public StatefulServiceFix statefulServiceFix() {
            return new StatefulServiceFix();
        }
    }

}