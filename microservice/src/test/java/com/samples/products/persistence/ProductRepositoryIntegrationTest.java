package com.samples.products.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.samples.products.persistence.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProductRepository productRepository;

  @Test
  public void whenFindById_thenReturnProduct() {
    Product product = new Product("product 1", "Product One", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);

    Optional<Product> productFound = productRepository.findById(savedProduct.getId());
    assertTrue(productFound.isPresent());
    assertNotNull(productFound.get().getId());
    assertThat(productFound.get().getName(), is(equalTo(product.getName())));
  }

  @Test
  public void whenFindByName_thenReturnMatchingProducts() {
    Product product = new Product("product 2", "Product two", BigDecimal.ONE, BigDecimal.TEN);
    entityManager.persistAndFlush(product);

    List<Product> productFound = productRepository
        .findByNameIgnoreCaseContaining(product.getName());
    assertThat(productFound, hasSize(1));
    assertNotNull(productFound.iterator().next().getId());
    assertThat(productFound.iterator().next().getName(), is(equalTo(product.getName())));
    assertThat(productFound.iterator().next().getDescription(),
        is(equalTo(product.getDescription())));
    assertThat(productFound.iterator().next().getDeliveryPrice(),
        is(equalTo(product.getDeliveryPrice())));
    assertThat(productFound.iterator().next().getPrice(), is(equalTo(product.getPrice())));
  }

  @Test
  public void whenSave_thenReturnSavedProduct() {
    Product product = new Product("product 3", "Product three", BigDecimal.ONE, BigDecimal.TEN);

    Product productFound = productRepository.save(product);
    assertNotNull(productFound.getId());
    assertThat(productFound.getName(), is(equalTo(product.getName())));
    assertThat(productFound.getDescription(),
        is(equalTo(product.getDescription())));
    assertThat(productFound.getDeliveryPrice(),
        is(equalTo(product.getDeliveryPrice())));
    assertThat(productFound.getPrice(), is(equalTo(product.getPrice())));
  }

  @Test
  public void whenFindAll_thenReturnAllProducts() {
    Product product1 = new Product("product 1", "Product one", BigDecimal.ONE, BigDecimal.TEN);
    Product product2 = new Product("product 2", "Product two", BigDecimal.ONE, BigDecimal.TEN);
    entityManager.persistAndFlush(product1);
    entityManager.persistAndFlush(product2);

    List<Product> productsFound = productRepository.findAll();
    assertThat(productsFound, hasSize(2));
  }

  @Test
  public void whenDelete_thenDeleteProduct() {
    Product product = new Product("product 4", "Product four", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);

    productRepository.delete(savedProduct);
    assertNotNull(savedProduct.getId());
    Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
    assertFalse(deletedProduct.isPresent());
  }

  @Test
  public void whenExistsById_thenProductExists() {
    Product product = new Product("product 4", "Product four", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);

    boolean result = productRepository.existsById(savedProduct.getId());
    assertNotNull(savedProduct.getId());
    assertTrue(result);
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void whenDeleteRandomProduct_throwException() {
    UUID randomProductId = UUID.randomUUID();
    productRepository.deleteById(randomProductId);
  }

  @Test(expected = InvalidDataAccessApiUsageException.class)
  public void whenSaveInvalidProduct_throwException() {
    productRepository.save(null);
  }
}
