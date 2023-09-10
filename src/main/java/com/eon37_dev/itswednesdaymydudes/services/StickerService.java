package com.eon37_dev.itswednesdaymydudes.services;

import com.eon37_dev.itswednesdaymydudes.Bot;
import com.eon37_dev.itswednesdaymydudes.exceptions.ChatInactiveException;
import com.eon37_dev.itswednesdaymydudes.model.Sticker;
import com.eon37_dev.itswednesdaymydudes.repositories.StickerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StickerService {
  private static final Logger logger = LoggerFactory.getLogger(StickerService.class);
  private static final String DEFAULT_WEDNESDAY = "CAACAgEAAxkBAAElz2xk_EDb2HwCx6pRxp2pPPINARoseQACCQAD3tJRF6KuCsJUHxe1MAQ";
  private final StickerRepository stickerRepository;

  public void save(Sticker sticker) {
    try {
      stickerRepository.save(sticker);
    } catch (DataIntegrityViolationException e) {
      logger.warn("Sticker already exists");
    } catch (Exception e) {
      logger.error("Error saving sticker", e);
    }
  }

  public Sticker getRandom() {
    Optional<Sticker> sticker = stickerRepository.findRandom();
    return sticker.orElse(new Sticker(null, DEFAULT_WEDNESDAY));
  }

  public List<Sticker> getAll() {
    return stickerRepository.findAll();
  }

  public void sendSticker(long chatId, Bot bot) throws ChatInactiveException {
    SendSticker sendSticker = new SendSticker();
    sendSticker.setChatId(chatId);
    sendSticker.setSticker(new InputFile(getRandom().getStickerId()));

    try {
      bot.execute(sendSticker);
    } catch (TelegramApiRequestException e) {
      if (e.getErrorCode() == 403 || e.getErrorCode() == 400) {
        logger.error("Chat inactive");
        throw new ChatInactiveException("Chat inactive", chatId);
      }
      logger.error("Error sending sticker");
      throw new RuntimeException(e);
    } catch (TelegramApiException e) {
      logger.error("Error sending sticker");
      throw new RuntimeException(e);
    }
  }
}
