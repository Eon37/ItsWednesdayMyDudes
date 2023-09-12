package com.eon37_dev.itswednesdaymydudes.services;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.exceptions.ChatInactiveException;
import com.eon37_dev.itswednesdaymydudes.exceptions.IncorrectInputFormat;
import com.eon37_dev.itswednesdaymydudes.exceptions.NotFoundException;
import com.eon37_dev.itswednesdaymydudes.model.Chat;
import com.eon37_dev.itswednesdaymydudes.model.Sticker;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.*;
import java.time.temporal.ChronoField;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class BotService {
  private static final String INCORRECT_FORMAT_MESSAGE = "Incorrect format. Correct format: [h]h:mm[+|-[h]h[:mm]], values in [] are optional.\nExample: 15:30+3";
  private static final Logger logger = LoggerFactory.getLogger(BotService.class);
  private final ChatService chatService;
  private final StickerService stickerService;
  private static final Pattern INPUT_TIME_PATTERN = Pattern.compile("(([0-2])?[0-9]):([0-5][0-9])(([+|-])(([0-1])?[0-9])(:([0-5][0-9]))?)?");

  public void start(long chatId, Bot bot) {
    OffsetTime offsetTime = OffsetTime.of(LocalTime.of(10, 0), ZoneOffset.UTC);
    chatService.save(new Chat(null, chatId, offsetTime, calculateDayOffset(offsetTime)));
    logger.info("Chat added");
    SendMessage sendMessage = new SendMessage(String.valueOf(chatId),
            """
                  Welcome to its wednesday my dudes bot. Here are the list of commands:
                  /set_time - set the time to receive messages. Optionally you can set timezone (default is UTC+0)
                  Format: [h]h:mm[+|-[h]h[:mm]] (e.g. 15:30+3).
                """);

    sendMessage(sendMessage, bot);
  }

  public void setTimeToSendMessages(long chatId, String time, Bot bot) {
    SendMessage errorMessage = new SendMessage(String.valueOf(chatId), INCORRECT_FORMAT_MESSAGE);

    Matcher matcher = INPUT_TIME_PATTERN.matcher(time);
    if (matcher.matches()) {
      try {
        Chat chat = chatService.getByChatId(chatId);
        OffsetTime offsetTime = extractZonedTime(matcher);
        chat.setTimeToSend(offsetTime);
        chat.setDayOffset(calculateDayOffset(offsetTime));
        chatService.save(chat);
        logger.info("Time zone changed to [{}]", time);
      } catch (NotFoundException e) {
        logger.error("Chat not found", e);
      } catch (IncorrectInputFormat e) {
        logger.error("Error while setting time", e);
        sendMessage(errorMessage, bot);
      } catch (Exception e) {
        logger.error("Error while setting time", e);
        sendMessage(new SendMessage(String.valueOf(chatId), "Unexpected error while setting time"), bot);
      }
    } else {
      sendMessage(errorMessage, bot);
    }
  }

  private static int calculateDayOffset(OffsetTime offsetTime) {
    OffsetDateTime userTime = OffsetDateTime.of(LocalDate.now(), LocalTime.of(offsetTime.getHour(), offsetTime.getMinute()), offsetTime.getOffset());
    ZoneOffset systemOffset = OffsetDateTime.now().getOffset();

    OffsetDateTime odt = userTime
            .plusSeconds(systemOffset.getLong(ChronoField.OFFSET_SECONDS))
            .minusSeconds(userTime.getLong(ChronoField.OFFSET_SECONDS));

    return Integer.compare(odt.getDayOfMonth(), LocalDate.now().getDayOfMonth());
  }

  public void addSticker(String sticker) {
    stickerService.save(new Sticker(null, sticker));
    logger.info("Sticker added [{}]", sticker);
  }

  public void testSend(long chatId, Bot bot) {
    try {
      stickerService.sendSticker(chatId, bot);
    } catch (ChatInactiveException e) {
      chatService.delete(chatId);
    }
  }

  private static OffsetTime extractZonedTime(Matcher matcher) {
    try {
      int hour = Integer.parseInt(matcher.group(1));
      int minute = Integer.parseInt(matcher.group(3));

      if (matcher.group(4) == null) {
        return OffsetTime.of(LocalTime.of(hour, minute), ZoneOffset.UTC);
      }

      int zoneHour = Integer.parseInt(matcher.group(6));
      int zoneMinute = matcher.group(8) == null ? 0 : Integer.parseInt(matcher.group(9));

      return OffsetTime.of(LocalTime.of(hour, minute),
              ZoneOffset.ofHoursMinutes(
                      "-".equals(matcher.group(5)) ? -zoneHour : zoneHour,
                      "-".equals(matcher.group(5)) ? -zoneMinute : zoneMinute));
    } catch (Exception e) {
      throw new IncorrectInputFormat();
    }
  }

  private void sendMessage(SendMessage sendMessage, Bot bot) {
    try {
      bot.execute(sendMessage);
    } catch (TelegramApiException e) {
      logger.error("Error while sending message", e);
      throw new RuntimeException(e);
    }
  }
}
