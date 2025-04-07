package org.dco.muffin.foundation.core.test.configuration;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.util.Locale;

import org.dco.muffin.foundation.core.configuration.CoreProperties;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class CorePropertiesTest extends CoreTest {

  @Autowired
  private CoreProperties coreProperties;

  @Test
  void core_properties_are_loaded() {
    assertThat(coreProperties).isNotNull();
    var localizationProperties = coreProperties.getLocalization();
    assertThat(localizationProperties.getCacheMillis()).isEqualTo(100);
    assertThat(localizationProperties.getBasenames()).contains("classpath:i18n/messages",
                                                               "classpath:i18n/exceptions",
                                                               "classpath:i18n/exceptions-core",
                                                               "classpath:exceptions-test",
                                                               "classpath:i18n/validations",
                                                               "classpath:i18n/emergencies",
                                                               "classpath:i18n/site",
                                                               "classpath:validations-test");
    assertThat(localizationProperties.getCommonMessages()).containsEntry("common", "Commun");
    assertThat(localizationProperties.getDefaultEncoding()).isEqualTo(StandardCharsets.UTF_8);
    assertThat(localizationProperties.getDefaultLocale()).isEqualTo(Locale.ENGLISH);
    assertThat(localizationProperties.getFileExtensions()).contains(".properties", ".props");
    assertThat(localizationProperties.isFallbackToSystem()).isTrue();
    assertThat(localizationProperties.isFallbackToDefault()).isTrue();
    assertThat(localizationProperties.isUseCodeAsMessage()).isTrue();
  }

}
