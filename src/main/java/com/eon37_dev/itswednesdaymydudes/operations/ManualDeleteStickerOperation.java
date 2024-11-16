package com.eon37_dev.itswednesdaymydudes.operations;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.services.BotService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("/manual_delete_sticker")
public class ManualDeleteStickerOperation extends Operation {
  public ManualDeleteStickerOperation(BotService botService, Set<String> adminChats) {
    super(botService, adminChats);
  }

  @Override
  public Void process(Long chatId, String input, Bot bot) {
    if (hasAdminRights(String.valueOf(chatId), bot)) botService.deleteStickerById(Long.parseLong(input));
    return null;
  }
}
