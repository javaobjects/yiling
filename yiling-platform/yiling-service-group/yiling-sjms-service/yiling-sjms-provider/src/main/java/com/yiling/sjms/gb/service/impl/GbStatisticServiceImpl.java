package com.yiling.sjms.gb.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbGoodsApi;
import com.yiling.sjms.gb.dao.GbStatisticMapper;
import com.yiling.sjms.gb.dto.GoodsDTO;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;
import com.yiling.sjms.gb.entity.GbGoodsDO;
import com.yiling.sjms.gb.entity.GbStatisticDO;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;
import com.yiling.sjms.gb.service.GbGoodsService;
import com.yiling.sjms.gb.service.GbStatisticService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 团购统计信息实现
 * </p>
 *
 * @author wei.wang
 * @date 2023-02-14
 */
@Service
public class GbStatisticServiceImpl extends BaseServiceImpl<GbStatisticMapper, GbStatisticDO> implements GbStatisticService {

    @DubboReference
    GbGoodsApi gbGoodsApi;

    @Override
    public GbStatisticDO getStatisticOne(String provinceName, Long goodsCode, Date dayTime,Date month) {
        QueryWrapper<GbStatisticDO> wrapper = new QueryWrapper();
        wrapper.lambda()
                .eq(GbStatisticDO:: getProvinceName,provinceName)
                .eq(GbStatisticDO:: getDayTime,dayTime)
                .eq(GbStatisticDO:: getGoodsCode,goodsCode)
                .eq(GbStatisticDO :: getMonth,month)
                .last( " limit 1 ");
        return getOne(wrapper);
    }

    @Override
    public Page<StatisticDTO> getStatistic(GbFormStatisticPageRequest request) {
        if(request.getStartTime()!= null){
            request.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(request.getEndTime()!= null){
            request.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }

        if(StringUtils.isNotBlank(request.getStartMonthTime())){
            request.setStartMontDate(DateUtil.beginOfDay(DateUtil.parse(request.getStartMonthTime(), "yyyy-MM")));
        }
        if(StringUtils.isNotBlank(request.getEndMonthTime())){
            request.setEndMonthDate(DateUtil.endOfDay(DateUtil.parse(request.getEndMonthTime(), "yyyy-MM")));
        }

        if(request.getType() == 6 ){
            QueryWrapper<GbStatisticDO> wrapper = new QueryWrapper<>();
            wrapper.lambda().ge(GbStatisticDO::getDayTime,request.getStartTime())
                    .le(GbStatisticDO::getDayTime, request.getEndTime());
            if(StringUtils.isNotEmpty(request.getProvinceName())){
                wrapper.lambda().eq(GbStatisticDO::getProvinceName,request.getProvinceName());
            }
            if(request.getCode()!=null && request.getCode()!=0){
                wrapper.lambda().eq(GbStatisticDO::getGoodsCode,request.getCode());
            }
            if( request.getStartMontDate() != null){
                wrapper.lambda().ge(GbStatisticDO::getMonth,request.getStartMontDate());
            }
            if( request.getEndMonthDate() != null){
                wrapper.lambda().le(GbStatisticDO::getMonth,request.getEndMonthDate());
            }

            wrapper.lambda().orderByDesc(GbStatisticDO:: getDayTime);
            Page<GbStatisticDO> page = this.page(request.getPage(), wrapper);
            Page<StatisticDTO> result = PojoUtils.map(page, StatisticDTO.class);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                List<Long> codeList = result.getRecords().stream().map(StatisticDTO::getGoodsCode).collect(Collectors.toList());
                List<GoodsDTO> goodsList = gbGoodsApi.listByCode(codeList);
                Map<Long, String> map = goodsList.stream().collect(Collectors.toMap(GoodsDTO::getCode, GoodsDTO::getName));
                for(StatisticDTO one : result.getRecords()){
                    one.setGoodsName(map.getOrDefault(one.getGoodsCode(),""));
                    one.setMonthDate(DateUtil.format(one.getMonth(),"yyyy-MM"));
                }
            }

            return result;

        }else if (request.getType() == 2){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByProvinceListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setGoodsName("全部");
                    one.setMonthDate("全部");
                }
            }
            return result;
        }else if(request.getType() == 1){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByCodeListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setProvinceName("全部");
                    one.setMonthDate("全部");
                }
                return result;
            }
        }else if (request.getType() == 8){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByTimeListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setProvinceName("全部");
                    one.setGoodsName("全部");
                    one.setMonthDate("全部");
                }
                return result;
            }
        }else if(request.getType() == 3){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByProvinceListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setGoodsName("全部");
                    one.setProvinceName("全部");
                    one.setMonthDate(DateUtil.format(one.getMonth(),"yyyy-MM"));
                }
            }
            return result;
        }else if(request.getType() == 7){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByProvinceListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setGoodsName("全部");
                    one.setMonthDate(DateUtil.format(one.getMonth(),"yyyy-MM"));
                }
            }
            return result;
        }else if(request.getType() == 4){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByCodeListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setMonthDate("全部");
                }
                return result;
            }
        }
        else if(request.getType() == 5){
            Page<StatisticDTO> result = this.baseMapper.getGBStatisticByCodeListPage(request.getPage(), request);
            if(CollectionUtil.isNotEmpty(result.getRecords())){
                for(StatisticDTO one : result.getRecords()){
                    one.setProvinceName("全部");
                    one.setMonthDate(DateUtil.format(one.getMonth(),"yyyy-MM"));
                }
                return result;
            }
        }
        return new Page<StatisticDTO>();
    }

    @Override
    public void gBStatisticProgram() {
        List<StatisticDTO> gbStatistic = this.baseMapper.getGBStatisticProgram(GbFormBizTypeEnum.SUBMIT.getCode());
        Map<String,StatisticDTO> map = new HashMap<>();
        if(CollectionUtil.isNotEmpty(gbStatistic)){
            map = gbStatistic.stream().collect(Collectors.toMap(r -> r.getDayTime() + "_" + r.getProvinceName()+"_"+r.getGoodsCode()+"_"+r.getMonth(), r -> r));
        }


        List<StatisticDTO> gbStatisticCancel = this.baseMapper.getGBStatisticProgram(GbFormBizTypeEnum.CANCEL.getCode());
        Map<String,StatisticDTO> mapCancel = new HashMap<>();
        if(CollectionUtil.isNotEmpty(gbStatisticCancel)){
            mapCancel = gbStatisticCancel.stream().collect(Collectors.toMap(r -> r.getDayTime() + "_" + r.getProvinceName()+"_"+r.getGoodsCode()+"_"+r.getMonth(), r -> r));
        }
        List<GbStatisticDO> list = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(gbStatistic)){
            for(StatisticDTO one : gbStatistic ){
                GoodsDTO goods = gbGoodsApi.getOneByCode(one.getGoodsCode());
                StringBuilder str = new StringBuilder();
                GbStatisticDO gbStatisticDO = PojoUtils.map(one, GbStatisticDO.class);
                gbStatisticDO.setGoodsName(goods.getName());
                StatisticDTO dto = mapCancel.get(gbStatisticDO.getDayTime() + "_" + gbStatisticDO.getProvinceName() + "_" + gbStatisticDO.getGoodsCode()+"_"+gbStatisticDO.getMonth());
                if(dto != null ){
                    gbStatisticDO.setCancelQuantityBox(dto.getQuantityBox());
                    gbStatisticDO.setCancelFinalAmount(dto.getFinalAmount());
                    gbStatisticDO.setGbListId(str.append(gbStatisticDO.getGbListId()).append(",").append(dto.getGbListId()).toString());
                    gbStatisticDO.setGoodsName(goods.getName());
                }
                list.add(gbStatisticDO);
            }
            //寻找多出来的取消提报的数据
            if(CollectionUtil.isNotEmpty(gbStatisticCancel)){
                for(StatisticDTO one : gbStatisticCancel ){
                    StatisticDTO dto = map.get(one.getDayTime() + "_" + one.getProvinceName() + "_" + one.getGoodsCode()+"_"+one.getMonth());
                    GoodsDTO goods = gbGoodsApi.getOneByCode(one.getGoodsCode());
                    if(dto == null){
                        GbStatisticDO gbStatisticDO = PojoUtils.map(one, GbStatisticDO.class);
                        gbStatisticDO.setQuantityBox(0);
                        gbStatisticDO.setFinalAmount(BigDecimal.ZERO);
                        gbStatisticDO.setCancelQuantityBox(one.getQuantityBox());
                        gbStatisticDO.setCancelFinalAmount(one.getFinalAmount());
                        gbStatisticDO.setGoodsName(goods.getName());
                        list.add(gbStatisticDO);
                    }
                }

            }

        }else{
            if(CollectionUtil.isNotEmpty(gbStatisticCancel)){
                for(StatisticDTO one : gbStatisticCancel ){
                    GoodsDTO goods = gbGoodsApi.getOneByCode(one.getGoodsCode());
                    GbStatisticDO gbStatisticDO = PojoUtils.map(one, GbStatisticDO.class);
                    gbStatisticDO.setQuantityBox(0);
                    gbStatisticDO.setFinalAmount(BigDecimal.ZERO);
                    gbStatisticDO.setCancelQuantityBox(one.getQuantityBox());
                    gbStatisticDO.setCancelFinalAmount(one.getFinalAmount());
                    gbStatisticDO.setGoodsName(goods.getName());
                    list.add(gbStatisticDO);
                }
            }
        }
        if(CollectionUtil.isNotEmpty(list)){
            saveBatch(list);
        }
    }


}
