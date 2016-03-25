package demo.inventory;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface InventoryRepository extends GraphRepository<Inventory> {
}
