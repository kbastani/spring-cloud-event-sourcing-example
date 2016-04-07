package demo.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import demo.address.Address;
import demo.address.AddressType;
import demo.domain.BaseEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple domain class for the {@link Order} concept in the order context.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
public class Order extends BaseEntity implements Serializable {

    @JsonProperty(value = "orderId")
    private String orderId;
    private String accountNumber;
    private OrderStatus orderStatus;
    private List<LineItem> lineItems = new ArrayList<>();
    private Address shippingAddress;

    public Order() {
    }

    public Order(String accountNumber, Address shippingAddress) {
        this.accountNumber = accountNumber;
        this.shippingAddress = shippingAddress;
        this.shippingAddress.setAddressType(AddressType.SHIPPING);
        this.orderStatus = OrderStatus.PURCHASED;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Address getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(Address shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void addLineItem(LineItem lineItem) {
        lineItems.add(lineItem);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", orderStatus=" + orderStatus +
                ", lineItems=" + lineItems +
                ", shippingAddress=" + shippingAddress +
                "} " + super.toString();
    }
}
