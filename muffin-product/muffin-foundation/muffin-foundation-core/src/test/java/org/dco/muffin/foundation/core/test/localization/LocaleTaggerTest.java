package org.dco.muffin.foundation.core.test.localization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.dco.muffin.foundation.core.localization.LocaleTagger;
import org.junit.jupiter.api.Test;


/**
 * Test the locale tag resolver
 */
class LocaleTaggerTest {

  @Test
  void when_locale_has_variant_then_tags_contains_3_elements() {
    var locale = new Locale("fr", "FR", "UTF-8");
    var localeTagger = new LocaleTagger(locale);
    assertThat(localeTagger.getLocale()).isEqualTo(locale);
    assertThat(localeTagger.getTags()).containsExactly("fr_FR_UTF-8", "fr_FR", "fr");
  }

  @Test
  void when_locale_has_country_without_variant_then_tags_contains_2_elements() {
    var locale = new Locale("fr", "FR");
    var localeTagger = new LocaleTagger(locale);
    assertThat(localeTagger.getLocale()).isEqualTo(locale);
    assertThat(localeTagger.getTags()).containsExactly("fr_FR", "fr");
  }

  @Test
  void when_locale_has_only_language_then_tags_contains_1_elements() {
    var locale = new Locale("fr");
    var localeTagger = new LocaleTagger(locale);
    assertThat(localeTagger.getLocale()).isEqualTo(locale);
    assertThat(localeTagger.getTags()).containsExactly("fr");
  }

}
