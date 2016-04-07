package demo.api.v1;

import demo.account.Account;
import demo.address.AddressType;
import demo.order.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceV1 {

    private OrderRepository orderRepository;
    private OrderEventRepository orderEventRepository;
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public OrderServiceV1(OrderRepository orderRepository,
                          OrderEventRepository orderEventRepository,
                          @LoadBalanced OAuth2RestTemplate oAuth2RestTemplate) {
        this.orderRepository = orderRepository;
        this.orderEventRepository = orderEventRepository;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    public Order createOrder(List<LineItem> lineItems) {
        Account[] accounts = oAuth2RestTemplate.getForObject("http://account-service/v1/accounts", Account[].class);

        Account defaultAccount = Arrays.asList(accounts).stream()
                .filter(Account::getDefaultAccount)
                .findFirst().orElse(null);

        if (defaultAccount == null) {
            return null;
        }

        Order newOrder = new Order(defaultAccount.getAccountNumber(), defaultAccount.getAddresses().stream()
                .filter(address -> address.getAddressType() == AddressType.SHIPPING)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Default account does not have a shipping address")));

        newOrder.setLineItems(lineItems);

        newOrder = orderRepository.save(newOrder);

        return newOrder;
    }

    public Boolean addOrderEvent(OrderEvent orderEvent, Boolean validate) throws Exception {
        // Get the order for the event
        Order order = orderRepository.findOne(orderEvent.getOrderId());

        if (validate) {
            // Validate the account number of the event's order belongs to the user
            validateAccountNumber(order.getAccountNumber());
        }

        // Save the order event
        orderEventRepository.save(orderEvent);

        return true;
    }

    public Order getOrder(String orderId, Boolean validate) {
        // Get the order for the event
        Order order = orderRepository.findOne(orderId);

        if (validate) {
            try {
                // Validate the account number of the event's order belongs to the user
                validateAccountNumber(order.getAccountNumber());
            } catch (Exception ex) {
                return null;
            }
        }

        Flux<OrderEvent> orderEvents =
                Flux.fromStream(orderEventRepository.findOrderEventsByOrderId(order.getOrderId()));

        // Aggregate the state of order
        return orderEvents
                .takeWhile(orderEvent -> orderEvent.getType() != OrderEventType.DELIVERED)
                .reduceWith(() -> order, Order::incorporate)
                .get();
    }

    public List<Order> getOrdersForAccount(String accountNumber) throws Exception {
        List<Order> orders;
        validateAccountNumber(accountNumber);

        orders = orderRepository.findByAccountNumber(accountNumber);

        return orders.stream()
                .map(order -> getOrder(order.getOrderId(), true))
                .filter(order -> order != null)
                .collect(Collectors.toList());
    }

    public boolean validateAccountNumber(String accountNumber) throws Exception {
        Account[] accounts = oAuth2RestTemplate.getForObject("http://account-service/v1/accounts", Account[].class);

        // Ensure account number is owned by the authenticated user
        if (accounts != null &&
                !Arrays.asList(accounts).stream().anyMatch(acct ->
                        Objects.equals(acct.getAccountNumber(), accountNumber))) {
            throw new Exception("Account number invalid");
        }

        return true;
    }
}
