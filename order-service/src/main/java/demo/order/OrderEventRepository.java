package demo.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface OrderEventRepository extends JpaRepository<OrderEvent, Long> {
    Stream<OrderEvent> findOrderEventsByOrderId(@Param("orderId") String orderId);
}
