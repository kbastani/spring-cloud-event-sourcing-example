package demo.config;

import demo.address.Address;
import demo.invoice.Invoice;
import demo.invoice.InvoiceRepository;
import demo.order.LineItem;
import demo.order.Order;
import demo.order.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("docker")
public class DatabaseInitializer {

    private OrderRepository orderRepository;
    private InvoiceRepository invoiceRepository;

    @Autowired
    public DatabaseInitializer(OrderRepository orderRepository, InvoiceRepository invoiceRepository) {
        this.orderRepository = orderRepository;
        this.invoiceRepository = invoiceRepository;
    }

    public void populate() {
        // Clear existing data
        orderRepository.deleteAll();
        invoiceRepository.deleteAll();

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

        // Create a new billing address
        Address billingAddress = new Address("875 Howard St", null,
                "CA", "San Francisco", "United States", 94103);

        // Create a new invoice
        Invoice invoice = new Invoice("918273465", billingAddress);

        // Add the order to the invoice
        invoice.addOrder(order);

        // Save the invoice
        invoiceRepository.save(invoice);
    }
}
