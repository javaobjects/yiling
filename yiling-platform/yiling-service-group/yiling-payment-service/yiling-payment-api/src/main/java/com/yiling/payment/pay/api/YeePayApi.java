package com.yiling.payment.pay.api;

import java.util.Map;

/**
 * @author dexi.yao
 * @date 2021-11-10
 */
public interface YeePayApi {

	/**
	 * 解密易宝回调参数
	 *
	 * @param ciphertext
	 * @return
	 */
	Map<String,Object> decrypt(String ciphertext);

	/**
	 * 解密易宝回调参数==易宝测试账号解密
	 *
	 * @param ciphertext
	 * @return
	 */
	Map<String,Object> decryptForTest(String ciphertext);
}
