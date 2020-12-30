package com.samples.products.exception;

import static org.apache.logging.log4j.util.Strings.EMPTY;
import static org.springframework.util.ObjectUtils.isEmpty;

import java.util.Map;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class GlobalExceptionHandler extends DefaultErrorAttributes {

  /**
   * This override is to enrich the error response with actual error message.
   */
  @Override
  public Map<String, Object> getErrorAttributes(WebRequest webRequest,
      ErrorAttributeOptions options) {
    Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
    Throwable throwable = super.getError(webRequest);
    errorAttributes
        .put("message", !isEmpty(throwable) ? (!isEmpty(throwable.getCause()) ? throwable.getCause()
            .getLocalizedMessage().split(":")[0] : throwable.getLocalizedMessage().split(":")[0])
            : EMPTY);
    return errorAttributes;
  }


}
