package com.yiling.hmc.goods.dto.request;

import com.yiling.framework.common.base.request.QueryPageListRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 GoodsControlPageRequest
 * @描述
 * @创建时间 2022/4/1
 * @修改人 shichen
 * @修改时间 2022/4/1
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GoodsControlPageRequest extends QueryPageListRequest {
    /**
     * 商品名称
     */
    private String name;
    /**
     * 注册证号
     */
    private String licenseNo;
}
