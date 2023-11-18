package com.yiling.payment.web.callback.form;

import com.yiling.framework.common.base.form.BaseForm;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/** 交行回调参数
 * @author zhigang.guo
 * @date: 2023/5/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BocomPayNotifyForm extends BaseForm {

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
