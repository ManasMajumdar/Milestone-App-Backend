package com.mindit.milestone.services.impl;

import com.mindit.milestone.contants.enums.AudienceType;
import com.mindit.milestone.data.entity.Audience;
import com.mindit.milestone.data.repository.AudienceRepository;
import com.mindit.milestone.exception.CommonErrorCode;
import com.mindit.milestone.exception.MMException;
import com.mindit.milestone.models.request.AudienceRequest;
import com.mindit.milestone.services.AudienceService;
import com.mindit.milestone.utils.EmailUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class AudienceServiceImpl implements AudienceService {

  private final AudienceRepository audienceRepository;
  private final JavaMailSender javaMailSender;

  @Override
  public void addAudience(List<AudienceRequest> audienceRequests) {
    try {
      String sendEmail = null;
      String ccEmail = null;
      ArrayList<String> emails = new ArrayList<>();
      for (AudienceRequest request : audienceRequests) {
        emails.add(request.getEmail());
      }
      for (AudienceRequest request : audienceRequests) {
        validateEmailAndThrowIfInvalid(request.getEmail());
        List<Audience> existingAudience = audienceRepository.findByEmail(request.getEmail());
        if (existingAudience.isEmpty()) {
          if ("TO".equalsIgnoreCase(request.getType())) {
            createNewAudience(request.getEmail(), "TO");
            createNewAudience(request.getEmail(), "CC");
          } else if ("CC".equalsIgnoreCase(request.getType())) {
            createNewAudience(request.getEmail(), "CC");
            createNewAudience(request.getEmail(), "TO");
          }
        } else {
          log.error(existingAudience.get(0) + " " + request.getType());
          updateExistingAudience(existingAudience.get(0), request.getType());
        }
        sendEmail = emails.get(0);
        ccEmail = emails.get(1);
        sendMail(sendEmail, ccEmail, request.getInbox());
      }

    } catch (MMException ex) {
      log.error("Error occurred while adding emails: [{}]", ex.getMessage());
      throw ex;
    } catch (MessagingException | IOException e) {
      log.error("Error occurred while sending email or handling IO: [{}]", e.getMessage());
    }
  }

  private void validateEmailAndThrowIfInvalid(String email) {
    if (!EmailUtil.isValidEmail(email)) {
      throw new MMException(CommonErrorCode.BAD_REQUEST);
    }
  }

  private void createNewAudience(String email, String type) {
    Audience audience = new Audience();
    audience.setEmail(email);
    audience.setIsActive(true);
    AudienceType audienceType = getAudienceType(type);
    audience.setType(audienceType);
    audienceRepository.save(audience);
  }

  private Audience updateExistingAudience(Audience existingAudience, String type) {
    try {
      if (existingAudience.getType() != getAudienceType(type)) {
        existingAudience.setType(getAudienceType(type));
        audienceRepository.save(existingAudience);
      }
    } catch (Exception e) {
      return null;
    }
    return null;
  }

  private AudienceType getAudienceType(String type) {
    if (type.equalsIgnoreCase(AudienceType.TO.name())) {
      return AudienceType.TO;
    } else if (type.equalsIgnoreCase(AudienceType.CC.name())) {
      return AudienceType.CC;
    } else {
      throw new MMException(CommonErrorCode.BAD_REQUEST);
    }
  }

  public void sendMail(String toRecipients, String ccRecipients, String inbox)
      throws MessagingException, IOException {
    MimeMessage message = javaMailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      String subject = "Important Mail!";
      helper.setFrom("mindhosting19@gmail.com");
      helper.setTo(toRecipients);
      if (ccRecipients != null) {
        helper.setCc(ccRecipients);
      }
      helper.setSubject(subject);
      helper.setText(inbox, true);
      javaMailSender.send(message);
      log.info("Email sent successfully to: {}, CC: {}", toRecipients, ccRecipients);
    } catch (MessagingException ex) {
      log.error("An error occurred while sending the email: {}", ex.getMessage());
      log.debug("MimeMessage content: {}", message.getContent());
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  @Override
  public List<String> getAudience(String type) {
    try {
      List<String> emailsList;
      if (AudienceType.TO.name().equalsIgnoreCase(type)) {
        log.info("Retrieving all email addresses from the database where type='TO'");
        emailsList = audienceRepository.getAllEmailsWhoseTypeIsTO();
      } else if (AudienceType.CC.name().equalsIgnoreCase(type)) {
        log.info("Retrieving all email addresses from the database where type='CC'");
        emailsList = audienceRepository.getAllEmailsWhoseTypeIsCC();
      } else {
        throw new MMException(CommonErrorCode.BAD_REQUEST);
      }
      return emailsList;
    } catch (MMException ex) {
      log.error("An error occurred while retrieving email addresses: {}", ex.getMessage());
      throw new MMException(CommonErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}
