package com.yiling.hmc.wechat.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * 微信公众号签名入参
 * @Description
 * @Author fan.shen
 * @Date 2022-12-30
 */
@Data
@ToString
public class WxSignatureForm implements Serializable {

	private static final long serialVersionUID = -7722430332896313642L;

	private String url;

}
