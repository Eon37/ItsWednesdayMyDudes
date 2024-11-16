package com.eon37_dev.itswednesdaymydudes;

import com.eon37_dev.itswednesdaymydudes.operations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

@Component
public class Bot extends TelegramLongPollingBot {
  private static final Logger logger = LoggerFactory.getLogger(Bot.class);
  private final String botUsername;
  private Map<String, Operation> operations;

  public Bot(@Value("${telegram.bot.token}") String botToken,
             @Value("${telegram.bot.username}") String botUsername,
             Map<String, Operation> operations) {
    super(botToken);
    this.botUsername = botUsername;
    this.operations = operations;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      Long chatId = update.getMessage().getChatId();

      String[] commandLine = update.getMessage().getText().split(" ");
      String command = commandLine[0].contains("@" + botUsername) ? commandLine[0].substring(0, commandLine[0].indexOf("@")) : commandLine[0].trim();
      String commandInput = commandLine.length > 1 ? commandLine[1].trim() : null;

      operations.getOrDefault(command, operations.get("/default")).process(chatId, commandInput, this);
    }
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }
}