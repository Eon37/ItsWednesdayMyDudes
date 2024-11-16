package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("/set_time")
public class SetTimeOperation extends Operation {
  public SetTimeOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    botService.setTimeToSendMessages(chatId, input, bot);
    return null;
  }
}
