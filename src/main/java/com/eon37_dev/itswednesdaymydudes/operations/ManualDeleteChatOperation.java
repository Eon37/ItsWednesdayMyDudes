package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("/manual_delete_chat")
public class ManualDeleteChatOperation extends Operation {
  public ManualDeleteChatOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    if (hasAdminRights(String.valueOf(chatId), bot)) botService.deleteChatById(Long.parseLong(input));
    return null;
  }
}
