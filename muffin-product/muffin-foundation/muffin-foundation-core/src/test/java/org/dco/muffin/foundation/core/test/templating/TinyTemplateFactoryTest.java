package org.dco.muffin.foundation.core.test.templating;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.dco.muffin.foundation.core.templating.TinyTemplate.TemplateToken;
import org.dco.muffin.foundation.core.templating.TinyTemplateFactory;
import org.junit.jupiter.api.Test;

class TinyTemplateFactoryTest {

  @Test
  void with_template_bonjour_first_name_last_name_then_format_is_like_bonjour_denis_collet() {
    var t = TinyTemplateFactory.get("Bonjour {{firstName}} {{lastName, string, %8s}}, vous êtes né le : {{birthDay, date, dd/MM/yyyy}} à {{when, date-time, HH:mm}}, vous pesez : {{pound, number, #.##}} m, bravo");

    Map<String, Object> m = new HashMap<>();

    m.put("firstName", "Denis");
    m.put("lastName", "COLLET");
    m.put("birthDay", LocalDate.of(1963, 8, 15));
    m.put("pound", 1.78D);
    m.put("when", LocalDateTime.of(1963, 8, 15, 15, 0, 0));

    var f = t.format(m);

    assertThat(f).isEqualTo("Bonjour Denis  COLLET, vous êtes né le : 15/08/1963 à 15:00, vous pesez : 1,78 m, bravo");
  }

  @Test
  void if_template_is_empty_parse_do_nothing_end_format_return_empty() {
    var t = TinyTemplateFactory.get("");
    Map<String, Object> m = new HashMap<>();
    assertThat(t.format(m)).isEmpty();
  }

  @Test
  void when_template_has_malformed_then_raise_error() {
    assertThatThrownBy(() -> TinyTemplateFactory.get("{{test")).isInstanceOf(IllegalArgumentException.class);
    assertThatThrownBy(() -> TinyTemplateFactory.get("{{}}")).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void when_value_is_null_then_use_empty() {
    var t = TinyTemplateFactory.get("{{test}}");
    Map<String, Object> m = new HashMap<>();
    assertThat(t.format(m)).isEmpty();
  }

  @Test
  void token_builder_has_to_string_well_implemented() {
    assertThat(TemplateToken.builder().toString()).isNotBlank();
  }

}
