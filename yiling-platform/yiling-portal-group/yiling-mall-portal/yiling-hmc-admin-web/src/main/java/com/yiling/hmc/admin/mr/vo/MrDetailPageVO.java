package com.yiling.hmc.admin.mr.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 医药代表详情页 VO
 *
 * @author: xuan.zhou
 * @date: 2022/6/6
 */
@Data
public class MrDetailPageVO {

    @ApiModelProperty("药代基本信息")
    private BaseInfoVO baseInfo;

    @ApiModelProperty("药代可售药品信息列表")
    private List<SalesGoodsInfoVO> salesGoodsInfoList;

    @ApiModelProperty("药代绑定医生信息列表")
    private List<DoctorInfoVO> doctorInfoList;

    @Data
    public static class BaseInfoVO {

        @ApiModelProperty("工号")
        private String code;

        @ApiModelProperty("姓名")
        private String name;

        @ApiModelProperty("手机号")
        private String mobile;
    }

    @Data
    public static class SalesGoodsInfoVO {

        @ApiModelProperty("商品ID")
        private Long id;

        @ApiModelProperty("商品名称")
        private String name;

        @ApiModelProperty("售卖规格名称")
        private String sellSpecifications;
    }

    @Data
    public static class DoctorInfoVO {

        @ApiModelProperty("姓名")
        private String name;

        @ApiModelProperty("职称")
        private String jobTitle;

        @ApiModelProperty("手机号")
        private String mobile;

        @ApiModelProperty("身份证号")
        private String idNumber;

        @ApiModelProperty("机构名称")
        private String orgName;

        @ApiModelProperty("机构科室名称")
        private String deptName;

        @ApiModelProperty("实名认证状态")
        private String status;
    }
}
