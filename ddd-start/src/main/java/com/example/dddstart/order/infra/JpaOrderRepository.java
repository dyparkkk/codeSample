package com.example.dddstart.order.infra;

import com.example.dddstart.order.domain.Order;
import com.example.dddstart.order.domain.OrderNo;
import com.example.dddstart.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpaOrderRepository implements OrderRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    public Order findById(OrderNo id) {
        return em.find(Order.class, id);
    }

    @Override
    public void save(Order order) {
        em.persist(order);
    }

    @Override
    public List<Order> findByOrdererId(String ordererId, int startRow, int size) {
        return em.createQuery("select o from Order o" +
                " where o.orderer.memberId.id = :orderId" +
                " order by o.number.number desc", Order.class)
                .setParameter("ordererId", ordererId)
                .setFirstResult(startRow)
                .setMaxResults(size)
                .getResultList();
    }

    @Override
    public Long countsAll() {
        return em.createQuery("select count(o) from Order o", Long.class)
                .getSingleResult();
    }
}
