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
    public ShoppingCartServiceV1(CartEventRepository cartEventRepository,
                                 @LoadBalanced OAuth2RestTemplate oAuth2RestTemplate,
                                 @LoadBalanced RestTemplate normalRestTemplate) {
        this.cartEventRepository = cartEventRepository;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.restTemplate = normalRestTemplate;
    }

    /**
     * Get the authenticated user from the user service
     * @return the currently authenticated user
     */
    public User getAuthenticatedUser() {
        return oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);
    }

    /**
     * Adds a shopping cart event for the authenticated user
     *
     * @param cartEvent is the event detailing the action performed by the user
     * @return a flag indicating whether the result was a success
     */
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

    /**
     * Get the shopping cart for the currently authenticated user
     *
     * @return an aggregate object derived from events performed by the user
     * @throws Exception
     */
    public ShoppingCart getShoppingCart() throws Exception {
        User user = oAuth2RestTemplate.getForObject("http://user-service/uaa/v1/me", User.class);
        ShoppingCart shoppingCart = null;
        if (user != null) {
            Catalog catalog = restTemplate.getForObject("http://catalog-service/v1/catalog", Catalog.class);
            shoppingCart = aggregateCartEvents(user, catalog);
        }
        return shoppingCart;
    }

    /**
     * Aggregate the cart events of a user and return a {@link ShoppingCart} object
     *
     * @param user    is the user to retrieve the shopping cart for
     * @param catalog is the catalog used to generate the shopping cart
     * @return a shopping cart representing the aggregate state of the user's cart
     * @throws Exception
     */
    public ShoppingCart aggregateCartEvents(User user, Catalog catalog) throws Exception {
        Flux<CartEvent> cartEvents =
                Flux.fromStream(cartEventRepository.findCartEventsByUserId(user.getId()));

        // Aggregate the state of the shopping cart
        ShoppingCart shoppingCart = cartEvents
                .takeWhile(cartEvent -> !ShoppingCart.isTerminal(cartEvent.getCartEventType()))
                .reduceWith(() -> new ShoppingCart(catalog), ShoppingCart::incorporate)
                .get();

        shoppingCart.getLineItems();

        return shoppingCart;
    }
}
