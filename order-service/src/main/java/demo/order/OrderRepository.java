package demo.order;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface OrderRepository extends PagingAndSortingRepository<Order, String> {
    List<Order> findByAccountNumber(String accountNumber);
}
