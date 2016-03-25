package demo.catalog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface CatalogInfoRepository extends JpaRepository<CatalogInfo, String> {
    CatalogInfo findCatalogByActive(@Param("active") Boolean active);
}

