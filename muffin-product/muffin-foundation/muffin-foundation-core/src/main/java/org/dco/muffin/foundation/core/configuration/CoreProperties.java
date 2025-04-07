package org.dco.muffin.foundation.core.configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Core properties
 */
@Component
@ConfigurationProperties(prefix = CoreProperties.CORE_PREFIX)
@Getter
public class CoreProperties {
  /**
   * The root prefix
   */
  public static final String ROOT_PREFIX = "muffin";

  /**
   * The core prefix
   */
  public static final String CORE_PREFIX = ROOT_PREFIX + ".core";

  /**
   * Localisation properties
   */
  @NoArgsConstructor
  public static final class LocalizationProperties {
    private static final String MESSAGES_BASENAME = "messages";
    private static final String EXCEPTIONS_BASENAME = "exceptions";
    private static final String VALIDATIONS_BASENAME = "validations";
    private static final String EMERGENCIES_BASENAME = "emergencies";
    private static final String SITE_BASENAME = "site";
    private static final String PROPERTIES_EXTENSION = ".properties";
    private static final String PROPS_EXTENSION = ".props";

    @Getter
    private List<String> basenames = new LinkedList<>(Arrays.asList(MESSAGES_BASENAME,
                                                                    EXCEPTIONS_BASENAME,
                                                                    VALIDATIONS_BASENAME,
                                                                    EMERGENCIES_BASENAME,
                                                                    SITE_BASENAME));

    @Getter
    @Setter
    private long cacheMillis = -1;

    @Getter
    private Properties commonMessages = new Properties();

    @Getter
    @Setter
    private Charset defaultEncoding = StandardCharsets.UTF_8;

    @Getter
    @Setter
    private Locale defaultLocale = Locale.getDefault();

    @Getter
    @Setter
    private boolean fallbackToSystem = true;

    @Getter
    @Setter
    private boolean fallbackToDefault = true;

    @Getter
    private List<String> fileExtensions = new ArrayList<>(Arrays.asList(PROPERTIES_EXTENSION,
                                                                        PROPS_EXTENSION));

    @Getter
    @Setter
    private boolean useCodeAsMessage = true;
  }

  private LocalizationProperties localization = new LocalizationProperties();
}
