package org.dco.muffin.foundation.core.io;

import org.dco.muffin.foundation.core.configuration.CoreProperties;

/**
 * All reason of error during input/output operation
 */
public enum InputOutputReason {
  PATH_TYPE_UNSUPPORTED,
  PATH_INVALID_TYPE,
  DIRECTORY_NOT_FOUND,
  DIRECTORY_CREATE_ERROR,
  DIRECTORY_MOVE_ERROR,
  DIRECTORY_COPY_ERROR,
  DIRECTORY_DELETE_ERROR;

  /**
   * @return Prefix for code of exception
   */
  public String prefix() {
    return CoreProperties.CORE_PREFIX + ".io";
  }

}
