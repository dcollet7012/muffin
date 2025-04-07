package org.dco.muffin.foundation.core.measurement;

import org.dco.muffin.foundation.core.configuration.CoreProperties;
import org.dco.muffin.foundation.core.localization.LocalizedException;

/**
 * Exception on error in measurement
 */
public class MeasurementException extends LocalizedException {
  private static final long serialVersionUID = 1L;

  /**
   * Reason of error in measurement
   */
  public enum MeasurementReason {
    UNIT_OF_SIZE_INVALID,
    SIZE_PARSE_ERROR;

    /**
     * @return The prefix
     */
    public String prefix() {
      return CoreProperties.CORE_PREFIX + ".measurement";
    }

  }

  public MeasurementException(MeasurementReason reason, Object... arguments) {
    super(reason, arguments);
  }

}
