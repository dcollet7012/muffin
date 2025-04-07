package org.dco.muffin.foundation.core.measurement;

import java.util.regex.Pattern;

import org.dco.muffin.foundation.core.measurement.MeasurementException.MeasurementReason;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Measurement of size in byte
 */
@RequiredArgsConstructor
@Getter
public class SizeInByte {
  private static final Pattern PATTERN = Pattern.compile("(^-?\\d+\\.*\\d+?)(.*)");
  private final double quantity;
  private final UnitOfByte unit;

  /**
   * The zero
   */
  public static final SizeInByte ZERO = new SizeInByte(0, UnitOfByte.BYTES);

  /**
   * @param value - The value
   * @return The measurement by byte
   */
  @JsonCreator
  public static SizeInByte of(String value) {
    if (!StringUtils.hasText(value)) {
      return ZERO;
    }

    value = value.trim();

    var matcher = PATTERN.matcher(value);

    if (!matcher.matches()) {
      throw new MeasurementException(MeasurementReason.SIZE_PARSE_ERROR, value);
    }

    var quantity = Double.parseDouble(matcher.group(1));
    var unit = UnitOfByte.of(matcher.group(2));

    return new SizeInByte(quantity, unit);
  }

  /**
   * @return The size in bytes
   */
  public double bytes() {
    return quantity * unit.pound();
  }
}
