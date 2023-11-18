package com.yiling.basic.sms.entity;

import lombok.Data;

/**
 * @author: fei.wu <br>
 * @date: 2021/7/12 <br>
 */
@Data
public class SmsResultDO {

    Integer result;

    String  faillist;

    String  balance;

    String  linkid;

    String  description;

}
