package com.eon37_dev.itswednesdaymydudes.exceptions;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChatInactiveException extends Exception {
  private final String message;
  private final long chatId;
}
