package com.yiling.hmc.insurance.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 0-成功；1-保单不存在；2-保单已失效；3-药品目录校验未通过；
 * 4-药品购药限制校验未通过（超过购药次数或购药金额等的限制）；5-处方诊断结果中的疾病不再疾病目录中；6-批准文号错误（西药）；
 * 7-存在除外药品（中药）；8-服务使用人与被保人不一致;9-账户金锁定失败；
 * 99-失败。不会控制的校验，试算理算就不会出现相应的结果编码
 *
 * @author: yong.zhang
 * @date: 2022/7/11
 */
@Getter
@AllArgsConstructor
public enum TkResultCodeEnum {

    /**
     * 0-成功
     */
    SUCCESS(0, "成功"),

    /**
     * 1-保单不存在
     */
    INSURANCE_RECORD_NOT_EXISTS(1, "保单不存在"),

    /**
     * 2-保单已失效
     */
    INSURANCE_RECORD_FAILURE(2, "保单不存在"),

    /**
     * 3-药品目录校验未通过
     */
    DRUG_LIST_FAILED(3, "药品目录校验未通过"),

    /**
     * 4-药品购药限制校验未通过（超过购药次数或购药金额等的限制）
     */
    DRUG_LIMIT_CHECK_FAILED(4, "药品购药限制校验未通过（超过购药次数或购药金额等的限制）"),

    /**
     * 5-处方诊断结果中的疾病不再疾病目录中
     */
    DISEASE_NOT_LIST(5, "处方诊断结果中的疾病不再疾病目录中"),

    /**
     * 6-批准文号错误（西药）
     */
    NUMBER_INCORRECT(6, "批准文号错误（西药）"),

    /**
     * 7-存在除外药品（中药）
     */
    EXCLUDED_DRUGS_EXISTS(7, "存在除外药品（中药）"),

    /**
     * 8-服务使用人与被保人不一致
     */
    USER_INCONSISTENT(8, "服务使用人与被保人不一致"),

    /**
     * 9-账户金锁定失败；
     */
    ACCOUNT_LOCK_FAILED(9, "账户金锁定失败"),

    /**
     * 99-失败。不会控制的校验，试算理算就不会出现相应的结果编码
     */
    CHECK_NOT(99, "失败。不会控制的校验，试算理算就不会出现相应的结果编码"),

    /**
     * 同步订单数据到保司出现连接问题，无法推送过去
     */
    CONNECT_REFUSE(10113, "同步订单数据到保司出现连接问题，无法推送过去"),
    ;

    private final Integer code;
    private final String name;

    public static TkResultCodeEnum getByCode(Integer code) {
        for (TkResultCodeEnum e : TkResultCodeEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
