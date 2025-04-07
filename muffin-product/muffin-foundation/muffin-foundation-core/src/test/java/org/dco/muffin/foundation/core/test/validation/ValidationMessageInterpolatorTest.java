package org.dco.muffin.foundation.core.test.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;

import org.dco.muffin.foundation.core.templating.TinyTemplateFactory;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.dco.muffin.foundation.core.test.common.Party;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.MessageInterpolator;
import jakarta.validation.Validator;

class ValidationMessageInterpolatorTest extends CoreTest {

  @Autowired
  private Validator validator;

  @Autowired
  private MessageInterpolator interpolator;

  @Test
  void first_name_required_validation_message_in_french_is_prenom_requis() {
    var p = Party.builder()
                 .age(12)
                 .size(400)
                 .build();

    var v = validator.validate(p);

    var c = v.stream()
             .map(ConstraintViolation::getMessage)
             .toList();

    assertThat(c).contains("Prénom requis",
                           "12 ans n'est pas un age légal, 18 ans minimum requis",
                           "Trop grand");
  }

  @Test
  void when_locale_is_not_set_then_use_default() {
    assertThat(interpolator.interpolate("test", null)).isEqualTo("test");
  }

  @Test
  void cant_instanciate_a_tiny_template_factory() throws Exception {
    var c = TinyTemplateFactory.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertThatThrownBy(() -> c.newInstance()).isInstanceOf(InvocationTargetException.class);
  }

}
