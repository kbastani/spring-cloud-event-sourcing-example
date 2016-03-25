package demo.api.v1;


import demo.catalog.Catalog;
import demo.catalog.CatalogInfo;
import demo.catalog.CatalogInfoRepository;
import demo.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.stream.Collectors;

@Service
public class CatalogServiceV1 {
    private CatalogInfoRepository catalogInfoRepository;
    private RestTemplate restTemplate;

    @Autowired
    public CatalogServiceV1(CatalogInfoRepository catalogInfoRepository,
                            @LoadBalanced RestTemplate restTemplate) {
        this.catalogInfoRepository = catalogInfoRepository;
        this.restTemplate = restTemplate;
    }

    public Catalog getCatalog() {
        Catalog catalog;
        CatalogInfo activeCatalog = catalogInfoRepository.findCatalogByActive(true);
        catalog = restTemplate.getForObject(String.format("http://inventory-service/catalogs/%s",
                activeCatalog.getCatalogId()), Catalog.class);
        catalog.setId(activeCatalog.getCatalogId());

        ProductsResource products = restTemplate.getForObject(String.format("http://inventory-service/catalogs/%s/products",
                activeCatalog.getCatalogId()), ProductsResource.class);
        catalog.setProducts(products.getContent().stream().collect(Collectors.toSet()));
        return catalog;
    }

    public Product getProduct(Long productId) {
        return restTemplate.getForObject(String.format("http://inventory-service/products/%s",
                productId), Product.class);
    }
}
