package org.dco.muffin.foundation.core.templating;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.dco.muffin.foundation.core.templating.TinyTemplate.TemplateToken.TokenType;
import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * A really simple template
 */
public class TinyTemplate {
  private static final String START_VARIABLE = "{{";
  private static final String END_VARIABLE = "}}";

  /**
   * A token in template
   */
  @RequiredArgsConstructor
  @Builder
  public static class TemplateToken {
    /**
     * Type of token supported
     */
    public enum TokenType {
      STRING,
      NUMBER,
      DATE,
      DATE_TIME;

      /**
       * @return The type
       */
      public String type() {
        return name().toLowerCase(Locale.FRENCH).replace('_', '-');
      }

      /**
       * @param value - The value
       * @return The type
       */
      public static TokenType of(String value) {
        if (!StringUtils.hasText(value)) {
          return STRING;
        }

        var type = value.toLowerCase(Locale.FRENCH).replace('_', '-');

        return Arrays.stream(values())
                     .filter(t -> t.type().equals(type))
                     .findFirst()
                     .orElse(STRING);
      }
    }

    private final String before;
    private final String name;
    private final TokenType type;
    private final String format;

    /**
     * @param value - Value of variable
     * @return The  variable value formatted
     */
    public String format(Object value) {
      var text = new StringBuilder();

      if (StringUtils.hasText(before)) {
        text.append(before);
      }

      if (!StringUtils.hasText(name)) {
        return text.toString();
      }

      if (value == null) {
        return text.toString();
      }

      if (!StringUtils.hasText(format)) {
        return text.append(value.toString()).toString();
      }

      switch (type) {
      case NUMBER: return text.append(new DecimalFormat(format).format(value)).toString();
      case DATE: return text.append(DateTimeFormatter.ofPattern(format).format((LocalDate) value)).toString();
      case DATE_TIME: return text.append(DateTimeFormatter.ofPattern(format).format((LocalDateTime) value)).toString();
      default: return text.append(String.format(format, (String) value)).toString();
      }
    }
  }

  private final String template;
  private final List<TemplateToken> tokens = new ArrayList<>();

  /**
   * @param template - The template
   */
  protected TinyTemplate(String template) {
    this.template = template;
    parse();
  }

  /**
   * Parse the template and populate tokens
   */
  private void parse() {
    if (!StringUtils.hasText(template)) {
      return;
    }

    var start = 0;
    var len = template.length();

    while (start < len) {
      var startVarPos = template.indexOf(START_VARIABLE, start);

      if (startVarPos == -1) {
        tokens.add(TemplateToken.builder()
                                .before(template.substring(start))
                                .build());
        return;
      }

      var endVarPos = template.indexOf(END_VARIABLE, startVarPos + START_VARIABLE.length());

      if (endVarPos == -1) {
        throw new IllegalArgumentException("End of variable expected after [" + startVarPos + "] on template [" + template + "]");
      }

      var variableToken = template.substring(startVarPos + START_VARIABLE.length(), endVarPos);

      if (!StringUtils.hasText(variableToken)) {
        throw new IllegalArgumentException("Variable token is empty");
      }

      var variableElements = variableToken.split(",");
      var variableName = variableElements[0].trim();
      var variableType = variableElements.length > 1 ? variableElements[1].trim() : null;
      var variableFormat = variableElements.length > 2 ? variableElements[2].trim() : null;
      var textBeforeVariable = template.substring(start, startVarPos);

      tokens.add(TemplateToken.builder()
                              .before(textBeforeVariable)
                              .name(variableName)
                              .type(TokenType.of(variableType))
                              .format(variableFormat)
                              .build());

      start = endVarPos + END_VARIABLE.length();
     }
  }

  /**
   * @param variables - Map of variable value
   * @return The text formatted
   */
  public String format(Map<String, Object> variables) {
    var text = new StringBuilder();

    tokens.forEach(t -> text.append(t.format(variables.get(t.name))));

    return text.toString();
  }
}
