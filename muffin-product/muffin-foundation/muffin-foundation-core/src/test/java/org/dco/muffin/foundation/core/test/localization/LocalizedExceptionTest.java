package org.dco.muffin.foundation.core.test.localization;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.dco.muffin.foundation.core.test.CoreTest;
import org.dco.muffin.foundation.core.test.localization.MyException.ReasonWithCodeMethod;
import org.dco.muffin.foundation.core.test.localization.MyException.ReasonWithPrefixMethod;
import org.dco.muffin.foundation.core.test.localization.MyException.ReasonWithoutAnyMethod;
import org.junit.jupiter.api.Test;

class LocalizedExceptionTest extends CoreTest {

  @Test
  void one_with_cause_and_locale_is_localized() {
    var e = new MyException(ReasonWithPrefixMethod.ONE, Locale.FRENCH, new NullPointerException(), "test");
    assertThat(e.getApplication()).isEqualTo("MUFFIN");
    assertThat(e.getArguments()).contains("test");
    assertThat(e.getCause()).isInstanceOf(NullPointerException.class);
    assertThat(e.getCode()).isEqualTo("muffin.test.one");
    assertThat(e.getComponent()).isEqualTo("TEST");
    assertThat(e.getErrno()).isEqualTo(1000);
    assertThat(e.getLocale()).isEqualTo(Locale.FRENCH);
    assertThat(e.getLocalizedMessage()).isEqualTo("Première exception [test]");
    assertThat(e.getMessage()).isEqualTo("MUFFIN|CORE|TEST|1000#Première exception [test]");
    assertThat(e.getModule()).isEqualTo("CORE");
    ReasonWithPrefixMethod r = e.getReason();
    assertThat(r).isEqualTo(ReasonWithPrefixMethod.ONE);
  }

  @Test
  void one_without_cause_and_locale_is_localized() {
    var e = new MyException(ReasonWithPrefixMethod.ONE, Locale.FRENCH, "test");
    assertThat(e.getApplication()).isEqualTo("MUFFIN");
    assertThat(e.getArguments()).contains("test");
    assertThat(e.getCode()).isEqualTo("muffin.test.one");
    assertThat(e.getComponent()).isEqualTo("TEST");
    assertThat(e.getErrno()).isEqualTo(1000);
    assertThat(e.getLocale()).isEqualTo(Locale.FRENCH);
    assertThat(e.getLocalizedMessage()).isEqualTo("Première exception [test]");
    assertThat(e.getMessage()).isEqualTo("MUFFIN|CORE|TEST|1000#Première exception [test]");
  }

  @Test
  void one_without_cause_and_without_locale_is_localized() {
    var e = new MyException(ReasonWithPrefixMethod.ONE, "test");
    assertThat(e.getApplication()).isEqualTo("MUFFIN");
    assertThat(e.getArguments()).contains("test");
    assertThat(e.getCode()).isEqualTo("muffin.test.one");
    assertThat(e.getComponent()).isEqualTo("TEST");
    assertThat(e.getErrno()).isEqualTo(1000);
    assertThat(e.getLocale()).isEqualTo(Locale.FRENCH);
    assertThat(e.getLocalizedMessage()).isEqualTo("Première exception [test]");
    assertThat(e.getMessage()).isEqualTo("MUFFIN|CORE|TEST|1000#Première exception [test]");
  }

  @Test
  void two_is_localized_with_errno_negative() {
    var e = new MyException(ReasonWithPrefixMethod.TWO);
    assertThat(e.getErrno()).isEqualTo(-1);

  }

  @Test
  void three_is_localized_with_unknow_component() {
    var e = new MyException(ReasonWithPrefixMethod.THREE);
    assertThat(e.getErrno()).isEqualTo(-1);
    assertThat(e.getComponent()).isEqualTo("UNK");
  }

  @Test
  void four_is_localized_with_unknow_module() {
    var e = new MyException(ReasonWithPrefixMethod.FOUR);
    assertThat(e.getErrno()).isEqualTo(-1);
    assertThat(e.getModule()).isEqualTo("UNK");
  }

  @Test
  void when_code_is_not_resolved_then_everything_are_unknown() {
    var e = new MyException(ReasonWithPrefixMethod.BAD);
    assertThat(e.getApplication()).isEqualTo("UNK");
    assertThat(e.getModule()).isEqualTo("UNK");
    assertThat(e.getComponent()).isEqualTo("UNK");
  }

  @Test
  void when_code_method_is_defined_then_use_it() {
    var e = new MyException(ReasonWithCodeMethod.ONE);
    assertThat(e.getCode()).isEqualTo("muffin.test.one");
  }

  @Test
  void when_prefix_methid_is_defined_then_use_it() {
    var e = new MyException(ReasonWithoutAnyMethod.ONE);
    assertThat(e.getCode()).isEqualTo("muffin.foundation.core.test.localization.one");
  }

}
