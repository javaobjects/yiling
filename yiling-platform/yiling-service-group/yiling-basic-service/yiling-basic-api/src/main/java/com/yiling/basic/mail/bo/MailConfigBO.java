package com.yiling.basic.mail.bo;


import java.io.Serializable;

import lombok.Data;

/**
 * @author shichen
 * @类名 MailConfigBO
 * @描述
 * @创建时间 2022/2/9
 * @修改人 shichen
 * @修改时间 2022/2/9
 **/
@Data
public class MailConfigBO implements Serializable {
    /**
     * 发件人
     */
    private String from;

    /**
     * 发件人昵称
     */
    protected String fromName;

    /**
     * 收件人
     */
    protected String[] to;

    /**
     * 标题
     */
    protected String subject;

    /**
     * 内容
     */
    protected String text;

    /**
     * 是否html
     */
    protected Boolean htmlFlag;

    public MailConfigBO getConfig(){
        MailConfigBO configBO = new MailConfigBO();
        configBO.setFrom(this.from);
        configBO.setFromName(this.fromName);
        configBO.setTo(this.to);
        configBO.setSubject(this.subject);
        configBO.setText(this.text);
        configBO.setHtmlFlag(this.htmlFlag);
        return configBO;
    }
}
