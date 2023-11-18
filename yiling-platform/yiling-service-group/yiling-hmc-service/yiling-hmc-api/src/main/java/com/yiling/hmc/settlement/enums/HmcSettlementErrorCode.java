package com.yiling.hmc.settlement.enums;

import com.yiling.framework.common.enums.IErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单相关错误枚举
 *
 * @author: yong.zhang
 * @date: 2022/3/30
 */
@Getter
@AllArgsConstructor
public enum HmcSettlementErrorCode implements IErrorCode {

    /**
     * 导入结算单的数据已经存在结算单
     */
    SETTLEMENT_EXISTS_ERROR(10111, "结算数据已经结算过"),

    /**
     * 保司导入的结算信息存在已经结算过的订单号
     */
    INSURANCE_SETTLEMENT_ORDER_EXIST_ERROR(10112, "保司导入的结算信息存在已经结算过的订单号"),

    /**
     * 已经处理过理赔信息回调
     */
    INSURANCE_SETTLEMENT_EXIST_ERROR(10113, "已经处理过理赔信息回调"),

    /**
     * 理赔资料上传出现错误
     */
    INSURANCE_RECORD_PIC_PUSH_ERROR(10114, "理赔资料上传出现错误"),

    /**
     * 用户身份证正面照不存在
     */
    ID_CARD_FRONT_PHOTO_NOT_EXIST(10115, "用户身份证正面照不存在"),

    /**
     * 用户身份证反面照不存在
     */
    ID_CARD_BACK_PHOTO_NOT_EXIST(10116, "用户身份证反面照不存在"),

    /**
     * 手写签名不存在
     */
    HAND_SIGNATURE_PICTURE_NOT_EXIST(10117, "手写签名不存在"),

    /**
     * 订单票据不存在
     */
    ORDER_RECEIPTS_PICTURE_NOT_EXIST(10118, "订单票据不存在"),

    /**
     * 处方图片不存在
     */
    ORDER_PRESCRIPTION_SNAPSHOT_PICTURE_NOT_EXIST(10118, "处方图片不存在"),

    /**
     * 理赔资料上传前下载缓存出现错误
     */
    INSURANCE_RECORD_DOWNLOAD_PIC_PUSH_ERROR(10119, "理赔资料上传前下载缓存出现错误"),
    ;

    private final Integer code;
    private final String message;
}
