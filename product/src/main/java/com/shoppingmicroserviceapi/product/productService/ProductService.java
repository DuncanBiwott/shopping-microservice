package com.shoppingmicroserviceapi.product.productService;

import com.shoppingmicroserviceapi.product.entity.Product;
import com.shoppingmicroserviceapi.product.exception.ProductServiceCustomException;
import com.shoppingmicroserviceapi.product.productReponse.ProductResponse;
import com.shoppingmicroserviceapi.product.productRepository.ProductRepository;
import com.shoppingmicroserviceapi.product.productRequest.ProductRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.springframework.beans.BeanUtils.copyProperties;

@Service
@Log4j2
public class ProductService {
    @Autowired
    private  ProductRepository productRepository;

    public long addProduct(ProductRequest productRequest) {
        log.info("addProduct is called");

        Product product
                = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        product = productRepository.save(product);

        log.info("Product Created");
        log.info("Product with " + product.getProductId()+ "created");
        return product.getProductId();
    }

    public ProductResponse getProductById(long productId) {

        log.info("getProductById is called");
        log.info("Get the product for productId: {}", productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(
                        () -> new ProductServiceCustomException("Product with given Id not found","PRODUCT_NOT_FOUND"));

        ProductResponse productResponse
                = new ProductResponse();

        copyProperties(product, productResponse);

        log.info("ProductResponse :" + productResponse.toString());

        return productResponse;
    }

    public void reduceQuantity(long productId, long quantity) {

        log.info("Reduce Quantity {} for Id: {}", quantity,productId);

        Product product
                = productRepository.findById(productId)
                .orElseThrow(() -> new ProductServiceCustomException(
                        "Product with given Id not found",
                        "PRODUCT_NOT_FOUND"
                ));

        if(product.getQuantity() < quantity) {
            throw new ProductServiceCustomException(
                    "Product does not have sufficient Quantity",
                    "INSUFFICIENT_QUANTITY"
            );
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepository.save(product);
        log.info("Product Quantity updated Successfully");
    }

    public void deleteProductById(long productId) {
        log.info("Product id: To be deleted{}", productId);

        if (!productRepository.existsById(productId)) {
            log.info("Im in this loop {}", !productRepository.existsById(productId));
            throw new ProductServiceCustomException(
                    "Product with given with Id: " + productId + " not found:",
                    "PRODUCT_NOT_FOUND");
        }
        log.info("Deleting Product with id: {}", productId);
        productRepository.deleteById(productId);

    }

}
