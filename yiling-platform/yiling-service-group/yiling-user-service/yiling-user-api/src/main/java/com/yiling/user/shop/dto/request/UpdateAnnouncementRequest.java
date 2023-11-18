package com.yiling.user.shop.dto.request;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * B2B-修改公告 Request
 * </p>
 *
 * @author lun.yu
 * @date 2021/12/30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateAnnouncementRequest extends BaseRequest {

    /**
     * 店铺公告
     */
    private String shopAnnouncement;

    /**
     * 当前店铺企业ID
     */
    private Long shopEid;


}
