package com.shoppingmicroserviceapi.product.productRepository;

import com.shoppingmicroserviceapi.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long> {
}
