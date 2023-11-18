package com.yiling.export.imports.handler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.yiling.export.excel.handler.BaseImportHandler;
import com.yiling.export.imports.model.ImportMrSalesGoodsDataModel;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.GoodsInfoDTO;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.enums.EmployeeTypeEnum;
import com.yiling.user.system.api.MrApi;
import com.yiling.user.system.dto.request.UpdateMrSalesGoodsRequest;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 导入批量修改企业管理员账号处理器
 *
 * @author xuan.zhou
 * @date 2022/4/12
 */
@Slf4j
@Service
public class ImportMrSalesGoodsDataHandler extends BaseImportHandler<ImportMrSalesGoodsDataModel> {

    @DubboReference
    MrApi mrApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    PopGoodsApi popGoodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportMrSalesGoodsDataModel form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        String salesGoodsIds = form.getSalesGoodsIds();
        if (StrUtil.isNotBlank(salesGoodsIds)) {
            // 中文逗号转换成英文逗号
            salesGoodsIds = StrUtil.replaceChars(salesGoodsIds, "，", ",");
            List<String> salesGoodsIdList = StrUtil.splitTrim(salesGoodsIds, ",");
            if (CollUtil.isNotEmpty(salesGoodsIdList)) {
                form.setGoodsIds(salesGoodsIdList.stream().map(Long::valueOf).distinct().collect(Collectors.toList()));
            }
        }

        return result;
    }

    @Override
    public List<ImportMrSalesGoodsDataModel> execute(List<ImportMrSalesGoodsDataModel> object, Map<String, Object> paramMap) {
        Long opUserId = (Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_USER_ID, 0L);
        Long opEid = (Long) paramMap.getOrDefault(MyMetaHandler.FIELD_OP_EID, 0L);
        List<Long> opEids = ListUtil.toList(opEid);
        if (Constants.YILING_EID.equals(opEid)) {
            opEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        }

        for (ImportMrSalesGoodsDataModel form : object) {
            Result validateResult = this.validateEmployeeId(opEid, form.getEmployeeId());
            if (!validateResult.isSuccessful()) {
                form.setErrorMsg(validateResult.getMessage());
                continue;
            }

            validateResult = this.valiateGoodsIds(opEids, form.getGoodsIds());
            if (!validateResult.isSuccessful()) {
                form.setErrorMsg(validateResult.getMessage());
                continue;
            }

            try {
                UpdateMrSalesGoodsRequest request = PojoUtils.map(form, UpdateMrSalesGoodsRequest.class);
                request.setOpUserId(opUserId);
                boolean result = mrApi.updateSalesGoods(request);
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

    private Result validateEmployeeId(Long eid, Long employeeId) {
        EnterpriseEmployeeDTO enterpriseEmployeeDTO = employeeApi.getById(employeeId);
        if (enterpriseEmployeeDTO == null) {
            return Result.failed("未找到员工ID对应的信息");
        } else if (!enterpriseEmployeeDTO.getEid().equals(eid)) {
            return Result.failed("非本公司员工ID");
        } else if (EmployeeTypeEnum.getByCode(enterpriseEmployeeDTO.getType()) != EmployeeTypeEnum.MR) {
            return Result.failed("该员工不是医药代表");
        } else if (EnableStatusEnum.getByCode(enterpriseEmployeeDTO.getStatus()) != EnableStatusEnum.ENABLED) {
            return Result.failed("该员工非启用状态");
        }

        return Result.success();
    }

    private Result valiateGoodsIds(List<Long> eids, List<Long> goodsIds) {
        if (CollUtil.isEmpty(goodsIds)) {
            return Result.success();
        }

        List<GoodsInfoDTO> list = popGoodsApi.batchQueryPopGoods(goodsIds);
        List<Long> validGoodsIds = list.stream().map(GoodsInfoDTO::getId).collect(Collectors.toList());
        List<Long> invaildGoodsIds = goodsIds.stream().filter(e -> !validGoodsIds.contains(e)).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(invaildGoodsIds)) {
            return Result.failed("未找到ID对应的商品信息：" + StrUtil.join(",", invaildGoodsIds));
        }

        List<GoodsInfoDTO> invalidGoodsInfoDTOList = list.stream().filter(e -> !eids.contains(e.getEid())).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(invalidGoodsInfoDTOList)) {
            return Result.failed("非本公司商品ID：" + StrUtil.join(",", invalidGoodsInfoDTOList.stream().map(GoodsInfoDTO::getId).collect(Collectors.toList())));
        }

        return Result.success();
    }
}
