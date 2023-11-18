package com.yiling.open.mail.service;


import java.util.List;

/**
 * @author shichen
 * @类名 ErpOrderMailService
 * @描述
 * @创建时间 2022/2/9
 * @修改人 shichen
 * @修改时间 2022/2/9
 **/
public interface ErpOrderMailService {
    /**
     * 订单推送失败发送邮件
     * @param orderIds
     * @return
     */
    public boolean sendMailByPushOrderFail(List<Long> orderIds);
}
