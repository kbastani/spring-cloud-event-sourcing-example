package service;

import demo.AccountApplication;
import demo.account.Account;
import demo.address.Address;
import demo.address.AddressType;
import demo.creditcard.CreditCard;
import demo.creditcard.CreditCardType;
import demo.customer.Customer;
import demo.customer.CustomerRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AccountApplication.class)
@ActiveProfiles(profiles = "test")
@WebIntegrationTest
public class AccountApplicationTests extends TestCase {

    private Logger log = LoggerFactory.getLogger(AccountApplicationTests.class);

    @Autowired
    CustomerRepository customerRepository;

    @Test
    public void customerTest() {
        log.info("*** Starting Customer Test");

        // Create a new account
        Account account = new Account("5", "12345");

        // Create a new customer for the account
        Customer customer = new Customer("Jane", "Doe", "jane.doe@gmail.com", account);

        // Create a new credit card for the account
        CreditCard creditCard = new CreditCard("1234567801234567", CreditCardType.VISA);

        // Add the credit card to the customer's account
        customer.getAccount()
                .getCreditCards()
                .add(creditCard);

        // Create a new shipping address for the customer
        Address address = new Address("1600 Pennsylvania Ave NW", null,
                "DC", "Washington", "United States", AddressType.SHIPPING, 20500);

        // Add address to the customer's account
        customer.getAccount()
                .getAddresses()
                .add(address);

        // Apply the cascading update by persisting the customer object
        customer = customerRepository.save(customer);

        // Query for the customer object to ensure cascading persistence of the object graph
        log.info(customerRepository.findOne(customer.getId()).toString());
    }
}