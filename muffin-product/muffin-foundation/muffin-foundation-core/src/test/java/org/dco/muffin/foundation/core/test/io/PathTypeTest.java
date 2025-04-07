package org.dco.muffin.foundation.core.test.io;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.dco.muffin.foundation.core.io.InputOutputException;
import org.dco.muffin.foundation.core.io.PathType;
import org.junit.jupiter.api.Test;

class PathTypeTest {

  @Test
  void d_is_symbol_of_directory_path_type() {
    assertThat(PathType.DIRECTORY.symbol()).isEqualTo('d');
    assertThat(PathType.of("d")).isEqualTo(PathType.DIRECTORY);
  }

  @Test
  void u_is_not_a_valid_that_type() {
    assertThatThrownBy(() -> PathType.of("u")).isInstanceOf(InputOutputException.class);
  }

  @Test
  void when_symbol_is_empty_raise_error() {
    assertThatThrownBy(() -> PathType.of("")).isInstanceOf(InputOutputException.class);
  }
}
