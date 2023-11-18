package com.yiling.admin.hmc.goods.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: shuang.zhang
 * @date: 2021/6/3
 */
@Data
public class EnterpriseGoodsListVO {
    @ApiModelProperty(value = "企业ID")
    private Long id;
    /**
     * 状态：1-启用 2-停用
     */
    @ApiModelProperty(value = "状态：1-启用 2-停用")
    private Integer status;
    /**
     * 企业名称
     */
    @ApiModelProperty(value = "企业名称")
    private String name;
    /**
     * 类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所
     */
    @ApiModelProperty(value = "类型：1-工业 2-商业 3-连锁总店 4-连锁直营 5-连锁加盟 6-单体药房 7-医院 8-诊所")
    private Integer type;

    /**
     * 联系人
     */
    @ApiModelProperty(value = "联系人")
    private String contactor;

    /**
     * 联系人电话
     */
    @ApiModelProperty(value = "联系人电话")
    private String contactorPhone;


    /**
     * 上架
     */
    @ApiModelProperty(value = "上架")
    private Long upShelfCount;

    /**
     * 下架
     */
    @ApiModelProperty(value = "下架")
    private Long unShelfCount;

    private Long goodsCount;

}
