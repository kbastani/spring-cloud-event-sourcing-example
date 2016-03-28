package demo.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.stream.Stream;

public interface CartEventRepository extends JpaRepository<CartEvent, Long> {
    Stream<CartEvent> findCartEventsByUserId(@Param("userId") Long userId);
}
