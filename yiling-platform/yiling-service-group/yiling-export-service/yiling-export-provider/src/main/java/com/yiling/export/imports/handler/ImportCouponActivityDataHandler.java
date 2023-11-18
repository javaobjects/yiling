package com.yiling.export.imports.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.yiling.export.imports.model.ImportCouponInfoExcel;
import com.yiling.export.excel.service.ExcelTaskConfigService;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;

import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.export.excel.handler.ImportDataHandler;
import com.yiling.marketing.common.enums.CouponGetTypeEnum;
import com.yiling.marketing.coupon.dto.request.SaveCouponRequest;
import com.yiling.marketing.couponactivity.api.CouponActivityApi;
import com.yiling.marketing.couponactivity.dto.CouponActivityDetailDTO;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: sun.shixing
 * @date: 2022/11/18
 */
@Component
@Slf4j
public class ImportCouponActivityDataHandler implements ImportDataHandler<ImportCouponInfoExcel> {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    CouponActivityApi couponActivityApi;

    @Autowired
    ExcelTaskConfigService excelTaskConfigService;


    @Override
    public List<ImportCouponInfoExcel> execute(List<ImportCouponInfoExcel> object, Map<String, Object> paramMap) {
        log.info("excel导入发券开始,paramMap{}", paramMap);
        if (CollectionUtil.isEmpty(object)) {
            List<ImportCouponInfoExcel> objects = new ArrayList<>();
            ImportCouponInfoExcel importCouponInfoExcel = new ImportCouponInfoExcel();
            importCouponInfoExcel.setErrorMsg("数据为空");
            objects.add(importCouponInfoExcel);
            return new ArrayList<>();
        }
        log.info("excel导入发券开始,excel内容{}", object.size() < 100 ? object : CollectionUtil.sub(object, 0, 99));
        List<Long> eidList = object.stream().map(ImportCouponInfoExcel::getEid).distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = enterpriseApi.getMapByIds(eidList);
        List<Long> couponActivityIds = object.stream().map(ImportCouponInfoExcel::getCouponActivityId).distinct().collect(Collectors.toList());
        List<CouponActivityDetailDTO> couponActivityById = couponActivityApi.getCouponActivityById(couponActivityIds);
        Map<Long, CouponActivityDetailDTO> couponMap = new HashMap<>(500);
        if (CollectionUtil.isNotEmpty(couponActivityById)) {
            couponMap = couponActivityById.stream().collect(Collectors.toMap(CouponActivityDetailDTO::getId, Function.identity(), (k1, k2) -> k2));
        }
        Map<Long, Integer> remainByActivityIds = couponActivityApi.getRemainByActivityIds(couponActivityIds);
        for (ImportCouponInfoExcel importGoodsForm : object) {
            try {
                EnterpriseDTO enterpriseDTO = enterpriseDTOMap.get(importGoodsForm.getEid());
                CouponActivityDetailDTO couponActivityDetailDTO = couponMap.get(importGoodsForm.getCouponActivityId());
                if (ObjectUtil.isNull(enterpriseDTO)) {
                    importGoodsForm.setErrorMsg("企业信息不存在");
                    importGoodsForm.setStatus("失败");
                    continue;
                }
                if (ObjectUtil.isNull(couponActivityDetailDTO)) {
                    importGoodsForm.setErrorMsg("优惠券信息不存在");
                    importGoodsForm.setStatus("失败");
                    continue;
                }
                if (couponActivityDetailDTO.getStatus() != 1) {
                    importGoodsForm.setErrorMsg("优惠券非启用状态");
                    importGoodsForm.setStatus("失败");
                    continue;
                }
                if (ObjectUtil.isNull(importGoodsForm.getNum()) || importGoodsForm.getNum() == 0) {
                    importGoodsForm.setErrorMsg("填写的发放数量为0");
                    importGoodsForm.setStatus("失败");
                    continue;
                }
                Integer integer = remainByActivityIds.get(importGoodsForm.getCouponActivityId());
                Integer num = importGoodsForm.getNum();
                if (num > integer) {
                    importGoodsForm.setErrorMsg("剩余数量不足");
                    importGoodsForm.setStatus("失败");
                    continue;
                }
                SaveCouponRequest saveCouponRequest = new SaveCouponRequest();
                saveCouponRequest.setCouponActivityId(importGoodsForm.getCouponActivityId());
                saveCouponRequest.setEid(importGoodsForm.getEid());
                // excel自动发放
                Object createUser = paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
                saveCouponRequest.setCreateUser(ObjectUtil.isNull(createUser) ? 0L : Long.parseLong(createUser.toString()));
                saveCouponRequest.setGetUserId(ObjectUtil.isNull(createUser) ? 0L : Long.parseLong(createUser.toString()));
                saveCouponRequest.setCreateTime(new Date());
                saveCouponRequest.setGetType(CouponGetTypeEnum.GIVE.getCode());
                saveCouponRequest.setExpectGiveNumber(importGoodsForm.getNum());
                Boolean flag = couponActivityApi.giveCouponBySingal(saveCouponRequest, couponActivityDetailDTO);
                if (!flag) {
                    importGoodsForm.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                    continue;
                }
                importGoodsForm.setStatus("成功");
                // 成功以后扣减查出来的map里面数量。有并发问题
                Integer remain = remainByActivityIds.get(importGoodsForm.getCouponActivityId());
                remainByActivityIds.put(importGoodsForm.getCouponActivityId(), remain - importGoodsForm.getNum());
            } catch (BusinessException e) {
                importGoodsForm.setErrorMsg(e.getMessage());
                log.error("excel批量发券报错", e);
            } catch (Exception e) {
                importGoodsForm.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
