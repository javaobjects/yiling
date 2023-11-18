package com.yiling.admin.data.center.report.vo;

import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 报表参数-商品类型
 * </p>
 *
 * @author gxl
 * @date 2022-02-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ReportGoodsCategoryPageVO<T> extends Page<T> {

    /**
     * 商品分类
     */
    @ApiModelProperty(value = "商品分类")
    private String category;

    @Data
    public static class ReportGoodsCategoryItemVO {

        /**
         * id
         */
        @ApiModelProperty("id")
        private Long id;

        /**
         * 对应以岭的商品id
         */
        @ApiModelProperty("对应以岭的商品id")
        private Long ylGoodsId;

        /**
         * 商品名称
         */
        @ApiModelProperty("商品名称")
        private String goodsName;

        /**
         * 商品规格
         */
        @ApiModelProperty("商品规格")
        private String goodsSpecification;

        /**
         * 开始时间
         */
        @ApiModelProperty("开始时间")
        private Date startTime;

        /**
         * 结束时间
         */
        @ApiModelProperty("结束时间")
        private Date endTime;

        /**
         * 修改时间
         */
        @ApiModelProperty("修改时间")
        private Date updateTime;

        /**
         * 修改人
         */
        @JsonIgnore
        @ApiModelProperty(value = "修改人", hidden = true)
        private Long updateUser;

        /**
         * 操作人
         */
        @ApiModelProperty("操作人")
        private String updateUserName;
    }
}
