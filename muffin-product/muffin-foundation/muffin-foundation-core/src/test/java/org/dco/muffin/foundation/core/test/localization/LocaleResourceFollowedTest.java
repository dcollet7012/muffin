package org.dco.muffin.foundation.core.test.localization;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.dco.muffin.foundation.core.localization.LocalePropertiesResourceFollowed;
import org.dco.muffin.foundation.core.localization.LocaleTextResourceFollowed;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

/**
 * Test a resource who contains properties and than can be followed
 */
class LocaleResourceFollowedTest {

  @Test
  void can_load_properties() {
    var r = new ClassPathResource("how.properties");
    var p = new LocalePropertiesResourceFollowed(r, StandardCharsets.UTF_8, 0);
    var c = p.content();
    assertThat(c.getProperty("one")).isEqualTo("One");
    assertThat(p.isDirty()).isFalse();
    c = p.content();
    assertThat(c.getProperty("two")).isEqualTo("Two");
    assertThat(p.resource()).isEqualTo(r);
    assertThat(p.lastLoaded()).isPositive();
  }

  @Test
  void when_resource_doesnt_exists_then_content_is_null() {
    var r = new ClassPathResource("bad.properties");
    var p = new LocalePropertiesResourceFollowed(r, StandardCharsets.UTF_8, 2);
    assertThat(p.content()).isNull();
    assertThat(p.lastModified()).isEqualTo(-1L);
  }

  @Test
  void when_cant_access_resource_then_reload_if_need_do_nothing() throws IOException {
    var r = mock(ClassPathResource.class);
    when(r.getInputStream()).thenThrow(new IOException());
    when(r.lastModified()).thenReturn(1000L);
    var p = new LocalePropertiesResourceFollowed(r, StandardCharsets.UTF_8, 2);
    assertThat(p.content()).isNull();
  }

  @Test
  void can_read_text_from_resource() {
    var r = new ClassPathResource("templates/test.tpl");
    var p = new LocaleTextResourceFollowed(r, StandardCharsets.UTF_8, 2);
    assertThat(p.content()).isNotBlank();
  }
}
