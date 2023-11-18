package com.yiling.payment.channel.yee;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;
import java.util.TreeMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yeepay.g3.sdk.yop.client.YopResponse;
import com.yeepay.g3.sdk.yop.encrypt.CertTypeEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigestAlgEnum;
import com.yeepay.g3.sdk.yop.encrypt.DigitalSignatureDTO;
import com.yeepay.g3.sdk.yop.utils.DigitalEnvelopeUtils;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.payment.enums.PaymentErrorCode;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * @author dexi.yao
 * @date 2021-11-08
 */
@Slf4j
public class YeePayUtil {

	/**
	 * 获取请求收银台的sign
	 *
	 * @param stringBuilder
	 * @param appKey
	 * @param privateKey
	 * @return
	 */
	public static String getSign(String stringBuilder, String appKey, String privateKey) {
		PrivateKey isvPrivateKey = getPrivateKey(privateKey);
		DigitalSignatureDTO digitalSignatureDTO = new DigitalSignatureDTO();
		digitalSignatureDTO.setAppKey(appKey);
		digitalSignatureDTO.setCertType(CertTypeEnum.RSA2048);
		digitalSignatureDTO.setDigestAlg(DigestAlgEnum.SHA256);
		digitalSignatureDTO.setPlainText(stringBuilder);
		String sign = DigitalEnvelopeUtils.sign0(digitalSignatureDTO, isvPrivateKey);
		return sign;

	}

	/**
	 * 将获取到的response转换成json格式
	 *
	 * @param response
	 * @return
	 */
	public static Map<String, String> parseResponse(YopResponse response) {

		Map<String, String> jsonMap;
		jsonMap = JSON.parseObject(response.getStringResult(), new TypeReference<TreeMap<String, String>>() {
		});
        if (ObjectUtil.isNull(jsonMap)){
            log.error("向易宝发起请求返回的response解析后为空,参数={}，异常信息={}",JSON.toJSONString(response),JSON.toJSON(response.getError()));
            throw new BusinessException(PaymentErrorCode.YEE_RESPONSE_IS_NULL);
        }
		return jsonMap;
	}

	/**
	 * 获取私钥对象
	 *
	 * @param priKey
	 * @return
	 */
	public static PrivateKey getPrivateKey(String priKey) {
		PrivateKey privateKey;
		PKCS8EncodedKeySpec priPKCS8;
		try {

			priPKCS8 = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(priKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			privateKey = keyf.generatePrivate(priPKCS8);
		} catch (NoSuchAlgorithmException e) {
			log.error("实例化易宝私钥异常，私钥：{}，原因：{}", priKey, e);
			throw new BusinessException(PaymentErrorCode.YEE_PRIVATE_KEY_FAIL);
		} catch (InvalidKeySpecException e) {
			log.error("实例化易宝私钥异常，私钥：{}，原因：{}", priKey, e);
			throw new BusinessException(PaymentErrorCode.YEE_PRIVATE_KEY_FAIL);
		}
		return privateKey;

	}

	/**
	 * 实例化公钥
	 *
	 * @return
	 */
	public static PublicKey getPubKey(String publicKeyStr) {
		PublicKey publicKey;
		try {
			java.security.spec.X509EncodedKeySpec bobPubKeySpec = new java.security.spec.X509EncodedKeySpec(
					Base64.getDecoder().decode(publicKeyStr));
			// RSA对称加密算法
			KeyFactory keyFactory= KeyFactory.getInstance("RSA");
			// 取公钥匙对象
			publicKey = keyFactory.generatePublic(bobPubKeySpec);
		} catch (NoSuchAlgorithmException e) {
			log.error("实例化易宝公钥异常，公钥：{}，原因：{}", publicKeyStr, e);
			throw new BusinessException(PaymentErrorCode.YEE_PUBLIC_KEY_FAIL);
		} catch (InvalidKeySpecException e) {
			log.error("实例化易宝公钥异常，公钥：{}，原因：{}", publicKeyStr, e);
			throw new BusinessException(PaymentErrorCode.YEE_PUBLIC_KEY_FAIL);
		}
		return publicKey;
	}

}
