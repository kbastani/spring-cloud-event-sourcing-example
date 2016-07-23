package demo.catalog;

import demo.product.Product;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long catalogNumber;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Product> products = new ArrayList<>();

    private String name;

    public Catalog() {
    }

    public Catalog(String name, Long catalogNumber) {
        this.name = name;
        this.catalogNumber = catalogNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(Long catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "id=" + id +
                ", catalogNumber=" + catalogNumber +
                ", products=" + products +
                ", name='" + name + '\'' +
                '}';
    }
}
