package demo.order;

/**
 * A simple domain class for the {@link LineItem} concept in the order context.
 *
 * @author Kenny Bastani
 * @author Josh Long
 */
public class LineItem {

    private String name, productId;
    private Integer quantity;
    private Double price, tax;

    public LineItem() {
    }

    public LineItem(String name, String productId, Integer quantity,
                    Double price, Double tax) {
        this.name = name;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.tax = tax;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "name='" + name + '\'' +
                ", productId='" + productId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", tax=" + tax +
                '}';
    }
}
