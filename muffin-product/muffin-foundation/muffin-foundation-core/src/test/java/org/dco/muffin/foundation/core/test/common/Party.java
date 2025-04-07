package org.dco.muffin.foundation.core.test.common;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class Party {
  @NotBlank(message = "{first-name.required}")
  private final String firstName;
  @NotBlank(message = "")
  private final String lastName;

  @Max(message = "Trop grand", value = 200)
  private final long size;

  @Min(value = 18, message = "{age.to-young}")
  private final int age;

  @NotEmpty(message = "{one-contact-required}")
  private final List<String> contacts;

}
