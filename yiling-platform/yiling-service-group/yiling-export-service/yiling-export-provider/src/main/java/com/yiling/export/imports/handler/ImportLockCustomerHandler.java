package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.basic.location.api.LocationApi;
import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportLockCustomerModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.lockcustomer.api.LockCustomerApi;
import com.yiling.user.lockcustomer.dto.LockCustomerDTO;
import com.yiling.user.lockcustomer.dto.request.ImportLockCustomerRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入锁定用户数据验证处理器
 *
 * @author: lun.yu
 * @date: 2022-04-02
 */
@Slf4j
@Component
public class ImportLockCustomerHandler extends BaseImportHandler<ImportLockCustomerModel> {

    @DubboReference
    LocationApi locationApi;
    @DubboReference
    LockCustomerApi lockCustomerApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportLockCustomerModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        {
            // 社会统一信用代码/医疗机构执业许可证
            String licenseNumber = model.getLicenseNumber();
            LockCustomerDTO lockCustomerDTO = lockCustomerApi.getByLicenseNumber(licenseNumber);
            if (Objects.nonNull(lockCustomerDTO)) {
                return this.error("已存在该社会统一信用代码/医疗机构执业许可证号");
            }
        }

        {
            String statusName = model.getStatusName();
            if (StrUtil.isNotEmpty(statusName) && !"启用".equals(statusName) && !"禁用".equals(statusName)) {
                return this.error("状态只能为启用或禁用");
            }
        }

        {
            // 企业类型
            String typeName = model.getTypeName();
            if (EnterpriseTypeEnum.getByName(typeName) == null) {
                return this.error("企业类型不匹配");
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

        return result;
    }

    @Override
    public List<ImportLockCustomerModel> execute(List<ImportLockCustomerModel> object, Map<String,Object> paramMap) {
        for (ImportLockCustomerModel form : object) {
            try {
                ImportLockCustomerRequest request = PojoUtils.map(form, ImportLockCustomerRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));

                String[] locations = locationApi.getCodesByNames(request.getProvinceName(), request.getCityName(), request.getRegionName());
                request.setProvinceCode(locations[0]);
                request.setCityCode(locations[1]);
                request.setRegionCode(locations[2]);

                request.setType(EnterpriseTypeEnum.getByName(form.getTypeName()).getCode());
                if ("启用".equals(form.getStatusName())) {
                    request.setStatus(1);
                } else {
                    request.setStatus(2);
                }

                boolean result = lockCustomerApi.importData(request);
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
