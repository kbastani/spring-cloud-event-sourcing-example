package demo.order;

import demo.address.Address;
import demo.address.AddressType;
import demo.domain.BaseEntity;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple domain class for the {@link Order} concept in the order context.
 *
 * @author Kenny Bastani
 */
@Document
public class Order extends BaseEntity {

    @Id
    private ObjectId orderId;
    private String accountNumber;
    @Transient
    private OrderStatus orderStatus;
    private List<LineItem> lineItems = new ArrayList<>();
    private Address shippingAddress;

    public Order() {
        this.orderStatus = OrderStatus.PURCHASED;
    }

    public Order(String accountNumber, Address shippingAddress) {
        this();
        this.accountNumber = accountNumber;
        this.shippingAddress = shippingAddress;
        if (shippingAddress.getAddressType() == null)
            this.shippingAddress.setAddressType(AddressType.SHIPPING);
    }

    public String getOrderId() {
        return orderId != null ? orderId.toHexString() : null;
    }

    public void setOrderId(String id) {
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

    /**
     * The incorporate method uses a simple state machine for an order's status to generate
     * the current state of an order using event sourcing and aggregation.
     * <p>
     * The event diagram below represents how events are incorporated into generating the
     * order status. The event log for the order status can be used to rollback the state
     * of the order in the case of a failed distributed transaction.
     * <p>
     * Events:   +<--PURCHASED+  +<--CREATED+  +<---ORDERED+  +<----SHIPPED+
     * *         |            |  |          |  |           |  |            |
     * Status    +PURCHASED---+PENDING------+CONFIRMED-----+SHIPPED--------+DELIVERED
     * *         |            |  |          |  |           |  |            |
     * Events:   +CREATED---->+  +ORDERED-->+  +SHIPPED--->+  +DELIVERED-->+
     *
     * @param orderEvent is the event to incorporate into the state machine
     * @return the aggregate {@link Order} with the aggregated order status
     */
    public Order incorporate(OrderEvent orderEvent) {

        if (orderStatus == null)
            orderStatus = OrderStatus.PURCHASED;

        switch (orderStatus) {
            case PURCHASED:
                if (orderEvent.getType() == OrderEventType.CREATED)
                    orderStatus = OrderStatus.PENDING;
                break;
            case PENDING:
                if (orderEvent.getType() == OrderEventType.ORDERED) {
                    orderStatus = OrderStatus.CONFIRMED;
                } else if (orderEvent.getType() == OrderEventType.PURCHASED) {
                    orderStatus = OrderStatus.PURCHASED;
                }
                break;
            case CONFIRMED:
                if (orderEvent.getType() == OrderEventType.SHIPPED) {
                    orderStatus = OrderStatus.SHIPPED;
                } else if (orderEvent.getType() == OrderEventType.CREATED) {
                    orderStatus = OrderStatus.PENDING;
                }
                break;
            case SHIPPED:
                if (orderEvent.getType() == OrderEventType.DELIVERED) {
                    orderStatus = OrderStatus.DELIVERED;
                } else if (orderEvent.getType() == OrderEventType.ORDERED) {
                    orderStatus = OrderStatus.CONFIRMED;
                }
                break;
            case DELIVERED:
                if (orderEvent.getType() == OrderEventType.SHIPPED)
                    orderStatus = OrderStatus.SHIPPED;
                break;
            default:
                // Invalid event type with regards to the order status
                break;
        }

        return this;
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
