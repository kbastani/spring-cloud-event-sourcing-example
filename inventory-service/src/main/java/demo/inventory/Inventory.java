package demo.inventory;

import demo.product.Product;
import demo.warehouse.Warehouse;

import javax.persistence.*;

@Entity
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String inventoryNumber;

    @OneToOne(cascade = CascadeType.MERGE)
    private Product product;

    @OneToOne(cascade = CascadeType.MERGE)
    private Warehouse warehouse;

    @Enumerated(EnumType.STRING)
    private InventoryStatus status;

    public Inventory() {
    }

    public Inventory(String inventoryNumber, Product product) {
        this.inventoryNumber = inventoryNumber;
        this.product = product;
    }

    public Inventory(String inventoryNumber, Product product, Warehouse warehouse, InventoryStatus status) {
        this.inventoryNumber = inventoryNumber;
        this.product = product;
        this.warehouse = warehouse;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "id=" + id +
                ", inventoryNumber='" + inventoryNumber + '\'' +
                ", product=" + product +
                ", warehouse=" + warehouse +
                ", status=" + status +
                '}';
    }
}
