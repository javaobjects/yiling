package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityGetApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGetDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityGetExportDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailExportDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.dto.request.QueryLotteryActivityGetPageRequest;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashStatusEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityGetTypeEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityRewardTypeEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 运营后台-抽奖机会明细导出
 *
 * @author: lun.yu
 * @date: 2022-10-09
 */
@Service("lotteryActivityGetExportService")
public class LotteryActivityGetExportServiceImpl implements BaseExportQueryDataService<QueryLotteryActivityGetPageRequest> {

    @DubboReference
    LotteryActivityGetApi lotteryActivityGetApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("createTime", "时间");
        FIELD.put("lotteryActivityId", "活动ID");
        FIELD.put("activityName", "活动名称");
        FIELD.put("provinceName", "省");
        FIELD.put("cityName", "市");
        FIELD.put("regionName", "区");
        FIELD.put("uid", "用户ID");
        FIELD.put("uname", "用户名称");
        FIELD.put("getTypeName", "获取途径");
        FIELD.put("getTimes", "获取次数");
    }

    @Override
    public QueryExportDataDTO queryData(QueryLotteryActivityGetPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<LotteryActivityGetDTO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = lotteryActivityGetApi.queryPageList(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }

            // 如果为B2B平台，查询出企业信息
            Map<Long, EnterpriseDTO> enterpriseDTOMap = MapUtil.newHashMap();
            Integer platformType = page.getRecords().get(0).getPlatformType();
            if (platformType == 1) {
                List<Long> eidList = page.getRecords().stream().map(LotteryActivityGetDTO::getUid).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(eidList)) {
                    enterpriseDTOMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity(), (k1, k2) -> k1));
                }
            }

            Page<LotteryActivityGetExportDTO> exportDTOPage = PojoUtils.map(page, LotteryActivityGetExportDTO.class);

            Map<Long, EnterpriseDTO> finalEnterpriseDTOMap = enterpriseDTOMap;
            exportDTOPage.getRecords().forEach(activityGetExportDTO -> {

                activityGetExportDTO.setGetTypeName(LotteryActivityGetTypeEnum.getByCode(activityGetExportDTO.getGetType()).getName());
                // B2B平台需要设置企业的省市区导出
                if (platformType == 1) {
                    EnterpriseDTO enterpriseDTO = finalEnterpriseDTOMap.getOrDefault(activityGetExportDTO.getUid(), new EnterpriseDTO());
                    activityGetExportDTO.setProvinceName(enterpriseDTO.getProvinceName());
                    activityGetExportDTO.setCityName(enterpriseDTO.getCityName());
                    activityGetExportDTO.setRegionName(enterpriseDTO.getRegionName());
                }

                data.add(BeanUtil.beanToMap(activityGetExportDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        String sheetName = "抽奖机会明细导出";
        exportDataDTO.setSheetName(sheetName);
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
    public QueryLotteryActivityGetPageRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, QueryLotteryActivityGetPageRequest.class);
    }

}
