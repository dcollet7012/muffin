package org.dco.muffin.foundation.core.localization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A exception with here location
 */
@Getter
@Slf4j
public abstract class LocalizedException extends RuntimeException {
  private static final String DCO_PACKAGE_NAME = "org.dco.";
  private static final String UNKNOWN = "UNK";
  private static final int DEFAULT_ERRNO = -1;

  private static final String PREFIX_METHOD = "prefix";
  private static final String CODE_METHOD = "code";
  private static final long serialVersionUID = 1L;
  private final Enum<?> reason;
  private final Locale locale;
  private final String code;
  private final transient List<Object> arguments;
  private final String application;
  private final String module;
  private final String component;
  private final int errno;
  private final String localizedMessage;

  /**
   * @param reason - The reason
   * @return The code from reason
   */
  private static String reasonToCode(Enum<?> reason) {
    try {
      var codeMethod = reason.getClass().getDeclaredMethod(CODE_METHOD);
      var code = (String) codeMethod.invoke(reason);
      log.debug("Reason [{}] has code [{}] from method", reason, code);
      return code;
    }
    catch (Exception e) {
      log.trace("No code method on reason class [{}]", reason.getClass(), e);
    }

    String prefix = null;

    try {
      var prefixMethod = reason.getClass().getDeclaredMethod(PREFIX_METHOD);
      prefix = (String) prefixMethod.invoke(reason);
      log.debug("Reason [{}] has prefix [{}] from method", reason, prefix);
    }
    catch (Exception e) {
      log.trace("No prefix method on reason class [{}]", reason.getClass(), e);
      var packageName = reason.getClass().getPackageName();
      prefix = packageName.replace(DCO_PACKAGE_NAME, "");
    }

    var code = prefix + '.' + reason.name()
                                    .toLowerCase(Locale.FRENCH)
                                    .replace('_', '-');

    log.debug("Reason [{}] has code [{}]", reason, code);

    return code;
  }

  /**
   * @param reason - The reason
   * @param locale - The locale
   * @param arguments - The arguments
   * 
   * @return The message from reason
   */
  private static String reasonToMessage(Enum<?> reason, Locale locale, Object... arguments) {
    return LocaleMessageSourceProvider.messageSource()
                                      .getMessage(reasonToCode(reason), arguments, locale);
  }

  /**
   * Construct it with locale and cause
   * @param reason - The reason
   * @param locale - The locale
   * @param cause - The cause
   * @param arguments - The arguments
   */
  protected LocalizedException(Enum<?> reason, Locale locale, Throwable cause, Object... arguments) {
    super(reasonToMessage(reason, locale, arguments), cause);

    this.reason = reason;
    this.locale = locale;
    this.code = reasonToCode(reason);
    this.arguments = new ArrayList<>(Arrays.asList(arguments));

    var message  = getMessage();
    var diesePos = message.indexOf('#');

    if (diesePos == -1) {
      application = UNKNOWN;
      module = UNKNOWN;
      component = UNKNOWN;
      errno = DEFAULT_ERRNO;
      localizedMessage = message;
    }
    else {
      localizedMessage = message.substring(diesePos + 1);
      var tokens = message.substring(0, diesePos).split("\\|");
      application = tokens[0];
      module = tokens.length > 1 ? tokens[1] : UNKNOWN;
      component = tokens.length > 2 ? tokens[2] : UNKNOWN;
      errno = tokens.length > 3 ? Integer.parseInt(tokens[3]) : DEFAULT_ERRNO;
    }
  }

  /**
   * Construct it with locale
   * @param reason - The reason
   * @param locale - The locale
   * @param arguments - The arguments
   */
  protected LocalizedException(Enum<?> reason, Locale locale, Object... arguments) {
    this(reason, locale, null, arguments);
  }

  /**
   * Construct it with cause use default locale
   * 
   * @param reason - The reason
   * @param cause - The cause
   * @param arguments - The arguments
   */
  protected LocalizedException(Enum<?> reason, Throwable cause, Object... arguments) {
    this(reason, LocaleContextHolder.getLocale(), cause, arguments);
  }

  /**
   * Construct it without cause use default locale
   * 
   * @param reason - The reason
   * @param arguments - The arguments
   */
  protected LocalizedException(Enum<?> reason, Object... arguments) {
    this(reason, (Throwable) null, arguments);
  }

  /**
   * @param <R> - Type of reason
   * @return The reason
   */
  @SuppressWarnings("unchecked")
  public <R extends Enum<R>> R getReason() {
    return (R) reason;
  }

}
