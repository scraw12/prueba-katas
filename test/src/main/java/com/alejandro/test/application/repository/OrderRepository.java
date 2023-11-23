package com.alejandro.test.application.repository;

import com.alejandro.test.application.model.order.Order;
import com.alejandro.test.application.model.order.OrderGroupByItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    @Query("SELECT new com.alejandro.test.application.model.order.OrderGroupByItemType(o.itemType, count(o)) from Order o group by o.itemType")
    public List<OrderGroupByItemType> getCountByItemType();

    public List<Order> findAllByOrderByIdAsc();
}
