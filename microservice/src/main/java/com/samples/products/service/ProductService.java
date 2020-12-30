package com.samples.products.service;

import com.samples.products.model.ProductOptions;
import com.samples.products.model.Products;
import com.samples.products.exception.ProductNotFoundException;
import com.samples.products.persistence.ProductOptionRepository;
import com.samples.products.persistence.ProductRepository;
import com.samples.products.persistence.entity.Product;
import com.samples.products.persistence.entity.ProductOption;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;

  private final ProductOptionRepository productOptionRepository;

  /**
   * Fetch All Product Collection.
   *
   * @return {@link Products}
   */
  public Products findAllProducts() {
    return new Products(productRepository.findAll());
  }

  /**
   * Fetch Products by Name or any of its substring.
   *
   * @param name productName or like productName.
   * @return {@link Products}
   */
  public Products findProductsByName(String name) {
    return new Products(productRepository.findByNameIgnoreCaseContaining(name));
  }

  /**
   * Fetch a Product by its id.
   *
   * @param id productId.
   * @return {@link Product}
   */
  public Optional<Product> findProductById(UUID id) {
    return productRepository.findById(id);
  }

  /**
   * Saves a new product.
   *
   * @param product Product request body.
   * @return {@link Product}
   */
  public Product saveProduct(Product product) {
    return productRepository.save(product);
  }

  /**
   * Delete product by Id.
   *
   * @param id ProductId.
   */
  public void deleteProductById(UUID id) {
    productRepository.deleteById(id);
  }

  /**
   * Find All productOptions for a productId.
   *
   * @param productId productId.
   * @return {@link ProductOptions}
   */
  public ProductOptions findAllOptionsByProductId(UUID productId) {
    return new ProductOptions(productOptionRepository.findAllByProductId(productId));
  }

  /**
   * Fetch a productOption by Product Id and OptionId.
   *
   * @param id optionId.
   * @param productId productId.
   * @return {@link ProductOption}
   */
  public Optional<ProductOption> findProductOptionByIdAndProductId(UUID id, UUID productId) {
    return productOptionRepository.findProductOptionByIdAndProductId(id, productId);
  }

  /**
   * Save/update a productOption.
   *
   * @param productOption request Body.
   * @return {@link ProductOption}
   */
  public ProductOption saveProductOption(ProductOption productOption) {
    return productOptionRepository.save(productOption);
  }

  /**
   * Delete a productOption by productId and OptionId.
   *
   * @param id optionId.
   * @param productId productId.
   */
  public void deleteProductOptionById(UUID id, UUID productId) {
    productOptionRepository.deleteByIdAndProductId(id, productId);
  }

  /**
   * Verify if a product already Exists in database.
   *
   * @param productId Product Identifier UUID.
   */
  public void verifyProductExists(UUID productId) {
    if (!productRepository.existsById(productId)) {
      throw new ProductNotFoundException(
          String.format("Product with id %s was not found", productId));
    }

  }

  /**
   * Verify if a productOption already Exists in database.
   *
   * @param productId Product Identifier UUID.
   * @param optionId ProductOption Identifier UUID.
   */
  public void verifyProductOptionExists(UUID productId, UUID optionId) {
    if (!productOptionRepository.existsByIdAndProductId(optionId, productId)) {
      throw new ProductNotFoundException(
          String.format("Product with id %s and productOption id %s was not found", productId,
              optionId));
    }
  }
}
