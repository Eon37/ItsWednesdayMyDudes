package com.eon37_dev.itswednesdaymydudes.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotFoundException extends RuntimeException {
  private final String message;
}
