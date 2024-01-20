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
  Optional<Chat> findByChatId(String chatId);
  @Query(nativeQuery = true, value = """
          select\s
              c.id,\s
              TRIM(CHAR(0) FROM UTF8TOSTRING(DECRYPT('AES', HASH('SHA256', STRINGTOUTF8('${SECRET_KEY}'),1), c.chat_id))) as chat_id,\s
              c.time_to_send,\s
              c.day_offset\s
          from chats c where abs(extract(epoch from(\s
          cast(:currentTime as timestamp with time zone)\s
          -\s
          CASE\s
            WHEN iso_day_of_week(current_date()) = 2 THEN cast(c.time_to_send as timestamp with time zone) + 1\s
            WHEN iso_day_of_week(current_date()) = 3 THEN cast(c.time_to_send as timestamp with time zone)\s
            WHEN iso_day_of_week(current_date()) = 4 THEN cast(c.time_to_send as timestamp with time zone) - 1\s
          END\s
          ))/60) < 0.5;""")
  List<Chat> findAllBetweenTime(OffsetTime currentTime);
}
