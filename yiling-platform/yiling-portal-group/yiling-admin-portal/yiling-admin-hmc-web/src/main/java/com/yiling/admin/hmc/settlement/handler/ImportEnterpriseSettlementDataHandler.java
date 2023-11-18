package com.yiling.admin.hmc.settlement.handler;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.yiling.export.excel.handler.ImportDataHandler;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.yiling.admin.hmc.settlement.form.ImportEnterpriseSettlementForm;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.mybatis.MyMetaHandler;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.order.api.OrderApi;
import com.yiling.hmc.order.api.OrderDetailApi;
import com.yiling.hmc.order.dto.OrderDTO;
import com.yiling.hmc.order.dto.OrderDetailDTO;
import com.yiling.hmc.order.enums.HmcOrderStatusEnum;
import com.yiling.hmc.settlement.api.EnterpriseSettlementApi;
import com.yiling.hmc.settlement.dto.request.EnterpriseSettlementRequest;
import com.yiling.hmc.settlement.enums.InsuranceSettlementStatusEnum;
import com.yiling.hmc.settlement.enums.TerminalSettlementStatusEnum;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: yong.zhang
 * @date: 2022/4/20
 */
@Component
@Slf4j
public class ImportEnterpriseSettlementDataHandler implements IExcelVerifyHandler<ImportEnterpriseSettlementForm>, ImportDataHandler<ImportEnterpriseSettlementForm> {

    @DubboReference
    EnterpriseSettlementApi enterpriseSettlementApi;

    @DubboReference
    OrderDetailApi orderDetailApi;

    @DubboReference
    OrderApi orderApi;

    private ExcelVerifyHandlerResult error(String errorMessage) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(false);
        result.setMsg(errorMessage);
        return result;
    }

    @Override
    public ExcelVerifyHandlerResult verifyHandler(ImportEnterpriseSettlementForm form) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);

        // 校验导入的数据是否符合规则
        Long orderId;
        OrderDetailDTO orderDetailDTO;
        {
            // 订单明细编号
            Long detailId = form.getDetailId();
            orderDetailDTO = orderDetailApi.getById(detailId);
            if (Objects.isNull(orderDetailDTO)) {
                return this.error("此订单明细不存在");
            }
            orderId = orderDetailDTO.getOrderId();
        }
        OrderDTO orderDTO;
        {
            // 订单编号
            String orderNo = form.getOrderNo();
            orderDTO = orderApi.queryById(orderId);
            if (Objects.isNull(orderDTO) || !orderDTO.getOrderNo().equals(orderNo)) {
                return this.error("此订单编号与订单明细编号不匹配");
            }
            if (TerminalSettlementStatusEnum.getByCode(orderDTO.getTerminalSettleStatus()) == TerminalSettlementStatusEnum.SETTLEMENT) {
                return this.error("此导入订单明细已经结算");
            }
        }

        {
            // 订单状态
            String orderStatusStr = form.getOrderStatus();
            Integer orderStatus = orderDTO.getOrderStatus();
            if (null == HmcOrderStatusEnum.getByCode(orderStatus)) {
                return this.error("此订单的订单状态有误");
            }
            if (!orderStatusStr.equals(HmcOrderStatusEnum.getByCode(orderStatus).getName())) {
                return this.error("导入的订单订单状态错误");
            }
        }

        {
            // 保司结算状态
            String insuranceSettlementStatus = form.getInsuranceSettlementStatus();
            Integer insuranceSettleStatus = orderDTO.getInsuranceSettleStatus();
            if (null == InsuranceSettlementStatusEnum.getByCode(insuranceSettleStatus)) {
                return this.error("此订单的保司结算状态有误");
            }
            if (!insuranceSettlementStatus.equals(InsuranceSettlementStatusEnum.getByCode(insuranceSettleStatus).getName())) {
                return this.error("导入的保司结算状态错误");
            }
        }

        {
            // 药品服务终端ID
            Long eid = form.getEid();
            if (!eid.equals(orderDTO.getEid())) {
                return this.error("导入的药品服务终端ID错误");
            }
        }

        {
            // 药品服务终端名称
            String ename = form.getEname();
            if (!ename.equals(orderDTO.getEname())) {
                return this.error("导入的药品服务终端名称错误");
            }
        }

        {
            // 金额格式校验
            try {
                BigDecimal settlementAmount = new BigDecimal(form.getSettlementAmount());
                BigDecimal terminalSettleAmount = orderDetailDTO.getTerminalSettleAmount();
                if (terminalSettleAmount.compareTo(settlementAmount) != 0) {
                    return this.error("导入的药品结算金额与计算金额不一致");
                }
            } catch (Exception e) {
                form.setErrorMsg("结账金额格式错误");
            }
        }
        return result;
    }

    @Override
    public List<ImportEnterpriseSettlementForm> execute(List<ImportEnterpriseSettlementForm> object, Map<String, Object> paramMap) {
        log.info("商家导入结算数据, 导入成功后待新增的数据为:[{}]", JSON.toJSONString(object));
        Long opUserId = (Long) paramMap.get(MyMetaHandler.FIELD_OP_USER_ID);
        Long eid = (Long) paramMap.get("eid");
        for (ImportEnterpriseSettlementForm form : object) {
            try {
                if (!eid.equals(form.getEid())) {
                    form.setErrorMsg("导入结算数据企业不匹配");
                    continue;
                }
                EnterpriseSettlementRequest request = PojoUtils.map(form, EnterpriseSettlementRequest.class);
                request.setSettleAmount(new BigDecimal(form.getSettlementAmount()));
                request.setSettleTime(DateUtil.parse(form.getSettlementTime()));
                request.setExecutionTime(DateUtil.parse(form.getExecutionTime()));
                request.setOpUserId(opUserId);
                boolean result = enterpriseSettlementApi.importSettlementData(request);
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
