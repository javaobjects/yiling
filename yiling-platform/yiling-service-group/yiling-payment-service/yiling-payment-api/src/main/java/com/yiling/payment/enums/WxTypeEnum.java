package com.yiling.payment.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WxTypeEnum {

    /**
     * 以岭健康管理中心公众号
     */
    HMC_MP_PROD("wxfbd61abfd05c127e", "以岭健康管理中心公众号", TypeEnum.gzh),

    /**
     * 补方测试公众号
     */
    HMC_MP_TEST("wxbdb7fce20be7cf4d", "补方测试公众号", TypeEnum.gzh),

    /**
     * 大运河服务号
     */
    B2B_MP_PROD("wx848e6606c18a0413", "大运河服务号", TypeEnum.gzh),

    /**
     * 大运河服务号小程序
     */
    B2B_MA_PROD("wx21a99659d9ea1dbd", "大运河服务号小程序", TypeEnum.miniProgram),
    ;

    private String appId;

    private String name;

    private TypeEnum type;

    public static WxTypeEnum getByAppId(String appId) {
        for (WxTypeEnum e : WxTypeEnum.values()) {
            if (e.getAppId().equals(appId)) {
                return e;
            }
        }
        return null;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum TypeEnum {

        gzh("mp", "公众号"), miniProgram("ma", "小程序");

        private String code;

        private String name;

        public static TypeEnum getByCode(String code) {
            for (TypeEnum e : TypeEnum.values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
            return null;
        }
    }


}
