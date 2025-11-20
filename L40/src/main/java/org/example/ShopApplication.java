package org.example;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "org.example")
public class ShopApplication {


    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(ShopApplication.class);
        ProductRepository productRepository = context.getBean("productRepository", ProductRepository.class);
        System.out.println("Available products:");
        List<Product> products = productRepository.getAllProducts();
        for (Product product : products) {
            System.out.println(product);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nStarting first shopping session:");
        Cart cart1 = context.getBean(Cart.class);
        System.out.println("Cart 1 hashcode: " + System.identityHashCode(cart1));
        System.out.println("Add products to cart (enter product ID or 0 to finish):");
        while (true) {
            System.out.print("Product ID: ");
            Long productId = scanner.nextLong();
            if (productId == 0) {
                break;
            }
            cart1.addItem(productId);
        }

        System.out.println("\nCart 1 contents: " + cart1.getItems());
        System.out.println("\nStarting second shopping session...");
        Cart cart2 = context.getBean(Cart.class);
        System.out.println("Cart 2 hashcode: " + System.identityHashCode(cart2));
        System.out.println("Add products to cart (enter product ID or 0 to finish):");
        while (true) {
            System.out.print("Product ID: ");
            Long productId = scanner.nextLong();
            if (productId == 0) {
                break;
            }
            cart2.addItem(productId);
        }

        System.out.println("\nCart 2 contents: " + cart2.getItems());
        System.out.println("\nVerifying different instances:");
        System.out.println("Cart 1 hashcode: " + System.identityHashCode(cart1));
        System.out.println("Cart 2 hashcode: " + System.identityHashCode(cart2));
        scanner.close();
        ((AnnotationConfigApplicationContext) context).close();
    }
}


