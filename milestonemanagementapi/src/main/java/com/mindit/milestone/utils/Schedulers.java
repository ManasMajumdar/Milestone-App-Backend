package com.mindit.milestone.utils;

import com.mindit.milestone.services.ReminderService;
import jakarta.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class Schedulers {
  private final ReminderService reminderService;

  @Scheduled(cron = "0 00 11 * * ?")
  public void ProjectEmailTask() throws InterruptedException, MessagingException, IOException {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDateTime = currentDate.format(formatter);
    reminderService.GetProjectDate();
    log.info("Scheduler time" + formattedDateTime);
  }

  @Scheduled(cron = "0 00 11 * * ?")
  public void MilestoneEmailTask() throws InterruptedException, MessagingException, IOException {
    LocalDateTime currentDate = LocalDateTime.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    String formattedDateTime = currentDate.format(formatter);
    reminderService.GetMilestoneDate();
    log.info("Scheduler time" + formattedDateTime);
  }
}
