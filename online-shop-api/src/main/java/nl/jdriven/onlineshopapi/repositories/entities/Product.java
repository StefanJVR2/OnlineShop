package nl.jdriven.onlineshopapi.repositories.entities;


import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Table
public class Product implements Persistable<Long> {

    @Id
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private double price;
    @Transient
    private boolean insertProduct;

    public Product() {
        // Empty constructor required by JPA
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Transient
    @Override
    public boolean isNew() {
        return insertProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setInsertProduct(boolean insertProduct) {
        this.insertProduct = insertProduct;
    }
}
