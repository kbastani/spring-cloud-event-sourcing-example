package demo.api.v1;

import demo.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/v1")
public class OrderControllerV1 {

    private OrderServiceV1 orderService;

    @Autowired
    public OrderControllerV1(OrderServiceV1 orderService) {
        this.orderService = orderService;
    }

    @RequestMapping(path = "/accounts/{accountNumber}/orders")
    public ResponseEntity getOrders(@PathVariable("accountNumber") String accountNumber) throws Exception {
        return Optional.ofNullable(orderService.getOrdersForAccount(accountNumber))
                .map(a -> new ResponseEntity<List<Order>>(a, HttpStatus.OK))
                .orElseThrow(() -> new Exception("Accounts for user do not exist"));
    }
}
