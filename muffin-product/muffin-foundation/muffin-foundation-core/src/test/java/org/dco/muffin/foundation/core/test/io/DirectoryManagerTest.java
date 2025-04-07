package org.dco.muffin.foundation.core.test.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.dco.muffin.foundation.core.io.DirectoryManager;
import org.dco.muffin.foundation.core.io.InputOutputException;
import org.dco.muffin.foundation.core.io.PathType;
import org.dco.muffin.foundation.core.test.CoreTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

/**
 * Test the directory manager
 */
class DirectoryManagerTest extends CoreTest {
  private static final Path P_SOURCE = Paths.get("/tmp/muffin/source");
  private static final Path P_DESTINATION = Paths.get("/tmp/muffin/destination");

  @BeforeEach
  public void setUp() throws Exception {
    FileSystemUtils.deleteRecursively(P_SOURCE);
    FileSystemUtils.deleteRecursively(P_DESTINATION);
  }

  @Test
  void if_auto_create_is_enabled_then_verify_is_success() {
    var m = new DirectoryManager(P_SOURCE).verify();
    assertThat(m.getPath()).isEqualTo(P_SOURCE);
    assertThat(m.getType()).isEqualTo(PathType.DIRECTORY);
    assertThat(m.isAutoCreate()).isTrue();

    var m1 = new DirectoryManager(P_SOURCE).verify();
    assertThat(m1.getPath()).isEqualTo(P_SOURCE);
    assertThat(m1.getType()).isEqualTo(PathType.DIRECTORY);
    assertThat(m1.isAutoCreate()).isTrue();
  }

  @Test
  void if_auto_create_is_disabled_then_verify_raise_error() {
    assertThatThrownBy(() -> new DirectoryManager(P_SOURCE, false).verify()).isInstanceOf(InputOutputException.class);
  }

  @Test
  void when_path_is_regular_file_then_raise_error() throws IOException {
    Files.createFile(P_SOURCE);
    assertThatThrownBy(() -> new DirectoryManager(P_SOURCE).verify()).isInstanceOf(InputOutputException.class);
    Files.deleteIfExists(P_SOURCE);
  }

  @Test
  void when_cant_create_path_then_raise_error() {
    try (var staticFiles = mockStatic(Files.class))  {
      staticFiles.when(() -> Files.isDirectory(any(Path.class))).thenReturn(false);
      staticFiles.when(() -> Files.createDirectories(any(Path.class))).thenThrow(new IOException());
      assertThatThrownBy(() -> new DirectoryManager(P_SOURCE).verify()).isInstanceOf(InputOutputException.class);
    }
  }

  @Test
  void can_move_source() {
    var m0 = new DirectoryManager(P_SOURCE).verify();
    var m1 = m0.move(P_DESTINATION, true).verify();

    assertThat(m1.getPath()).isDirectory();

    var m2 = m1.move(P_DESTINATION).verify();

    assertThat(m2.getPath()).isDirectory();
  }

  @Test
  void can_copy_source() {
    var m0 = new DirectoryManager(P_SOURCE).verify();
    var m1 = m0.copy(P_DESTINATION).verify();
    assertThat(m1.getPath()).isDirectory();
  }

  @Test
  void when_copy_failed_then_raise_error() {
    var m0 = new DirectoryManager(P_SOURCE).verify();

    try (var staticFiles = mockStatic(FileSystemUtils.class)) {
      staticFiles.when(() -> FileSystemUtils.copyRecursively(any(Path.class), any(Path.class))).thenThrow(new IOException());
      assertThatThrownBy(() -> m0.copy(P_DESTINATION)).isInstanceOf(InputOutputException.class);
    }
  }

  @Test
  void when_move_failed_then_raise_error() {
    var m0 = new DirectoryManager(P_SOURCE).verify();

    try (var staticFiles = mockStatic(Files.class)) {
      staticFiles.when(() -> Files.move(any(Path.class), any(Path.class))).thenThrow(new IOException());
      assertThatThrownBy(() -> m0.move(P_DESTINATION, false)).isInstanceOf(InputOutputException.class);
    }
  }

  @Test
  void can_delete() {
    var m0 = new DirectoryManager(P_SOURCE).verify().delete();
    assertThat(Files.isDirectory(m0.getPath())).isFalse();
    assertThat(Files.isDirectory(m0.delete().getPath())).isFalse();
  }

  @Test
  void when_cant_delete_then_raise_error() {
    var m0 = new DirectoryManager(P_SOURCE).verify();

    try (var m = mockStatic(FileSystemUtils.class)) {
      m.when(() -> FileSystemUtils.deleteRecursively(any(Path.class))).thenThrow(new IOException());
      assertThatThrownBy(() -> m0.delete()).isInstanceOf(InputOutputException.class);
    }
  }


}
