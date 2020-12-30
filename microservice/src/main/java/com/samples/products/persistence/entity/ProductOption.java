package com.samples.products.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Getter
@Setter
@Entity
@Table(name = "product_option")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class ProductOption {

  @Id
  @GenericGenerator(name = "GenerateUniqueIdentityOrUseExisting",
      strategy = "com.samples.products.persistence.GenerateUniqueIdentityOrUseExisting")
  @GeneratedValue(generator = "GenerateUniqueIdentityOrUseExisting")
  private UUID id;

  private String name;

  private String description;

  @Column(nullable = false)
  @JsonIgnore
  private UUID productId;

  @ManyToOne
  @JoinColumn(name = "productid", insertable = false, updatable = false)
  @JsonIgnore
  private Product product;

  /**
   * Constructor.
   */
  public ProductOption(String name, String description, UUID productId) {
    this.name = name;
    this.description = description;
    this.productId = productId;
  }
}
