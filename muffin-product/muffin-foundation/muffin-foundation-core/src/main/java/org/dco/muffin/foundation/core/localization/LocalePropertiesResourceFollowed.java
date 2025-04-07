package org.dco.muffin.foundation.core.localization;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

import org.springframework.core.io.Resource;

/**
 * A resource how contains properties that it was also followed
 */
public class LocalePropertiesResourceFollowed extends LocaleResourceFollowed<Properties> {

  /**
   * @param resource - The resource
   * @param encoding - The encoding
   * @param delay - The following delay
   */
  public LocalePropertiesResourceFollowed(Resource resource, Charset encoding, long delay) {
    super(Properties.class, resource, encoding, delay);
  }

  @Override
  protected Properties loadContent() throws IOException {
     var p = new Properties();

     try (var r = reader()) {
       p.load(r);
     }

     return  p;
  }

}
