package demo.api.v1;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import demo.account.Account;
import demo.order.Order;
import demo.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class OrderServiceV1 {

    private OrderRepository orderRepository;
    private OAuth2RestTemplate oAuth2RestTemplate;

    @Autowired
    public OrderServiceV1(OrderRepository orderRepository,
                          @LoadBalanced OAuth2RestTemplate oAuth2RestTemplate) {
        this.orderRepository = orderRepository;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
    }

    @HystrixCommand
    public List<Order> getOrdersForAccount(String accountNumber) throws Exception {
        List<Order> orders;
        Account[] accounts = oAuth2RestTemplate.getForObject("http://account-service/v1/accounts", Account[].class);

        // Ensure account number is owned by the authenticated user
        if (accounts != null &&
                !Arrays.asList(accounts).stream().anyMatch(acct ->
                        Objects.equals(acct.getAccountNumber(), accountNumber))) {
            throw new Exception("Account number invalid");
        }

        orders = orderRepository.findByAccountNumber(accountNumber);

        return orders;
    }
}
