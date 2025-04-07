package org.dco.muffin.foundation.core.measurement;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import org.dco.muffin.foundation.core.measurement.MeasurementException.MeasurementReason;
import org.springframework.util.StringUtils;

/**
 * Unit use for size of file, memory as multiple in bytes
 */
public enum UnitOfByte {
  BYTES,
  KILO_BYTES,
  MEGA_BYTE,
  GIGA_BYTE,
  TERA_BYTE;

  /**
   * @return The symbol of unit
   */
  public char symbol() {
    return name().toLowerCase(Locale.FRENCH).charAt(0);
  }

  /**
   * @return All symbols supported
   */
  public static String symbols() {
    return Arrays.stream(values())
                 .map(u -> "" + u.symbol())
                 .collect(Collectors.joining(","));
  }

  /**
   * @return The pound in bytes
   */
  public double pound() {
    return Math.pow(1024D, ordinal());
  }

  /**
   * @param value è The value
   * @return The size
   */
  public static UnitOfByte of(String value) {
    if (!StringUtils.hasText(value)) {
      return BYTES;
    }

    var symbol = value.trim().toLowerCase(Locale.FRENCH).charAt(0);

    return Arrays.stream(values())
                 .filter(u -> u.symbol() == symbol)
                 .findFirst()
                 .orElseThrow(() ->  new MeasurementException(MeasurementReason.UNIT_OF_SIZE_INVALID, value, symbols()));
  }

}
