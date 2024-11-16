package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.Set;

@Component("/default")
public class DefaultOperation extends Operation {
  public DefaultOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    botService.sendMessage(new SendMessage(String.valueOf(chatId), "Unknown command"), bot);
    return null;
  }
}
