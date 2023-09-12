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
  @Query(nativeQuery = true, value = """
          select * from chats c where abs(extract(epoch from(
          cast(:currentTime as timestamp with time zone)
          -\s
          CASE\s
            WHEN iso_day_of_week(current_date()) = 2 THEN cast(c.time_to_send as timestamp with time zone) + 1\s
            WHEN iso_day_of_week(current_date()) = 3 THEN cast(c.time_to_send as timestamp with time zone)\s
            WHEN iso_day_of_week(current_date()) = 4 THEN cast(c.time_to_send as timestamp with time zone) - 1\s
          END
          ))/60) < 1;""")
  List<Chat> findAllBetweenTime(OffsetTime currentTime);
  Optional<Chat> findByChatId(Long chatId);
  @Transactional
  void deleteByChatId(Long chatId);
}
