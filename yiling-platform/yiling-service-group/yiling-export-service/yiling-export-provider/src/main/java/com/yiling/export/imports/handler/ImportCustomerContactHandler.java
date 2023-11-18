package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportCustomerContactModel;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.CustomerContactApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerContactDTO;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.request.ImportCustomerContactRequest;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.dto.UserDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入企业客户商务联系人数据验证处理器
 *
 * @author: lun.yu
 * @date: 2023-06-05
 */
@Slf4j
@Component
public class ImportCustomerContactHandler extends AbstractExcelImportHandler<ImportCustomerContactModel> {

    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    CustomerApi customerApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    UserApi userApi;
    @DubboReference
    CustomerContactApi customerContactApi;

    @Override
    public ExcelVerifyHandlerResult verify(ImportCustomerContactModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        {
            // 采购商企业ID
            Long customerEid = model.getCustomerEid();
            if (Objects.nonNull(customerEid) && customerEid != 0) {
                EnterpriseDTO enterpriseDTO = enterpriseApi.getById(customerEid);
                if (Objects.isNull(enterpriseDTO)) {
                    return this.error("渠道商ID不存在");
                }

                EnterpriseCustomerDTO enterpriseCustomerDTO = customerApi.get(Constants.YILING_EID, customerEid);
                if (Objects.isNull(enterpriseCustomerDTO)) {
                    return this.error("渠道商ID未维护");
                }
            }
        }

        {
            // 用户ID
            Long userId = model.getContactUserId();
            if (Objects.nonNull(userId) && userId != 0) {
                UserDTO userDTO = userApi.getById(userId);
                if (Objects.isNull(userDTO)) {
                    return this.error("业务员ID不存在");
                }

                EnterpriseEmployeeDTO employeeDTO = employeeApi.getByEidUserId(Constants.YILING_EID, userId);
                if (Objects.isNull(employeeDTO)) {
                    return this.error("业务员ID未维护");
                }

                List<EnterpriseCustomerContactDTO> customerContactDTOList = customerContactApi.listByEidAndCustomerEid(Constants.YILING_EID, model.getCustomerEid());
                List<Long> contactUserIdList = customerContactDTOList.stream().map(EnterpriseCustomerContactDTO::getContactUserId).distinct().collect(Collectors.toList());
                if (contactUserIdList.contains(userId)) {
                    return this.error("渠道商对应的业务员已存在");
                }

            }
        }

        return result;
    }

    @Override
    public List<ImportCustomerContactModel> importData(List<ImportCustomerContactModel> object, Map<String,Object> paramMap) {

        List<ImportCustomerContactRequest> customerContactRequestList = PojoUtils.map(object, ImportCustomerContactRequest.class);
        customerContactRequestList.forEach(importCustomerContactRequest -> {
            importCustomerContactRequest.setEid(Constants.YILING_EID);
            importCustomerContactRequest.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID,0));
        });

        try {
            customerContactApi.importCustomerContact(customerContactRequestList);
        } catch (Exception e) {
            log.error("数据保存出错：{}", e.getMessage(), e);
        }
        return object;
    }

}
