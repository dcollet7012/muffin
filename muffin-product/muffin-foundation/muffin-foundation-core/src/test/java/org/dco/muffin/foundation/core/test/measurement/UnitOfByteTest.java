package org.dco.muffin.foundation.core.test.measurement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.dco.muffin.foundation.core.measurement.MeasurementException;
import org.dco.muffin.foundation.core.measurement.UnitOfByte;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.junit.jupiter.api.Test;

/*
 * Test the unit of size
 */
class UnitOfByteTest extends CoreTest {

  @Test
  void kilo_as_pound_of_1024_and_symbol_k() {
    assertThat(UnitOfByte.KILO_BYTES.pound()).isEqualTo(1024D);
    assertThat(UnitOfByte.KILO_BYTES.symbol()).isEqualTo('k');
  }

  @Test
  void when_value_is_empty_use_bytes_as_default() {
    assertThat(UnitOfByte.of("")).isEqualTo(UnitOfByte.BYTES);
  }

  @Test
  void z_is_no_valid_uos_symbol() {
    assertThatThrownBy(() -> UnitOfByte.of("z")).isInstanceOf(MeasurementException.class);
  }

}
