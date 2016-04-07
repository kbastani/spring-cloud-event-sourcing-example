package demo.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.stream.Stream;

@RepositoryRestResource
public interface CartEventRepository extends JpaRepository<CartEvent, Long> {
    Stream<CartEvent> findCartEventsByUserIdOrderByCreatedAtDesc(@Param("userId") Long userId);
}
