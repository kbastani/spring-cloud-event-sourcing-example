package demo.v1;


import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.inventory.Inventory;
import demo.inventory.InventoryRepository;
import demo.product.Product;
import demo.product.ProductRepository;
import org.neo4j.ogm.session.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class InventoryServiceV1 {
    private InventoryRepository inventoryRepository;
    private ProductRepository productRepository;
    private Session neo4jTemplate;

    @Autowired
    public InventoryServiceV1(InventoryRepository inventoryRepository,
                              ProductRepository productRepository, Session neo4jTemplate) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.neo4jTemplate = neo4jTemplate;
    }

    @HystrixCommand(fallbackMethod = "getProductFallback")
    public Product getProduct(String productId) {
        Product product;

        product = productRepository.getProductByProductId(productId);

        if (product != null) {
            Stream<Inventory> availableInventory = inventoryRepository.getAvailableInventoryForProduct(productId).stream();
            product.setInStock(availableInventory.findAny().isPresent());
        }

        return product;
    }

    private Product getProductFallback(String productId) {
        return null;
    }

    public List<Inventory> getAvailableInventoryForProductIds(String productIds) {
        List<Inventory> inventoryList;

        inventoryList = inventoryRepository.getAvailableInventoryForProductList(productIds.split(","));

        return neo4jTemplate.loadAll(inventoryList, 1)
                .stream().collect(Collectors.toList());
    }
}
