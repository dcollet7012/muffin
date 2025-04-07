package org.dco.muffin.foundation.core.test.localization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;

import org.dco.muffin.foundation.core.localization.LocaleMessageSourceProvider;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.junit.jupiter.api.Test;

class LocaleMessageSourceProviderTest extends CoreTest {


  @Test
  void provider_is_initialized() {
    assertThat(LocaleMessageSourceProvider.messageSource()).isNotNull();
  }

  @Test
  void cant_instancite_the_provider() throws Exception {
    var c = LocaleMessageSourceProvider.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertThatThrownBy(() -> c.newInstance()).isInstanceOf(InvocationTargetException.class);
  }
}
