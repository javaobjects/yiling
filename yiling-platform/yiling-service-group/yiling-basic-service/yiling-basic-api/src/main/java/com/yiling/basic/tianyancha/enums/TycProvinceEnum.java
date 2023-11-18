package com.yiling.basic.tianyancha.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author shichen
 * @类名 TycProvinceEnum
 * @描述
 * @创建时间 2022/1/21
 * @修改人 shichen
 * @修改时间 2022/1/21
 **/
@Getter
@AllArgsConstructor
public enum TycProvinceEnum {
    GUO_JIA("gj","国家"),
    SHANG_HAI("sh","上海市"),
    BEI_JING("bj","北京市"),
    TIAN_JIN("tj","天津市"),
    GUANG_DONG("gd","广东省"),
    ZHE_JIANG("zj","浙江省"),
    FU_JIAN("fj","福建省"),
    HAI_NAN("han","海南省"),
    CHONG_QING("cq","重庆市"),
    JIANG_SU("js","江苏省"),
    HE_BEI("heb","河北省"),
    SI_CHUAN("sc","四川省"),
    HE_NAN("hen","河南省"),
    AN_HAI("ah","安徽省"),
    SHAN_N_XI("snx","陕西省"),
    SHAN_DONG("sd","山东省"),
    NING_XIA("nx","宁夏回族自治区"),
    GUI_ZHOU("gz","贵州省"),
    JIANG_XI("jx","江西省"),
    GUANG_XI("gx","广西壮族自治区"),
    SHAN_XI("sx","山西省"),
    XIN_JIANG("xj","新疆维吾尔自治区"),
    HU_BEI("hub","湖北省"),
    LIAO_NING("ln","辽宁省"),
    NEI_MENG_GU("nmg","内蒙古自治区"),
    JI_LIN("jl","吉林省"),
    QING_HAI("qh","青海省"),
    XI_ZANG("xz","西藏自治区"),
    YUN_NAN("yn","云南省"),
    GAN_SU("gs","甘肃省"),
    HEI_LONG_JIANG("hlj","黑龙江省"),
    HU_NAN("hun","湖南省"),
    ;
    private String code;

    private String provinceName;

    public static TycProvinceEnum getByCode(String code) {
        for (TycProvinceEnum e : TycProvinceEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }

}
