package org.dco.muffin.foundation.core.test.localization;

import java.util.Locale;

import org.dco.muffin.foundation.core.localization.LocalizedException;

/**
 * A localized exception for test
 */
public class MyException extends LocalizedException {
  private static final long serialVersionUID = 1L;

  public enum ReasonWithPrefixMethod {
    ONE,
    TWO,
    THREE,
    FOUR,
    BAD;

    public String prefix() {
      return "muffin.test";
    }
  }

  public enum ReasonWithoutAnyMethod {
    ONE,
    TWO,
    THREE,
    FOUR,
    BAD;
  }

  public enum ReasonWithCodeMethod {
    ONE,
    TWO,
    THREE,
    FOUR,
    BAD;

    public String code() {
      return "muffin.test." + name().toLowerCase(Locale.FRENCH);
    }
  }



  public MyException(ReasonWithPrefixMethod reason, Locale locale, Throwable cause, Object... arguments) {
    super(reason, locale, cause, arguments);
  }

  public MyException(ReasonWithPrefixMethod reason, Locale locale, Object... arguments) {
    super(reason, locale, arguments);
  }

  public MyException(ReasonWithPrefixMethod reason, Throwable cause, Object... arguments) {
    super(reason, cause, arguments);
  }

  public MyException(ReasonWithPrefixMethod reason, Object... arguments) {
    super(reason, arguments);
  }

  public MyException(ReasonWithoutAnyMethod reason, Object... arguments) {
    super(reason, arguments);
  }

  public MyException(ReasonWithCodeMethod reason, Object... arguments) {
    super(reason, arguments);
  }


}
