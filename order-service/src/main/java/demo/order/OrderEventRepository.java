package demo.order;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface OrderEventRepository extends PagingAndSortingRepository<OrderEvent, String> {
    Stream<OrderEvent> findOrderEventsByOrderId(@Param("orderId") String orderId);
}
