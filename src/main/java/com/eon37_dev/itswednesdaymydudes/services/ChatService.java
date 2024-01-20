package com.eon37_dev.itswednesdaymydudes.services;

import com.eon37_dev.itswednesdaymydudes.exceptions.NotFoundException;
import com.eon37_dev.itswednesdaymydudes.model.Chat;
import com.eon37_dev.itswednesdaymydudes.repositories.ChatRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.OffsetTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
  private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
  private final ChatRepository chatRepository;

  public void save(Chat chat) {
    try {
      chatRepository.save(chat);
    } catch (DataIntegrityViolationException e) {
      logger.warn("Chat already exists");
    } catch (Exception e) {
      logger.error("Error saving chat", e);
    }
  }

  public Chat getByChatId(long chatId) {
    return chatRepository.findByChatId(String.valueOf(chatId)).orElseThrow(() -> new NotFoundException("Chat does not exist"));
  }

  public List<Chat> getAll() {
    return chatRepository.findAll();
  }

  public List<Chat> getAllBetweenTime(OffsetTime currentTime) {
    return chatRepository.findAllBetweenTime(currentTime);
  }

  public void deleteById(long id) {
    chatRepository.deleteById(id);
    logger.info("Chat removed");
  }

  public long count() {
    return chatRepository.count();
  }
}
