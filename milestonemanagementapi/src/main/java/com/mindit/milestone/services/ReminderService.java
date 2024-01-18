package com.mindit.milestone.services;

import jakarta.mail.MessagingException;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public interface ReminderService {
  void GetProjectDate() throws MessagingException, IOException;

  void GetMilestoneDate() throws MessagingException, IOException;
}
