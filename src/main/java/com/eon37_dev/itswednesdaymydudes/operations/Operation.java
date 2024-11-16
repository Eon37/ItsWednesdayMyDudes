package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;

public abstract class Operation {
  protected final BotService botService;
  protected final Set<String> adminChats;
  public Operation(BotService botService, Set<String> adminChats) {
    this.botService = botService;
    this.adminChats = adminChats;
  }
  public abstract Void process(Long chatId, String input, Bot bot);

  protected boolean hasAdminRights(String chatId, Bot bot) {
    if (adminChats.contains(chatId)) return true;

    botService.sendMessage(new SendMessage(chatId, "Unknown command"), bot);
    return false;
  }
}
