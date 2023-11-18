package com.yiling.open.erp.dto;

import java.util.Date;

import com.yiling.framework.common.base.BaseDTO;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 ErpShopMappingDTO
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class ErpShopMappingDTO extends BaseDTO {
    /**
     * 总店企业id
     */
    private Long mainShopEid;

    /**
     * 总店名称
     */
    private String mainShopName;

    /**
     * 门店企业id
     */
    private Long shopEid;

    /**
     * 门店名称
     */
    private String shopName;

    /**
     * 门店编码
     */
    private String shopCode;

    /**
     * 同步状态 0：关闭 1：开启
     */
    private Integer syncStatus;

    private Date createTime;
}
