package com.yiling.b2b.admin.shop.vo;

import com.yiling.framework.common.base.BaseVO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-店铺公告VO
 * </p>
 *
 * @author lun.yu
 * @date 2021/12/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel
public class ShopAnnouncementVO extends BaseVO {

    /**
     * 店铺公告
     */
    @ApiModelProperty("店铺公告")
    private String shopAnnouncement;

}
