package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("/show_configs")
public class ShowConfigsOperation extends Operation {
  public ShowConfigsOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    botService.showConfigsList(chatId, adminChats.contains(String.valueOf(chatId)), bot);
    return null;
  }
}
