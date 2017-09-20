package demo.address;

import demo.data.BaseEntity;

import javax.persistence.*;

/**
 * A simple {@link Address} entity for an account.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
@Entity
public class Address extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String street1;
    private String street2;
    private String state;
    private String city;
    private String country;
    private Integer zipCode;

    @Enumerated(EnumType.STRING)
    private AddressType addressType;

    public Address() {
    }

    public Address(String street1, String street2, String state,
                   String city, String country, AddressType addressType,
                   Integer zipCode) {
        this.street1 = street1;
        this.street2 = street2;
        this.state = state;
        this.city = city;
        this.country = country;
        this.addressType = addressType;
        this.zipCode = zipCode;
    }



    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street1='" + street1 + '\'' +
                ", street2='" + street2 + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", addressType='" + addressType + '\'' +
                ", zipCode=" + zipCode +
                '}';
    }
}
