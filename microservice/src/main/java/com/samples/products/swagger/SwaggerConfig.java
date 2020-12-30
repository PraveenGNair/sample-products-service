package com.samples.products.swagger;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static springfox.documentation.builders.PathSelectors.regex;

import com.samples.products.exception.MicroserviceError;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Value("${swagger.application.basePath}")
  private String bastPath;


  /**
   * Basic Swagger configuration.
   *
   * @return {@link Docket}
   */
  @Bean
  public Docket productApi(@Value("${swagger.application.title}") String title,
      @Value("${swagger.application.description}") String description,
      @Value("${swagger.application.version}") String version,
      @Value("${swagger.application.contactTeam}") String name,
      @Value("${swagger.application.contactUrl}") String contactUrl,
      @Value("${swagger.application.contactEmail}") String contactEmail,
      @Value("${swagger.application.termsOfServiceUrl}") String termsOfService,
      @Value("${swagger.application.security.scheme}") String scheme) {

    ModelRef errorModel = new ModelRef(MicroserviceError.class.getSimpleName());
    List<ResponseMessage> responseMessages = asList(
        new ResponseMessageBuilder().code(400).message("Bad request.").responseModel(errorModel)
            .build(),
        new ResponseMessageBuilder().code(401).message("Unauthorized or Invalid Credentials.")
            .responseModel(errorModel)
            .build(),
        new ResponseMessageBuilder().code(404).message("No Products or ProductOptions Found.")
            .responseModel(errorModel)
            .build(),
        new ResponseMessageBuilder().code(405)
            .message("The requested resource does not support the supplied verb.")
            .responseModel(errorModel)
            .build(),
        new ResponseMessageBuilder().code(415)
            .message("API does not support the requested content type.")
            .responseModel(errorModel)
            .build(),
        new ResponseMessageBuilder().code(500)
            .message("An internal error occurred when processing the request.")
            .responseModel(errorModel)
            .build());

    return new Docket(DocumentationType.SWAGGER_2)
        .globalResponseMessage(RequestMethod.POST, responseMessages)
        .globalResponseMessage(RequestMethod.PUT, responseMessages)
        .globalResponseMessage(RequestMethod.GET, responseMessages)
        .globalResponseMessage(RequestMethod.DELETE, responseMessages)
        .useDefaultResponseMessages(false)
        .securitySchemes(basicScheme())
        .securityContexts(singletonList(securityContext()))
        .protocols(singleton(scheme))
        .apiInfo(new ApiInfoBuilder()
            .title(title)
            .description(description)
            .version(version)
            .contact(
                new Contact(name, contactUrl,
                    contactEmail))
            .termsOfServiceUrl(termsOfService)
            .build())
        .select()
        .paths(regex(bastPath))
        .build();
  }

  private List<SecurityScheme> basicScheme() {
    return asList(new BasicAuth("basicAuth"));
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(
            singletonList(new SecurityReference("basicAuth", new AuthorizationScope[]{})))
        .forPaths(regex(bastPath))
        .build();
  }
}
