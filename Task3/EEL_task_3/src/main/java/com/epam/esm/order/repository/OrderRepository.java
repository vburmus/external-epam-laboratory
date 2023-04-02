package com.epam.esm.order.repository;

import com.epam.esm.order.model.Order;
import com.epam.esm.utils.AppQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query(value = AppQuery.Order.IS_GC_IN_ORDER_EXIST, nativeQuery = true)
    boolean existsByIdAndGiftCertificateId(Long orderId, Long giftCertificateId);

    @Modifying
    @Query(value = AppQuery.Order.INCREMENT_QUANTITY, nativeQuery = true)
    int incrementQuantityByGiftCertificateIdAndOrderId(Long giftCertificateId, Long orderId);
}
