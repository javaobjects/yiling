package com.yiling.open.cms.mr.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 医药代表全信息 VO
 *
 * @author: xuan.zhou
 * @date: 2022/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class MrFullInfoVO extends MrInfoVO {

    @ApiModelProperty("所属企业")
    private String ename;

    @ApiModelProperty("关联可售药品名称集合")
    private List<SalesGoodsInfoVO> SalesGoodsInfoList;

    @Data
    public static class SalesGoodsInfoVO {

        @ApiModelProperty("药品ID")
        private Long id;

        @ApiModelProperty("药品名称")
        private String name;

        @ApiModelProperty("销售规格")
        private String sellSpecifications;
    }
}
