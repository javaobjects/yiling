package com.yiling.dataflow.statistics.dto.request;

import java.io.Serializable;
import java.util.List;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author fucheng.bai
 * @date 2022/8/24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class FlushGoodsSpecIdRequest extends BaseRequest {

    private Long eid;

    private List<FlushDataRequest> flushDataList;

    @Data
    public static class FlushDataRequest implements Serializable {

        private String goodsName;

        private String spec;

        private String manufacturer;

        private String recommendGoodsName;

        private String recommendSpec;

        private Long recommendSpecificationId;

        private Integer recommendScore;
    }
}
