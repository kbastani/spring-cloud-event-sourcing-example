package demo.inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    @Query("select inv from Inventory inv\n" +
            "where inv.product.productId = :productId")
    List<Inventory> getAvailableInventoryForProduct(@Param("productId") String productId);

    // SELECT * FROM product prd
    // JOIN inventory inv ON inv.productId = prd.productId
    // WHERE prd.productId = {productId}
    // AND

    @Query("select inv from Inventory inv\n" +
            "where inv.product.productId in :productIds")
    List<Inventory> getAvailableInventoryForProductList(@Param("productIds") String[] productIds);
}
