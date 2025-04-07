package org.dco.muffin.foundation.core.io;

import org.dco.muffin.foundation.core.localization.LocalizedException;

/**
 * All input/output exception
 */
public class InputOutputException extends LocalizedException {

  private static final long serialVersionUID = -6371575497059679478L;

  public InputOutputException(InputOutputReason reason, Throwable cause, Object... arguments) {
    super(reason, cause, arguments);
  }

  public InputOutputException(InputOutputReason reason, Object... arguments) {
    super(reason, arguments);
  }

}
