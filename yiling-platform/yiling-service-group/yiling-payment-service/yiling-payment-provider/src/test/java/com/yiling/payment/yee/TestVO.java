package com.yiling.payment.yee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author zhigang.guo
 * @date: 2023/5/26
 */
@Data
@Accessors(chain = true)
public class TestVO {

    private String biz_content;

    private String msg_id;

    private String timestamp;

    private String encrypt_key;

    private String sign;


    @Override
    public String toString() {

        return  "{" + "\"biz_content\":\"" + biz_content  +    "\",\"msg_id\":\"" + msg_id  + "\",\"timestamp\":\"" + timestamp + "\",\"encrypt_key\":\"" + encrypt_key + "\",\"sign\":\"" + sign + "\"}";
    }
}
