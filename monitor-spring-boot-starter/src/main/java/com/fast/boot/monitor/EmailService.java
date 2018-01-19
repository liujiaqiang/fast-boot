package com.fast.boot.monitor;

import java.util.Date;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.common.base.Strings;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: junqing.li
 * @date: 17/8/15
 */
@Slf4j
@Component
public class EmailService {


  @Autowired
  private MailProperties mailProperties;

  @Autowired
  private JavaMailSender mailSender;


  /**
   * 
   * @param appName
   * @param map
   */
  public void sendHtmlMail(String appName, Map<String, String> map) {

    try {

      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(message, true);
      helper.setFrom(mailProperties.getProperties().get("from"));
      String to = mailProperties.getProperties().get("to");

      if (Strings.isNullOrEmpty(to)) {

        log.info("[sendHtmlMail] send email to is empty");
        return;
      }

      helper.setTo(StringUtils.commaDelimitedListToStringArray(to));
      helper.setSubject(appName + "-异常报警");
      helper.setSentDate(new Date());
      StringBuilder content = new StringBuilder(512);
      for (String key : map.keySet()) {

        String value = map.get(key);
        if (Strings.isNullOrEmpty(value)) {
          continue;
        }
        if (value.length() > 300) {
          content.append("<div>").append(key).append(": ").append("<pre>").append(value)
              .append("</pre>").append("</div>");
          continue;
        }

        content.append("<div>").append(key).append(": ").append(map.get(key)).append("</div>");
      }
      helper.setText(content.toString() + htmlFooter(appName), true);
      mailSender.send(message);

    } catch (Exception e) {

      log.error("[sendHtmlMail] send slack email fail ", e);

    }

  }

  /**
   * 邮件footer
   * 
   * @param appName
   * @return
   */
  private String htmlFooter(String appName) {
    StringBuilder sb = new StringBuilder();
    sb.append("<p>--------------------------------------------</p>");
    sb.append("<p>" + appName + "</p>");
    sb.append("<p>").append("该邮件是系统自动发送，请勿回复").append("</p>");

    return sb.toString();
  }

}
