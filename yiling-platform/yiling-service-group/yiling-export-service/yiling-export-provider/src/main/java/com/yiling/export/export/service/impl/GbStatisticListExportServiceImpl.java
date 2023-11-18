
package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.bo.GbStatisticBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.gb.api.GbStatisticApi;
import com.yiling.sjms.gb.dto.StatisticDTO;
import com.yiling.sjms.gb.dto.request.GbFormStatisticPageRequest;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 团购统计列表导出
 *
 * @author:wei.wang
 * @date:2022/02/15
 */

@Slf4j
@Service("gbStatisticListExportService")
public class GbStatisticListExportServiceImpl implements BaseExportQueryDataService<GbFormStatisticPageRequest> {

    @DubboReference
    GbStatisticApi gbStatisticApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("dayDate", "日期");
        FIELD.put("provinceName", "省区");
        FIELD.put("goodsName", "产品名称");
        FIELD.put("monthDate", "团购月份");
        FIELD.put("quantityBox", "提报盒数");
        FIELD.put("finalAmount", "提报金额");
        FIELD.put("cancelQuantityBox", "取消盒数");
        FIELD.put("cancelFinalAmount", "取消金额");
    }


    /**
     * 查询excel中的数据
     *
     * @param request
     * @return
     */

    @Override
    public QueryExportDataDTO queryData(GbFormStatisticPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

      //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<StatisticDTO> page = null;
        int current = 1;
        do {
            request.setCurrent(current);
            request.setSize(500);
            page = gbStatisticApi.getStatistic(request);
            if (page == null || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            //Page<GbStatisticBO> statisticBOPage = PojoUtils.map(page, GbStatisticBO.class);

            for(StatisticDTO one : page.getRecords()){
                GbStatisticBO gbStatistic = PojoUtils.map(one, GbStatisticBO.class);
                String dayTime = DateUtil.format(one.getDayTime(), "yyyy-MM-dd");
                gbStatistic.setDayDate(dayTime);

                Map<String, Object> dataPojo = BeanUtil.beanToMap(gbStatistic);
                data.add(dataPojo);
            }

            current = current + 1;
        } while (page != null && CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("团购日统计");
        // 页签字段
        exportDataDTO.setFieldMap(FIELD);
        // 页签数据

        exportDataDTO.setData(data);

        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;
    }



    @Override
    public GbFormStatisticPageRequest getParam(Map<String, Object> map) {

        GbFormStatisticPageRequest request = PojoUtils.map(map, GbFormStatisticPageRequest.class);

        if(request.getGoodsType() == 1 ){
            //只勾选产品
            request.setType(1);
            if(request.getMonthType() == 1){
                //选择产品加团购月份
                request.setType(5);
            }
            if(request.getProvinceType() == 1 ){
                //选择产品加省区
                request.setType(4);
            }
            if(request.getProvinceType() == 1 && request.getMonthType() == 1 ){
                //全选
                request.setType(6);
            }
        }else{
            if(request.getProvinceType() == 0 && request.getMonthType() == 0 ){
                //默认不选
                request.setType(8);
            }
            if(request.getProvinceType() == 1  ){
                //只选省区
                request.setType(2);
            }
            if(request.getMonthType() == 1){
                //只选月份
                request.setType(3);
            }
            if(request.getProvinceType() == 1 && request.getMonthType() == 1 ){
                //省区和月份
                request.setType(7);
            }
        }
        return request;
    }
}

