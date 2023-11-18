package com.yiling.goods.standard.api.impl;

import java.util.List;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.standard.api.StandardGoodsDosageApi;
import com.yiling.goods.standard.dto.StandardGoodsDosageDTO;
import com.yiling.goods.standard.entity.StandardGoodsDosageDO;
import com.yiling.goods.standard.service.StandardGoodsDosageService;

/**
 * 剂型选择
 *
 * @author:wei.wang
 * @date:2021/5/21
 */
@DubboService
public class StandardGoodsDosageApiImpl implements StandardGoodsDosageApi {

    @Autowired
    private StandardGoodsDosageService standardGoodsDosageService;

    /**
     * 获取第一级剂型
     *
     * @return
     */
    @Override
    public List<StandardGoodsDosageDTO> getFirstDosage() {
        List<StandardGoodsDosageDO> result = standardGoodsDosageService.getFirstDosage();
        return PojoUtils.map(result,StandardGoodsDosageDTO.class);

    }
}
