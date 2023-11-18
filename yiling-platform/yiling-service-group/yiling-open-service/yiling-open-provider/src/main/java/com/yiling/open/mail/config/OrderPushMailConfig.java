package com.yiling.open.mail.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.yiling.basic.mail.bo.MailConfigBO;
/**
 * @author shichen
 * @类名 OrderPushMailDTO
 * @描述
 * @创建时间 2022/2/9
 * @修改人 shichen
 * @修改时间 2022/2/9
 **/
@Component
@ConfigurationProperties(prefix = "mail.order-push-fail")
@RefreshScope
@Data
public class OrderPushMailConfig extends MailConfigBO {
}
