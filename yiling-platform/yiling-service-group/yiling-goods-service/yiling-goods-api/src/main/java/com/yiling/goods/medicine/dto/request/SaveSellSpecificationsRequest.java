package com.yiling.goods.medicine.dto.request;

import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * description: 关联商品的售卖规格，如果商品的标准库id未关联。此处会一并更新标准库id <br>
 * date: 2020/4/29 13:46 <br>
 * @author shuan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class SaveSellSpecificationsRequest extends BaseRequest {

    private static final long serialVersionUID = -4672345329942342009L;

    /**
     * 审核ID
     */
    private Long id;

    /**
     * 标准库id
     */
    private Long standardId;

    /**
     * 销售规格（关联rk_sell_specifications表id）
     */
    private Long sellSpecificationsId;

    /**
     * pop的企业编号
     */
    private List<Long> popEidList;

}
