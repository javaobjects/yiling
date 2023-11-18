package com.yiling.sjms.gb.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.location.api.LocationApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseSimpleDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmEnterprisePageRequest;
import com.yiling.dataflow.crm.dto.request.SaveCrmEnterpriseRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.redis.RedisKey;
import com.yiling.framework.common.redis.service.RedisService;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.bo.OssCallbackResult;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.gb.api.GbAttachmentApi;
import com.yiling.sjms.gb.api.GbCustomerApi;
import com.yiling.sjms.gb.api.GbFormApi;
import com.yiling.sjms.gb.api.GbFormSubmitProcessApi;
import com.yiling.sjms.gb.api.GbGoodsApi;
import com.yiling.sjms.gb.dto.AttachmentDTO;
import com.yiling.sjms.gb.dto.CustomerDTO;
import com.yiling.sjms.gb.dto.GbFormInfoListDTO;
import com.yiling.sjms.gb.dto.GoodsDTO;
import com.yiling.sjms.gb.dto.request.QueryGBFormListPageRequest;
import com.yiling.sjms.gb.dto.request.QueryGBGoodsInfoPageRequest;
import com.yiling.sjms.gb.dto.request.SaveCustomerRequest;
import com.yiling.sjms.gb.dto.request.SaveGBCompanyRelationRequest;
import com.yiling.sjms.gb.dto.request.SaveGBFileRequest;
import com.yiling.sjms.gb.dto.request.SaveGBGoodsInfoRequest;
import com.yiling.sjms.gb.dto.request.SaveGBInfoRequest;
import com.yiling.sjms.gb.enums.GBErrorCode;
import com.yiling.sjms.gb.enums.GbFormBizTypeEnum;
import com.yiling.sjms.gb.form.QueryGBCrmEnterprisePageForm;
import com.yiling.sjms.gb.form.QueryGBFormListPageForm;
import com.yiling.sjms.gb.form.QueryGBGoodsInfoPageForm;
import com.yiling.sjms.gb.form.SaveCrmEnterpriseForm;
import com.yiling.sjms.gb.form.SaveCustomerForm;
import com.yiling.sjms.gb.form.SaveGBFileForm;
import com.yiling.sjms.gb.form.SaveGBInfoForm;
import com.yiling.sjms.gb.vo.GbCrmEnterpriseVO;
import com.yiling.sjms.gb.vo.GbCustomerVO;
import com.yiling.sjms.gb.vo.GbEmployeeDTOVO;
import com.yiling.sjms.gb.vo.GbFormSubmitListVO;
import com.yiling.sjms.gb.vo.GbGoodsVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 团购提报列表
 * @author: wei.wang
 * @date: 2022/11/28
 */
@Slf4j
@RestController
@RequestMapping("/gb/submit")
@Api(tags = "团购提报")
public class SubmitController extends BaseController {

    @DubboReference
    FormApi formApi;
    @DubboReference
    GbCustomerApi gbCustomerApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    GbFormSubmitProcessApi gbFormSubmitProcessApi;
    @DubboReference
    GbGoodsApi gbGoodsApi;
    @DubboReference(timeout = 80*1000)
    CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    GbAttachmentApi gbAttachmentApi;
    @DubboReference
    LocationApi locationApi;
    @DubboReference
    GbFormApi gbFormApi;


    @Autowired
    RedisService redisService;
    @Autowired
    FileService fileService;

    @ApiOperation(value = "团购提报列表")
    @PostMapping("/list")
    public Result<Page<GbFormSubmitListVO>> getGbSubmitFormListPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBFormListPageForm form){
        QueryGBFormListPageRequest request = PojoUtils.map(form, QueryGBFormListPageRequest.class);
        request.setBizType(GbFormBizTypeEnum.SUBMIT.getCode());


        request.setCreateUser(userInfo.getCurrentUserId());

        if(StringUtils.isNotBlank(form.getStartMonthTime())){
            request.setStartMonth(DateUtil.beginOfMonth(DateUtil.parse(form.getStartMonthTime(), "yyyy-MM")));
        }

        if(StringUtils.isNotBlank(form.getEndMonthTime())){
            request.setEndMonth(DateUtil.endOfMonth(DateUtil.parse(form.getEndMonthTime(), "yyyy-MM")));
        }

        if(request.getStartApproveTime()!= null){
            request.setStartApproveTime(DateUtil.beginOfDay(request.getStartApproveTime()));
        }
        if(request.getEndApproveTime()!= null){
            request.setEndApproveTime(DateUtil.endOfDay(request.getEndApproveTime()));
        }

        if(request.getStartSubmitTime()!= null){
            request.setStartSubmitTime(DateUtil.beginOfDay(request.getStartSubmitTime()));
        }
        if(request.getEndSubmitTime()!= null){
            request.setEndSubmitTime(DateUtil.endOfDay(request.getEndSubmitTime()));
        }

        Page<GbFormInfoListDTO> gbFormListPage = gbFormApi.getGBFormListPage(request);
        return Result.success(PojoUtils.map(gbFormListPage, GbFormSubmitListVO.class));
    }

    @ApiOperation(value = "添加团购单位")
    @PostMapping("/add/customer")
    public Result<GbCustomerVO> addCustomer(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveCustomerForm form){
        String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
        SaveCustomerRequest request = PojoUtils.map(form, SaveCustomerRequest.class);
        if(namesByCodes != null && namesByCodes.length == 3 ){
            request.setProvinceName(namesByCodes[0]);
            request.setCityName(namesByCodes[1]);
            request.setRegionName(namesByCodes[2]);
        }
        String regEx = "^[0-9A-HJ-NPQRTUWXY]{18}";
        CustomerDTO one = gbCustomerApi.getCustomerByName(form.getName());
        if(one != null){
            throw new BusinessException(GBErrorCode.GB_FORM_CUSTOMER_NAME_EXISTS);
        }
        if(!request.getCreditCode().equals("0")){
            Boolean matches = request.getCreditCode().matches(regEx);
            if(!matches){
                throw new BusinessException(GBErrorCode.GB_FORM_CREDIT_CODE_ERROR);
            }
            CustomerDTO customer = gbCustomerApi.getCustomerByCreditCode(request.getCreditCode());
            if(customer != null){
                throw new BusinessException(GBErrorCode.GB_FORM_CREDIT_CODE_EXISTS);
            }
        }
        Long id = gbCustomerApi.addCustomer(request);
        GbCustomerVO result = new GbCustomerVO();
        result.setId(id);
        result.setName(request.getName());
        return Result.success(result);
    }

    @ApiOperation(value = "获取团购单位")
    @PostMapping("/get/customer")
    public Result<Page<GbCustomerVO>> getCustomer(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBGoodsInfoPageForm form){
        QueryGBGoodsInfoPageRequest request = PojoUtils.map(form, QueryGBGoodsInfoPageRequest.class);
        Page<CustomerDTO> customer = gbCustomerApi.getCustomer(request);
        return Result.success(PojoUtils.map(customer,GbCustomerVO.class));
    }

    @ApiOperation(value = "获取员工信息")
    @GetMapping("/employee")
    public Result<GbEmployeeDTOVO> getEmployeeInfo(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam(value = "empId",required = true)String empId){
        EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(empId);
        GbEmployeeDTOVO result = PojoUtils.map(employee, GbEmployeeDTOVO.class);
        if(employee != null){
            boolean provinceManager = esbEmployeeApi.isProvinceManager(empId);
            result.setMarketFlag(provinceManager);
        }


        return Result.success(result);
    }


    @ApiOperation(value = "删除团购信息")
    @GetMapping("/delete")
    public Result deleteSubmitForm(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam(value = "id",required = true)Long id){
        gbFormApi.delete(id, userInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "保存草稿/提交流程")
    @PostMapping("/save")
    public Result saveSubmitForm(@CurrentUser CurrentSjmsUserInfo userInfo,  @RequestBody SaveGBInfoForm form){
        Boolean flag = (Boolean)redisService.get(RedisKey.generate("sjms", "gb","submit", "button"));
        if(flag == null){
            flag = false;
        }
        if(!flag){
            throw new BusinessException(GBErrorCode.GB_FORM_INFO_SUBMIT_START);
        }
        SaveGBInfoRequest request = PojoUtils.map(form, SaveGBInfoRequest.class);
        request.setBizType(GbFormBizTypeEnum.SUBMIT.getCode());
        if(request != null && CollectionUtil.isNotEmpty(request.getCompanyList())){
            for(SaveGBCompanyRelationRequest company :  request.getCompanyList()){
                if(CollectionUtil.isNotEmpty(company.getGbGoodsInfoList())){
                    for(SaveGBGoodsInfoRequest one : company.getGbGoodsInfoList() ){
                        if(one.getSmallPackage() != null && one.getSmallPackage() !=0){
                            one.setQuantity(BigDecimal.valueOf(one.getQuantityBox()).divide(BigDecimal.valueOf(one.getSmallPackage()),4, RoundingMode.HALF_UP));
                        }else{
                            //one.setQuantity(BigDecimal.ZERO);
                            throw new BusinessException(GBErrorCode.GB_FORM_PARAMETER_ERROR);
                        }
                        one.setFinalAmount(BigDecimal.valueOf(one.getQuantityBox()).multiply(one.getFinalPrice()));
                        one.setAmount(one.getPrice().multiply(BigDecimal.valueOf(one.getQuantityBox())));
                    }
                }
            }
        }

        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setBizArea(employee.getYxArea());
        request.setBizProvince(employee.getYxProvince());
        request.setEmpId(employee.getEmpId());
        request.setEmpName(employee.getEmpName());
        request.setDeptId(employee.getDeptId());
        request.setDeptName(employee.getYxDept());
        if(form.getType() == 1){
            gbFormApi.saveGBInfo(request);
        }else{
            gbFormSubmitProcessApi.submitFormProcess(request);
        }
        return Result.success();
    }

    @ApiOperation(value = "获取团购商品信息")
    @PostMapping("/get/goods")
    public Result<Page<GbGoodsVO>> getGBGoodsPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryGBGoodsInfoPageForm form){
        QueryGBGoodsInfoPageRequest request = PojoUtils.map(form, QueryGBGoodsInfoPageRequest.class);
        Page<GoodsDTO> gbGoodsPage = gbGoodsApi.getGBGoodsPage(request);
        return Result.success(PojoUtils.map(gbGoodsPage,GbGoodsVO.class));
    }

    @ApiOperation(value = "获取出库终端/出库商业")
    @PostMapping("/get/crm/enterprise")
    public Result<Page<GbCrmEnterpriseVO>> getGBCrmEnterprisePage(@CurrentUser CurrentSjmsUserInfo userInfo,
                                 @RequestBody @Valid QueryGBCrmEnterprisePageForm form){
        QueryCrmEnterprisePageRequest request = PojoUtils.map(form, QueryCrmEnterprisePageRequest.class);
        /*List<Integer> list = new ArrayList<>();
        if(form.getType() == 0){
            list.add(CrmSupplyChainRoleEnum.HOSPITAL.getCode());
            list.add(CrmSupplyChainRoleEnum.PHARMACY.getCode());

        }else if(form.getType() == 1){
            list.add(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
        }
        request.setRoleIds(list)*/;
        Page<CrmEnterpriseSimpleDTO> crmEnterpriseSimplePage = crmEnterpriseApi.getCrmEnterpriseSimplePage(request);
        return Result.success(PojoUtils.map(crmEnterpriseSimplePage, GbCrmEnterpriseVO.class));
    }

    @ApiOperation(value = "补充证据")
    @PostMapping("/add/support")
    public Result addSupport(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveGBFileForm form  ){
        SaveGBFileRequest request = PojoUtils.map(form, SaveGBFileRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        gbAttachmentApi.addSupport(request);
        return Result.success();
    }

    @ApiOperation(value = "oss上传成功回调")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "表单ID", required = true)
    })
    @PostMapping("/callback")
    public Result<OssCallbackResult> callback(HttpServletRequest request) {
        log.debug("团购ossCallbackResult -> {}，formId -{}", JSONUtil.toJsonStr(request.getParameterMap()));
        OssCallbackResult ossCallbackResult = fileService.callback(request);
        AttachmentDTO one = gbAttachmentApi.getAttachmentByMD5(ossCallbackResult.getMd5(),1);
        ossCallbackResult.setRepeatFlag(false);
        if(one != null){
            ossCallbackResult.setRepeatFlag(true);
        }
        return Result.success(ossCallbackResult);
    }

    @ApiOperation(value = "保存出库终端/出库商业")
    @PostMapping("/save/crm/enterprise")
    public Result<GbCustomerVO> saveCrmEnterpriseSimple(@CurrentUser CurrentSjmsUserInfo userInfo,
                                          @RequestBody @Valid SaveCrmEnterpriseForm form){
        SaveCrmEnterpriseRequest request = PojoUtils.map(form, SaveCrmEnterpriseRequest.class);
        String[] namesByCodes = locationApi.getNamesByCodes(form.getProvinceCode(), form.getCityCode(), form.getRegionCode());
        request.setProvinceName(namesByCodes[0]);
        request.setCityName(namesByCodes[1]);
        request.setRegionName(namesByCodes[2]);
        request.setOpUserId(userInfo.getCurrentUserId());
        /*if(form.getType() == 1 ){
            if(form.getRoleType() == 2){
                request.setSupplyChainRole(CrmSupplyChainRoleEnum.HOSPITAL.getCode());
            }else if(form.getRoleType() == 3){
                request.setSupplyChainRole(CrmSupplyChainRoleEnum.PHARMACY.getCode());
            }else{
                throw new BusinessException(GBErrorCode.GB_FORM_PARAMETER_ERROR);
            }
        }else if(form.getType() == 2) {
            request.setSupplyChainRole(CrmSupplyChainRoleEnum.DISTRIBUTOR.getCode());
        }else{
            throw new BusinessException(GBErrorCode.GB_FORM_PARAMETER_ERROR);
        }*/

        String regEx = "^[0-9A-HJ-NPQRTUWXY]{18}";

        if(!request.getLicenseNumber().equals("0")){
            Boolean matches = request.getLicenseNumber().matches(regEx);
            if(!matches){
                throw new BusinessException(GBErrorCode.GB_FORM_CREDIT_CODE_ERROR);
            }
        }
        //默认失效状态
        request.setBusinessCode(2);
        request.setSourceFlag(2);
        Long id = crmEnterpriseApi.saveCrmEnterpriseSimple(request);
        GbCustomerVO result = new GbCustomerVO();
        result.setProvinceCode(request.getProvinceCode());
        result.setCityCode(request.getCityCode());
        result.setRegionCode(request.getRegionCode());
        result.setProvinceName(request.getProvinceName());
        result.setCityName(request.getCityName());
        result.setRegionName(request.getRegionName());
        result.setName(form.getName());
        result.setId(id);

        return Result.success(result);
    }
}
