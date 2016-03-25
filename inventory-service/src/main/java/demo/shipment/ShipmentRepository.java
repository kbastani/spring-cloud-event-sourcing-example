package demo.shipment;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface ShipmentRepository extends GraphRepository<Shipment> {

}
