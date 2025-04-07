package org.dco.muffin.foundation.core.validation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.dco.muffin.foundation.core.localization.LocaleMessageSource;
import org.dco.muffin.foundation.core.templating.TinyTemplate;
import org.dco.muffin.foundation.core.templating.TinyTemplateFactory;
import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.springframework.context.annotation.Primary;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.validation.MessageInterpolator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The component how interpolate message for validation
 */
@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class ValidationMessageInterpolator implements MessageInterpolator {
  private static final String VALIDATED_VALUE_PROPERTY = "validatedValue";
  private static final String VALUE_METHOD = "value";

  private final LocaleMessageSource messageSource;

  /**
   * @param messageTemplate - The message template
   * @param locale - The locale
   * @return The template
   */
  private TinyTemplate template(String messageTemplate, Locale locale) {
    var messageCode = messageTemplate.substring(1, messageTemplate.length() - 1);
    var messagePattern = messageSource.resolveCodeWithoutArguments(messageCode, locale);

    if (!StringUtils.hasText(messagePattern)) {
      return null;
    }

    return TinyTemplateFactory.get(messagePattern);
  }

  /**
   * @param context - The context
   * @return The context as map
   */
  private Map<String, Object> contextAsMap(Context context) {
    Map<String, Object> map = new HashMap<>();
    map.put(VALIDATED_VALUE_PROPERTY, context.getValidatedValue());

    if (context instanceof MessageInterpolatorContext ctx) {
      map.putAll(ctx.getMessageParameters());
      var annotationClass = context.getConstraintDescriptor().getAnnotation().annotationType();
      var key = annotationClass.getSimpleName().toLowerCase(Locale.getDefault());

      try {
        var valueMethod = annotationClass.getMethod(VALUE_METHOD);
        var value = valueMethod.invoke(context.getConstraintDescriptor().getAnnotation());
        map.put(key,  value);
      }
      catch (Exception e) {
        log.trace("No method value on annotation {}", e, annotationClass);
      }
    }

    return map;
  }

  @Override
  public String interpolate(String messageTemplate, Context context) {
    return interpolate(messageTemplate, context, LocaleContextHolder.getLocale());
  }

  @Override
  public String interpolate(String messageTemplate, Context context, Locale locale) {
    if (!StringUtils.hasText(messageTemplate)) {
      return "";
    }

    if (!messageTemplate.startsWith("{") && !messageTemplate.endsWith("}")) {
      return messageTemplate;
    }

    var template = template(messageTemplate, locale);

    if (template == null) {
      return messageTemplate;
    }

    return template.format(contextAsMap(context));
  }

}
