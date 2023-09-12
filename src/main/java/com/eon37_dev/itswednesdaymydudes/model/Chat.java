package com.eon37_dev.itswednesdaymydudes.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnTransformer;

import java.time.OffsetTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @Column(columnDefinition = "BINARY VARYING(16)", unique = true)
  @ColumnTransformer(
          read = "TRIM(CHAR(0) FROM UTF8TOSTRING(DECRYPT('AES', HASH('SHA256', STRINGTOUTF8('${SECRET_KEY}'),1), chat_id)))",
          write = "ENCRYPT('AES', HASH('SHA256', STRINGTOUTF8('${SECRET_KEY}'), 1), STRINGTOUTF8(?))")
  private String chatId;
  private OffsetTime timeToSend;
  private int dayOffset;
}
