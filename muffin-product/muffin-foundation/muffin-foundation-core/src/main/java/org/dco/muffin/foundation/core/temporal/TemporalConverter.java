package org.dco.muffin.foundation.core.temporal;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Conversion of temporal unit
 */
public final class TemporalConverter {

  /**
   * Hide the constructor
   */
  private TemporalConverter() {
    throw new UnsupportedOperationException("Can't instanciate epoch converter");
  }

  /**
   * @param epochMillis - The epoch in millis
   * 
   * @return The local date time of epoch in millis
   */
  public static LocalDateTime localeDateTime(long epochMillis) {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMillis), ZoneId.systemDefault());
  }

}
