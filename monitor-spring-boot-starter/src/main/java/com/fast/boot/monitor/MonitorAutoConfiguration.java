package com.fast.boot.monitor;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: junqing.li
 * @date: 17/8/16
 */
@Import(MonitorConfig.class)
@Configuration
@AutoConfigureAfter(MailSenderAutoConfiguration.class)
public class MonitorAutoConfiguration {



}
