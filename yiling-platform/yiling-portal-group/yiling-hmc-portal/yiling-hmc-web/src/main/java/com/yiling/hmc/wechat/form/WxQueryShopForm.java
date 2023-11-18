package com.yiling.hmc.wechat.form;

import cn.hutool.core.util.StrUtil;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.hmc.wechat.enums.SourceEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;

/**
 * 查询店铺入参
 * @Description
 * @Author fan.shen
 * @Date 2022/3/25
 */
@Data
@ToString
@ApiModel(value = "WxQueryShopForm", description = "查询店铺入参")
@Slf4j
public class WxQueryShopForm implements Serializable {

	private static final long serialVersionUID = -7722430332896313642L;

	@ApiModelProperty(value = "售卖规格ID")
	private Long sellSpecificationsId;

	/**
	 * 保险ID
	 */
	@ApiModelProperty(value = "保险ID")
	private Long insuranceId;

	@ApiModelProperty(value = "二维码值 （注册来源_企业id_员工id）")
	private String qr;

	@ApiModelProperty(value = "注册来源", hidden = true)
	private Integer source;

	@ApiModelProperty(value = "企业id", hidden = true)
	private Long sellerEid;

	@ApiModelProperty(value = "店员或销售id", hidden = true)
	private Long sellerUserId;

	/**
	 * 转换二维码参数
	 */
	public void convertQrCodeValue() {

		if (StrUtil.isEmpty(qr)) {
			return;
		}

		log.info("qrCodeValue参数：{}", qr);

		Map<String, String> valueMap = YlStrUtils.dealParam(qr);
		if (valueMap.containsKey("so")) {
			source = Integer.valueOf(valueMap.get("so"));
		}

		// 店员或销售
		if (SourceEnum.STAFF_SELLER.getType().equals(source)) {

			sellerEid = Long.valueOf(valueMap.get("sEId"));

			sellerUserId = Long.valueOf(valueMap.get("sUId"));
		}

	}
}
