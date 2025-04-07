package org.dco.muffin.foundation.core.configuration;

import org.dco.muffin.foundation.core.CoreRoot;
import org.dco.muffin.foundation.core.validation.ValidationMessageInterpolator;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.Validator;

/**
 * Configuration of core.
 */
@EnableAutoConfiguration
@Configuration
@ComponentScan(basePackageClasses = CoreRoot.class)
public class CoreConfiguration {

  /**
   * @param messageInterpolator - Interpolator
   * @return The validator
   */
  @Bean
  @Primary
  Validator validator(ValidationMessageInterpolator messageInterpolator) {
    try (var validator = new LocalValidatorFactoryBean()) {
      validator.setMessageInterpolator(messageInterpolator);
      return validator;
    }
  }

}
