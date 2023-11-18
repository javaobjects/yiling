package com.yiling.admin.erp.enterprise.vo;

import java.util.Date;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author shichen
 * @类名 ErpShopMappingVO
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Data
public class ErpShopMappingVO extends BaseVO {

    /**
     * 总店企业id
     */
    @ApiModelProperty(value = "总店企业id")
    private Long mainShopEid;

    /**
     * 总店名称
     */
    @ApiModelProperty(value = "总店名称")
    private String mainShopName;

    /**
     * 门店企业id
     */
    @ApiModelProperty(value = "门店企业id")
    private Long shopEid;

    /**
     * 门店名称
     */
    @ApiModelProperty(value = "门店名称")
    private String shopName;

    /**
     * 门店编码
     */
    @ApiModelProperty(value = "门店编码")
    private String shopCode;

    /**
     * 同步状态 0：关闭 1：开启
     */
    @ApiModelProperty(value = "同步状态 0：关闭 1：开启")
    private Integer syncStatus;

    @ApiModelProperty(value = "创建时间/同步时间")
    private Date createTime;
}
