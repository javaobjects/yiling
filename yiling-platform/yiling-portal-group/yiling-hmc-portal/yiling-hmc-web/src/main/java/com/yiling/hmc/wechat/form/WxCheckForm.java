package com.yiling.hmc.wechat.form;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

/**
 * 微信服务token校验入参
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */
@Data
@ToString
public class WxCheckForm implements Serializable {

	private static final long serialVersionUID = -7722430332896313642L;

	private String signature;

	private String timestamp;

	private String nonce;

	private String echostr;

}
