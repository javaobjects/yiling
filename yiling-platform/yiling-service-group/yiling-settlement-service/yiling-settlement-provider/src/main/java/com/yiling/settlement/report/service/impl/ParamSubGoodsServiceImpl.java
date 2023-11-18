package com.yiling.settlement.report.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.yiling.settlement.report.enums.ReportParSubGoodsOrderSourceEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.exception.ServiceException;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.settlement.report.bo.LadderGoodsInfoBO;
import com.yiling.settlement.report.dao.ParamSubGoodsMapper;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.ReportYlGoodsCategoryDTO;
import com.yiling.settlement.report.dto.request.QueryGoodsCategoryRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsInfoRequest;
import com.yiling.settlement.report.dto.request.QueryParamSubGoodsPageListRequest;
import com.yiling.settlement.report.dto.request.SaveOrUpdateParamSubGoodsRequest;
import com.yiling.settlement.report.dto.request.UpdateYlGoodsIdRequest;
import com.yiling.settlement.report.entity.ParamSubDO;
import com.yiling.settlement.report.entity.ParamSubGoodsDO;
import com.yiling.settlement.report.enums.ReportErrorCode;
import com.yiling.settlement.report.enums.ReportParamTypeEnum;
import com.yiling.settlement.report.service.ParamLogService;
import com.yiling.settlement.report.service.ParamSubGoodsService;
import com.yiling.settlement.report.service.ParamSubService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 报表子参数商品关联表 服务实现类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
@Slf4j
@Service
public class ParamSubGoodsServiceImpl extends BaseServiceImpl<ParamSubGoodsMapper, ParamSubGoodsDO> implements ParamSubGoodsService {

    @Autowired
    ParamSubService paramSubService;
    @Autowired
    ParamLogService paramLogService;

    @Override
    public Page<ReportParamSubGoodsDTO> queryParamSubGoodsPageList(QueryParamSubGoodsPageListRequest request) {
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getParamId()) && ObjectUtil.notEqual(0L, request.getParamId()), ParamSubGoodsDO::getParamId, request.getParamId());
        wrapper.eq(ObjectUtil.isNotNull(request.getParamSubId()) && ObjectUtil.notEqual(0L, request.getParamSubId()), ParamSubGoodsDO::getParamSubId, request.getParamSubId());
        wrapper.eq(ObjectUtil.isNotNull(request.getParType()) && ObjectUtil.notEqual(0, request.getParType()), ParamSubGoodsDO::getParType, request.getParType());
        wrapper.like(StrUtil.isNotBlank(request.getActivityName()), ParamSubGoodsDO::getActivityName, request.getActivityName());
        wrapper.like(StrUtil.isNotBlank(request.getGoodsName()), ParamSubGoodsDO::getGoodsName, request.getGoodsName());
        if (ObjectUtil.isNotNull(request.getStartTime()) && ObjectUtil.isNotNull(request.getEndTime())) {
            wrapper.ge(ParamSubGoodsDO::getUpdateTime, DateUtil.beginOfDay(request.getStartTime()));
            wrapper.le(ParamSubGoodsDO::getUpdateTime, DateUtil.offsetSecond(DateUtil.endOfDay(request.getEndTime()), -1));
        }
        wrapper.in(CollUtil.isNotEmpty(request.getEidList()), ParamSubGoodsDO::getEid, request.getEidList());
        wrapper.in(CollUtil.isNotEmpty(request.getUpdateUserList()), ParamSubGoodsDO::getUpdateUser, request.getUpdateUserList());
        wrapper.orderByDesc(ParamSubGoodsDO::getId);
        Page<ParamSubGoodsDO> page = page(request.getPage(), wrapper);
        return PojoUtils.map(page, ReportParamSubGoodsDTO.class);
    }

    @Override
    public List<ReportYlGoodsCategoryDTO> queryCategoryByYlGoodsIds(QueryGoodsCategoryRequest queryGoodsCategoryRequest) {
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ParamSubGoodsDO::getParType, ReportParamTypeEnum.GOODS.getCode()).in(ParamSubGoodsDO::getYlGoodsId, queryGoodsCategoryRequest.getGoodsIds()).le(ParamSubGoodsDO::getStartTime, queryGoodsCategoryRequest.getDate()).ge(ParamSubGoodsDO::getEndTime, queryGoodsCategoryRequest.getDate());
        List<ParamSubGoodsDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return Lists.newArrayList();
        }
        //查询商品
        List<ReportYlGoodsCategoryDTO> result = PojoUtils.map(list, ReportYlGoodsCategoryDTO.class);
        List<Long> paramSubIds = result.stream().map(ReportYlGoodsCategoryDTO::getParamSubId).distinct().collect(Collectors.toList());

        //查询分类
        List<ReportParamSubDTO> reportParamSubDTOS = paramSubService.queryReportParamSubInfoByIds(paramSubIds);

        Map<Long, ReportParamSubDTO> categoryDOMap = reportParamSubDTOS.stream().collect(Collectors.toMap(ReportParamSubDTO::getId, Function.identity()));

        result.forEach(r -> {
            if (null != categoryDOMap.get(r.getParamSubId())) {
                r.setParamId(categoryDOMap.get(r.getParamSubId()).getParamId());
                r.setParamSubId(r.getParamSubId());
                r.setName(categoryDOMap.get(r.getParamSubId()).getName());
            }
        });
        return result;
    }

    @Override
    public List<LadderGoodsInfoBO> queryLadderInfo(QueryGoodsInfoRequest request) {
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getEid()) && ObjectUtil.notEqual(0L, request.getEid()), ParamSubGoodsDO::getEid, request.getEid());
        wrapper.eq(ParamSubGoodsDO::getParType, ReportParamTypeEnum.LADDER.getCode());
        wrapper.in(ObjectUtil.isNotNull(request.getOrderSource()) && ObjectUtil.notEqual(0, request.getOrderSource()), ParamSubGoodsDO::getOrderSource, ListUtil.toList(ReportParSubGoodsOrderSourceEnum.ALL.getCode(),request.getOrderSource()));
        wrapper.in(CollUtil.isNotEmpty(request.getGoodsInSns()), ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSns());
        List<ParamSubGoodsDO> list = list(wrapper);
        if (CollUtil.isEmpty(list)) {
            return ListUtil.toList();
        }
        List<Long> parSubList = list.stream().map(ParamSubGoodsDO::getParamSubId).distinct().collect(Collectors.toList());
        //查询分类
        List<ReportParamSubDTO> reportParamSubDTOS = paramSubService.queryReportParamSubInfoByIds(parSubList);

        Map<Long, ReportParamSubDTO> categoryDOMap = reportParamSubDTOS.stream().collect(Collectors.toMap(ReportParamSubDTO::getId, Function.identity()));
        List<LadderGoodsInfoBO> result = PojoUtils.map(list, LadderGoodsInfoBO.class);
        result.forEach(e -> {
            e.setLadderRange(categoryDOMap.get(e.getParamSubId()).getLadderRange());
            e.setName(categoryDOMap.get(e.getParamSubId()).getName());
        });

        return result;
    }

    @Override
    public List<ReportParamSubGoodsDTO> queryActivityGoodsInfo(QueryGoodsInfoRequest request) {
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ObjectUtil.isNotNull(request.getEid()) && ObjectUtil.notEqual(0L, request.getEid()), ParamSubGoodsDO::getEid, request.getEid());
        wrapper.eq(ParamSubGoodsDO::getParType, ReportParamTypeEnum.ACTIVITY.getCode());
        wrapper.in(CollUtil.isNotEmpty(request.getGoodsInSns()), ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSns());
        List<ParamSubGoodsDO> list = list(wrapper);
        return PojoUtils.map(list, ReportParamSubGoodsDTO.class);
    }

    @Override
    public Boolean saveOrUpdateParamSubCategoryGoods(SaveOrUpdateParamSubGoodsRequest request) {
        //如果是新增
        if (ObjectUtil.isNull(request.getId()) || ObjectUtil.equal(0L, request.getId())) {
            request.setEndTime((DateUtil.offsetSecond(DateUtil.endOfDay(request.getEndTime()), -1)));
            request.setStartTime(DateUtil.offsetSecond(request.getStartTime(), 1));
            //当前类型时间校验 同一个类型下相同商品执行时间段不能有交集
            LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(ParamSubGoodsDO::getYlGoodsId, request.getYlGoodsId()).eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            ParamSubGoodsDO other = this.getOne(wrapper);
            if (Objects.nonNull(other)) {
                throw new BusinessException(ReportErrorCode.CATEGORY_GOODS_EXIST_OF_TIME);
            }
            //一个商品只能
            LambdaQueryWrapper<ParamSubGoodsDO> categoryRelationDOLambdaQueryWrapper = Wrappers.lambdaQuery();
            categoryRelationDOLambdaQueryWrapper.eq(ParamSubGoodsDO::getYlGoodsId, request.getYlGoodsId()).eq(ParamSubGoodsDO::getParType, request.getParType()).ne(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            ParamSubGoodsDO categoryRelationDO = this.getOne(categoryRelationDOLambdaQueryWrapper);
            if (Objects.nonNull(categoryRelationDO)) {
                throw new BusinessException(ReportErrorCode.CATEGORY_GOODS_EXIST_OF_CATEGORY);
            }
        } else {
            //如果是更新
            request.setEndTime(DateUtil.offsetSecond(DateUtil.endOfDay(request.getEndTime()), -1));
            ParamSubGoodsDO categoryRelationDO = this.getById(request.getId());
            //当前类型时间校验
            LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(ParamSubGoodsDO::getYlGoodsId, categoryRelationDO.getYlGoodsId())
                    .eq(ParamSubGoodsDO::getParType, request.getParType()).ne(ParamSubGoodsDO::getId, request.getId()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, categoryRelationDO.getStartTime()).last("limit 1");
            ParamSubGoodsDO other = this.getOne(wrapper);
            if (Objects.nonNull(other)) {
                throw new BusinessException(ReportErrorCode.CATEGORY_GOODS_EXIST_OF_TIME);
            }
        }

        ParamSubGoodsDO goodsDO = PojoUtils.map(request, ParamSubGoodsDO.class);
        if (ObjectUtil.isNull(goodsDO.getId())) {
            paramLogService.addOarSubGoodsLog(null, goodsDO, request.getOpUserId());
        } else {
            ParamSubGoodsDO subGoodsDO = getById(goodsDO.getId());
            paramLogService.addOarSubGoodsLog(subGoodsDO, goodsDO, request.getOpUserId());
        }
        return saveOrUpdate(goodsDO);
    }

    @Override
    public Boolean saveOrUpdateParamSubActivityGoods(SaveOrUpdateParamSubGoodsRequest request) {
        request.setStartTime(DateUtil.offsetSecond(request.getStartTime(), 1));
        request.setEndTime(DateUtil.offsetSecond(DateUtil.endOfDay(request.getEndTime()), -1));
        //当前类型时间校验 同一个类型下相同商品执行时间段不能有交集
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        //如果是新增
        if (ObjectUtil.isNull(request.getId()) || ObjectUtil.equal(0L, request.getId())) {
            //新增时如果订单来源为全部，则忽略订单来源查询
            if (ObjectUtil.equal(request.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())){
                wrapper.eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }else {
                wrapper.eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).and(w->w.eq(ParamSubGoodsDO::getOrderSource,request.getOrderSource()).or().eq(ParamSubGoodsDO::getOrderSource, ReportParSubGoodsOrderSourceEnum.ALL.getCode())).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }
        } else {
            //如果是修改
            //修改时如果订单来源为全部，则忽略订单来源查询
            if (ObjectUtil.equal(request.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())){
                wrapper.eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).ne(ParamSubGoodsDO::getId, request.getId()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }else {
                wrapper.eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).and(w->w.eq(ParamSubGoodsDO::getOrderSource,request.getOrderSource()).or().eq(ParamSubGoodsDO::getOrderSource, ReportParSubGoodsOrderSourceEnum.ALL.getCode())).ne(ParamSubGoodsDO::getId, request.getId()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }
        }
        ParamSubGoodsDO other = this.getOne(wrapper);
        if (Objects.nonNull(other)) {
            throw new BusinessException(ReportErrorCode.ACTIVITY_GOODS_EXIST_OF_TIME);
        }
        ParamSubGoodsDO goodsDO = PojoUtils.map(request, ParamSubGoodsDO.class);
        if (ObjectUtil.isNull(goodsDO.getId())) {
            paramLogService.addOarSubGoodsLog(null, goodsDO, request.getOpUserId());
        } else {
            ParamSubGoodsDO subGoodsDO = getById(goodsDO.getId());
            paramLogService.addOarSubGoodsLog(subGoodsDO, goodsDO, request.getOpUserId());
        }
        return saveOrUpdate(goodsDO);
    }

    @Override
    public Boolean saveOrUpdateParamSubLadderGoods(SaveOrUpdateParamSubGoodsRequest request) {
        request.setStartTime(DateUtil.offsetSecond(request.getStartTime(), 1));
        request.setEndTime(DateUtil.offsetSecond(DateUtil.endOfDay(request.getEndTime()), -1));
        //判断同一阶梯内同一商业  同一时间 只能有一条商品在同一阶梯中
        //当前类型时间校验
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        //如果是新增
        if (ObjectUtil.isNull(request.getId()) || ObjectUtil.equal(0L, request.getId())) {
            //修改时如果订单来源为全部，则忽略订单来源查询
            if (ObjectUtil.equal(request.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())){
//                wrapper.eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
                wrapper.eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }else {
                wrapper.eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).eq(ParamSubGoodsDO::getEid, request.getEid()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).and(w->w.eq(ParamSubGoodsDO::getOrderSource,request.getOrderSource()).or().eq(ParamSubGoodsDO::getOrderSource, ReportParSubGoodsOrderSourceEnum.ALL.getCode())).ge(ParamSubGoodsDO::getOrderSource, ReportParSubGoodsOrderSourceEnum.ALL.getCode()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }
        } else {
            //修改时如果订单来源为全部，则忽略订单来源查询
            if (ObjectUtil.equal(request.getOrderSource(),ReportParSubGoodsOrderSourceEnum.ALL.getCode())){
//                wrapper.eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).eq(ParamSubGoodsDO::getEid, request.getEid()).ne(ParamSubGoodsDO::getId, request.getId()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
                wrapper.eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).eq(ParamSubGoodsDO::getEid, request.getEid()).ne(ParamSubGoodsDO::getId, request.getId()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }else {
                wrapper.eq(ParamSubGoodsDO::getParamSubId, request.getParamSubId()).eq(ParamSubGoodsDO::getParType, request.getParType()).eq(ParamSubGoodsDO::getEid, request.getEid()).ne(ParamSubGoodsDO::getId, request.getId()).eq(ParamSubGoodsDO::getGoodsInSn, request.getGoodsInSn()).and(w->w.eq(ParamSubGoodsDO::getOrderSource,request.getOrderSource()).or().eq(ParamSubGoodsDO::getOrderSource, ReportParSubGoodsOrderSourceEnum.ALL.getCode())).le(ParamSubGoodsDO::getStartTime, request.getEndTime()).ge(ParamSubGoodsDO::getEndTime, request.getStartTime()).last("limit 1");
            }
        }
        ParamSubGoodsDO other = this.getOne(wrapper);
        if (Objects.nonNull(other)) {
            log.warn("该时间段内在阶梯"+other.getParamSubId()+"中已存在此商品");
            throw new BusinessException(ReportErrorCode.LADDER_GOODS_EXIST_OF_TIME, "该时间段内在阶梯"+other.getParamSubId()+"中已存在此商品");
        }
        QueryGoodsInfoRequest infoRequest = new QueryGoodsInfoRequest();
        infoRequest.setOrderSource(request.getOrderSource());
        ParamSubGoodsDO dbGoodsDO = null;
        if (ObjectUtil.isNull(request.getId()) || ObjectUtil.equal(0L, request.getId())) {
            infoRequest.setEid(request.getEid());
            infoRequest.setGoodsInSns(ListUtil.toList(request.getGoodsInSn()));
        } else {
            dbGoodsDO = getById(request.getId());
            infoRequest.setEid(dbGoodsDO.getEid());
            infoRequest.setGoodsInSns(ListUtil.toList(dbGoodsDO.getGoodsInSn()));
            request.setParamSubId(dbGoodsDO.getParamSubId());
        }
        List<LadderGoodsInfoBO> infoBOList = queryLadderInfo(infoRequest).stream().filter(e->DateUtil.compare(request.getStartTime(),e.getEndTime())<=0&&DateUtil.compare(request.getEndTime(),e.getStartTime())>=0).sorted(Comparator.comparing(LadderGoodsInfoBO::getLadderRange)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(infoBOList)) {
            //查询当前阶梯序号
            ParamSubDO currentSub = paramSubService.getById(request.getParamSubId());
            int range = currentSub.getLadderRange();
            if (range != 1) {
                //判断起步数量是否小于此商业的此品是否小于等于 下一（代表低于该阶梯的所有阶梯） 阶梯的最大起步数量，如大于等于提示在 下一 阶梯设置
                List<LadderGoodsInfoBO> var1 = infoBOList.stream().filter(e -> e.getLadderRange() < range).collect(Collectors.toList());
                for (int i = 0; i < var1.size(); i++) {
                    LadderGoodsInfoBO infoBO = var1.get(i);
                    if (infoBO.getThresholdCount() >= request.getThresholdCount()) {
                        log.warn("此商品的当前起步数量小于或等于阶梯{}中的起步数量，请在阶梯{}中设置，或调大起步数量", infoBO.getLadderRange(), infoBO.getLadderRange());
                        throw new BusinessException(ReportErrorCode.LADDER_GOODS_RANGE_INVALID, "此商品的当前起步数量小于或等于阶梯" + infoBO.getLadderRange() + "中的起步数量，请在阶梯" + infoBO.getLadderRange() + "中设置，或调大起步数量");
                    }
                }
            }
            Integer maxRange = paramSubService.queryLadderMaxRange();
            if (range < maxRange) {
                //判断起步数量是否大于此商业的此品是否大于等于 上一（代表高于该阶梯的所有阶梯） 阶梯的最小起步数量，如小于等于提示在 上一 阶梯设置
                List<LadderGoodsInfoBO> var2 = infoBOList.stream().filter(e -> e.getLadderRange() > range).collect(Collectors.toList());
                for (int i = 0; i < var2.size(); i++) {
                    LadderGoodsInfoBO infoBO = var2.get(i);
                    if (infoBO.getThresholdCount() <= request.getThresholdCount()) {
                        log.warn("此商品的当前起步数量大于或等于阶梯{}中的起步数量，请在阶梯{}中设置，或调小起步数量", infoBO.getLadderRange(), infoBO.getLadderRange());
                        throw new BusinessException(ReportErrorCode.LADDER_GOODS_RANGE_INVALID, "此商品的当前起步数量大于或等于阶梯" + infoBO.getLadderRange() + "中的起步数量，请在阶梯" + infoBO.getLadderRange() + "中设置，或调小起步数量");
                    }
                }
            }
        }
        ParamSubGoodsDO goodsDO = PojoUtils.map(request, ParamSubGoodsDO.class);
        boolean isSucceed = saveOrUpdate(goodsDO);
        if (!isSucceed) {
            return isSucceed;
        }
        if (ObjectUtil.isNull(goodsDO.getId())) {
            paramLogService.addOarSubGoodsLog(null, goodsDO, request.getOpUserId());
        } else {
            paramLogService.addOarSubGoodsLog(dbGoodsDO, goodsDO, request.getOpUserId());
        }
        return Boolean.TRUE;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateYlGoodsId(UpdateYlGoodsIdRequest request) {
        LambdaQueryWrapper<ParamSubGoodsDO> wrapper = Wrappers.lambdaQuery();
        //如果是以岭id置为空
        if (ObjectUtil.equal(request.getNewId(), 0L) && ObjectUtil.notEqual(request.getOldId(), 0L)) {
            wrapper.eq(ParamSubGoodsDO::getEid, request.getEid());
            wrapper.eq(ParamSubGoodsDO::getYlGoodsId, request.getOldId());
            List<ParamSubGoodsDO> list = list(wrapper);
            if (CollUtil.isNotEmpty(list)) {
                ParamSubGoodsDO var = new ParamSubGoodsDO();
                var.setOpUserId(request.getOpUserId());
                var.setOpTime(new Date());
                LambdaQueryWrapper<ParamSubGoodsDO> deleteWrapper = Wrappers.lambdaQuery();
                deleteWrapper.in(ParamSubGoodsDO::getId, list.stream().map(ParamSubGoodsDO::getId).collect(Collectors.toList()));
                int row = batchDeleteWithFill(var, deleteWrapper);
                if (row == 0) {
                    log.error("以岭品对应关系变动时，把对应关系置为空时更新报表参数商品表异常，id={}", list.stream().map(ParamSubGoodsDO::getId).collect(Collectors.toList()));
                    throw new ServiceException(ReportErrorCode.UPDATE_GOODS_ID_FAIL.getMessage());
                }
                list.forEach(e -> {
                    paramLogService.addOarSubGoodsLog(e, null, request.getOpUserId());
                });
            }
            return Boolean.TRUE;
        }
        //如果是新增商品关系
        if (ObjectUtil.notEqual(request.getNewId(), 0L) && ObjectUtil.equal(request.getOldId(), 0L)) {
            return Boolean.TRUE;
        }
        //如果是更新商品关系
        if (ObjectUtil.notEqual(request.getNewId(), 0L) && ObjectUtil.notEqual(request.getOldId(), 0L)) {
            wrapper.eq(ParamSubGoodsDO::getEid, request.getEid());
            wrapper.eq(ParamSubGoodsDO::getYlGoodsId, request.getOldId());
            List<ParamSubGoodsDO> list = list(wrapper);
            if (CollUtil.isEmpty(list)) {
                return Boolean.TRUE;
            }
            ParamSubGoodsDO goodsDO = new ParamSubGoodsDO();
            goodsDO.setYlGoodsId(request.getNewId());
            goodsDO.setYlGoodsIdUpdateTime(new Date());
            goodsDO.setYlGoodsIdOld(request.getOldId());
            boolean isSucceed = update(goodsDO, wrapper);
            if (!isSucceed) {
                log.error("以岭品对应关系变动时，更新报表参数商品表异常，id={}", list.stream().map(ParamSubGoodsDO::getId).collect(Collectors.toList()));
                throw new ServiceException(ReportErrorCode.UPDATE_GOODS_ID_FAIL.getMessage());
            }
            list.forEach(e -> {
                paramLogService.addOarSubGoodsLog(e, goodsDO, request.getOpUserId());
            });
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean deleteParSubGoods(Long id, Long opUser) {
        ParamSubGoodsDO subGoodsDO = getById(id);
        if (ObjectUtil.isNull(subGoodsDO)) {
            throw new BusinessException(ReportErrorCode.PAR_SUB_GOODS_NOT_FOUND);
        }
        ParamSubGoodsDO goodsDO = new ParamSubGoodsDO();
        goodsDO.setId(id);
        goodsDO.setOpUserId(opUser);
        goodsDO.setOpTime(new Date());
        int row = deleteByIdWithFill(goodsDO);
        if (row == 0) {
            return Boolean.FALSE;
        }
        paramLogService.addOarSubGoodsLog(subGoodsDO, null, opUser);
        return Boolean.TRUE;
    }


}
