package demo;

import demo.address.Address;
import demo.address.AddressRepository;
import demo.catalog.Catalog;
import demo.catalog.CatalogRepository;
import demo.inventory.Inventory;
import demo.inventory.InventoryRepository;
import demo.inventory.InventoryStatus;
import demo.product.Product;
import demo.product.ProductRepository;
import demo.shipment.Shipment;
import demo.shipment.ShipmentRepository;
import demo.shipment.ShipmentStatus;
import demo.warehouse.Warehouse;
import demo.warehouse.WarehouseRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {InventoryApplication.class})
@ActiveProfiles(profiles = "test")
public class InventoryApplicationTests {

    private Logger log = LoggerFactory.getLogger(InventoryApplicationTests.class);
    private Boolean neo4jConnection = true;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CatalogRepository catalogRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private Neo4jConfiguration neo4jConfiguration;

    @Before
    public void setup() {
        try {
            neo4jConfiguration.getSession().query(
                    "MATCH (n) OPTIONAL MATCH (n)-[r]-() DELETE n, r;", new HashMap<>())
                    .queryResults();
        } catch (Exception e) {
            neo4jConnection = false;
        }
    }

    @Test
    public void inventoryTest() {
        if(neo4jConnection) {
            Warehouse warehouse = new Warehouse("Pivotal SF");

            List<Product> products = Arrays.asList(

                    new Product("Best. Cloud. Ever. (T-Shirt, Men's Large)", "SKU-24642", "<p>Do you love your cloud platform? " +
                            "Do you push code continuously into production on a daily basis? " +
                            "Are you living the cloud native microservice dream? Then rain or shine, this T-Shirt is for you. " +
                            "Show the world you're a stylish cloud platform architect with this cute yet casual tee. " +
                            "<br /><br />&nbsp; <strong>Cloud Native Tee Collection</strong><br />" +
                            "&nbsp; 110% cloud stuff, 5% spandex<br />&nbsp; Rain wash only<br />&nbsp; " +
                            "Four nines of <em>stylability</em></p>", 21.99),

                    new Product("Like a BOSH (T-Shirt, Women's Medium)", "SKU-34563", "<p>The BOSH Outer Shell (<strong>BOSH</strong>) " +
                            "is an elegant release engineering tool for a more virtualized cloud-native age. " +
                            "The feeling of spinning up a highly available distributed system of VMs is second only to the " +
                            "feeling of frequently pushing code to production. Show the cloud <em>who's BOSH</em> with " +
                            "this stylish cloud native ops tee.<br /><br />&nbsp; <strong>Cloud Native Tee Collection</strong><br />&nbsp; " +
                            "99% YAML, 11% CLI<br />&nbsp; BOSH CCK <span style='text-decoration: underline;'><em>recommended</em></span><br />&nbsp; " +
                            "4 nines of <em>re-washability</em></p>", 14.99),

                    new Product("We're gonna need a bigger VM (T-Shirt, Women's Small)", "SKU-12464", 13.99),
                    new Product("cf push awesome (Hoodie, Men's Medium)", "SKU-64233",
                            "<p>One of the great natives of the cloud once said \"<em>" +
                                    "Production is the happiest place on earth for us - it's better than Disneyland</em>\". " +
                                    "With this stylish Cloud Foundry hoodie you can push code to the cloud all day while staying " +
                                    "comfortable and casual. <br /><br />&nbsp; <strong>Cloud Native PaaS Collection</strong><br />" +
                                    "&nbsp; 10% cloud stuff, 90% platform nylon<br />&nbsp; Cloud wash safe<br />" +
                                    "&nbsp; Five nines of <em>comfortability</em></p>", 21.99))
                    .stream()
                    .collect(Collectors.toList());

            productRepository.save(products);

            Product product1 = productRepository.findOne(products.get(0).getId());

            assertThat(product1, is(notNullValue()));
            assertThat(product1.getName(), is(products.get(0).getName()));
            assertThat(product1.getUnitPrice(), is(products.get(0).getUnitPrice()));

            Catalog catalog = new Catalog("Fall Catalog", 0L);

            catalog.getProducts().addAll(products);

            catalogRepository.save(catalog);

            Catalog catalog1 = catalogRepository.findOne(catalog.getId());

            assertThat(catalog1, is(notNullValue()));
            assertThat(catalog1.getName(), is(catalog.getName()));

            Address warehouseAddress = new Address("875 Howard St", null,
                    "CA", "San Francisco", "United States", 94103);

            Address shipToAddress = new Address("1600 Amphitheatre Parkway", null,
                    "CA", "Mountain View", "United States", 94043);

            // Save the addresses
            addressRepository.save(Arrays.asList(warehouseAddress, shipToAddress));

            Address address1 = addressRepository.findOne(shipToAddress.getId());

            assertThat(address1, is(notNullValue()));
            assertThat(address1.toString(), is(shipToAddress.toString()));

            Address address2 = addressRepository.findOne(warehouseAddress.getId());

            assertThat(address2, is(notNullValue()));
            assertThat(address2.toString(), is(warehouseAddress.toString()));

            log.info(warehouseAddress.toString());
            log.info(shipToAddress.toString());

            warehouse.setAddress(warehouseAddress);
            warehouse = warehouseRepository.save(warehouse);

            Warehouse warehouse1 = warehouseRepository.findOne(warehouse.getId());

            assertThat(warehouse1, is(notNullValue()));
            assertThat(warehouse1.toString(), is(warehouse.toString()));

            log.info(warehouse.toString());

            Warehouse finalWarehouse = warehouse;

            // Create a new set of inventories with a randomized inventory number
            Set<Inventory> inventories = products.stream()
                    .map(a -> new Inventory(IntStream.range(0, 9)
                            .mapToObj(x -> Integer.toString(new Random().nextInt(9)))
                            .collect(Collectors.joining("")), a, finalWarehouse, InventoryStatus.IN_STOCK))
                    .collect(Collectors.toSet());

            inventoryRepository.save(inventories);

            Shipment shipment = new Shipment(inventories, shipToAddress,
                    warehouse, ShipmentStatus.SHIPPED);

            shipmentRepository.save(shipment);

            Shipment shipment1 = shipmentRepository.findOne(shipment.getId());

            assertThat(shipment1, is(notNullValue()));
            assertThat(shipment1.toString(), is(shipment.toString()));
        }
    }
}
