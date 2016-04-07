package demo.api.v1;

import demo.cart.CartEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/v1")
public class ShoppingCartControllerV1 {

    private ShoppingCartServiceV1 shoppingCartService;

    @Autowired
    public ShoppingCartControllerV1(ShoppingCartServiceV1 shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @RequestMapping(path = "/events", method = RequestMethod.POST)
    public ResponseEntity addCartEvent(@RequestBody CartEvent cartEvent) throws Exception {
        return Optional.ofNullable(shoppingCartService.addCartEvent(cartEvent))
                .map(event -> new ResponseEntity(HttpStatus.NO_CONTENT))
                .orElseThrow(() -> new Exception("Could not find shopping cart"));
    }

    @RequestMapping(path = "/checkout", method = RequestMethod.POST)
    public ResponseEntity checkoutCart() throws Exception {
        return Optional.ofNullable(shoppingCartService.checkout())
                .map(checkoutResult -> new ResponseEntity<>(checkoutResult, HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not checkout"));
    }

    @RequestMapping(path = "/cart", method = RequestMethod.GET)
    public ResponseEntity getCart() throws Exception {
        return Optional.ofNullable(shoppingCartService.getShoppingCart())
                .map(cart -> new ResponseEntity<>(cart, HttpStatus.OK))
                .orElseThrow(() -> new Exception("Could not find shopping cart"));
    }
}
