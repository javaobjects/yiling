package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.marketing.promotion.api.PromotionGoodsGiftLimitApi;
import com.yiling.marketing.promotion.dto.PromotionGoodsGiftUsedDTO;
import com.yiling.marketing.promotion.dto.request.PromotionGoodsGiftUsedRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2021/11/23
 */
@Slf4j
@Service("promotionGoodsGiftUsedExportService")
public class PromotionGoodsGiftUsedExportServiceImpl implements BaseExportQueryDataService<PromotionGoodsGiftUsedRequest> {

    @DubboReference
    PromotionGoodsGiftLimitApi giftLimitApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<>();

    static {
        FIELD.put("giftName", "赠品名称");
        FIELD.put("orderNo", "订单编号");
        FIELD.put("buyerName", "员工姓名");
        FIELD.put("buyerTel", "员工电话");
        FIELD.put("address", "企业地址");
    }

    @Override
    public QueryExportDataDTO queryData(PromotionGoodsGiftUsedRequest request) {
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();

        Page<PromotionGoodsGiftUsedDTO> page;
        int current = 1;
        log.info("【满赠活动导出】活动使用信息导出，请求数据为:[{}]", request);
        do {
            request.setCurrent(current);
            request.setSize(500);
            //  查询导出的数据填入data
            page = giftLimitApi.pageGiftOrder(request);

            if (CollUtil.isNotEmpty(page.getRecords())) {
                List<Long> userIdList = page.getRecords().stream().map(PromotionGoodsGiftUsedDTO::getCreateUser).collect(Collectors.toList());
                List<Long> buyerEIdList = page.getRecords().stream().map(PromotionGoodsGiftUsedDTO::getBuyerEid).collect(Collectors.toList());
                List<UserDTO> userDTOList = userApi.listByIds(userIdList);
                List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(buyerEIdList);
                Map<Long, UserDTO> userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, o -> o, (k1, k2) -> k1));
                Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, o -> o, (k1, k2) -> k1));
                page.getRecords().stream().forEach(item -> {
                    item.setBuyerName(Optional.ofNullable(userDTOMap.get(item.getCreateUser())).map(UserDTO::getName).orElse(null));
                    item.setBuyerTel(Optional.ofNullable(userDTOMap.get(item.getCreateUser())).map(UserDTO::getMobile).orElse(null));
                    item.setAddress(Optional.ofNullable(enterpriseDTOMap.get(item.getBuyerEid())).map(EnterpriseDTO::getAddress).orElse(null));
                });
            }

            for (PromotionGoodsGiftUsedDTO goodsGiftUsedDTO : page.getRecords()) {
                Map<String, Object> dataPojo = BeanUtil.beanToMap(goodsGiftUsedDTO);
                data.add(dataPojo);
            }
            current = current + 1;
        } while (CollectionUtils.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();

        exportDataDTO.setSheetName("满赠活动已使用信息导出");
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
    public PromotionGoodsGiftUsedRequest getParam(Map<String, Object> map) {
        return PojoUtils.map(map, PromotionGoodsGiftUsedRequest.class);
    }
}
