package demo.invoice;

import demo.address.Address;
import demo.address.AddressType;
import demo.domain.BaseEntity;
import demo.order.Order;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * A simple domain class for the {@link Invoice} concept of the order context.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
@Entity
public class Invoice extends BaseEntity {

    @Id
    private String invoiceId;
    private String customerId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Order> orders = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    private Address billingAddress;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus invoiceStatus;

    public Invoice() {
        this.invoiceId = UUID.randomUUID().toString();
    }

    public Invoice(String customerId, Address billingAddress) {
        this();
        this.customerId = customerId;
        this.billingAddress = billingAddress;
        this.billingAddress.setAddressType(AddressType.BILLING);
        this.invoiceStatus = InvoiceStatus.CREATED;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public InvoiceStatus getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(InvoiceStatus invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "invoiceId='" + invoiceId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", orders=" + orders +
                ", billingAddress=" + billingAddress +
                ", invoiceStatus=" + invoiceStatus +
                "} " + super.toString();
    }
}
