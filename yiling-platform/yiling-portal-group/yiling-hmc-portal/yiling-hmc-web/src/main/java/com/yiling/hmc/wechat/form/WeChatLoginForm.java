package com.yiling.hmc.wechat.form;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotEmpty;

import com.yiling.framework.common.pojo.bo.TerminalInfo;
import com.yiling.framework.common.util.YlStrUtils;
import com.yiling.hmc.wechat.enums.HmcActivitySourceEnum;
import com.yiling.hmc.wechat.enums.SourceEnum;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * 微信登录入参
 *
 * @Description
 * @Author fan.shen
 * @Date 2022/3/23
 */
@Data
@ToString
@ApiModel(value = "WeChatLoginForm", description = "微信登录参数")
@Slf4j
public class WeChatLoginForm implements Serializable {

    private static final long serialVersionUID = -7722430332896313642L;
//
//    @NotEmpty
//	@ApiModelProperty(value = "临时登录凭证code ")
//	private String code;

//    @NotEmpty
//    @ApiModelProperty(value = "加密用户数据")
//    private String encryptedData;

//    @NotEmpty
//    @ApiModelProperty(value = "加密算法的初始向量")
//    private String iv;

    @NotEmpty
    @ApiModelProperty(value = "手机授权code ")
    private String phoneCode;

    @NotEmpty
    @ApiModelProperty(value = "手机加密数据")
    private String phoneEncryptedData;

    @NotEmpty
    @ApiModelProperty(value = "手机加密算法的初始向量")
    private String phoneIv;
//
//    @NotEmpty
//    @ApiModelProperty(value = "原始数据字符串")
//    private String signature;

    @ApiModelProperty(value = "终端设备信息")
    private TerminalInfo terminalInfo;

    /**
     * 唯一标志
     */
    @ApiModelProperty(value = "唯一标志")
    private String uk;

    @ApiModelProperty(value = "二维码值 （注册来源_企业id_员工id）")
    private String qr;

    @ApiModelProperty(value = "注册来源", hidden = true)
    private Integer source = SourceEnum.NATURE.getType();

    @ApiModelProperty(value = "店员或销售企业id", hidden = true)
    private Long sellerEid;

    @ApiModelProperty(value = "店员或销售id、医生id", hidden = true)
    private Long sellerUserId;

    @ApiModelProperty(value = "C端推荐用户id")
    private Long userId;

    @ApiModelProperty(value = "C端活动id")
    private Long activityId;

    @ApiModelProperty(value = "C端活动来源 1-患教活动，2-医带患，3-八子补肾")
    private Integer activitySource = HmcActivitySourceEnum.PATIENT_EDUCATE.getType();

    /**
     * 转换二维码参数
     */
    public void convertQrCodeValue() {

        // 扫码进入的
        if (StrUtil.isNotBlank(qr)) {
            log.info("qrCodeValue参数：{}", qr);

            Map<String, String> valueMap = YlStrUtils.dealParam(qr);
            if (valueMap.containsKey("so")) {
                source = Integer.valueOf(valueMap.get("so"));
            }

            // 店员或销售
            if (SourceEnum.STAFF_SELLER.getType().equals(source)) {

                sellerEid = Long.valueOf(valueMap.get("eId"));

                sellerUserId = Long.valueOf(valueMap.get("uId"));
            }

            // C端活动
            if (valueMap.containsKey("actId")) {
                // 活动id
                activityId = Long.valueOf(valueMap.get("actId"));
            }

            if (valueMap.containsKey("docId")) {
                // 注册来源 - 医生推荐
                source = SourceEnum.DOCTOR.getType();

                // 推荐人 - 医生id
                sellerUserId = Long.valueOf(valueMap.get("docId"));
            }

            // qt:40 -> 医带患活动二维码
            if (valueMap.containsKey("qt") && valueMap.get("qt").equals("40")) {
                // C端活动来源-> 2-医带患
                activitySource = HmcActivitySourceEnum.DOC_TO_PATIENT.getType();
            }
        }

        // 用户分享进入的
        if (Objects.nonNull(userId) && userId > 0) {

            // 这里赋值给sellerUserId
            sellerUserId = userId;

            // 这里赋值为用户推荐
            source = SourceEnum.USER_INVITE.getType();
        }

    }
}
