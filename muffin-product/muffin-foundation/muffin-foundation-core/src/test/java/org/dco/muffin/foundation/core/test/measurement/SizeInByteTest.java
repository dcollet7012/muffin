package org.dco.muffin.foundation.core.test.measurement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.dco.muffin.foundation.core.measurement.SizeInByte;
import org.dco.muffin.foundation.core.measurement.UnitOfByte;
import org.dco.muffin.foundation.core.measurement.MeasurementException;
import org.dco.muffin.foundation.core.measurement.MeasurementException.MeasurementReason;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.junit.jupiter.api.Test;

class SizeInByteTest extends CoreTest {

  @Test
  void two_kilos_is_2048_bytes() {
    var s = new SizeInByte(2, UnitOfByte.KILO_BYTES);
    assertThat(s.getQuantity()).isEqualTo(2D);
    assertThat(s.getUnit()).isEqualTo(UnitOfByte.KILO_BYTES);
    assertThat(s.bytes()).isEqualTo(2048D);
  }

  @Test
  void when_value_is_empty_use_zero() {
    assertThat(SizeInByte.of(null)).isEqualTo(SizeInByte.ZERO);
  }

  @Test
  void when_value_is_minus_2k_then_size_in_bytes_is_minus_2048() {
    var s = SizeInByte.of("-2.1k");
    assertThat(s.getQuantity()).isEqualTo(-2.1D);
    assertThat(s.getUnit()).isEqualTo(UnitOfByte.KILO_BYTES);
    assertThat(s.bytes()).isEqualTo(-2150.4D);
  }

  @Test
  void toto_is_not_a_valid_byte_size() {
    assertThatThrownBy(() -> SizeInByte.of("toto")).isInstanceOf(MeasurementException.class)
                                                 .hasFieldOrPropertyWithValue("reason", MeasurementReason.SIZE_PARSE_ERROR);
  }

}
