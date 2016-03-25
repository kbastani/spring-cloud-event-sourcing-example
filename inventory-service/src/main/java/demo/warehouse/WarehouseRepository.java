package demo.warehouse;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface WarehouseRepository extends GraphRepository<Warehouse> {
}
