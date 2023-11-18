package com.yiling.f2b.admin.order.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollectionUtil;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.f2b.admin.order.form.QueryOrderManagePageForm;
import com.yiling.f2b.admin.order.vo.OrderAddressVO;
import com.yiling.f2b.admin.order.vo.OrderDetailVO;
import com.yiling.f2b.admin.order.vo.OrderManageDetailVO;
import com.yiling.f2b.admin.order.vo.OrderManageDisableVO;
import com.yiling.f2b.admin.order.vo.OrderManagePageVO;
import com.yiling.f2b.admin.order.vo.OrderManageStatusNumberVO;
import com.yiling.f2b.admin.order.vo.PaymentDaysInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.oss.enums.FileTypeEnum;
import com.yiling.framework.oss.service.FileService;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.inventory.dto.InventoryDTO;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.dto.GoodsSkuDTO;
import com.yiling.order.order.api.OrderAddressApi;
import com.yiling.order.order.api.OrderApi;
import com.yiling.order.order.api.OrderDetailApi;
import com.yiling.order.order.api.OrderDetailChangeApi;
import com.yiling.order.order.dto.OrderAddressDTO;
import com.yiling.order.order.dto.OrderAttachmentDTO;
import com.yiling.order.order.dto.OrderDTO;
import com.yiling.order.order.dto.OrderDetailChangeDTO;
import com.yiling.order.order.dto.OrderDetailDTO;
import com.yiling.order.order.dto.OrderGoodsTypeAndNumberDTO;
import com.yiling.order.order.dto.OrderManageStatusNumberDTO;
import com.yiling.order.order.dto.request.QueryOrderManagePageRequest;
import com.yiling.order.order.enums.OrderAttachmentTypeEnum;
import com.yiling.order.order.enums.OrderAuditStatusEnum;
import com.yiling.order.order.enums.OrderErrorCode;
import com.yiling.order.order.enums.OrderSourceEnum;
import com.yiling.user.common.util.bean.In;
import com.yiling.user.enterprise.api.DepartmentApi;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterpriseDepartmentDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDTO;
import com.yiling.user.enterprise.dto.EnterpriseEmployeeDepartmentDTO;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysAccountDTO;
import com.yiling.user.system.bo.CurrentStaffInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author:wei.wang
 * @date:2021/7/12
 */
@Slf4j
@RestController
@Api(tags = "订单审核管理")
@RequestMapping("/order/manage")
public class OrderManageController extends BaseController {
    @DubboReference
    OrderApi              orderApi;
    @DubboReference
    OrderDetailApi        orderDetailApi;
    @DubboReference
    OrderAddressApi       orderAddressApi;
    @DubboReference
    GoodsApi              goodsApi;
    @DubboReference
    InventoryApi          inventoryApi;
    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    OrderDetailChangeApi  orderDetailChangeApi;
    @DubboReference
    EnterpriseApi         enterpriseApi;
    @DubboReference
    DepartmentApi departmentApi;

    @Autowired
    FileService fileService;
    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @ApiOperation(value = "审核订单数量")
    @GetMapping("/get/number")
    public Result<OrderManageStatusNumberVO> getOrderReviewStatusNumber(@CurrentUser CurrentStaffInfo staffInfo,
                                                                        @RequestParam(value = "departmentType") Integer departmentType) {

        List<Long> sellerEidList;
        if (staffInfo.getYilingFlag()) {
            sellerEidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        } else {
            sellerEidList = Collections.singletonList(staffInfo.getCurrentEid());
        }
        EnterpriseDepartmentDTO  enterpriseDepartment = null;
        if(departmentType == 1 || departmentType == 2){
            //万州
           enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.WZ_DEPARTMENT_CODE);
        }else if (departmentType == 3){
            //大运河数拓部门
            enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
        }else if(departmentType == 4){
            //大运河分销部门
            enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
        }

        Long departmentId = null ;
        if(enterpriseDepartment != null){
            departmentId = enterpriseDepartment.getId();
        }else{
            if(departmentType != 2){
                //订单审核没有部门
                return Result.success(new OrderManageStatusNumberVO());
            }
        }


        OrderManageStatusNumberDTO orderReviewStatusNumber = orderApi.getOrderReviewStatusNumber(sellerEidList,departmentId,departmentType);

        return Result.success(PojoUtils.map(orderReviewStatusNumber, OrderManageStatusNumberVO.class));
    }

    @ApiOperation(value = "审核列表信息")
    @PostMapping("/get/page")
    public Result<Page<OrderManagePageVO>> getOrderManagePage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryOrderManagePageForm form) {
        QueryOrderManagePageRequest request = PojoUtils.map(form, QueryOrderManagePageRequest.class);

        List<Long> sellerEidList;
        if (staffInfo.getYilingFlag()) {
            sellerEidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        } else {
            sellerEidList = Collections.singletonList(staffInfo.getCurrentEid());
        }
        request.setOrderType(OrderSourceEnum.POP_PC.getCode());
        request.setSellerEidList(sellerEidList);

        EnterpriseDepartmentDTO  enterpriseDepartment = null;
        if(request.getDepartmentType() != null){
            if(request.getDepartmentType() == 1 || request.getDepartmentType() == 2){
                //万州
                enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.WZ_DEPARTMENT_CODE);
            }else if (request.getDepartmentType() == 3){
                //大运河数拓部门
                enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_ST_DEPARTMENT_CODE);
            }else if(request.getDepartmentType() == 4){
                //大运河分销部门
                enterpriseDepartment = departmentApi.getByEidCode(Constants.YILING_EID, Constants.DYH_FX_DEPARTMENT_CODE);
            }
        }


        if(enterpriseDepartment != null){
            request.setDepartmentId(enterpriseDepartment.getId());
        }

        Page<OrderDTO> orderManagePage = orderApi.getOrderManagePage(request);
        Page<OrderManagePageVO> result = PojoUtils.map(orderManagePage, OrderManagePageVO.class);

        if (result != null && CollectionUtil.isNotEmpty(result.getRecords())) {
            List<Long> idList = result.getRecords().stream().map(OrderManagePageVO::getId).collect(Collectors.toList());

            List<Long> eidList = result.getRecords().stream().map(OrderManagePageVO::getBuyerEid).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseLists = enterpriseApi.listByIds(eidList);
            Map<Long, EnterpriseDTO> enterpriseMap = enterpriseLists.stream().collect(Collectors.toMap(EnterpriseDTO::getId, one -> one, (k1, k2) -> k1));

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderIds(idList);

            // 查询出所有以岭企业(订单审核优化：增加非大运河工业供应商库存校验)
            List<Long> subEidList = enterpriseApi.listSubEids(Constants.YILING_EID);
            List<Long> directEidList = new ArrayList<Long>() {{
                add(Constants.DAYUNHE_EID);
            }};
            List<Long> longList = subEidList.stream().filter(item -> !directEidList.contains(item)).collect(Collectors.toList());
            Map<Long, List<OrderDetailChangeDTO>> longListMap = orderDetailChangeList.stream().collect(Collectors.groupingBy(OrderDetailChangeDTO::getOrderId));

            List<Long> departmentIdList = orderManagePage.getRecords().stream().map(OrderDTO::getDepartmentId).collect(Collectors.toList());
            List<EnterpriseDepartmentDTO> enterpriseDepartmentList = departmentApi.listByIds(departmentIdList);
            Map<Long, String> departmentNameMap = enterpriseDepartmentList.stream().collect(Collectors.toMap(EnterpriseDepartmentDTO::getId, EnterpriseDepartmentDTO::getName, (k1, k2) -> k1));

            for (OrderManagePageVO one : result.getRecords()) {
                EnterpriseDTO enterpriseDTO = enterpriseMap.get(one.getBuyerEid());
                //货款总额
                BigDecimal totalAmount = BigDecimal.ZERO;
                //折扣金额
                BigDecimal discountAmount = BigDecimal.ZERO;

                for (OrderDetailChangeDTO changeOne : orderDetailChangeList) {
                    if (one.getId().equals(changeOne.getOrderId())) {
                        totalAmount = totalAmount.add(changeOne.getGoodsAmount().subtract(changeOne.getSellerReturnAmount()));
                        discountAmount = discountAmount.add(changeOne.getCashDiscountAmount()).add(changeOne.getTicketDiscountAmount());
                    }
                }
                one.setDepartmentName(departmentNameMap.get(one.getDepartmentId()));

                OrderGoodsTypeAndNumberDTO goodsTypeNumber = orderDetailApi.getOrderGoodsTypeAndNumber(one.getId());
                one.setGoodsOrderNum(goodsTypeNumber.getGoodsOrderNum()).setGoodsOrderPieceNum(goodsTypeNumber.getGoodsOrderPieceNum()).setTotalAmount(totalAmount).setPaymentAmount(totalAmount.subtract(discountAmount)).setDiscountAmount(discountAmount);
                if (enterpriseDTO != null) {
                    one.setBuyerAddress(enterpriseDTO.getProvinceName() + enterpriseDTO.getCityName() + enterpriseDTO.getRegionName());
                }

                {
                    // 是否库存不足
                    boolean checkDisable = false;
                    //List<OrderDetailChangeDTO> changeDTOList = new ArrayList<>();
                    // 待审核状态
                    if (OrderAuditStatusEnum.UNAUDIT.getCode().equals(one.getAuditStatus())) {
                        checkDisable = true;
                        // 如果是以岭非大运河的才需要判断库存是否充足
                        /*if (longList.contains(one.getSellerEid())) {
                            changeDTOList = longListMap.get(one.getId());
                        }*/
                    }
                    /*for (OrderDetailChangeDTO changeDTO : changeDTOList) {
                        //  判断库存数量是否充足
                        if (checkDisable && OrderAuditStatusEnum.UNAUDIT.getCode().equals(one.getAuditStatus()) && longList.contains(one.getSellerEid()) && 0L != changeDTO.getGoodsSkuId()) {
                            // 非大运河(工业直属)企业--库存数量不充足时不显示按钮
                            InventoryDTO inventoryDTO = inventoryApi.getBySkuId(changeDTO.getGoodsSkuId());
                            log.info("审核列表信息，开始判断库存是否充足，库存数据为:[{}],订单商品数据为:[{}]", inventoryDTO, changeDTO);
                            if (null != inventoryDTO && inventoryDTO.getQty() < inventoryDTO.getFrozenQty()) {
                                log.info("审核列表信息,库存不足:[{}],订单号:[{}]", inventoryDTO, one.getOrderNo());
                                checkDisable = false;
                            }
                        }
                    }*/
                    OrderManageDisableVO disableVO = new OrderManageDisableVO();
                    disableVO.setCheckDisable(checkDisable);
                    one.setOrderManageDisableVO(disableVO);
                }
            }
        }
        return Result.success(result);
    }


    @ApiOperation(value = "审核订单详情列表")
    @GetMapping("/get/detail")
    public Result<OrderManageDetailVO> getOrderManageDetail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "orderId") Long orderId) {
        OrderDTO order = orderApi.getOrderInfo(orderId);
        OrderManageDetailVO result = PojoUtils.map(order, OrderManageDetailVO.class);

        EnterpriseDepartmentDTO enterprise = departmentApi.getById(order.getDepartmentId());
        if(enterprise != null){
            result.setDepartmentName(enterprise.getName());
        }


        EnterpriseDTO enterpriseDTO = enterpriseApi.getById(order.getBuyerEid());
        OrderAddressDTO addressInfo = orderAddressApi.getOrderAddressInfo(orderId);
        OrderAddressVO address = PojoUtils.map(addressInfo, OrderAddressVO.class);
        if (addressInfo != null) {
            address.setAddress(addressInfo.getAddress());
            if (enterpriseDTO != null) {
                address.setProvinceName(enterpriseDTO.getProvinceName());
                address.setBuyerEname(order.getBuyerEname());
            }

        }
        result.setOrderAddress(address);

        List<OrderAttachmentDTO> orderContractList = orderApi.listOrderAttachmentsByType(orderId, OrderAttachmentTypeEnum.SALES_CONTRACT_FILE);
        List<String> keyFiles = orderContractList.stream().map(one -> one.getFileKey()).collect(Collectors.toList());
        List<String> urlList = new ArrayList<>();
        for (String key : keyFiles) {
            String url = fileService.getUrl(key, FileTypeEnum.ORDER_SALES_CONTRACT);
            urlList.add(url);
        }
        result.setOrderContractUrl(urlList);

        List<OrderDetailDTO> orderDetailInfo = orderDetailApi.getOrderDetailInfo(orderId);
        List<OrderDetailVO> orderDetailLists = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(orderDetailInfo)) {
            List<Long> listIds = orderDetailInfo.stream().map(one -> one.getGoodsId()).collect(Collectors.toList());
            List<Long> goodsSukIds = orderDetailInfo.stream().map(OrderDetailDTO::getGoodsSkuId).collect(Collectors.toList());

            List<OrderDetailChangeDTO> orderDetailChangeList = orderDetailChangeApi.listByOrderId(orderId);
            Map<Long, OrderDetailChangeDTO> orderDetailChangeMap = orderDetailChangeList.stream().collect(Collectors.toMap(OrderDetailChangeDTO::getDetailId, o -> o, (k1, k2) -> k1));
            //现折金额
            BigDecimal cashDiscountAmount = BigDecimal.ZERO;
            //货款总额
            BigDecimal totalAmount = BigDecimal.ZERO;
            //票折总金额
            BigDecimal ticketDiscountAmount = BigDecimal.ZERO;

            Map<Long, String> map = goodsApi.getPictureUrlMapByGoodsIds(listIds);
            List<GoodsSkuDTO> goodsSkuByIds = goodsApi.getGoodsSkuByIds(goodsSukIds);
            Map<Long, GoodsSkuDTO> skuGoodsMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(goodsSkuByIds)) {
                skuGoodsMap = goodsSkuByIds.stream().collect(Collectors.toMap(GoodsSkuDTO::getId, o -> o, (k1, k2) -> k1));
            }
            for (OrderDetailDTO one : orderDetailInfo) {
                OrderDetailVO detailOne = PojoUtils.map(one, OrderDetailVO.class);
                detailOne.setGoodsPic(pictureUrlUtils.getGoodsPicUrl(map.get(one.getGoodsId())));
                detailOne.setPackageNumber(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getPackageNumber() : 1L);
                detailOne.setGoodsRemark(skuGoodsMap.get(one.getGoodsSkuId()) != null ? skuGoodsMap.get(one.getGoodsSkuId()).getRemark() : "");
                OrderDetailChangeDTO orderDetailChangeOne = orderDetailChangeMap.get(one.getId());
                detailOne.setGoodsQuantity(orderDetailChangeOne.getGoodsQuantity()).setDeliveryQuantity(orderDetailChangeOne.getDeliveryQuantity()).setGoodsPrice(orderDetailChangeOne.getGoodsPrice()).setGoodsAmount(orderDetailChangeOne.getGoodsAmount().subtract(orderDetailChangeOne.getSellerReturnAmount())).setDiscountAmount(orderDetailChangeOne.getCashDiscountAmount().add(orderDetailChangeOne.getTicketDiscountAmount()));
                detailOne.setRealAmount(detailOne.getGoodsAmount().subtract(detailOne.getDiscountAmount()));

                ticketDiscountAmount = ticketDiscountAmount.add(orderDetailChangeOne.getTicketDiscountAmount());
                cashDiscountAmount = cashDiscountAmount.add(orderDetailChangeOne.getCashDiscountAmount());
                totalAmount = totalAmount.add(detailOne.getGoodsAmount());
                orderDetailLists.add(detailOne);
            }
            result.setOrderDetailList(orderDetailLists);
            result.setTotalAmount(totalAmount);
            result.setPaymentAmount(totalAmount.subtract(cashDiscountAmount).subtract(ticketDiscountAmount));
            result.setDiscountAmount(ticketDiscountAmount.add(cashDiscountAmount));
        }

        PaymentDaysAccountDTO paymentDays = paymentDaysAccountApi.getByCustomerEid(order.getSellerEid(), order.getBuyerEid());
        PaymentDaysInfoVO paymentDaysInfo = PojoUtils.map(paymentDays, PaymentDaysInfoVO.class);
        result.setPaymentDaysInfo(paymentDaysInfo);

        return Result.success(result);
    }


    @ApiOperation(value = "审核订单通过")
    @GetMapping("/review/pass")
    public Result<BoolObject> reviewOrderPass(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "id") Long id) {
        OrderDTO orderDTO = orderApi.getOrderInfo(id);
        if (OrderAuditStatusEnum.getByCode(orderDTO.getAuditStatus()) != OrderAuditStatusEnum.UNAUDIT) {
            throw new BusinessException(OrderErrorCode.ORDER_INFO_STATUS_CHANGE);
        }

        // 查询出所有以岭企业(订单审核优化：增加非大运河工业供应商库存校验)
        /*List<Long> subEidList = enterpriseApi.listSubEids(Constants.YILING_EID);
        List<Long> directEidList = new ArrayList<Long>() {{
            add(Constants.DAYUNHE_EID);
        }};
        List<Long> longList = subEidList.stream().filter(item -> !directEidList.contains(item)).collect(Collectors.toList());
        if (longList.contains(orderDTO.getSellerEid())) {
            List<OrderDetailChangeDTO> orderDetailChangeDTOList = orderDetailChangeApi.listByOrderId(id);
            for (OrderDetailChangeDTO detailChangeDTO : orderDetailChangeDTOList) {
                InventoryDTO inventoryDTO = inventoryApi.getBySkuId(detailChangeDTO.getGoodsSkuId());
                log.info("审核订单，开始判断库存是否充足，库存数据为:[{}],订单商品数据为:[{}]", inventoryDTO, detailChangeDTO);
                if (null != inventoryDTO && inventoryDTO.getQty() < inventoryDTO.getFrozenQty()) {
                    throw new BusinessException(OrderErrorCode.ORDER_GOODS_NOT_STOCK);
                }
            }
        }*/

        boolean result = orderApi.updateAuditStatus(id, OrderAuditStatusEnum.UNAUDIT, OrderAuditStatusEnum.AUDIT_PASS, staffInfo.getCurrentUserId(), null);

        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "审核订单驳回")
    @GetMapping("/reject")
    public Result<BoolObject> rejectOrder(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam(value = "id") Long id, @RequestParam(value = "auditRejectReason") String auditRejectReason) {

        boolean result = orderApi.updateAuditStatus(id, OrderAuditStatusEnum.UNAUDIT, OrderAuditStatusEnum.AUDIT_REJECT, staffInfo.getCurrentUserId(), auditRejectReason);
        return Result.success(new BoolObject(result));
    }


}

