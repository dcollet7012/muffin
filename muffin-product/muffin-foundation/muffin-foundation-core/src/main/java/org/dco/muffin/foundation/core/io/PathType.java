package org.dco.muffin.foundation.core.io;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

/**
 * Type of path
 */
public enum PathType {
  /**
   * A directory
   */
  DIRECTORY,

  /**
   * A file
   */
  FILE,

  /**
   * A link
   */
  LINK,

  /**
   * Other ?
   */
  OTHER;

  /**
   * @return The symbol
   */
  public char symbol() {
    return name().toLowerCase(Locale.FRENCH).charAt(0);
  }

  /**
   * @return All symbols
   */
  public static String symbols() {
    return Arrays.stream(values())
                 .map(s -> "" + s.symbol())
                 .collect(Collectors.joining(", "));
  }

  /**
   * @param value - The value
   * @return The path type
   */
  public static PathType of(String value) {
    if (!StringUtils.hasText(value)) {
      throw new InputOutputException(InputOutputReason.PATH_TYPE_UNSUPPORTED, value, symbols());
    }

    var symbol = value.trim().toLowerCase(Locale.FRENCH).charAt(0);

    return Arrays.stream(values())
                 .filter(s -> s.symbol() == symbol)
                 .findFirst()
                 .orElseThrow(() -> new InputOutputException(InputOutputReason.PATH_TYPE_UNSUPPORTED, value, symbols()));
  }
}
