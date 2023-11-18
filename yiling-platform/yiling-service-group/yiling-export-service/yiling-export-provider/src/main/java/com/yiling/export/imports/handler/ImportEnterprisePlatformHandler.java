package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.handler.AbstractExcelImportHandler;
import com.yiling.export.imports.model.ImportEnterprisePlatformModel;
import com.yiling.framework.common.enums.PlatformEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.ImportEnterprisePlatformRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.enterprise.enums.EnterpriseHmcTypeEnum;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入企业开通平台处理器
 *
 * @author: lun.yu
 * @date: 2022-06-29
 */
@Slf4j
@Service
public class ImportEnterprisePlatformHandler extends AbstractExcelImportHandler<ImportEnterprisePlatformModel> {

    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public ExcelVerifyHandlerResult verify(ImportEnterprisePlatformModel model) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        Long id = model.getId();
        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(id);
        {
            // 企业ID
            if (Objects.isNull(id)) {
                return this.error("企业ID不能为空");
            } else if (Objects.isNull(enterpriseDTO)) {
                return this.error("企业ID不存在");
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
                if (EnterpriseTypeEnum.getByCode(enterpriseDTO.getType()) == EnterpriseTypeEnum.INDUSTRY || EnterpriseTypeEnum.getByCode(enterpriseDTO.getType()) == EnterpriseTypeEnum.BUSINESS) {
                    return this.error("工业或商业无法开通健康管理中心");
                }
            }
        }

        return result;
    }

    @Override
    public List<ImportEnterprisePlatformModel> importData(List<ImportEnterprisePlatformModel> object, Map<String, Object> paramMap) {
        for (ImportEnterprisePlatformModel form : object) {
            try {
                ImportEnterprisePlatformRequest request = PojoUtils.map(form, ImportEnterprisePlatformRequest.class);
                request.setOpUserId((Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0));

                EnterpriseChannelEnum enterpriseChannelEnum = EnterpriseChannelEnum.getByName(form.getChannelName());
                if (Objects.nonNull(enterpriseChannelEnum)) {
                    request.setChannelId(enterpriseChannelEnum.getCode());
                }

                // 药+险业务类型
                EnterpriseHmcTypeEnum hmcTypeEnum = EnterpriseHmcTypeEnum.getByName(form.getHmcType());
                if (hmcTypeEnum != null) {
                    request.setHmcTypeEnum(hmcTypeEnum);
                }

                List<String> productNames = StrUtil.splitTrim(form.getProducts(), "，");
                List<PlatformEnum> platformEnumList = CollUtil.newArrayList();
                for (String productName : productNames) {
                    platformEnumList.add(PlatformEnum.getByName(productName));
                }

                request.setPlatformEnumList(platformEnumList);

                boolean result = enterpriseApi.importEnterprisePlatform(request);
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
