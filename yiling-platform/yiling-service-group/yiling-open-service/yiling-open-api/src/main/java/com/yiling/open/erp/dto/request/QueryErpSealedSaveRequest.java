package com.yiling.open.erp.dto.request;

import java.util.List;
import java.util.Map;

import com.yiling.framework.common.base.request.BaseRequest;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author: houjie.sun
 * @date: 2022/4/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class QueryErpSealedSaveRequest extends BaseRequest {

    /**
     * 商业ID
     */
    private List<Long> eidList;

    /**
     * 流向类型，字典(erp_flow_type)：1-采购流向 2-销售流向 3-连锁纯销流向 0-全部
     */
    private List<Integer> typeList;

    /**
     * 封存月份, 仅支持前推6个整月
     */
    private List<String> monthList;

    /**
     * 商业id、商业名称
     */
    private Map<Long,String> enameMap;

    /**
     * 初始化
     */
    public QueryErpSealedSaveRequest(){
        this.enameMap = MapUtil.empty();
    }

}
