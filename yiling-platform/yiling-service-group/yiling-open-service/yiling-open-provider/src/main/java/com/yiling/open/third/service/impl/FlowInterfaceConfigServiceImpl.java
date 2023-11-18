package com.yiling.open.third.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.SpringUtils;
import com.yiling.open.third.dao.FlowInterfaceConfigMapper;
import com.yiling.open.third.entity.FlowInterfaceConfigDO;
import com.yiling.open.third.service.BaseFlowInterfaceService;
import com.yiling.open.third.service.FlowAbstractTemplate;
import com.yiling.open.third.service.FlowInterfaceConfigService;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author shuang.zhang
 * @date 2022-04-11
 */
@Slf4j
@Service
public class FlowInterfaceConfigServiceImpl extends BaseServiceImpl<FlowInterfaceConfigMapper, FlowInterfaceConfigDO> implements FlowInterfaceConfigService {

    @Override
    @Async("asyncExecutor")
    public void executeFlowInterface() {
        List<FlowInterfaceConfigDO> flowInterfaceConfigDOList = this.list();
        for (FlowInterfaceConfigDO flowInterfaceConfigDO : flowInterfaceConfigDOList) {
            if (flowInterfaceConfigDO.getSyncStatus() == 2) {
                String startTime = DateUtil.format(new Date(), "yyyyMMdd");
                String compareTime = DateUtil.format(flowInterfaceConfigDO.getStartTime(), "yyyyMMdd");
                if (StrUtil.equals(startTime, compareTime)) {
                    continue;
                }
            }
            // 校验执行时间
            if (!checkTaskTime(flowInterfaceConfigDO.getSuId(), flowInterfaceConfigDO.getTaskTime())) {
                continue;
            }
            try {
                flowInterfaceConfigDO.setStartTime(new Date());
                flowInterfaceConfigDO.setSyncStatus(1);
                this.updateById(flowInterfaceConfigDO);
                BaseFlowInterfaceService baseService = (BaseFlowInterfaceService) SpringUtils.getBean(flowInterfaceConfigDO.getSpringId());
                Map<String, String> paramMap = new HashMap<>();
                JSONObject jsonObject = JSONObject.parseObject(flowInterfaceConfigDO.getParam());
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    paramMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                }
                paramMap.put(FlowAbstractTemplate.endTime, DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                paramMap.put(FlowAbstractTemplate.startTime, DateUtil.format(getStartTime(flowInterfaceConfigDO.getFlowDay()), "yyyy-MM-dd HH:mm:ss"));
                baseService.process(paramMap, flowInterfaceConfigDO.getPurchaseMapping(), flowInterfaceConfigDO.getSaleMapping(), flowInterfaceConfigDO.getGoodsBatchMapping(), flowInterfaceConfigDO.getSuId(), flowInterfaceConfigDO.getFlowDay());
                flowInterfaceConfigDO.setSyncStatus(2);
                flowInterfaceConfigDO.setSyncMsg("同步成功");
                flowInterfaceConfigDO.setEndTime(new Date());
                this.updateById(flowInterfaceConfigDO);
            } catch (Exception e) {
                flowInterfaceConfigDO.setSyncStatus(3);
                flowInterfaceConfigDO.setSyncMsg(ExceptionUtil.stacktraceToOneLineString(e, 1000));
                flowInterfaceConfigDO.setEndTime(new Date());
                this.updateById(flowInterfaceConfigDO);
                log.info("对接接口对接调度执行失败su_id={}", flowInterfaceConfigDO.getSuId(), e);
            }
        }
    }

    /**
     * 开始时间
     *
     * @param flowDateCount
     * @return
     */
    public Date getStartTime(Integer flowDateCount) {
        Integer count = flowDateCount - 1;
        Date endDate = new Date();
        Date startDate = DateUtil.offset(endDate, DateField.DAY_OF_MONTH, count * -1);
        return DateUtil.beginOfDay(startDate);
    }

    /**
     * 校验执行时间
     *
     * @param suid 企业ID
     * @param taskTime 执行时间，小时：0-24
     * @return
     */
    private boolean checkTaskTime(Long suid, Integer taskTime) {
        if (ObjectUtil.isNull(taskTime)) {
            taskTime = 0;
        }
        if (taskTime >= 24) {
            log.error("接口对接配置的执行时间格式不对, suid:{}, taskTime:{}", suid, taskTime);
            return false;
        }
        int nowHour = DateUtil.thisHour(true);
        if (nowHour < taskTime) {
            return false;
        }
        return true;
    }

}
