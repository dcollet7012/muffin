package org.dco.muffin.foundation.core.localization;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import lombok.Getter;

/**
 * Resolver of tags of locale
 */
@Getter
public class LocaleTagger {
  private final Locale locale;
  private final List<String> tags = new LinkedList<>();

  /**
   * Construct it
   * @param locale - The locale
   */
  public LocaleTagger(final Locale locale) {
    Assert.notNull(locale, "Locale required");
    this.locale = locale;

    var language = locale.getLanguage();
    var country = locale.getCountry();
    var variant = locale.getVariant();

    if (StringUtils.hasText(variant)) {
      tags.add(language + '_' + country + '_' + variant);
    }

    if (StringUtils.hasText(country)) {
      tags.add(language + '_' + country);
    }

    tags.add(language);
  }

}
