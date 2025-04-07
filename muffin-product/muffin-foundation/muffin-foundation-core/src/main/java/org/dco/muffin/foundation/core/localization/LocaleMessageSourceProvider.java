package org.dco.muffin.foundation.core.localization;

import org.springframework.context.MessageSource;

/**
 * The provider of message source
 */
public final class LocaleMessageSourceProvider {
  private static MessageSource messageSource;

  /**
   * Hide the constructor
   */
  private LocaleMessageSourceProvider() {
    throw new UnsupportedOperationException();
  }

  /**
   * @param messageSource - The message source
   */
  protected static synchronized void initialize(MessageSource messageSource) {
    LocaleMessageSourceProvider.messageSource = messageSource;
  }

  /**
   * @return The message source
   */
  public static synchronized MessageSource messageSource() {
    return messageSource;
  }

}
