package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportUpdateManagerAccountModel;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.request.UpdateManagerMobileRequest;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.bo.Staff;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入批量修改企业管理员账号处理器
 *
 * @author xuan.zhou
 * @date 2022/4/12
 */
@Slf4j
@Service
public class ImportUpdateManagerAccountHandler extends BaseImportHandler<ImportUpdateManagerAccountModel> {

    @DubboReference
    StaffApi staffApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportUpdateManagerAccountModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        List<Staff> staffList = employeeApi.listAdminsByEid(model.getEid());
        if (CollUtil.isEmpty(staffList)) {
            return this.error("未找到企业管理员信息");
        } else {
            model.setUserId(staffList.get(0).getId());
        }

        return result;
    }

    @Override
    public List<ImportUpdateManagerAccountModel> execute(List<ImportUpdateManagerAccountModel> object, Map<String,Object> paramMap) {
        for (ImportUpdateManagerAccountModel form : object) {
            try {
                UpdateManagerMobileRequest request = PojoUtils.map(form, UpdateManagerMobileRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
                boolean result = enterpriseApi.updateManagerMobile(request);
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
