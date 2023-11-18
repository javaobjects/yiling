package com.yiling.basic.mail.api;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.yiling.basic.mail.bo.MailConfigBO;
import com.yiling.basic.mail.enums.MailEnum;
import com.yiling.framework.common.base.BaseDTO;

/**
 * @author shichen
 * @类名 MailApi
 * @描述
 * @创建时间 2022/3/9
 * @修改人 shichen
 * @修改时间 2022/3/9
 **/
public interface MailApi {

    public Boolean sendHtmlMail(MailConfigBO mailConfig, String mailId, JSONArray jsonArray);

    public List<Long> getSendSuccessBusinessIds(MailEnum mailEnum, List<Long> businessIds);
}
