package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.basic.location.api.LocationApi;
import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportEnterpriseModel;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.ImportEnterpriseRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入企业处理器
 *
 * @author: xuan.zhou
 * @date: 2021/5/26
 */
@Slf4j
@Service
public class ImportEnterpriseHandler extends AbstractExcelImportHandler<ImportEnterpriseModel> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    LocationApi locationApi;

    @Override
    public ExcelVerifyHandlerResult verify(ImportEnterpriseModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        String errorMsg = model.getErrorMsg();
        if (StrUtil.isNotEmpty(errorMsg)) {
            return this.error(errorMsg);
        }

        {
            // 企业类型
            String typeName = model.getTypeName();
            if (StrUtil.isBlank(typeName)) {
                return this.error("企业类型不能为空");
            } else if (EnterpriseTypeEnum.getByName(typeName) == null) {
                return this.error("企业类型不匹配");
            }
        }

        {
            // 所属总店ID
            Long parentId = model.getParentId();
            if (parentId != null && parentId != 0L) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(parentId);
                if (enterpriseDTO == null) {
                    return this.error("所属总店ID错误");
                }
            }
        }

        {
            // 社会统一信用代码/医疗机构执业许可证
            String licenseNumber = model.getLicenseNumber();
            EnterpriseDTO enterpriseDTO = enterpriseApi.getByLicenseNumber(licenseNumber);
            if (enterpriseDTO != null) {
                return this.error("社会统一信用代码/医疗机构执业许可证号对应的企业已存在");
            }
        }

        {
            // 省市区名称
            boolean validate = locationApi.validateName(model.getProvinceName(), model.getCityName(), model.getRegionName());
            if (!validate) {
                result.setSuccess(false);
                result.setMsg("省市区地址错误");
                return result;
            }
        }

        {
            // 开通产品线
            String products = model.getProducts();
            List<String> productNames = StrUtil.splitTrim(products, "，");
            for (String productName : productNames) {
                if (PlatformEnum.getByName(productName) == null) {
                    return this.error("产品线名称不匹配");
                }
            }

            // 渠道类型
            if (productNames.contains(PlatformEnum.POP.getName())) {
                String channelName = model.getChannelName();
                if (StrUtil.isBlank(channelName)) {
                    return this.error("渠道类型不能为空");
                } else if (EnterpriseChannelEnum.getByName(channelName) == null) {
                    return this.error("渠道类型不匹配");
                }
            }

            // 药+险业务类型
            if (productNames.contains(PlatformEnum.HMC.getName())) {
                String hmcType = model.getHmcType();
                if (StrUtil.isBlank(hmcType)) {
                    return this.error("药+险业务类型不能为空");
                } else if (EnterpriseHmcTypeEnum.getByName(hmcType) == null) {
                    return this.error("药+险业务类型不匹配");
                }

                // 工业和商业不允许开通健康管理中心
                if (EnterpriseTypeEnum.getByName(model.getTypeName()) == EnterpriseTypeEnum.INDUSTRY || EnterpriseTypeEnum.getByName(model.getTypeName()) == EnterpriseTypeEnum.BUSINESS) {
                    return this.error("工业或商业无法开通健康管理中心");
                }
            }
        }

        return result;
    }

    @Override
    public List<ImportEnterpriseModel> importData(List<ImportEnterpriseModel> object, Map<String, Object> paramMap) {
        for (ImportEnterpriseModel form : object) {
            try {
                ImportEnterpriseRequest request = PojoUtils.map(form, ImportEnterpriseRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));

                String[] locations = locationApi.getCodesByNames(request.getProvinceName(), request.getCityName(), request.getRegionName());
                request.setProvinceCode(locations[0]);
                request.setCityCode(locations[1]);
                request.setRegionCode(locations[2]);

                request.setType(EnterpriseTypeEnum.getByName(form.getTypeName()).getCode());
                EnterpriseChannelEnum enterpriseChannelEnum = EnterpriseChannelEnum.getByName(form.getChannelName());
                if (enterpriseChannelEnum != null) {
                    request.setChannelId(enterpriseChannelEnum.getCode());
                }

                // 药+险业务类型
                EnterpriseHmcTypeEnum hmcTypeEnum = EnterpriseHmcTypeEnum.getByName(form.getHmcType());
                if (Objects.nonNull(hmcTypeEnum)) {
                    request.setHmcTypeEnum(hmcTypeEnum);
                }

                List<String> productNames = StrUtil.splitTrim(form.getProducts(), "，");
                List<PlatformEnum> platformEnumList = CollUtil.newArrayList();
                for (String productName : productNames) {
                    platformEnumList.add(PlatformEnum.getByName(productName));
                }
                request.setPlatformEnumList(platformEnumList);

                boolean result = enterpriseApi.importData(request);
                if (!result) {
                    form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                }
            } catch (BusinessException be) {
                form.setErrorMsg(be.getMessage());
            } catch (Exception e) {
                form.setErrorMsg(ResultCode.EXCEL_DATA_SAVING_FAILED.getMessage());
                log.error("数据保存出错：{}", e.getMessage(), e);
            }
        }
        return object;
    }
}
