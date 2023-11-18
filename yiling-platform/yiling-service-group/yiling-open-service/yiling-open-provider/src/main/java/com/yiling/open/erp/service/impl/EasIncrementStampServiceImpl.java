package com.yiling.open.erp.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.dao.EasIncrementStampMapper;
import com.yiling.open.erp.dto.EasIncrementStampDTO;
import com.yiling.open.erp.dto.request.SaveEasIncrementStampRequest;
import com.yiling.open.erp.entity.EasIncrementStampDO;
import com.yiling.open.erp.service.EasIncrementStampService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2021-08-02
 */
@Service
public class EasIncrementStampServiceImpl extends BaseServiceImpl<EasIncrementStampMapper, EasIncrementStampDO> implements EasIncrementStampService {

    @Override
    public EasIncrementStampDTO findEasIncrementStampBySuIdAndTaskNo(Long suId, String taskNo) {
        QueryWrapper<EasIncrementStampDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(EasIncrementStampDO::getSuId, suId);
        queryWrapper.lambda().eq(EasIncrementStampDO::getTaskNo, taskNo);

        return PojoUtils.map(this.getOne(queryWrapper), EasIncrementStampDTO.class);
    }

    @Override
    public boolean saveOrUpdateEasIncrementStamp(SaveEasIncrementStampRequest request) {
        EasIncrementStampDO easIncrementStampDO=PojoUtils.map(request,EasIncrementStampDO.class);
        return this.saveOrUpdate(easIncrementStampDO);
    }
}
