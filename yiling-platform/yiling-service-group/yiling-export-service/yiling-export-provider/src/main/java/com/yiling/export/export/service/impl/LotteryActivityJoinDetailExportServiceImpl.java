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
import com.yiling.marketing.lotteryactivity.api.LotteryActivityApi;
import com.yiling.marketing.lotteryactivity.api.LotteryActivityJoinDetailApi;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityJoinDetailExportDTO;
import com.yiling.marketing.lotteryactivity.dto.LotteryActivityReceiptInfoDTO;
import com.yiling.marketing.lotteryactivity.dto.request.QueryJoinDetailPageRequest;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityCashStatusEnum;
import com.yiling.marketing.lotteryactivity.enums.LotteryActivityPlatformEnum;
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
 * 运营后台-抽奖活动参与明细导出
 *
 * @author: lun.yu
 * @date: 2022-09-02
 */
@Service("lotteryActivityJoinDetailExportService")
public class LotteryActivityJoinDetailExportServiceImpl implements BaseExportQueryDataService<QueryJoinDetailPageRequest> {

    @DubboReference
    LotteryActivityJoinDetailApi lotteryActivityJoinDetailApi;
    @DubboReference
    LotteryActivityApi lotteryActivityApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("lotteryTime", "时间");
        FIELD.put("lotteryActivityId", "活动ID");
        FIELD.put("activityName", "活动名称");
        FIELD.put("enterpriseProvinceName", "省");
        FIELD.put("enterpriseCityName", "市");
        FIELD.put("enterpriseRegionName", "区");
        FIELD.put("uid", "用户ID");
        FIELD.put("uname", "用户名称");
        FIELD.put("shopEname", "商家");
        FIELD.put("rewardTypeName", "奖品类型");
        FIELD.put("rewardName", "奖品名称");
        FIELD.put("rewardNumber", "奖品数量");
        FIELD.put("statusName", "兑付状态");
        FIELD.put("contactor", "收件人");
        FIELD.put("contactorPhone", "联系电话");
        FIELD.put("provinceName", "收货地址-省");
        FIELD.put("cityName", "收货地址-市");
        FIELD.put("regionName", "收货地址-区");
        FIELD.put("address", "收货地址");
        FIELD.put("expressCompanyOrderNo", "快递单号");
    }

    @Override
    public QueryExportDataDTO queryData(QueryJoinDetailPageRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();

        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<LotteryActivityJoinDetailDTO> page;
        int current = 1;

        do {
            request.setCurrent(current);
            request.setSize(500);
            page = lotteryActivityJoinDetailApi.queryJoinDetailPage(request);
            if (Objects.isNull(page) || CollectionUtils.isEmpty(page.getRecords())) {
                break;
            }
            Map<Long, LotteryActivityReceiptInfoDTO> receiptInfoDTOMap = MapUtil.newHashMap();
            for (LotteryActivityJoinDetailDTO lotteryActivityJoinDetailDTO : page.getRecords()) {
                receiptInfoDTOMap.put(lotteryActivityJoinDetailDTO.getId(), lotteryActivityJoinDetailDTO.getLotteryActivityReceiptInfo());
            }

            Page<LotteryActivityJoinDetailExportDTO> exportDTOPage = PojoUtils.map(page, LotteryActivityJoinDetailExportDTO.class);

            // 如果为B2B平台，查询出企业信息
            Map<Long, EnterpriseDTO> enterpriseDTOMap = MapUtil.newHashMap();
            LotteryActivityDTO activityDTO = lotteryActivityApi.getById(exportDTOPage.getRecords().get(0).getLotteryActivityId());
            if (LotteryActivityPlatformEnum.getByCode(activityDTO.getPlatform()) == LotteryActivityPlatformEnum.B2B) {
                List<Long> eidList = exportDTOPage.getRecords().stream().map(LotteryActivityJoinDetailExportDTO::getUid).collect(Collectors.toList());
                if (CollUtil.isNotEmpty(eidList)) {
                    enterpriseDTOMap = enterpriseApi.listByIds(eidList).stream().collect(Collectors.toMap(BaseDTO::getId, Function.identity(), (k1, k2) -> k1));
                }
            }

            Map<Long, EnterpriseDTO> finalEnterpriseDTOMap = enterpriseDTOMap;
            exportDTOPage.getRecords().forEach(joinDetailExportDTO -> {

                joinDetailExportDTO.setRewardTypeName(LotteryActivityRewardTypeEnum.getByCode(joinDetailExportDTO.getRewardType()).getName());
                joinDetailExportDTO.setStatusName(LotteryActivityCashStatusEnum.getByCode(joinDetailExportDTO.getStatus()).getName());
                // 真实物品设置地址和快递单号
                if (LotteryActivityRewardTypeEnum.getByCode(joinDetailExportDTO.getRewardType()) == LotteryActivityRewardTypeEnum.REAL_GOODS) {
                    LotteryActivityReceiptInfoDTO receiptInfoDTO = receiptInfoDTOMap.get(joinDetailExportDTO.getId());
                    if (Objects.nonNull(receiptInfoDTO)) {
                        joinDetailExportDTO.setContactor(receiptInfoDTO.getContactor());
                        joinDetailExportDTO.setContactorPhone(receiptInfoDTO.getContactorPhone());
                        joinDetailExportDTO.setProvinceName(receiptInfoDTO.getProvinceName());
                        joinDetailExportDTO.setCityName(receiptInfoDTO.getCityName());
                        joinDetailExportDTO.setRegionName(receiptInfoDTO.getRegionName());
                        joinDetailExportDTO.setAddress(receiptInfoDTO.getAddress());
                        joinDetailExportDTO.setExpressCompanyOrderNo(receiptInfoDTO.getExpressCompany() + " " + receiptInfoDTO.getExpressOrderNo());
                    }

                }
                // 设置企业的地址信息
                if (LotteryActivityPlatformEnum.getByCode(activityDTO.getPlatform()) == LotteryActivityPlatformEnum.B2B) {
                    EnterpriseDTO enterpriseDTO = finalEnterpriseDTOMap.getOrDefault(joinDetailExportDTO.getUid(), new EnterpriseDTO());
                    joinDetailExportDTO.setEnterpriseProvinceName(enterpriseDTO.getProvinceName());
                    joinDetailExportDTO.setEnterpriseCityName(enterpriseDTO.getCityName());
                    joinDetailExportDTO.setEnterpriseRegionName(enterpriseDTO.getRegionName());
                }

                data.add(BeanUtil.beanToMap(joinDetailExportDTO));
            });
            current = current + 1;

        } while (CollectionUtils.isNotEmpty(page.getRecords()));


        ExportDataDTO exportDataDTO = new ExportDataDTO();
        String sheetName = CollUtil.isEmpty(request.getNotInRewardTypeList()) ? "参与次数导出" : "中奖次数导出";
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
    public QueryJoinDetailPageRequest getParam(Map<String, Object> map) {

        String pageTypeObj = (String) map.get("pageType");
        int pageType = Integer.parseInt(pageTypeObj);
        QueryJoinDetailPageRequest request = PojoUtils.map(map, QueryJoinDetailPageRequest.class);
        if (pageType == 2) {
            request.setNotInRewardTypeList(ListUtil.toList(LotteryActivityRewardTypeEnum.EMPTY.getCode()));
        }

        return request;
    }

}
