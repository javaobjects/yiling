package com.yiling.goods.medicine.dto.request;

import java.util.Date;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 UpdateShelfLifeRequest
 * @描述
 * @创建时间 2023/5/4
 * @修改人 shichen
 * @修改时间 2023/5/4
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class UpdateShelfLifeRequest extends BaseRequest {

    private Long goodsId;

    /**
     * 生产日期
     */
    private Date manufacturingDate;

    /**
     * 有效期
     */
    private Date expiryDate;
}
