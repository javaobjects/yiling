package com.yiling.dataflow.agency.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 以岭级别
 *
 * @author: shixing.sun
 * @date: 2023/2/17
 */
@Getter
@AllArgsConstructor
public enum CrmYlLevelEnum {

    PROVINCE(1, "省级三级"),
    CITY(2, "市级三级"),
    ZHUAN(3, "专科医院三级"),
    ARMY(4, "部队三级"),
    OTHERTWO(5, "其他二级医院"),
    CITYPEOPLE(6, "县人民医院"),
    CITYONE(7, "市级一级"),
    OTHERCITY(8, "县级其他"),
    UNIVERSITY(9, "其他厂矿职工高校"),
    OTHERPRIVATE(10, "其他私人终端"), CENTURYWEISHENG(11, "乡镇卫生院"),
    SHEQU(12, "社区卫生服务中心"),
    SHEQUFUWUZHAN(13, "社区卫生服务站"),
    SHIJIQITA(14, "市级其他"),
    XIANZHONGYI(15, "县中医院"),
    KEWEISHENGYUAN(16, "村卫生室"),
    QITACHANGZHIGONG(17, "部队二级"),
    ZHUANKEYIYUAN(18, "专科医院二级"),
    SIRENZHONGDUAN(19, "私人终端二级"),
    CHANGKUANG(20, "厂矿职工高校二级"),
    SIREN(21, "私人终端三级"),
    CHANGKUANGGAOXIAO(22, "厂矿职工高校三级"),
    QITAZHUANKE(23, "其他专科医院"),
    OTHERARMY(24, "其他部队医院"),
    XIANJISANJI(25, "县级三级"),
    XIANGZHEN(26, "乡镇卫生院二级"),
    SHEQUFUWUERJI(27, "社区卫生服务中心二级"),
    SHEQUWEISHENGZHONGXIN(28, "社区卫生服务中心三级")
    ;

    private final Integer code;
    private final String name;

    public static CrmYlLevelEnum getByCode(Integer code) {
        for (CrmYlLevelEnum e : CrmYlLevelEnum.values()) {
            if (e.getCode().equals(code)) {
                return e;
            }
        }
        return null;
    }
}
