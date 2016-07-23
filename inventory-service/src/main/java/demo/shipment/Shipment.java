package demo.shipment;

import demo.address.Address;
import demo.inventory.Inventory;
import demo.warehouse.Warehouse;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple domain class for the {@link Shipment}
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
@Entity
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Set<Inventory> inventories = new HashSet<>();

    @OneToOne(cascade = CascadeType.MERGE)
    private Address deliveryAddress;

    @OneToOne(cascade = CascadeType.MERGE)
    private Warehouse fromWarehouse;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    public Shipment() {
    }

    public Shipment(Set<Inventory> inventories, Address deliveryAddress, Warehouse fromWarehouse, ShipmentStatus shipmentStatus) {
        this.inventories = inventories;
        this.deliveryAddress = deliveryAddress;
        this.fromWarehouse = fromWarehouse;
        this.shipmentStatus = shipmentStatus;
    }

    public Long getId() {
        return id;
    }

    public Set<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(Set<Inventory> inventories) {
        this.inventories = inventories;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(Address deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Warehouse getFromWarehouse() {
        return fromWarehouse;
    }

    public void setFromWarehouse(Warehouse fromWarehouse) {
        this.fromWarehouse = fromWarehouse;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public void setShipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }

    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + id +
                ", inventories=" + inventories +
                ", deliveryAddress=" + deliveryAddress +
                ", fromWarehouse=" + fromWarehouse +
                ", shipmentStatus=" + shipmentStatus +
                '}';
    }
}
