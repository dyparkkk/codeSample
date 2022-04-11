package com.example.dddstart.order.query;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class JpaOrderViewDao {
    @PersistenceContext
    private EntityManager em;

    public List<OrderView> selectBYOrderer(String ordererId) {
        String queryString = "select new com.example.dddstart.order.query.OrderView(o, m, p)" +
                " from Order o join o.orderLines ol, Member m, Product p" +
                " where o.orderer.memberId.id = :ordererId" +
                " and o.orderer.memverId = m.id" +
                " and index(ol) = 0" +
                " and ol.productId = p.id" +
                " order by o.number.number desc";
        TypedQuery<OrderView> query = em.createQuery(queryString, OrderView.class);
        query.setParameter("orderId", ordererId);
        return query.getResultList();
    }
}
