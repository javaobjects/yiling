package com.yiling.settlement.report.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.dao.ParamSubMapper;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.request.AddReportSubParamRequest;
import com.yiling.settlement.report.dto.request.QueryReportParamSubPageListRequest;
import com.yiling.settlement.report.entity.ParamSubDO;
import com.yiling.settlement.report.entity.ParamSubGoodsDO;
import com.yiling.settlement.report.enums.ReportErrorCode;
import com.yiling.settlement.report.enums.ReportParSubGoodsOrderSourceEnum;
import com.yiling.settlement.report.enums.ReportParamTypeEnum;
import com.yiling.settlement.report.service.ParamLogService;
import com.yiling.settlement.report.service.ParamSubService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;

/**
 * <p>
 * 子参数表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Service
public class ParamSubServiceImpl extends BaseServiceImpl<ParamSubMapper, ParamSubDO> implements ParamSubService {

    @Autowired
    ParamLogService paramLogService;

    @Override
    public Page<ReportParamSubDTO> queryReportParamSubPageList(QueryReportParamSubPageListRequest request) {
        LambdaQueryWrapper<ParamSubDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getParamId()) && ObjectUtil.notEqual(0L, request.getParamId()), ParamSubDO::getParamId, request.getParamId());
        wrapper.in(CollUtil.isNotEmpty(request.getEidList()), ParamSubDO::getEid, request.getEidList());
        wrapper.eq(ObjectUtil.isNotNull(request.getParType()) && ObjectUtil.notEqual(0, request.getParType()), ParamSubDO::getParType, request.getParType());
        wrapper.eq(ObjectUtil.isNotNull(request.getMemberSource()) && ObjectUtil.notEqual(0, request.getMemberSource()), ParamSubDO::getMemberSource, request.getMemberSource());
        wrapper.eq(ObjectUtil.isNotNull(request.getMemberId()) && ObjectUtil.notEqual(0L, request.getMemberId()), ParamSubDO::getMemberId, request.getMemberId());
        wrapper.eq(ObjectUtil.isNotNull(request.getThresholdAmount()) && request.getThresholdAmount().compareTo(BigDecimal.ZERO) != 0, ParamSubDO::getThresholdAmount, request.getThresholdAmount());
        wrapper.in(CollUtil.isNotEmpty(request.getUpdateUser()), ParamSubDO::getUpdateUser, request.getUpdateUser());
        if (ObjectUtil.isNotNull(request.getEndUpdateTime())) {
            wrapper.le(ParamSubDO::getUpdateTime, DateUtil.endOfDay(request.getEndUpdateTime()));
        }
        if (ObjectUtil.isNotNull(request.getStartUpdateTime())) {
            wrapper.ge(ParamSubDO::getUpdateTime, request.getStartUpdateTime());
        }
        wrapper.orderByDesc(ParamSubDO::getId);
        Page<ParamSubDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportParamSubDTO.class);
    }

    @Override
    public List<ReportParamSubDTO> queryReportParamSubInfoByIds(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.toList();
        }
        return PojoUtils.map(listByIds(ids), ReportParamSubDTO.class);
    }

    @Override
    public Integer queryLadderMaxRange() {
        LambdaQueryWrapper<ParamSubDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ParamSubDO::getParType, ReportParamTypeEnum.LADDER.getCode());
        wrapper.orderByDesc(ParamSubDO::getLadderRange);
        wrapper.last("limit 1");
        return getOne(wrapper).getLadderRange();
    }

    @Override
    public Boolean saveOrUpdateReportSubMemberParam(AddReportSubParamRequest request) {
        request.setStartTime(DateUtil.offsetSecond(request.getStartTime(), 1));
        request.setEndTime(DateUtil.offsetSecond(DateUtil.endOfDay(request.getEndTime()), -1));
        //判断同一时间同一价格只能由一个
        LambdaQueryWrapper<ParamSubDO> wrapper = Wrappers.lambdaQuery();
        //如果是新增
        ParamSubDO subDO = null;
        if (ObjectUtil.isNull(request.getId()) || ObjectUtil.equal(0L, request.getId())) {
            //新增时如果会员id为全部，则忽略会员id查询
            if (ObjectUtil.equal(request.getMemberId(), 0L)){
                wrapper.eq(ParamSubDO::getEid, request.getEid()).eq(ParamSubDO::getThresholdAmount, request.getThresholdAmount()).eq(ParamSubDO::getMemberSource, request.getMemberSource()).le(ParamSubDO::getStartTime, request.getEndTime()).ge(ParamSubDO::getEndTime, request.getStartTime()).last("limit 1");
            }else {
                wrapper.eq(ParamSubDO::getEid, request.getEid()).eq(ParamSubDO::getThresholdAmount, request.getThresholdAmount()).eq(ParamSubDO::getMemberSource, request.getMemberSource()).and(w->w.eq(ParamSubDO::getMemberId,request.getMemberId()).or().eq(ParamSubDO::getMemberId, 0L)).le(ParamSubDO::getStartTime, request.getEndTime()).ge(ParamSubDO::getEndTime, request.getStartTime()).last("limit 1");
            }
        } else {
            subDO = getById(request.getId());
            request.setEid(null);
            request.setParamId(null);
            request.setParType(null);
            //如果是修改
            //修改时如果会员id为全部，则忽略会员id查询
            if (ObjectUtil.equal(request.getMemberId(), 0L)){
                wrapper.eq(ParamSubDO::getEid, subDO.getEid()).eq(ParamSubDO::getThresholdAmount, request.getThresholdAmount()).eq(ParamSubDO::getMemberSource, request.getMemberSource()).ne(ParamSubDO::getId, request.getId()).le(ParamSubDO::getStartTime, request.getEndTime()).ge(ParamSubDO::getEndTime, request.getStartTime()).last("limit 1");
            }else {
                wrapper.eq(ParamSubDO::getEid, subDO.getEid()).eq(ParamSubDO::getThresholdAmount, request.getThresholdAmount()).eq(ParamSubDO::getMemberSource, request.getMemberSource()).and(w->w.eq(ParamSubDO::getMemberId,request.getMemberId()).or().eq(ParamSubDO::getMemberId, 0L)).ne(ParamSubDO::getId, request.getId()).le(ParamSubDO::getStartTime, request.getEndTime()).ge(ParamSubDO::getEndTime, request.getStartTime()).last("limit 1");
            }

        }
        ParamSubDO other = this.getOne(wrapper);
        if (Objects.nonNull(other)) {
            throw new BusinessException(ReportErrorCode.MEMBER_EXIST, "该企业在此时段下已经存在同样价格和来源的参数");
        }
        ParamSubDO paramSubDO = PojoUtils.map(request, ParamSubDO.class);
        if (ObjectUtil.isNull(paramSubDO.getId())) {
            paramLogService.addMemberLog(null, paramSubDO, request.getOpUserId());
        } else {
            paramLogService.addMemberLog(subDO, paramSubDO, request.getOpUserId());
        }
        return saveOrUpdate(paramSubDO);
    }

    @Override
    public List<ReportParamSubDTO> queryMemberParInfoByEid(Long eid) {
        LambdaQueryWrapper<ParamSubDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ParamSubDO::getEid, eid);
        wrapper.eq(ParamSubDO::getParType, ReportParamTypeEnum.MEMBER.getCode());
        List<ParamSubDO> list = list(wrapper);
        return PojoUtils.map(list, ReportParamSubDTO.class);
    }
}
