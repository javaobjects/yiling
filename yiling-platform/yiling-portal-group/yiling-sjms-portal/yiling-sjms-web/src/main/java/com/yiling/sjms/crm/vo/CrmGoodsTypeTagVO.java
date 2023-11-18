package com.yiling.sjms.crm.vo;

import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 CrmGoodsTypeTagVO
 * @描述
 * @创建时间 2023/5/29
 * @修改人 shichen
 * @修改时间 2023/5/29
 **/
@Data
@Accessors(chain = true)
public class CrmGoodsTypeTagVO {
    @ApiModelProperty(value = "非锁标签列表")
    private List<CrmGoodsTagVO> notLockTagList;

    @ApiModelProperty(value = "非锁标签列表")
    private List<CrmGoodsTagVO> groupPurchaseTagList;
}
