package org.dco.muffin.foundation.core.test.localization;



import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Locale;

import org.dco.muffin.foundation.core.configuration.CoreProperties;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;

class LocaleMessageSourceTest extends CoreTest {

  @Autowired
  private MessageSource messageSource;

  @Autowired
  private CoreProperties coreProperties;

  @BeforeAll
  public static void setUpBeforeAll() {
    Locale.setDefault(Locale.FRENCH);
  }

  @Test
  void welcome_in_french_is_bienvenue() {
    assertThat(messageSource.getMessage("welcome", new Object[] {"Monsieur", "Denis", "COLLET"}, Locale.FRENCH))
              .isEqualTo("Bienvenue chez Muffin, Monsieur Denis COLLET");

    coreProperties.getLocalization().setCacheMillis(-1);

    // Second time use cache
    assertThat(messageSource.getMessage("welcome", new Object[] {"Monsieur", "Denis", "COLLET"}, Locale.FRENCH))
               .isEqualTo("Bienvenue chez Muffin, Monsieur Denis COLLET");

    coreProperties.getLocalization().setCacheMillis(100);

    // Don't use default
    assertThat(messageSource.getMessage("welcome", new Object[] {"Monsieur", "Denis", "COLLET"}, "test", Locale.FRENCH))
              .isEqualTo("Bienvenue chez Muffin, Monsieur Denis COLLET");

  }

  @Test
  void welcome_in_chines_is_welcome_cause_fallback_to_default() {
    assertThat(messageSource.getMessage("welcome", new Object[] {"Mister", "Denis", "COLLET"}, Locale.CHINESE))
              .isEqualTo("Welcome to Muffin, Mister Denis COLLET");
  }

  @Test
  void bye_in_chines_is_au_revoir_cause_fallback_to_system() {
    assertThat(messageSource.getMessage("bye", new Object[] {"Monsieur", "Denis", "COLLET"}, Locale.CHINESE))
              .isEqualTo("Au revoir");
  }

  @Test
  void common_in_french_is_commun_from_cache() {
    assertThat(messageSource.getMessage("common", null, Locale.FRENCH))
              .isEqualTo("Commun");
  }

  @Test
  void hello_in_french_is_Bonjour__by_default() {
    assertThat(messageSource.getMessage("hello", null, "Bonjour", Locale.FRENCH))
              .isEqualTo("Bonjour");
  }

  @Test
  void hello_in_french_is_hello_or_raise_error() {
    assertThat(messageSource.getMessage("hello", null, Locale.FRENCH))
              .isEqualTo("hello");
    assertThat(messageSource.getMessage("hello", null, "", Locale.FRENCH))
              .isEqualTo("hello");

    coreProperties.getLocalization().setUseCodeAsMessage(false);
    assertThatThrownBy(() -> messageSource.getMessage("hello", null, Locale.FRENCH)).isInstanceOf(NoSuchMessageException.class);
    coreProperties.getLocalization().setUseCodeAsMessage(true);
  }

  @Test
  void welcome_in_french_is_bienvenue_from_resolvable() {
    var r = new MessageSourceResolvable() {

      @Override
      public String[] getCodes() {
        return new String[] {"welcome"};
      }

      @Override
      public Object[] getArguments() {
        return new Object[] {"Monsieur", "Denis", "COLLET"};
      }
    };

    assertThat(messageSource.getMessage(r, Locale.FRENCH))
              .isEqualTo("Bienvenue chez Muffin, Monsieur Denis COLLET");

  }

  @Test
  void hello_in_french_is_bonjour_from_default_in_resolvable() {
    var r = new MessageSourceResolvable() {

      @Override
      public String getDefaultMessage() {
        return "Bonjour";
      }

      @Override
      public String[] getCodes() {
        return new String[] {"h"};
      }
    };

    assertThat(messageSource.getMessage(r, Locale.FRENCH)).isEqualTo("Bonjour");
  }

  @Test
  void when_no_code_then_is_bonjour_from_default_in_resolvable() {
    var r = new MessageSourceResolvable() {

      @Override
      public String getDefaultMessage() {
        return "Bonjour";
      }

      @Override
      public String[] getCodes() {
        return null;
      }
    };

    assertThat(messageSource.getMessage(r, Locale.FRENCH)).isEqualTo("Bonjour");
  }

  @Test
  void when_code_is_not_resolved_from_resolvable_return_last_code() {
    var r = new MessageSourceResolvable() {

      @Override
      public String[] getCodes() {
        return new String[] {"hello"};
      }
    };

    assertThat(messageSource.getMessage(r, Locale.FRENCH)).isEqualTo("hello");
  }

  @Test
  void when_no_code_and_no_default_return_null() {
    var r = new MessageSourceResolvable() {

      @Override
      public String[] getCodes() {
        return null;
      }
    };

    assertThat(messageSource.getMessage(r, Locale.FRENCH)).isNull();
  }

}
