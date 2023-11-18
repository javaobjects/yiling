package com.yiling.export.imports.handler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmHospitalDrugstoreRelationApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmHospitalDrugstoreRelRequest;
import com.yiling.dataflow.crm.enums.CrmGoodsStatusEnum;
import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportCrmHospitalDrugstoreRelModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author fucheng.bai
 * @date 2023/6/1
 */
@Slf4j
@Service
public class ImportCrmHospitalDrugstoreRelHandler extends AbstractExcelImportHandler<ImportCrmHospitalDrugstoreRelModel> {

    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private CrmHospitalDrugstoreRelationApi crmHospitalDrugstoreRelationApi;

    @Override
    protected ExcelVerifyHandlerResult verify(ImportCrmHospitalDrugstoreRelModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        String errorMsg = model.getErrorMsg();
        if (StrUtil.isNotEmpty(errorMsg)) {
            return this.error(errorMsg);
        }

        {
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(model.getDrugstoreOrgId());
            if (crmEnterpriseDTO == null) {
                return this.error("院外药店机构编码错误");
            }
            if (crmEnterpriseDTO.getBusinessCode() == 2) {
                return this.error("院外药店机构编码已失效");
            }
            if (!crmEnterpriseDTO.getName().equals(model.getDrugstoreOrgName())) {
                return this.error("院外药店机构编码与名称不对应");
            }
        }

        {
            CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(model.getHospitalOrgId());
            if (crmEnterpriseDTO == null) {
                return this.error("医疗机构编码错误");
            }
            if (crmEnterpriseDTO.getBusinessCode() == 2) {
                return this.error("医疗机构编码已失效");
            }
            if (!crmEnterpriseDTO.getName().equals(model.getHospitalOrgName())) {
                return this.error("医疗机构编码与名称不对应");
            }
        }

        {
            CrmGoodsCategoryDTO crmGoodsCategoryDTO = crmGoodsCategoryApi.findByCodeOrName(null, model.getCategoryName());
            if (crmGoodsCategoryDTO == null) {
                return this.error("品种名称不存在");
            }
            model.setCategoryId(crmGoodsCategoryDTO.getId());

            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoApi.getByNameAndSpec(model.getCrmGoodsName(), model.getCrmGoodsSpec(), CrmGoodsStatusEnum.NORMAL.getCode());
            if (crmGoodsInfoDTO == null) {
                return this.error("产品名称或规格不存在");
            }
            if (!crmGoodsInfoDTO.getCategoryId().equals(model.getCategoryId())) {
                return this.error("产品名称和品种名称不对应");
            }
            model.setCrmGoodsCode(crmGoodsInfoDTO.getGoodsCode());
        }

        {
            try {
                Date startTime = DateUtil.parse(model.getEffectStartTime(), "yyyy-MM-dd");
                Date endTime = DateUtil.parse(model.getEffectEndTime(), "yyyy-MM-dd");
                if (startTime.after(endTime)) {
                    return this.error("开始时间在结束时间之后");
                }
            } catch (Exception e) {
                return this.error("生效时间格式错误");
            }
        }

        return result;
    }

    @Override
    protected List<ImportCrmHospitalDrugstoreRelModel> importData(List<ImportCrmHospitalDrugstoreRelModel> object, Map<String, Object> paramMap) {
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        for (ImportCrmHospitalDrugstoreRelModel model : object) {
            try {
                SaveOrUpdateCrmHospitalDrugstoreRelRequest request = PojoUtils.map(model, SaveOrUpdateCrmHospitalDrugstoreRelRequest.class);

                request.setEffectStartTime(DateUtil.parse(model.getEffectStartTime(), "yyyy-MM-dd"));
                request.setEffectEndTime(DateUtil.parse(model.getEffectEndTime(), "yyyy-MM-dd"));
                request.setDataSource(1);
                request.setLastOpTime(DateUtil.date());
                request.setLastOpUser(opUserId);
                request.setOpUserId(opUserId);
                crmHospitalDrugstoreRelationApi.saveOrUpdate(request);
            } catch (BusinessException e) {
                model.setErrorMsg(e.getMessage());
                log.warn("数据保存出错：{}", e.getMessage(), e);
            } catch (Exception e) {
                model.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
