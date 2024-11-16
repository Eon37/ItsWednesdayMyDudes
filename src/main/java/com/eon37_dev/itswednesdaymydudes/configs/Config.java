package com.eon37_dev.itswednesdaymydudes.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class Config {
  @Bean
  public Set<String> adminChats(@Value("${telegram.bot.admin_chats}") String adminChats) {
    return Arrays.stream(adminChats.split(",")).collect(Collectors.toUnmodifiableSet());
  }
}
