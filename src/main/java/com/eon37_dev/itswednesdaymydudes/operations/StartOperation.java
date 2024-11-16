package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("/start")
public class StartOperation extends Operation {
  public StartOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    botService.start(chatId, bot);
    return null;
  }
}
