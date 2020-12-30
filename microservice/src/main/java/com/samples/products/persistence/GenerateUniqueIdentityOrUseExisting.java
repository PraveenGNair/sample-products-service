package com.samples.products.persistence;

import static org.springframework.util.ObjectUtils.isEmpty;

import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;

/**
 * This is a custom implementation for auto generate uuid as primary key or use existing if
 * provided.
 */
public class GenerateUniqueIdentityOrUseExisting extends UUIDGenerator {

  /**
   * Override generate method to create a new identifier if not already provided.
   *
   * @param session session.
   * @param object entity object.
   * @return {@link Serializable}
   */
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    Serializable id = session.getEntityPersister(null, object).getClassMetadata()
        .getIdentifier(object, session);
    return !isEmpty(id) ? id : super.generate(session, object);
  }
}
