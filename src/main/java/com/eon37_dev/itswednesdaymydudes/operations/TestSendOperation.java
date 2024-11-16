package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("/test_send")
public class TestSendOperation extends Operation {
  public TestSendOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    if (hasAdminRights(String.valueOf(chatId), bot)) botService.testSend(chatId, bot);
    return null;
  }
}
