package com.eon37_dev.itswednesdaymydudes.repositories;

import com.eon37_dev.itswednesdaymydudes.model.Sticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StickerRepository extends JpaRepository<Sticker, Long> {
  @Query("select s from Sticker s order by rand() limit 1")
  Optional<Sticker> findRandom();
}
