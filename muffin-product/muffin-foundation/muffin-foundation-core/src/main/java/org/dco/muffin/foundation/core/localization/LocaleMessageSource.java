package org.dco.muffin.foundation.core.localization;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.dco.muffin.foundation.core.configuration.CoreProperties;
import org.dco.muffin.foundation.core.configuration.CoreProperties.LocalizationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.Nullable;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;


/**
 * The locale message source provide internationalization message
 */
@Slf4j
@Component
@Primary
public class LocaleMessageSource implements MessageSource {
  private final LocalizationProperties properties;
  private final LocaleResourceResolver resolver;

  private Map<Locale, Properties> localePropertiesMap = new ConcurrentHashMap<>();
  private Map<Locale, Map<Resource, LocalePropertiesResourceFollowed>> localeResourcesMap = new ConcurrentHashMap<>();

  /**
   * Construct it
   * @param coreProperties - The properties of core
   */
  public LocaleMessageSource(CoreProperties coreProperties) {
    this.properties = coreProperties.getLocalization();
    resolver = new LocaleResourceResolver(properties.getBasenames(), properties.getFileExtensions());
  }

  /**
   * @param locale - The locale
   * @return The properties for locale
   */
  private Properties localeProperties(Locale locale) {
    return localePropertiesMap.computeIfAbsent(locale, l -> new Properties());
  }

  /**
   * @param locale
   * @return All resource followed for locale
   */
  private Map<Resource, LocalePropertiesResourceFollowed> localeResources(Locale locale) {
    return localeResourcesMap.computeIfAbsent(locale, l -> new ConcurrentHashMap<>());
  }

  /**
   * @param resource - The resource
   * @return The resource to follow
   */
  private LocalePropertiesResourceFollowed resourceFollowed(Resource resource) {
    return new LocalePropertiesResourceFollowed(resource, properties.getDefaultEncoding(), properties.getCacheMillis());
  }

  /**
   * @param locale - The locale
   * @param resource - The resource to follow
   * @return The resource to follow
   */
  private LocalePropertiesResourceFollowed localeResource(Locale locale, Resource resource) {
    return localeResources(locale).computeIfAbsent(resource, this::resourceFollowed);
  }

  /**
   * @param code - The code
   * @param locale - The locale
   * @param resource - The resource
   * @return The code from resource if found
   */
  private String resolveCodeFromResource(String code, Locale locale, Resource resource) {
    var resourceFollowed = localeResource(locale, resource);
    var content = resourceFollowed.content();
    localeProperties(locale).putAll(content);
    var resolution = content.getProperty(code);

    if (StringUtils.hasText(resolution)) {
      log.debug("Code [{}] for locale [{}] resolved from resource [{}] -> ({})", code, locale, resource, resolution);
    }

    return resolution;
  }

  /**
   * @param code - The code
   * @param locale - The locale
   * @return The resolution of code for locale
   */
  public String resolveCodeWithoutArguments(String code, Locale locale) {
    var localeProperties = localeProperties(locale);
    var resolution = localeProperties.getProperty(code);

    if (StringUtils.hasText(resolution) && properties.getCacheMillis() <= 0) {
      log.debug("Code [{}] for locale [{}] resolved from cache -> ({})", code, locale, resolution);
      return resolution;
    }

    return resolver.resolve(locale)
                   .map(r -> resolveCodeFromResource(code, locale, r))
                   .filter(Objects::nonNull)
                   .findFirst()
                    .orElse(null);
  }

  /**
   * @param code - The code
   * @param locale - The locale
   * @param args - The arguments
   * @return The message for code
   */
  @Nullable
  private String resolveMessage(String code, Locale locale, Object... args) {
    var resolution = resolveCodeWithoutArguments(code, locale);

    if (StringUtils.hasText(resolution)) {
      return MessageFormat.format(resolution, args);
    }

    return null;
  }

  /**
   * @param code - The code
   * @param locale - The locale
   * 
   * @return The code if use code as message is enabled else raise error
   */
  private String codeOrRaiseError(String code, Locale locale) {
    if (properties.isUseCodeAsMessage()) {
      log.debug("Use code [{}] has message for locale [{}]", code, locale);
      return code;
    }

    throw new NoSuchMessageException(code, locale);
  }

  /**
   * @param code - The code
   * @param args - The arguments
   * 
   * @return The common message
   */
  private String commonMessage(String code, Object... args) {
    var resolution = properties.getCommonMessages().getProperty(code);

    if (StringUtils.hasText(resolution)) {
      var message = MessageFormat.format(resolution, args);
      log.debug("Code [{}] resolved from commons -> ({})", code, args);
      return message;
    }

    return null;
  }

  /**
   * @param code - The code
   * @param args - The arguments
   * @param locale - The locale
   * 
   * @return The message resolution or null
   */
  private String message(String code, Locale locale, Object... args) {
    var message = resolveMessage(code, locale, args);

    if (message != null) {
      return message;
    }

    if (properties.isFallbackToDefault()) {
      message = resolveMessage(code, properties.getDefaultLocale(), args);

      if (message != null) {
        return message;
      }
    }

    if (properties.isFallbackToSystem()) {
      message = resolveMessage(code, Locale.getDefault(), args);

      if (message != null) {
        return message;
      }
    }

    message = commonMessage(code, locale);

    if (message != null) {
      return message;
    }

    return null;
  }


  @Override
  public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
    var message = message(code, locale, args);

    if (StringUtils.hasText(message)) {
      return message;
    }

    if (StringUtils.hasText(defaultMessage)) {
      log.debug("Use default message [{}]", defaultMessage);
      return defaultMessage;
    }

    return codeOrRaiseError(code, locale);
  }

  @Override
  public String getMessage(String code, Object[] args, Locale locale) throws NoSuchMessageException {
    var message = message(code, locale, args);

    if (StringUtils.hasText(message)) {
      return message;
    }

    return codeOrRaiseError(code, locale);
  }

  @Override
  public String getMessage(MessageSourceResolvable resolvable, Locale locale) throws NoSuchMessageException {
    var codes = resolvable.getCodes();
    var args = resolvable.getArguments();
    var defaultMessage = resolvable.getDefaultMessage();

    if (codes == null || codes.length == 0) {
      if (StringUtils.hasText(defaultMessage)) {
        return  defaultMessage;
      }

      return codeOrRaiseError(null, locale);
    }

    for (var code: codes) {
      var message = message(code, locale, args);

      if (StringUtils.hasText(message)) {
        return message;
      }
    }

    if (StringUtils.hasText(defaultMessage)) {
      return defaultMessage;
    }

    var lastCode = codes[codes.length - 1];

    return codeOrRaiseError(lastCode, locale);
  }

  /**
   * Initialize the provider
   */
  @PostConstruct
  public void provide() {
    LocaleMessageSourceProvider.initialize(this);
  }

}
