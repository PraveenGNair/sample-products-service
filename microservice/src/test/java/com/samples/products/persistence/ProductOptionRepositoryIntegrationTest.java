package com.samples.products.persistence;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.samples.products.persistence.entity.Product;
import com.samples.products.persistence.entity.ProductOption;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ProductOptionRepositoryIntegrationTest {

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private ProductOptionRepository productOptionRepository;

  @Test
  public void whenFindByProductId_thenReturnProductOptions() {
    Product product = new Product("product 1", "Product One", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);
    ProductOption productOption = new ProductOption("product option 1", "product option desc 1",
        savedProduct.getId());
    ProductOption savedProductOption = entityManager.persistAndFlush(productOption);

    List<ProductOption> productOptionFound = productOptionRepository
        .findAllByProductId(savedProduct.getId());
    assertThat(productOptionFound, hasSize(1));
    assertNotNull(productOptionFound.iterator().next().getId());
    assertThat(productOptionFound.iterator().next().getName(),
        is(equalTo(savedProductOption.getName())));
    assertThat(productOptionFound.iterator().next().getDescription(),
        is(equalTo(savedProductOption.getDescription())));
  }

  @Test
  public void whenFindByIdAndProductId_thenReturnProductOption() {
    Product product = new Product("product 2", "Product two", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);
    ProductOption productOption = new ProductOption("product option 2", "product option desc 2",
        savedProduct.getId());
    ProductOption savedProductOption = entityManager.persistAndFlush(productOption);

    Optional<ProductOption> productOptionFound = productOptionRepository
        .findProductOptionByIdAndProductId(savedProductOption.getId(), savedProduct.getId());
    assertTrue(productOptionFound.isPresent());
    assertNotNull(productOptionFound.get().getId());
    assertThat(productOptionFound.get().getName(), is(equalTo(productOption.getName())));
    assertThat(productOptionFound.get().getDescription(),
        is(equalTo(productOption.getDescription())));
  }

  @Test
  public void whenSave_thenReturnSavedProductOption() {
    Product product = new Product("product 2", "Product two", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);
    ProductOption productOption = new ProductOption("product option 2", "product option desc 2",
        savedProduct.getId());

    ProductOption productOptionFound = productOptionRepository.save(productOption);
    assertNotNull(productOptionFound.getId());
    assertThat(productOptionFound.getName(), is(equalTo(productOption.getName())));
    assertThat(productOptionFound.getDescription(),
        is(equalTo(productOption.getDescription())));
  }

  @Test
  public void whenDelete_thenDeleteProductOption() {
    Product product = new Product("product 3", "Product three", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);
    ProductOption productOption = new ProductOption("product option 3", "product option desc 3",
        savedProduct.getId());
    ProductOption savedProductOption = entityManager.persistAndFlush(productOption);

    productOptionRepository.delete(savedProductOption);
    assertNotNull(savedProduct.getId());
    assertNotNull(savedProductOption.getId());
    Optional<ProductOption> deletedProduct = productOptionRepository
        .findProductOptionByIdAndProductId(savedProductOption.getId(), savedProduct.getId());
    assertFalse(deletedProduct.isPresent());
  }

  @Test
  public void whenExistsByIdAndProduct_thenProductOptionExists() {
    Product product = new Product("product 4", "Product Four", BigDecimal.ONE, BigDecimal.TEN);
    Product savedProduct = entityManager.persistAndFlush(product);
    ProductOption productOption = new ProductOption("product option 4", "product option desc 4",
        savedProduct.getId());
    ProductOption savedProductOption = entityManager.persistAndFlush(productOption);

    boolean result = productOptionRepository
        .existsByIdAndProductId(savedProductOption.getId(), savedProduct.getId());
    assertNotNull(savedProduct.getId());
    assertNotNull(savedProductOption.getId());
    assertTrue(result);
  }

  @Test
  public void whenInvalidId_thenReturnEmpty() {
    Optional<ProductOption> productOption = productOptionRepository.findById(UUID.randomUUID());
    assertFalse(productOption.isPresent());
  }

  @Test(expected = EmptyResultDataAccessException.class)
  public void whenDeleteRandomProductOption_BadInput() {
    UUID randomProductId = UUID.randomUUID();
    productOptionRepository.deleteById(randomProductId);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void whenSaveInvalidProductOption_ProductIdMissing() {
    productOptionRepository.save(new ProductOption(null, null, null));
  }
}
