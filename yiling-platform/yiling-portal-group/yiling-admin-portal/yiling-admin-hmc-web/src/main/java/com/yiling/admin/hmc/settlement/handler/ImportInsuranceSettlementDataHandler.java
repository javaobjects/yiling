package com.yiling.admin.hmc.settlement.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.admin.hmc.settlement.form.ImportInsuranceSettlementForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.dto.InsuranceCompanyDTO;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.settlement.api.InsuranceSettlementApi;
import com.yiling.hmc.settlement.dto.request.InsuranceSettlementRequest;
import com.yiling.hmc.settlement.enums.InsuranceSettlementStatusEnum;
import com.yiling.hmc.wechat.api.InsuranceRecordApi;
import com.yiling.hmc.wechat.dto.InsuranceRecordDTO;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * 保司导入结算数据校验
 *
 * @author: yong.zhang
 * @date: 2022/5/13
 */
@Component
@Slf4j
public class ImportInsuranceSettlementDataHandler implements IExcelVerifyHandler<ImportInsuranceSettlementForm>, ImportDataHandler<ImportInsuranceSettlementForm> {

    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;

    @DubboReference
    InsuranceRecordApi insuranceRecordApi;

    @DubboReference
    OrderApi orderApi;

    @DubboReference
    InsuranceSettlementApi insuranceSettlementApi;

    private ExcelVerifyHandlerResult error(String errorMessage) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
        result.setMsg(errorMessage);
        return result;
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportInsuranceSettlementForm form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        {
            // 校验服务商ID
            Long insuranceCompanyId = form.getInsuranceCompanyId();
            InsuranceCompanyDTO insuranceCompanyDTO = insuranceCompanyApi.queryById(insuranceCompanyId);
            if (Objects.isNull(insuranceCompanyDTO)) {
                return this.error("此服务商ID不存在");
            }
        }

        {
            // 校验保单号
            String policyNo = form.getPolicyNo();
            InsuranceRecordDTO insuranceRecordDTO = insuranceRecordApi.getByPolicyNo(policyNo);
            if (Objects.isNull(insuranceRecordDTO)) {
                return this.error("此保单号不存在");
            }
            if (!form.getInsuranceCompanyId().equals(insuranceRecordDTO.getInsuranceCompanyId())) {
                return this.error("此保单号与服务商ID不匹配");
            }
        }

        List<OrderDTO> orderDTOList = new ArrayList<>();
        {
            // 校验对应药品订单号
            String orderNoStr = form.getOrderNoStr();
            String[] orderStr = orderNoStr.split(",");
            for (String orderNo : orderStr) {
                OrderDTO orderDTO = orderApi.queryByOrderNo(orderNo);
                if (Objects.isNull(orderDTO)) {
                    return this.error("对应药品订单号" + orderNo + "不存在");
                }
                if (InsuranceSettlementStatusEnum.SETTLEMENT == InsuranceSettlementStatusEnum.getByCode(orderDTO.getInsuranceSettleStatus())) {
                    return this.error("对应药品订单号" + orderNo + "已经结算");
                }
                if (!form.getInsuranceCompanyId().equals(orderDTO.getInsuranceCompanyId())) {
                    return this.error("对应药品订单号与服务商ID不匹配");
                }
                if (!form.getPolicyNo().equals(orderDTO.getPolicyNo())) {
                    return this.error("对应药品订单号与保单号不匹配");
                }
                orderDTOList.add(orderDTO);
            }
        }

        {
            // 校验打款金额
            String amount = form.getPayAmount();
            try {
                BigDecimal payAmount = new BigDecimal(amount);
                BigDecimal totalAmount = orderDTOList.stream().map(OrderDTO::getInsuranceSettleAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                if (payAmount.compareTo(totalAmount) != 0) {
                    return this.error("打款金额与订单对应的结算金额不一致");
                }
            } catch (Exception e) {
                form.setErrorMsg("打款金额格式错误");
            }
        }

        {
            try {
                DateTime dateTime = DateUtil.parse(form.getPayTime());
            } catch (Exception e) {
                form.setErrorMsg("时间格式错误");
            }

        }
        return result;
    }

    @Override
    public List<ImportInsuranceSettlementForm> execute(List<ImportInsuranceSettlementForm> object, Map<String, Object> paramMap) {
        log.info("保司导入结算数据, 导入成功后待新增的数据为:[{}]", JSON.toJSONString(object));

        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        for (ImportInsuranceSettlementForm form : object) {
            // 成功导入的结算数据处理：新增结算单
            try {
                InsuranceSettlementRequest request = PojoUtils.map(form, InsuranceSettlementRequest.class);
                request.setPayTime(DateUtil.parse(form.getPayTime()));
                request.setPayAmount(new BigDecimal(form.getPayAmount()));
                request.setOpUserId(opUserId);
                boolean result = insuranceSettlementApi.importSettlementData(request);
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
