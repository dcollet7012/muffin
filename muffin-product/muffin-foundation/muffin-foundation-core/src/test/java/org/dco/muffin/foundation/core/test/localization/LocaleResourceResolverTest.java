package org.dco.muffin.foundation.core.test.localization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Locale;

import org.dco.muffin.foundation.core.localization.LocaleResourceResolver;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;


/**
 * Test the locale resource resolver
 */
class LocaleResourceResolverTest {
  private static LocaleResourceResolver localeResourceResolver = LocaleResourceResolver.builder()
                                                                                       .basenames(Arrays.asList("classpath:exceptions-test",
                                                                                                                "/tmp/test_fr.props"))
                                                                                       .extensions(Arrays.asList(".properties"))
                                                                                       .build();


  @Test
  void when_locale_resource_exists_then_resolution_is_present() {
    assertThat(localeResourceResolver.resolve(Locale.FRENCH)).contains(new ClassPathResource("exceptions-test_fr.properties"));
  }

  @Test
  void when_locale_resource_not_found_then_resolution_is_empty() {
    assertThat(localeResourceResolver.resolve(Locale.CHINESE)).isEmpty();
  }

  @Test
  void resolver_builder_has_to_string_well_implemented() {
    assertThat(LocaleResourceResolver.builder().toString()).isNotBlank();
  }

}
