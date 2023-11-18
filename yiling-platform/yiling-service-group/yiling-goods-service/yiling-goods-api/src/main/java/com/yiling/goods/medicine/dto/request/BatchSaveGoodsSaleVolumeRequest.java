package com.yiling.goods.medicine.dto.request;

import java.util.Date;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author shichen
 * @类名 BatchSaveGoodsSaleVolumeRequest
 * @描述
 * @创建时间 2023/5/10
 * @修改人 shichen
 * @修改时间 2023/5/10
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class BatchSaveGoodsSaleVolumeRequest extends BaseRequest {

    /**
     * 销售日期
     */
    private Date saleDate;

    /**
     * 销售日期
     */
    private List<SaveGoodsSaleVolumeRequest> goodsSaleVolumeList;

    @Data
    public static class SaveGoodsSaleVolumeRequest extends BaseRequest{

        /**
         * 商品id
         */
        private Long goodsId;

        /**
         * 商品产品线
         */
        private Integer goodsLine;

        /**
         * 标准库id
         */
        private Long standardId;

        /**
         * 规格id
         */
        private Long sellSpecificationsId;

        /**
         * 销量
         */
        private Long volume;
    }
}
