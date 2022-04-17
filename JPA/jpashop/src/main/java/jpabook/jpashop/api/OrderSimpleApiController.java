package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
/*
 xxxToOne 관계 (oneToOne, ManyToOne) -> join fetch 로 해결
 order -> member
 order -> delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-order")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-order")
    public List<SimpleOrderDto> orderV2() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        List<SimpleOrderDto> result = all.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-order")
    public List<SimpleOrderDto> orderV3() {
        List<Order> all = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = all.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderTime;
        private OrderStatus orderStatus;
        private Address address;


        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderTime = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
}
