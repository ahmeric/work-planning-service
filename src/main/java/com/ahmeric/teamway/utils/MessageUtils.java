package com.ahmeric.teamway.utils;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtils {

  private static MessageSource messageSource;

  @Autowired
  public MessageUtils(MessageSource messageSource) {
    MessageUtils.messageSource = messageSource;
  }

  public static String getMessage(String messageKey) {
    return messageSource.getMessage(messageKey, null, Locale.getDefault());
  }
}

