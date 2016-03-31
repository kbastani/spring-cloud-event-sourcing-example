package demo.catalog;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface CatalogRepository extends GraphRepository<Catalog> {
    Catalog findCatalogByCatalogNumber(@Param("catalogNumber") Long catalogNumber);
}
