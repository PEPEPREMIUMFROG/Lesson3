package org.example;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("productRepository")
public class ProductRepository {
    private List<Product> products = new ArrayList<>();

    @PostConstruct
    public void init() {
        products.add(new Product(1L, "Laptop", 999.99));
        products.add(new Product(2L, "Mouse", 19.99));
        products.add(new Product(3L, "Keyboard", 49.99));
        products.add(new Product(4L, "Monitor", 299.99));
        products.add(new Product(5L, "Headphones", 89.99));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Product findById(Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}