package hello.core.order;

import hello.core.AppConfig;
import hello.core.member.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {
    public static void main(String[] args) {

//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = applicationContext.getBean("MemberService", MemberService.class);
        OrderService orderService = applicationContext.getBean("OrderService", OrderService.class);

        Long memberId = 1L;
        Member member = new Member(memberId, "member1", Grade.Vip);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "item1", 10000);

        System.out.println("order = "+ order);
    }
}
