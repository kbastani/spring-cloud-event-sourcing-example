package demo;

import demo.address.Address;
import demo.api.v1.OrderServiceV1;
import demo.invoice.Invoice;
import demo.invoice.InvoiceRepository;
import demo.order.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = OrderApplication.class)
@WebIntegrationTest
public class OrderApplicationTest extends TestCase {

    private Logger log = LoggerFactory.getLogger(OrderApplicationTest.class);
    private Boolean mongoConnection = true;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    OrderServiceV1 orderServiceV1;

    @Before
    public void before() {
        try {
            orderRepository.deleteAll();
            invoiceRepository.deleteAll();
        } catch (DataAccessResourceFailureException ex) {
            mongoConnection = false;
        }
    }

    @Test
    public void orderTest() {

        if (mongoConnection) {
            // Create a new shipping address for the customer
            Address shippingAddress = new Address("1600 Pennsylvania Ave NW", null,
                    "DC", "Washington", "United States", 20500);

            // Create a new order
            Order order = new Order("12345", shippingAddress);

            // Add line items
            order.addLineItem(new LineItem("Best. Cloud. Ever. (T-Shirt, Men's Large)",
                    "SKU-24642", 1, 21.99, .06));

            order.addLineItem(new LineItem("Like a BOSH (T-Shirt, Women's Medium)",
                    "SKU-34563", 3, 14.99, .06));

            order.addLineItem(new LineItem("We're gonna need a bigger VM (T-Shirt, Women's Small)",
                    "SKU-12464", 4, 13.99, .06));

            order.addLineItem(new LineItem("cf push awesome (Hoodie, Men's Medium)",
                    "SKU-64233", 2, 21.99, .06));

            // Save the order
            order = orderRepository.save(order);

            // Log the result
            log.info(order.toString());

            // The lastModified and createdAt timestamps should now be different
            log.info(orderRepository.save(order).toString());

            // Create a new billing address
            Address billingAddress = new Address("875 Howard St", null,
                    "CA", "San Francisco", "United States", 94103);

            // Create a new invoice
            Invoice invoice = new Invoice("918273465", billingAddress);

            // Add the order to the invoice
            invoice.addOrder(order);

            // Save the invoice
            invoice = invoiceRepository.save(invoice);

            // Lookup orders by account number
            List<Order> orders = orderRepository.findByAccountNumber("918273465");

            log.info(orders.toString());

            try {
                orderServiceV1.addOrderEvent(new OrderEvent(OrderEventType.CREATED, order.getOrderId()), false);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Order orderPending = orderServiceV1.getOrder(order.getOrderId(), false);

            Assert.notNull(orderPending);
            Assert.isTrue(orderPending.getOrderStatus() == OrderStatus.PENDING);
        }
    }

    @After
    public void tearDown() {
        if (mongoConnection) {
            orderRepository.deleteAll();
            invoiceRepository.deleteAll();
        }
    }
}