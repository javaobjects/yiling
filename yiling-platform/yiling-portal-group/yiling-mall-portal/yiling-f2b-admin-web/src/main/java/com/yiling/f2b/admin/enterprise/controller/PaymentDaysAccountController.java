package com.yiling.f2b.admin.enterprise.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.enterprise.form.ApplyPaymentDaysAccountForm;
import com.yiling.f2b.admin.enterprise.form.CreatePaymentDaysAccountForm;
import com.yiling.f2b.admin.enterprise.form.PaymentDaysCompanyForm;
import com.yiling.f2b.admin.enterprise.form.QueryExpireDayOrderForm;
import com.yiling.f2b.admin.enterprise.form.QueryPaymentDaysAccountPageListForm;
import com.yiling.f2b.admin.enterprise.form.QueryQuotaOrderForm;
import com.yiling.f2b.admin.enterprise.form.QueryShortTimeQuotaAccountForm;
import com.yiling.f2b.admin.enterprise.form.UpdatePaymentDaysAccountForm;
import com.yiling.f2b.admin.enterprise.vo.MainPartEnterpriseVO;
import com.yiling.f2b.admin.enterprise.vo.PaymentDaysAccountListItemVO;
import com.yiling.f2b.admin.enterprise.vo.PaymentDaysCompanyVO;
import com.yiling.f2b.admin.enterprise.vo.PaymentDaysOrderVO;
import com.yiling.f2b.admin.enterprise.vo.PaymentRepaymentOrderVO;
import com.yiling.f2b.admin.enterprise.vo.QuotaOrderVO;
import com.yiling.f2b.admin.enterprise.vo.ShortTimeQuotaVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.user.common.UserErrorCode;
import com.yiling.user.enterprise.api.EmployeeApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.MainPartEnterpriseDTO;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.payment.api.PaymentDaysAccountApi;
import com.yiling.user.payment.dto.PaymentDaysCompanyDTO;
import com.yiling.user.payment.dto.PaymentDaysOrderDTO;
import com.yiling.user.payment.dto.QuotaOrderDTO;
import com.yiling.user.payment.dto.ShortTimeQuotaDTO;
import com.yiling.user.payment.dto.request.ApplyPaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.CreatePaymentDaysAccountRequest;
import com.yiling.user.payment.dto.request.PaymentDaysCompanyRequest;
import com.yiling.user.payment.dto.request.QueryExpireDayOrderRequest;
import com.yiling.user.payment.dto.request.QueryPaymentDaysAccountPageListRequest;
import com.yiling.user.payment.dto.request.QueryQuotaOrderRequest;
import com.yiling.user.payment.dto.request.QueryShortTimeQuotaAccountRequest;
import com.yiling.user.payment.dto.request.UpdatePaymentDaysAccountRequest;
import com.yiling.user.payment.enums.PaymentAccountTypeEnum;
import com.yiling.user.system.api.DataPermissionsApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.enums.PermissionAppEnum;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 非以岭账期 Controller
 *
 * @author tingwei.chen
 * @date 2021/7/2
 */
@RestController
@RequestMapping("/enterprise/paymentDays/")
@Api(tags = "账期模块接口")
@Slf4j
public class PaymentDaysAccountController extends BaseController {

    @DubboReference
    PaymentDaysAccountApi paymentDaysAccountApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    EmployeeApi employeeApi;
    @DubboReference
    DataPermissionsApi dataPermissionsApi;

    @ApiOperation(value = "账期额度管理/采购商账期列表")
    @PostMapping("/yilLingPageList")
    public Result<Page<PaymentDaysAccountListItemVO>> ylPageList(@CurrentUser CurrentStaffInfo staffInfo,  @RequestBody QueryPaymentDaysAccountPageListForm form){
        QueryPaymentDaysAccountPageListRequest request = PojoUtils.map(form, QueryPaymentDaysAccountPageListRequest.class);

        // 如果为账期额度管理页面：账期额度管理-商务，展示的数据是采购商的商务联系人为当前登录用户的账期信息
        if (staffInfo.getEnterpriseChannel() != EnterpriseChannelEnum.INDUSTRY_DIRECT &&
                request.getType() == 1 ) {
            List<Long> contactUserList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            List<Long> eidList = getEidList(staffInfo);
            request.setEidList(eidList);
            request.setContactUserList(contactUserList);
        } else {
            if(staffInfo.getYilingFlag()){
                List<Long> eidList = getEidList(staffInfo);
                request.setEidList(eidList);
            }else{
                request.setCurrentEid(staffInfo.getCurrentEid());
            }
        }

        Page<PaymentDaysAccountListItemVO> pageVO = PojoUtils.map(paymentDaysAccountApi.pageList(request), PaymentDaysAccountListItemVO.class);
        List<PaymentDaysAccountListItemVO> pageVoRecords = pageVO.getRecords();
        if(CollectionUtils.isEmpty(pageVoRecords)){
            return Result.success(pageVO);
        }

        // 优化采购商和供应商的名称显示
        List<Long> idList = ListUtil.toList();
        pageVoRecords.forEach(paymentDaysAccountListItemVO -> {
            idList.add(paymentDaysAccountListItemVO.getEid());
            idList.add(Long.valueOf(paymentDaysAccountListItemVO.getCustomerEid()));
        });
        Map<Long, String> nameMap = getEnterpriseNameMap(idList);

        for (PaymentDaysAccountListItemVO paymentDaysAccountListItemVO : pageVoRecords) {
            //待还款额度
            paymentDaysAccountListItemVO.setNeedRepaymentAmount(paymentDaysAccountListItemVO.getUsedAmount().subtract(paymentDaysAccountListItemVO.getRepaymentAmount()));
            //已使用额度
            paymentDaysAccountListItemVO.setUsedAmount(paymentDaysAccountListItemVO.getUsedAmount().add(paymentDaysAccountListItemVO.getHistoryUseAmount()));
            //已还款额度
            paymentDaysAccountListItemVO.setRepaymentAmount(paymentDaysAccountListItemVO.getRepaymentAmount().add(paymentDaysAccountListItemVO.getHistoryRepaymentAmount()));
            // 设置采购商和供应商名称
            paymentDaysAccountListItemVO.setEname(nameMap.getOrDefault(paymentDaysAccountListItemVO.getEid(), paymentDaysAccountListItemVO.getEname()));
            Long customerEid = Long.valueOf(paymentDaysAccountListItemVO.getCustomerEid());
            paymentDaysAccountListItemVO.setCustomerName(nameMap.getOrDefault(customerEid, paymentDaysAccountListItemVO.getCustomerName()));

        }
        return Result.success(pageVO);
    }

    /**
     * 获取企业名称map
     *
     * @param idList 企业id集合
     * @return
     */
    private Map<Long, String> getEnterpriseNameMap(List<Long> idList) {
        Map<Long, String> nameMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(idList)) {
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(idList);
            nameMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName, (k1, k2) -> k2));
        }
        return nameMap;
    }

    @ApiOperation(value = "创建账期账户")
    @PostMapping("/yilLingCreate")
    @Log(title = "创建账期账户", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> ylCreate(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid CreatePaymentDaysAccountForm form){
        CreatePaymentDaysAccountRequest request = PojoUtils.map(form,CreatePaymentDaysAccountRequest.class);

        if(Objects.isNull(request.getEid())){
            request.setEid(staffInfo.getCurrentEid());
            request.setType(PaymentAccountTypeEnum.NOT_YL_PAYMENT_ACCOUNT.getCode());
        }else{
            EnterpriseDTO enterpriseDto = Optional.ofNullable(enterpriseApi.getById(request.getEid()))
                    .orElseThrow(() -> new BusinessException(UserErrorCode.ENTERPRISE_NOT_EXISTS));

            if(EnterpriseChannelEnum.INDUSTRY_DIRECT == EnterpriseChannelEnum.getByCode(enterpriseDto.getChannelId())){
                request.setType(PaymentAccountTypeEnum.INDUSTRY_DIRECT_PAYMENT_ACCOUNT.getCode());
            }else{
                request.setType(PaymentAccountTypeEnum.YL_PAYMENT_ACCOUNT.getCode());
            }
        }

        boolean result = paymentDaysAccountApi.create(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改账期账户")
    @PostMapping("/update")
    @Log(title = "修改账期账户", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> update(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdatePaymentDaysAccountForm form){
        UpdatePaymentDaysAccountRequest request = PojoUtils.map(form,UpdatePaymentDaysAccountRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        boolean result = paymentDaysAccountApi.update(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "以岭采购商申请临时额度")
    @PostMapping("/applyQuota")
    @Log(title = "以岭采购商申请临时额度", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> applyQuota(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid ApplyPaymentDaysAccountForm form){
        ApplyPaymentDaysAccountRequest request = PojoUtils.map(form,ApplyPaymentDaysAccountRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        Boolean res = paymentDaysAccountApi.applyQuota(request);
        return Result.success(new BoolObject(res)) ;
    }

    @ApiOperation(value = "临时额度审核")
    @PostMapping("/checkQuota")
    @Log(title = "临时额度审核", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> checkQuota(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid ApplyPaymentDaysAccountForm form){
        ApplyPaymentDaysAccountRequest request = PojoUtils.map(form,ApplyPaymentDaysAccountRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        Boolean res = paymentDaysAccountApi.checkQuota(request);
        return Result.success(new BoolObject(res)) ;
    }

    @ApiOperation(value = "临时额度列表")
    @PostMapping("/shortTimeQuotaPage")
    public Result<Page<ShortTimeQuotaVO>> shortTimeQuotaPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryShortTimeQuotaAccountForm form){
        QueryShortTimeQuotaAccountRequest request = PojoUtils.map(form,QueryShortTimeQuotaAccountRequest.class);

        List<Long> eidList = new ArrayList<>();
        if(staffInfo.getYilingFlag()){
            eidList = getEidList(staffInfo);
        }else{
            eidList.add(staffInfo.getCurrentEid());
        }
        request.setEidList(eidList);

        Page<ShortTimeQuotaDTO> page = paymentDaysAccountApi.shortTimeQuotaPage(request);

        // 获取最新采购商和供应商名称展示
        List<Long> idList = ListUtil.toList();
        page.getRecords().forEach(shortTimeQuotaDTO -> {
            idList.add(Long.valueOf(shortTimeQuotaDTO.getEid()));
            idList.add(Long.valueOf(shortTimeQuotaDTO.getCustomerEid()));
        });
        Map<Long, String> nameMap = getEnterpriseNameMap(idList);
        page.getRecords().forEach(shortTimeQuotaDTO -> {
            shortTimeQuotaDTO.setEname(nameMap.getOrDefault(Long.valueOf(shortTimeQuotaDTO.getEid()), shortTimeQuotaDTO.getEname()));
            shortTimeQuotaDTO.setCustomerName(nameMap.getOrDefault(Long.valueOf(shortTimeQuotaDTO.getCustomerEid()), shortTimeQuotaDTO.getCustomerName()));
        });

        Page<ShortTimeQuotaVO> pageVO = PojoUtils.map(page,ShortTimeQuotaVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "账期绑定订单列表")
    @PostMapping("/quotaOrderPage")
    public Result<Page<QuotaOrderVO>> quotaOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryQuotaOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);
        Page<QuotaOrderDTO> page = paymentDaysAccountApi.quotaOrderPage(request);

        List<Long> idList = page.getRecords().stream().map(QuotaOrderDTO::getCustomerEid).collect(Collectors.toList());
        Map<Long, String> nameMap = this.getEnterpriseNameMap(idList);

        page.getRecords().forEach(e-> {
            //订单金额=订单总金额-(订单上的现折总金额+订单上的票折总金额+驳回退货单的退款金额) 即 订单金额 = 使用金额 - 驳回退货单的退款金额
            e.setOrderAmount(e.getUsedAmount().subtract(e.getReturnAmount()));
            //待还款金额 = 订单金额 – 已还款金额
            e.setUnRepaymentAmount(e.getOrderAmount().subtract(e.getRepaymentAmount()));
            // 设置最新的采购商名称
            e.setCustomerName(nameMap.getOrDefault(e.getCustomerEid(), e.getCustomerName()));
        });

        Page<QuotaOrderVO> pageVO = PojoUtils.map(page,QuotaOrderVO.class);
        return Result.success(pageVO);
    }

    @ApiOperation(value = "已使用/已还款订单金额总计")
    @PostMapping("/getRepaymentOrderAmount")
    public Result<PaymentRepaymentOrderVO> getRepaymentOrderAmount(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryQuotaOrderForm form){
        QueryQuotaOrderRequest request = PojoUtils.map(form,QueryQuotaOrderRequest.class);

        PaymentRepaymentOrderVO repaymentOrderVO = PojoUtils.map(paymentDaysAccountApi.getRepaymentOrderAmount(request), PaymentRepaymentOrderVO.class);
        return Result.success(repaymentOrderVO);
    }

    @ApiOperation(value = "账期到期提醒列表")
    @PostMapping("/expireDayOrderPage")
    public Result<Page<PaymentDaysOrderVO>> expireDayOrderPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryExpireDayOrderForm form){
        QueryExpireDayOrderRequest request = PojoUtils.map(form,QueryExpireDayOrderRequest.class);
        //以岭：如果是普通用户查看，展示订单商务联系人为自己的订单；如果是企业管理员查看，查看该企业所有的订单
        if(staffInfo.getYilingAdminFlag()){
            List<Long> eidList = getEidList(staffInfo);
            request.setEidList(eidList);
        }else{
            //工业直属的场景
            boolean adminFlag = employeeApi.isAdmin(staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
            if(adminFlag){
                List<Long> eidList = ListUtil.toList(staffInfo.getCurrentEid());
                request.setEidList(eidList);
            }else{
                List<Long> contactUserList = dataPermissionsApi.listAuthorizedUserIds(PermissionAppEnum.MALL_ADMIN_POP, staffInfo.getCurrentEid(), staffInfo.getCurrentUserId());
                request.setContactUserList(contactUserList);
            }
        }

        Page<PaymentDaysOrderDTO> page = paymentDaysAccountApi.expireDayOrderPage(request);
        Page<PaymentDaysOrderVO> pageVO = PojoUtils.map(page,PaymentDaysOrderVO.class);

        // 获取最新采购商和供应商名称展示
        if (CollUtil.isNotEmpty(pageVO.getRecords())) {
            List<Long> idList = ListUtil.toList();
            pageVO.getRecords().forEach(paymentDaysOrderDTO -> {
                idList.add(paymentDaysOrderDTO.getEid());
                idList.add(paymentDaysOrderDTO.getCustomerEid());
            });
            Map<Long, String> nameMap = getEnterpriseNameMap(idList);

            pageVO.getRecords().forEach(e-> {
                // 订单金额=订单总金额-(订单上的现折总金额+订单上的票折总金额+驳回退货单的退款金额)
                e.setUsedAmount(e.getUsedAmount().subtract(e.getReturnAmount()));
                // 待还款金额 = 订单金额 – 已还款金额
                e.setNeedRepaymentAmount(e.getUsedAmount().subtract(e.getRepaymentAmount()));
                // 设置最新的采购商和供应商名称
                e.setEname(nameMap.getOrDefault(e.getEid(), e.getEname()));
                e.setCustomerName(nameMap.getOrDefault(e.getCustomerEid(), e.getCustomerName()));
            });
        }

        return Result.success(pageVO);
    }

    @ApiOperation(value = "查询集团总额度")
    @GetMapping("/getCompanyDetail")
    public Result<PaymentDaysCompanyVO> getCompanyDetail(){
        PaymentDaysCompanyDTO paymentDaysCompanyDTO = paymentDaysAccountApi.get();
        PaymentDaysCompanyVO paymentDaysCompanyVO = PojoUtils.map(paymentDaysCompanyDTO,PaymentDaysCompanyVO.class);
        paymentDaysCompanyVO.setAvailableAmount(paymentDaysCompanyVO.getTotalAmount().subtract(paymentDaysCompanyVO.getUsedAmount()).add(paymentDaysCompanyVO.getRepaymentAmount()));

        return Result.success(paymentDaysCompanyVO);
    }

    @ApiOperation(value = "保存集团总额度")
    @PostMapping("/saveOrUpdateCompanyDetail")
    @Log(title = "保存集团总额度", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> saveOrUpdateCompanyDetail(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody @Valid PaymentDaysCompanyForm form){
        PaymentDaysCompanyRequest request = PojoUtils.map(form,PaymentDaysCompanyRequest.class);

        Boolean result = paymentDaysAccountApi.saveOrUpdateCompanyDetail(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "获取授信主体列表")
    @GetMapping("/mainPartList")
    public Result<MainPartEnterpriseVO> getMainPartList(@CurrentUser CurrentStaffInfo staffInfo) {
        MainPartEnterpriseDTO mainPartEnterpriseDto = paymentDaysAccountApi.listMainPart(staffInfo.getCurrentEid(),staffInfo.getCurrentUserId());
        return Result.success(PojoUtils.map(mainPartEnterpriseDto, MainPartEnterpriseVO.class));
    }

    private List<Long> getEidList(@CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
        if (CollectionUtils.isEmpty(eidList)) {
            eidList = Collections.singletonList(staffInfo.getCurrentEid());
        } else {
            eidList.add(staffInfo.getCurrentEid());
        }
        return eidList;
    }

}
