package com.samples.products.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@Entity
@Table(name = "product")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Product implements Serializable {

  @Id
  @GenericGenerator(name = "GenerateUniqueIdentityOrUseExisting",
      strategy = "com.samples.products.persistence.GenerateUniqueIdentityOrUseExisting")
  @GeneratedValue(generator = "GenerateUniqueIdentityOrUseExisting")
  private UUID id;

  private String name;

  private String description;

  private BigDecimal price;

  private BigDecimal deliveryPrice;

  @OneToMany(mappedBy = "product")
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JsonIgnore
  private List<ProductOption> productOptions;

  /**
   * Constructor.
   */
  public Product(String name, String description, BigDecimal price,
      BigDecimal deliveryPrice) {
    this.name = name;
    this.description = description;
    this.price = price;
    this.deliveryPrice = deliveryPrice;
  }
}
