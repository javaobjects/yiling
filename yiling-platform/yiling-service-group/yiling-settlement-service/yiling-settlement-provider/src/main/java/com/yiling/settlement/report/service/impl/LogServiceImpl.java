package com.yiling.settlement.report.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.dao.LogMapper;
import com.yiling.settlement.report.dto.ReportLogDTO;
import com.yiling.settlement.report.entity.LogDO;
import com.yiling.settlement.report.enums.ReportLogTypeEnum;
import com.yiling.settlement.report.service.LogService;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 报表操作日志表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Slf4j
@Service
public class LogServiceImpl extends BaseServiceImpl<LogMapper, LogDO> implements LogService {

    @Override
    public void createReportTypeLog(Long reportId, ReportLogTypeEnum typeEnum, Long opUser) {
        LogDO logDO = new LogDO();
        logDO.setReportId(reportId);
        logDO.setType(typeEnum.getCode());
        logDO.setOpUserId(opUser);
        logDO.setOpTime(new Date());
        boolean isSuccess = save(logDO);
        if (!isSuccess) {
            log.error("操作返利报表时插入日志失败，参数={}", logDO);
            throw new ServiceException("返利报表日志失败");
        }
    }

    @Override
    public List<ReportLogDTO> queryLogList(Long reportId) {
        LambdaQueryWrapper<LogDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(LogDO::getReportId, reportId);
        List<LogDO> list = list(wrapper);
        return PojoUtils.map(list, ReportLogDTO.class);
    }

    @Override
    public void createLog(LogDO logDO) {
        if (ObjectUtil.isNull(logDO.getOpUserId()) || ObjectUtil.equal(logDO.getOpUserId(), 0L)) {
            logDO.setRemark("系统操作");
        }
        logDO.setOpTime(new Date());
        boolean isSuccess = save(logDO);
        if (!isSuccess) {
            log.error("操作返利报表功能时插入日志失败，参数={}", logDO);
            throw new ServiceException("返利报表日志失败");
        }
    }
}
