package org.dco.muffin.foundation.core.localization;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import org.dco.muffin.foundation.core.temporal.TemporalConverter;
import org.springframework.core.io.Resource;

import io.micrometer.common.lang.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * A locale resource that can be follow
 */
@RequiredArgsConstructor
@Slf4j
public abstract class LocaleResourceFollowed<C> {
  protected final Class<C> contentClass;
  protected final Resource resource;
  protected final Charset encoding;
  protected final long delay;
  private long lastLoaded;
  private C content;

  /**
   * If resource is inaccessible then return -1
   * 
   * @return The last modification time of resource
   */
  public long lastModified() {
    try {
      var lastModified = resource.lastModified();
      log.debug("Resource [{}] modified at [{}]", resource, TemporalConverter.localeDateTime(lastModified));
      return lastModified;
    }
    catch (IOException e) {
      log.debug("Can't get modification time of resource [{}]", resource, e);
      return -1;
    }
  }

  /**
   * @return True if never load or resource has been modified
   */
  public boolean isDirty() {
    var dirty = lastLoaded + delay < lastModified();

    if (dirty) {
      log.debug("Resource [{}] is dirty", resource);
    }

    return dirty;
  }

  /**
   * @return The reader
   * @throws IOException - If can't get input stream of resource
   */
  protected Reader reader() throws IOException {
   return new BufferedReader(new InputStreamReader(resource.getInputStream(), encoding));
  }

  /**
   * @return The content loaded
   * @throws IOException - If can't read content
   */
  @Nullable
  protected abstract C loadContent() throws IOException;

  /**
   * Reload if need
   */
  private void reloadIfNeed() {
    if (!isDirty()) {
      return;
    }

    try {
      content = loadContent();
      lastLoaded = System.currentTimeMillis();
      log.debug("Resource [{}] loaded", resource);
    }
    catch (Exception e) {
      log.trace("Can't load", e);
    }
  }

  /**
   * Load the content if need
   * @return The content
   */
  public C content() {
    reloadIfNeed();
    return content;
  }

  /**
   * @return The resource
   */
  public Resource resource() {
    return resource;
  }

  /**
   * @return The last time when the resource has been modified
   */
  public long lastLoaded() {
    return lastLoaded;
  }
}
