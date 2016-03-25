package demo.address;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface AddressRepository extends GraphRepository<Address> {
}
