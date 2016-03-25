package demo.creditcard;

import demo.data.BaseEntity;

import java.io.Serializable;

public class CreditCard extends BaseEntity implements Serializable {

    private Long id;
    private String number;

    private CreditCardType type;

    public CreditCard() {
    }

    public CreditCard(String number, CreditCardType type) {
        this.number = number;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public CreditCardType getType() {
        return type;
    }

    public void setType(CreditCardType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", number='" + number.replaceAll("([\\d]{4})(?!$)", "****-") + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
