package org.dco.muffin.foundation.core.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.util.FileSystemUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Manager of directory in local file system
 */
@Slf4j
public class DirectoryManager extends PathManager {

  /**
   * Construct it
   * 
   * @param path - The path
   * @param autoCreate - Create if dosent't exists ?
   */
  public DirectoryManager(Path path, boolean autoCreate) {
    super(PathType.DIRECTORY, path, autoCreate);

    if (autoCreate) {
      create();
    }
  }

  /**
   * Construct it with auto creation enabled
   * 
   * @param path - The path
   */
  public DirectoryManager(Path path) {
    this(path, true);
  }

  /**
   * Create it if need
   */
  private void create() {
    if (Files.isDirectory(path)) {
      return;
    }

    if (Files.exists(path)) {
      throw new InputOutputException(InputOutputReason.PATH_INVALID_TYPE, path, PathType.FILE, PathType.DIRECTORY);
    }

    try {
      Files.createDirectories(path);
      log.debug("Directory [{}] created", path);
    }
    catch (IOException e) {
      throw new InputOutputException(InputOutputReason.DIRECTORY_CREATE_ERROR, e, path, e.getLocalizedMessage());
    }
  }

  /**
   * @return himself after verification of existence
   */
  public DirectoryManager verify() {
    var verify = Files.isDirectory(path);

    if (!verify) {
      throw new InputOutputException(InputOutputReason.DIRECTORY_NOT_FOUND, path);
    }

    log.debug("Directory [{}] verified", path);

    return this;
  }

  /**
   * @param destination - The destination
   * @param replaceExisting - Replace existing destination or not
   * @return The manager of destination
   */
  public DirectoryManager move(Path destination, boolean replaceExisting) {
    try {

      if (replaceExisting) {
        Files.move(path, destination, StandardCopyOption.REPLACE_EXISTING);
      }
      else {
        Files.move(path, destination);
      }

      log.debug("Directory [{}] moved to [{}]", path, destination);

      return new DirectoryManager(destination, false);
    }
    catch (IOException e) {
      throw new InputOutputException(InputOutputReason.DIRECTORY_MOVE_ERROR, e, path, destination, e.getLocalizedMessage());
    }
  }

  /**
   * Move to destination ,replace existing if need
   * 
   * @param destination - The destination
   * 
   * @return The manager of destination
   */
  public DirectoryManager move(Path destination) {
    return move(destination, true);
  }

  /**
   * @param destination - The destination of copy
   * @return The manager of destination
   */
  public DirectoryManager copy(Path destination) {
    try {
      FileSystemUtils.copyRecursively(path, destination);
      log.debug("Directory [{}] copied to [{}]", path, destination);
      return new DirectoryManager(destination, true);
    }
    catch (IOException e) {
      throw new InputOutputException(InputOutputReason.DIRECTORY_COPY_ERROR, e, path, destination, e.getLocalizedMessage());
    }
  }

  /**
   * @return The manager of directory deleted
   */
  public DirectoryManager delete() {
    if (!Files.isDirectory(path)) {
      return this;
    }

    try {
      FileSystemUtils.deleteRecursively(path);
      log.debug("Direcrory [{}] deleted", path);
      return this;
    }
    catch (IOException e) {
      throw new InputOutputException(InputOutputReason.DIRECTORY_DELETE_ERROR, e, path, e.getLocalizedMessage());
    }
  }

}
