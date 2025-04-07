package org.dco.muffin.foundation.core.io;

import java.nio.file.Path;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Manager of path in file system
 */
@Getter
@RequiredArgsConstructor
public abstract class PathManager {
  protected final PathType type;
  protected final Path path;
  protected final boolean autoCreate;
}
