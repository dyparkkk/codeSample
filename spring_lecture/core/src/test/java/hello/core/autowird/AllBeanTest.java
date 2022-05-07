package hello.core.autowird;

import hello.core.AppConfig;
import hello.core.AutoAppConfig;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

public class AllBeanTest {

    @Test
    void findAllBean(){
        // 스프링 컨테이너에 클래스를 빈으로 등록
        ApplicationContext ac = new AnnotationConfigApplicationContext(AutoAppConfig.class, DisCountService.class);
        DisCountService disCountService = ac.getBean(DisCountService.class);
        Member member = new Member(1L, "member1", Grade.Vip);

        int discountPrice = disCountService.discount(member, 10000, "fixDiscountPolicy");
        assertThat(disCountService).isInstanceOf(DisCountService.class);
        assertThat(discountPrice).isEqualTo(1000);

        int discountPrice2 = disCountService.discount(member, 20000, "rateDiscountPolicy");
        assertThat(discountPrice2).isEqualTo(2000);

    }

    // 빈으로 등록할 클래스
    // 생성자 주입 시점에 컨테이너가 자동으로 관련 클래스(빈으로 등록된)을 Map과 List에 담아줌
    static class DisCountService{
        private final Map<String, DiscountPolicy> policyMap;
        private final List<DiscountPolicy> policies;

        @Autowired
        public DisCountService(Map<String, DiscountPolicy> policyMap, List<DiscountPolicy> policies) {
            this.policyMap = policyMap;
            this.policies = policies;
            System.out.println("policyMap = " + policyMap);
            System.out.println("policies = " + policies);
        }

        // Map에서 가져오면 됨 ( 상당히 편리함)
        public int discount(Member member, int price, String discountCode) {
            DiscountPolicy discountPolicy = policyMap.get(discountCode);
            return discountPolicy.discount(member, price);
        }
    }
}
