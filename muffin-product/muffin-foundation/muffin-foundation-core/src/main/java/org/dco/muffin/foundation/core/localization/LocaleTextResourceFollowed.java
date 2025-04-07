package org.dco.muffin.foundation.core.localization;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.springframework.core.io.Resource;

/**
 * A resource how contains text that it was also followed
 */
public class LocaleTextResourceFollowed extends LocaleResourceFollowed<String> {

  /**
   * @param resource - The resource
   * @param encoding - Tne encoding
   * @param delay - The followind delay
   */
  public LocaleTextResourceFollowed(Resource resource, Charset encoding, long delay) {
    super(String.class, resource, encoding, delay);
  }

  @Override
  protected String loadContent() throws IOException {
    try (var r = reader()) {
      var sw = new StringWriter();
      r.transferTo(sw);
      return sw.toString();
    }
  }

}
