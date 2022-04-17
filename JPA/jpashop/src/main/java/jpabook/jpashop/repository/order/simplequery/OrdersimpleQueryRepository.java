package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class OrdersimpleQueryRepository{

    private final EntityManager em;

//    public List<OrderSimpleQueryDto>

}
