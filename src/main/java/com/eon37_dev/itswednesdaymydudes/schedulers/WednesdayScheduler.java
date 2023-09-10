package com.eon37_dev.itswednesdaymydudes.schedulers;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.exceptions.ChatInactiveException;
import com.eon37_dev.itswednesdaymydudes.services.ChatService;
import com.eon37_dev.itswednesdaymydudes.services.StickerService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

@Component
@RequiredArgsConstructor
public class WednesdayScheduler {
  private static final Logger logger = LoggerFactory.getLogger(WednesdayScheduler.class);
  private final Bot bot;
  private final StickerService stickerService;
  private final ChatService chatService;
  @Scheduled(cron = "0 */15 * ? * TUE,WED,THU")
  public void itsWednesdayMyDudes() {
    logger.info("Job started");
    chatService.getAllBetweenTime(OffsetTime.of(LocalTime.now(), OffsetDateTime.now().getOffset()))
            .forEach(chat -> {
              try {
                stickerService.sendSticker(chat.getChatId(), bot);
              } catch (ChatInactiveException e) {
                chatService.delete(chat.getId());
              }
            });
  }
}