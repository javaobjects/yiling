package com.yiling.payment.pay.ai.impl;

import java.util.Map;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.payment.channel.yee.YeePayService;
import com.yiling.payment.pay.api.YeePayApi;

import lombok.extern.slf4j.Slf4j;

/**
 * @author dexi.yao
 * @date 2021-11-10
 */
@DubboService
@Slf4j
public class YeePayApiImpl implements YeePayApi {

	@Autowired
	YeePayService yeePayService;

	@Override
	public Map<String,Object> decrypt(String ciphertext) {
		return yeePayService.decrypt(ciphertext);
	}

	@Override
	public Map<String, Object> decryptForTest(String ciphertext) {
		return yeePayService.decryptForTest(ciphertext);
	}
}
