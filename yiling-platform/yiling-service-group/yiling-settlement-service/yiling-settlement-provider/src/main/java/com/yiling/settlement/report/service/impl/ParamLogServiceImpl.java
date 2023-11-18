package com.yiling.settlement.report.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.goods.ylprice.dto.request.AddGoodsYilingPriceLogRequest;
import com.yiling.settlement.report.dao.ParamLogMapper;
import com.yiling.settlement.report.entity.ParamLogDO;
import com.yiling.settlement.report.entity.ParamSubDO;
import com.yiling.settlement.report.entity.ParamSubGoodsDO;
import com.yiling.settlement.report.service.ParamLogService;

import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 参数操作日志表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-06-09
 */
@Service
public class ParamLogServiceImpl extends BaseServiceImpl<ParamLogMapper, ParamLogDO> implements ParamLogService {


    @Override
    public void addOarSubGoodsLog(ParamSubGoodsDO before, ParamSubGoodsDO after, Long userId) {
        ParamLogDO paramLogDO = new ParamLogDO();
        paramLogDO.setOpUserId(userId);
        paramLogDO.setOpTime(new Date());

        if (ObjectUtil.isNull(before) && ObjectUtil.isNotNull(after)) {
            paramLogDO.setDataId(after.getId());
            paramLogDO.setParamId(after.getParamId());
            paramLogDO.setParamSubId(after.getParamSubId());
            paramLogDO.setParType(after.getParType());
            paramLogDO.setLogType(1);
            paramLogDO.setAfterData(JSON.toJSONString(after));
        } else if (ObjectUtil.isNotNull(before) && ObjectUtil.isNotNull(after)) {
            paramLogDO.setDataId(before.getId());
            paramLogDO.setParamId(before.getParamId());
            paramLogDO.setParamSubId(before.getParamSubId());
            paramLogDO.setParType(before.getParType());
            paramLogDO.setLogType(2);
            paramLogDO.setBeforeData(JSON.toJSONString(before));
            paramLogDO.setAfterData(JSON.toJSONString(after));
        } else if (ObjectUtil.isNotNull(before) && ObjectUtil.isNull(after)) {
            paramLogDO.setDataId(before.getId());
            paramLogDO.setParamId(before.getParamId());
            paramLogDO.setParamSubId(before.getParamSubId());
            paramLogDO.setParType(before.getParType());
            paramLogDO.setLogType(3);
            paramLogDO.setBeforeData(JSON.toJSONString(before));
        } else {
            return;
        }
        save(paramLogDO);
    }

    @Override
    public void addPriceLog(AddGoodsYilingPriceLogRequest before, AddGoodsYilingPriceLogRequest after, Long userId) {
        ParamLogDO paramLogDO = new ParamLogDO();
        paramLogDO.setParType(-1);
        paramLogDO.setOpUserId(userId);
        paramLogDO.setOpTime(new Date());

        if (ObjectUtil.isNull(before) && ObjectUtil.isNotNull(after)) {
            paramLogDO.setDataId(after.getId());
            paramLogDO.setParamId(after.getParamId());
            paramLogDO.setLogType(1);
            paramLogDO.setAfterData(JSON.toJSONString(after));
        } else if (ObjectUtil.isNotNull(before) && ObjectUtil.isNotNull(after)) {
            paramLogDO.setDataId(before.getId());
            paramLogDO.setParamId(before.getParamId());
            paramLogDO.setLogType(2);
            paramLogDO.setBeforeData(JSON.toJSONString(before));
            paramLogDO.setAfterData(JSON.toJSONString(after));
        } else {
            return;
        }
        save(paramLogDO);
    }

    @Override
    public void addMemberLog(ParamSubDO before, ParamSubDO after, Long userId) {
        ParamLogDO paramLogDO = new ParamLogDO();
        paramLogDO.setOpUserId(userId);
        paramLogDO.setOpTime(new Date());

        if (ObjectUtil.isNull(before) && ObjectUtil.isNotNull(after)) {
            paramLogDO.setDataId(after.getId());
            paramLogDO.setParamId(after.getParamId());
            paramLogDO.setParType(after.getParType());
            paramLogDO.setLogType(1);
            paramLogDO.setAfterData(JSON.toJSONString(after));
        } else if (ObjectUtil.isNotNull(before) && ObjectUtil.isNotNull(after)) {
            paramLogDO.setDataId(before.getId());
            paramLogDO.setParamId(before.getParamId());
            paramLogDO.setParType(before.getParType());
            paramLogDO.setLogType(2);
            paramLogDO.setBeforeData(JSON.toJSONString(before));
            paramLogDO.setAfterData(JSON.toJSONString(after));
        } else if (ObjectUtil.isNotNull(before) && ObjectUtil.isNull(after)) {
            paramLogDO.setDataId(before.getId());
            paramLogDO.setParamId(before.getParamId());
            paramLogDO.setParType(before.getParType());
            paramLogDO.setLogType(3);
            paramLogDO.setBeforeData(JSON.toJSONString(before));
        } else {
            return;
        }
        save(paramLogDO);
    }
}
