package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
public class Cart {
    private final ProductRepository productRepository;
    private final List<Product> items = new ArrayList<>();

    @Autowired
    public Cart(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public void addItem(Long productId) {
        Product product = productRepository.findById(productId);
        if (product != null) {
            items.add(product);
            System.out.println("Added to cart: " + product.getName());
        } else {
            System.out.println("Product with ID " + productId + " not found.");
        }
    }

    public List<Product> getItems() {
        return new ArrayList<>(items);
    }

    @Override
    public String toString() {
        return "Cart{items=" + items + "}";
    }
}