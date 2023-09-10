package com.eon37_dev.itswednesdaymydudes.repositories;

import com.eon37_dev.itswednesdaymydudes.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
  @Query(value = "select * from chats c where abs(extract(epoch from(cast(:currentTime as timestamp with time zone) - cast(c.time_to_send as timestamp with time zone) + c.day_offset * INTERVAL '1' day))/60) < 3", nativeQuery = true)
  List<Chat> findAllBetweenTime(OffsetTime currentTime);
  Optional<Chat> findByChatId(Long chatId);
  @Transactional
  void deleteByChatId(Long chatId);
}
