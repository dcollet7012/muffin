package org.dco.muffin.foundation.core.localization;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

/**
 * The resource resolver for locale
 */
@RequiredArgsConstructor
@Builder
public class LocaleResourceResolver {
  private final List<String> basenames;
  private final List<String> extensions;

  /**
   * @param filename - The filename
   * @return The resource with the filename
   */
  private Resource resolve(String filename) {
    if (filename.startsWith(ResourceLoader.CLASSPATH_URL_PREFIX)) {
      return new ClassPathResource(filename.substring(ResourceLoader.CLASSPATH_URL_PREFIX.length()));
    }

    return new FileSystemResource(filename);
  }

  /**
   * @param basename - The base name
   * @param tag - The tag
   * @param extension - The extension
   * @return The resource with file name has base name+tag+extension
   */
  private Resource resolve(String basename, String tag, String extension) {
    var filename = new StringBuilder(basename);

    if (StringUtils.hasText(tag)) {
      filename.append('_').append(tag);
    }

    filename.append(extension);

    return resolve(filename.toString());
  }

  /**
   * @param basename -The base name
   * @param tag - The tag
   * @return The resource found if exists
   */
  private Optional<Resource> resolve(String basename, String tag) {
    return extensions.stream()
                     .map(e -> resolve(basename, tag, e))
                     .filter(Resource::exists)
                     .findFirst();
  }

  /**
   * @param basename  - The base name
   * @param tags - List of tag
   * 
   * @return all resource for base name and tags
   */
  private Stream<Resource> resolve(String basename, List<String> tags) {
    return tags.stream()
               .map(t -> resolve(basename, t))
               .filter(Optional::isPresent)
               .map(Optional::get);
  }

  /**
   * @param tags - List of tag
   * @return All resources for base name and tags
   */
  private Stream<Resource> resolves(List<String> tags) {
    return basenames.stream()
                    .flatMap(b -> resolve(b, tags));
  }

  /**
   * @param locale - The locale
   * @return All resource for locale
   */
  public Stream<Resource> resolve(Locale locale) {
    return resolves(new LocaleTagger(locale).getTags());
  }
}
