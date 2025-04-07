package org.dco.muffin.foundation.core.test.temporal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.lang.reflect.InvocationTargetException;
import java.time.Month;
import java.util.Locale;

import org.dco.muffin.foundation.core.temporal.TemporalConverter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test the temporal concerter
 */
class TemporalConverterTest {

  @BeforeAll
  public static void setUpBeforeAll() {
    Locale.setDefault(Locale.FRANCE);
  }

  @Test
  void cant_instanciate_temporal_converter() throws Exception {
    var c = TemporalConverter.class.getDeclaredConstructor();
    c.setAccessible(true);
    assertThatThrownBy(() -> c.newInstance()).isInstanceOf(InvocationTargetException.class);
  }

  @Test
  void local_date_time_at_0_is_19700101_at_1hour() {
    var localeDateTime = TemporalConverter.localeDateTime(0L);
    assertThat(localeDateTime.getYear()).isEqualTo(1970);
    assertThat(localeDateTime.getMonth()).isEqualTo(Month.JANUARY);
    assertThat(localeDateTime.getDayOfMonth()).isEqualTo(1);
    assertThat(localeDateTime.getHour()).isEqualTo(1);
  }

}
