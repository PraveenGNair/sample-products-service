package com.samples.products.exception;

/**
 * This interface exists solely for the purpose of swagger documentation.
 */

public interface MicroserviceError {

  long getTimestamp();

  int getStatus();

  String getError();

  String getMessage();

  String getPath();

}
