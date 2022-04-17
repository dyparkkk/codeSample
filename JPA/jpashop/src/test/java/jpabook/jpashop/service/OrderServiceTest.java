package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    void 상품주문() {
        //given
        Member member = createMember();
        Book book = createBook("book1", 10000, 10);
        int orderCount = 3;

        System.out.println("book.getId() = " + book.getId());
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order testOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, testOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, testOrder.getOrderItems().size(), "주문한 상품 종류 수가 맞아야함");
        assertEquals(10000 * orderCount, testOrder.getTotalPrice(), "주문 가격 확인");
    }

    @Test
    void 상품주문_재고수량초과() {
        //given
        Member member = createMember();
        Book book = createBook("book1", 10000, 10);
        int orderCount = 11;

        //when

        //then
        assertThrows(NotEnoughStockException.class, ()->{
            orderService.order(member.getId(), book.getId(), orderCount);
        });
    }

    @Test
    void 상품취소() {
        //given
        Member member = createMember();
        Book book = createBook("book1", 10000, 10);
        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancleOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANEL, findOrder.getStatus(), "주문취소시 상태 = cancel");
        assertEquals(10, book.getStockQuantity(), "재고 수량 증가 확인");
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("seoul", "street", "123-123"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);

        return book;
    }
}