package com.eon37_dev.itswednesdaymydudes;

import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Bot extends TelegramLongPollingBot {
  private static final Logger logger = LoggerFactory.getLogger(Bot.class);
  private final String botUsername;
  private final Set<String> adminChats;
  private final BotService botService;

  public Bot(@Value("${telegram.bot.token}") String botToken,
             @Value("${telegram.bot.username}") String botUsername,
             @Value("${telegram.bot.admin_chats}") String adminChats,
             BotService botService) {
    super(botToken);
    this.botUsername = botUsername;
    this.adminChats = Arrays.stream(adminChats.split(",")).collect(Collectors.toUnmodifiableSet());
    this.botService = botService;
  }

  @Override
  public void onUpdateReceived(Update update) {
    if (update.hasMessage() && update.getMessage().hasText()) {
      String message = update.getMessage().getText();
      long chatId = update.getMessage().getChatId();

      if (message.startsWith("/start")) {
        botService.start(chatId, this);
      } else if (message.startsWith("/set_time")) {
        botService.setTimeToSendMessages(chatId, getCommandInput(message), this);
      } else if (message.startsWith("/show_configs")) {
        botService.showConfigsList(chatId, this);
      } else if (message.startsWith("/add_sticker")) {
        if (!adminChats.contains(update.getMessage().getChatId().toString())) {
          botService.sendMessage(new SendMessage(String.valueOf(chatId), "Unknown command"), this);
          return;
        }
        botService.addSticker(getCommandInput(message));
      } else if (message.startsWith("/test_send")) {
        if (!adminChats.contains(update.getMessage().getChatId().toString())) {
          botService.sendMessage(new SendMessage(String.valueOf(chatId), "Unknown command"), this);
          return;
        }
        botService.testSend(chatId, this);
      } else if (message.startsWith("/stats")) {
        if (!adminChats.contains(update.getMessage().getChatId().toString())) {
          botService.sendMessage(new SendMessage(String.valueOf(chatId), "Unknown command"), this);
          return;
        }
        botService.getStats(chatId, this);
      } else {
        botService.sendMessage(new SendMessage(String.valueOf(chatId), "Unknown command"), this);
      }
    }
  }

  private static String getCommandInput(String message) {
    return message.substring(message.lastIndexOf(" ") + 1).trim();
  }

  @Override
  public String getBotUsername() {
    return botUsername;
  }
}