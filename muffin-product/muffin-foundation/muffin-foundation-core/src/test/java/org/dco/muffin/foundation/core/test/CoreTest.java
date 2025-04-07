package org.dco.muffin.foundation.core.test;

import java.util.Locale;

import org.dco.muffin.foundation.core.configuration.CoreConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Skeleton for test of core
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CoreConfiguration.class},
                      initializers = ConfigDataApplicationContextInitializer.class)
public abstract class CoreTest {

  @BeforeAll
  public static void setUpBeforeAll() {
    Locale.setDefault(Locale.FRENCH);
  }
}
