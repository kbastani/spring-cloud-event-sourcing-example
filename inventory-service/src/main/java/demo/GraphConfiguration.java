package demo;

import demo.address.AddressRepository;
import demo.catalog.CatalogRepository;
import demo.inventory.InventoryRepository;
import demo.product.ProductRepository;
import demo.shipment.ShipmentRepository;
import demo.warehouse.WarehouseRepository;
import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
class GraphConfiguration extends Neo4jConfiguration {

    @Autowired
    private Neo4jProperties properties;

    @Bean
    public Neo4jServer neo4jServer() {
        String uri = this.properties.getUri();
        String pw = this.properties.getPassword();
        String user = this.properties.getUsername();
        if (StringUtils.hasText(pw) && StringUtils.hasText(user)) {
            return new RemoteServer(uri, user, pw);
        }
        return new RemoteServer(uri);
    }

    @Bean
    public SessionFactory getSessionFactory() {
        // we need to specify which packages Neo4j should scan
        // we'll use classes in each package to avoid brittleness
        Class<?>[] packageClasses = {
                ProductRepository.class,
                ShipmentRepository.class,
                WarehouseRepository.class,
                AddressRepository.class,
                CatalogRepository.class,
                InventoryRepository.class
        };
        String[] packageNames =
                Arrays.asList(packageClasses)
                        .stream()
                        .map( c -> getClass().getPackage().getName())
                        .collect(Collectors.toList())
                        .toArray(new String[packageClasses.length]);
        return new SessionFactory(packageNames);
    }

    @Bean
    public Session getSession() throws Exception {
        return super.getSession();
    }
}
