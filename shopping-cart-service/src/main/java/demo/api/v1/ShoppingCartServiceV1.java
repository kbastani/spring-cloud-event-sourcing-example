package demo.api.v1;

import demo.cart.CartEvent;
import demo.cart.CartEventRepository;
import demo.cart.ShoppingCart;
import demo.catalog.Catalog;
import demo.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;

@Service
public class ShoppingCartServiceV1 {

    private OAuth2RestTemplate oAuth2RestTemplate;
    private CartEventRepository cartEventRepository;
    private RestTemplate restTemplate;

    @Autowired
    public ShoppingCartServiceV1(
            @LoadBalanced OAuth2RestTemplate oAuth2RestTemplate,
            CartEventRepository cartEventRepository,
            @LoadBalanced RestTemplate normalRestTemplate) {
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.cartEventRepository = cartEventRepository;
        this.restTemplate = normalRestTemplate;
    }

    public User getAuthenticatedUser() {
        return oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);
    }

    public Boolean addCartEvent(CartEvent cartEvent) {
        User user = getAuthenticatedUser();
        if (user != null) {
            cartEvent.setUserId(user.getId());
            cartEventRepository.save(cartEvent);
        } else {
            return null;
        }
        return true;
    }

    public Boolean addCartEvent(CartEvent cartEvent, User user) {
        if (user != null) {
            cartEvent.setUserId(user.getId());
            cartEventRepository.save(cartEvent);
        } else {
            return null;
        }
        return true;
    }

    public ShoppingCart getShoppingCart() throws Exception {
        User user = oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);
        ShoppingCart shoppingCart = null;
        if (user != null) {
            Catalog catalog = restTemplate.getForObject("http://catalog-service/v1/catalog", Catalog.class);
            shoppingCart = aggregateCartEvents(user, catalog);
        }
        return shoppingCart;
    }

    public ShoppingCart aggregateCartEvents(User user, Catalog catalog) throws Exception {
        Flux<CartEvent> cartEvents =
                Flux.fromStream(cartEventRepository.findCartEventsByUserId(user.getId()));

        ShoppingCart shoppingCart = cartEvents
                .takeWhile(cartEvent -> !ShoppingCart.isTerminal(cartEvent.getCartEventType()))
                .reduceWith(() -> new ShoppingCart(catalog), ShoppingCart::incorporate)
                .get();

        shoppingCart.getLineItems();

        return shoppingCart;
    }
}
