package org.dco.muffin.foundation.core.templating;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The factory of template
 */
public final class TinyTemplateFactory {
  private static Map<String, TinyTemplate> templates = new ConcurrentHashMap<>();

  /**
   * Hide the constructor
   */
  private TinyTemplateFactory() {
    throw new UnsupportedOperationException("Can't instanciate tiny template factory");
  }

  /**
   * @param text - Text of template
   * @return The template
   */
  public static synchronized TinyTemplate get(String text) {
    return templates.computeIfAbsent(text, TinyTemplate::new);
  }

}
