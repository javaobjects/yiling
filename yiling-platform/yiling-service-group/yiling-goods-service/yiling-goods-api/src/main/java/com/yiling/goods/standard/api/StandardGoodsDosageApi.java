package com.yiling.goods.standard.api;

import java.util.List;

import com.yiling.goods.standard.dto.StandardGoodsDosageDTO;

/**
 * @author:wei.wang
 * @date:2021/5/21
 */
public interface StandardGoodsDosageApi {
    /**
     * 获取第一级剂型
     *
     * @return
     */
    List<StandardGoodsDosageDTO> getFirstDosage();
}
