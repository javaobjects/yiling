package com.yiling.sjms.flee.controller;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.basic.no.api.NoApi;
import com.yiling.dataflow.crm.api.CrmEnterpriseRelationShipApi;
import com.yiling.dataflow.report.api.FlowWashReportApi;
import com.yiling.dataflow.report.dto.FlowWashSaleReportDTO;
import com.yiling.dataflow.report.dto.request.FlowWashSaleReportPageRequest;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.oss.service.FileService;
import com.yiling.sjms.flee.api.SaleAppealSelectFlowFormApi;
import com.yiling.sjms.flee.api.SaleaAppealFormApi;

import com.yiling.sjms.flee.bo.SalesAppealFormBO;
import com.yiling.sjms.flee.dto.SaleAppealSelectFlowFormDTO;
import com.yiling.sjms.flee.dto.SalesAppealExtFormDTO;
import com.yiling.sjms.flee.dto.SalesAppealFormDTO;
import com.yiling.sjms.flee.dto.request.QuerySalesAppealPageRequest;
import com.yiling.sjms.flee.dto.request.RemoveFleeingGoodsFormRequest;
import com.yiling.sjms.flee.dto.request.RemoveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSalesAppealSubmitFormRequest;
import com.yiling.sjms.flee.dto.request.SaveSelectAppealFlowFormRequest;
import com.yiling.sjms.flee.dto.request.SubmitFleeingGoodsFormRequest;
import com.yiling.sjms.flee.form.QuerySalesAppealForm;
import com.yiling.sjms.flee.form.QuerySalesAppealPageForm;
import com.yiling.sjms.flee.form.RemoveSaleAppealUploadDataForm;
import com.yiling.sjms.flee.form.SaveOperateGroup;
import com.yiling.sjms.flee.form.SaveSalesAppealForm;
import com.yiling.sjms.flee.form.SelectFlowDataForm;
import com.yiling.sjms.flee.form.SubmitSalesAppealForm;
import com.yiling.sjms.flee.vo.AppendixDetailVO;
import com.yiling.sjms.flee.vo.SaleAppealSelectFlowFormVO;
import com.yiling.sjms.flee.vo.SalesAppealFormVO;
import com.yiling.sjms.flee.vo.SalesAppealVO;
import com.yiling.sjms.flee.vo.SeasAppealFormVO;
import com.yiling.sjms.form.api.FormApi;
import com.yiling.sjms.form.dto.FormDTO;
import com.yiling.sjms.form.enums.FormTypeEnum;
import com.yiling.sjms.gb.form.QueryFormWashSaleReportPageForm;
import com.yiling.sjms.org.api.BusinessDepartmentApi;
import com.yiling.dataflow.flowcollect.enums.TransferTypeEnum;
import com.yiling.sjms.util.DateUtilsCalculation;
import com.yiling.sjms.wash.vo.FlowWashSaleReportVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.esb.dto.EsbOrganizationDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import static com.yiling.framework.oss.enums.FileTypeEnum.SALES_APPEAL_UPLOAD_FILE;

/**
 * <p>
 * 销量申诉表单 前端控制器
 * </p>
 *
 * @author shixing.sun
 * @date 2023-03-14
 */
@Api(tags = "销量申诉表单")
@RestController
@RequestMapping("/sales/appeal")
public class SalesAppealFormController extends BaseController {

    @DubboReference
    SaleaAppealFormApi saleaAppealFormApi;

    @DubboReference
    FormApi formApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    private EsbEmployeeApi esbEmployeeApi;

    @DubboReference
    private NoApi noApi;
    @DubboReference
    private CrmEnterpriseRelationShipApi crmEnterpriseRelationShipApi;

    @DubboReference
    private BusinessDepartmentApi businessDepartmentApi;

    @Autowired
    FileService fileService;

    @DubboReference
    private FlowWashReportApi flowWashReportApi;

    @DubboReference
    SaleAppealSelectFlowFormApi saleAppealSelectFlowFormApi;

    @ApiOperation(value = "根据formId查我的代办列表-销量申诉")
    @PostMapping("/queryToDoList")
    public Result<List<SalesAppealVO>> queryToDoList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealPageForm form) {
        QuerySalesAppealPageRequest request = new QuerySalesAppealPageRequest();
        PojoUtils.map(form, request);
        List<SalesAppealFormBO> boPage = saleaAppealFormApi.queryToDoList(request);
        return Result.success(PojoUtils.map(boPage, SalesAppealVO.class));
    }


    @ApiOperation(value = "根据formId查看表头部分和附件-销量申诉")
    @PostMapping("/querySubmitInfo")
    public Result<SeasAppealFormVO> querySubmitInfo(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealPageForm form) {
        if(Objects.isNull(form.getFormId())){
            SeasAppealFormVO seasAppealFormVO = new SeasAppealFormVO();
            setEsbEmpProvince(seasAppealFormVO,userInfo);
            return Result.success(seasAppealFormVO);
        }
        FormDTO formDTO = formApi.getById(form.getFormId());
        SalesAppealExtFormDTO appendix = saleaAppealFormApi.queryAppendix(form.getFormId());
        SeasAppealFormVO result = PojoUtils.map(formDTO, SeasAppealFormVO.class);
        if(ObjectUtil.isNotEmpty(appendix)){
            result.setAppealAmount(appendix.getAppealAmount());
            result.setAppealDescribe(appendix.getAppealDescribe());
            if(StringUtils.isNotEmpty(appendix.getAppendix())){
                List<AppendixDetailVO> appendixDetails = JSON.parseArray(appendix.getAppendix(),AppendixDetailVO.class);
                result.setAppendixList(appendixDetails);
            }
            result.setAppealType(appendix.getAppealType());
            result.setMonthAppealType(appendix.getMonthAppealType());
        }
        return Result.success(result);
    }



    @ApiOperation("分页查询")
    @PostMapping("/pageList")
    public Result<List<SalesAppealFormVO>> pageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealForm form) {
        if (Objects.isNull(form.getFormId())) {
            return Result.success(new ArrayList<>());
        }

        List<SalesAppealFormDTO> dtoPage = saleaAppealFormApi.pageList(form.getFormId(),1);
        List<SalesAppealFormVO> voPage = PojoUtils.map(dtoPage, SalesAppealFormVO.class);
        for (SalesAppealFormVO item : voPage) {
            String uploader = item.getUploader();
            if (StringUtils.isNotEmpty(uploader)) {
                EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(uploader);
                item.setUploaderName(null != esbEmployeeDTO ? esbEmployeeDTO.getEmpName() : "");
            }
            String url = fileService.getUrl(item.getSourceUrl(), SALES_APPEAL_UPLOAD_FILE);
            if(StringUtils.isNotEmpty(url)){
                item.setSourceUrl(url);
                item.setDownLoadName(item.getExcelName());
            }
        }
        return Result.success(voPage);
    }

    @ApiOperation("销量申诉上传新增")
    @PostMapping("/saveUpload")
    public Result<Long> saveUpload(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveSalesAppealForm form) {
        SaveSalesAppealFormRequest request = PojoUtils.map(form, SaveSalesAppealFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setEmpId(esbEmployeeDTO.getEmpId());
        Long formId = saleaAppealFormApi.saveUpload(request);
        return Result.success(formId);
    }

    @ApiOperation("删除")
    @GetMapping("/remove")
    public Result<Boolean> remove(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveFleeingGoodsFormRequest request = new RemoveFleeingGoodsFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = saleaAppealFormApi.removeById(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }


    @ApiOperation("提交审核")
    @PostMapping("/submit")
    public Result<Boolean> submit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SubmitSalesAppealForm form) {
        //如果formId不存在，先创建
        //保存草稿
        SaveSalesAppealSubmitFormRequest request = PojoUtils.map(form, SaveSalesAppealSubmitFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        Long formId = saleaAppealFormApi.saveDraft(request);
        // 提交审核
        form.setFormId(formId);
        SubmitFleeingGoodsFormRequest submitRequest = PojoUtils.map(form, SubmitFleeingGoodsFormRequest.class);
        System.out.println(submitRequest.getFormId()+"formId");
        submitRequest.setOpUserId(userInfo.getCurrentUserId());
        submitRequest.setFormTypeEnum(FormTypeEnum.SALES_APPEAL);
        submitRequest.setEmpId(userInfo.getCurrentUserCode());
        submitRequest.setEmpName(esbEmployeeDTO.getEmpName());
        saleaAppealFormApi.submit(submitRequest);
        return Result.success(true);
    }

    public void setEsbEmpProvince(SeasAppealFormVO formVO, CurrentSjmsUserInfo userInfo){
        //如果没有待提交
        EsbEmployeeDTO employee = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        //PojoUtils.map(employee,formVO);
        // 员工信息
        formVO.setBizArea(employee.getYxArea());
        formVO.setBizProvince(employee.getYxProvince());
        formVO.setEmpName(employee.getEmpName());
        formVO.setEmpId(employee.getEmpId());
        formVO.setDeptId(employee.getDeptId());
        formVO.setDeptName(employee.getDeptName());
        formVO.setBizDept(employee.getYxDept());
        //获取申请人对应的事业部信息
        EsbOrganizationDTO esbOrganizationDTO = businessDepartmentApi.getByOrgId(employee.getDeptId());
        if (esbOrganizationDTO != null) {
            formVO.setBdDeptId(esbOrganizationDTO.getOrgId());
            formVO.setBdDeptName(esbOrganizationDTO.getOrgName());
        }
        // 获取省份信息
        String province = crmEnterpriseRelationShipApi.getProvinceAreaByThreeParms(employee.getYxDept(), employee.getYxProvince());
        formVO.setProvince(province);
    }

    @ApiOperation("删除全部上传的Excel数据-上传和选择流向tab页切实时使用")
    @PostMapping("/removeAll")
    public Result<Boolean> removeAllAppealUpload(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody RemoveSaleAppealUploadDataForm form) {
        RemoveSelectAppealFlowFormRequest request = PojoUtils.map(form, RemoveSelectAppealFlowFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        boolean isSuccess = saleaAppealFormApi.removeByIds(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation("提交审核兼容销售申诉选择流向")
    @PostMapping("/submitNewFlow")
    public Result<Boolean> submitNewFlow(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SubmitSalesAppealForm form) {
        // 如果formId不存在，先创建
        // 保存草稿
        SaveSalesAppealSubmitFormRequest request = PojoUtils.map(form, SaveSalesAppealSubmitFormRequest.class);
        if (request.getTransferType().equals(TransferTypeEnum.SELECT_APPEAL.getCode())) {
            // 验证选择流向数据是否被锁定 提示：申诉列表中已有流向数据被他人锁定申诉
            saleaAppealFormApi.valid(request.getFormId());
        }
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        // 销售申诉兼容选择流向新流程业务
        Long formId = saleaAppealFormApi.newSaveDraft(request);
        // 提交审核
        form.setFormId(formId);
        SubmitFleeingGoodsFormRequest submitRequest = PojoUtils.map(form, SubmitFleeingGoodsFormRequest.class);
        submitRequest.setOpUserId(userInfo.getCurrentUserId());
        submitRequest.setFormTypeEnum(FormTypeEnum.SALES_APPEAL);
        submitRequest.setEmpId(userInfo.getCurrentUserCode());
        submitRequest.setEmpName(esbEmployeeDTO.getEmpName());
        saleaAppealFormApi.submit(submitRequest);
        return Result.success(true);

    }

    @ApiOperation("销售申诉详情页选择流向数据列表查询")
    @PostMapping("/selectAppealFlowPageList")
    public Result<List<SaleAppealSelectFlowFormVO>> selectAppealFlowPageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QuerySalesAppealForm form) {
        if (Objects.isNull(form.getFormId())) {
            return Result.success(new ArrayList<>());
        }
        List<SaleAppealSelectFlowFormDTO> dtoPage = saleAppealSelectFlowFormApi.selectAppealFlowList(form.getFormId(), 1);
        dtoPage.stream().map(saleAppealSelectFlowFormDTO -> {
            Date saleTime = saleAppealSelectFlowFormDTO.getSaleTime();
            String dateDirTime = DateUtil.format(saleTime, "yyyy-MM-dd");
            saleAppealSelectFlowFormDTO.setSoTime(dateDirTime);
            return saleAppealSelectFlowFormDTO;
        }).collect(Collectors.toList());
        List<SaleAppealSelectFlowFormVO> voPage = PojoUtils.map(dtoPage, SaleAppealSelectFlowFormVO.class);
        return Result.success(voPage);
    }

    @ApiOperation("查询选择申诉流向分页数据")
    @PostMapping("/saleReportPageList")
    public Result<Page<FlowWashSaleReportVO>> saleReportPageList(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFormWashSaleReportPageForm queryFormWashSaleReportPageForm) {
        FlowWashSaleReportPageRequest request = PojoUtils.map(queryFormWashSaleReportPageForm, FlowWashSaleReportPageRequest.class);
        // 销售时间
        Date soTime = DateUtil.parse(queryFormWashSaleReportPageForm.getSoTime(), "yyyy-MM-dd");
        // 判断销售日期是否在系统当前时间前六个月范围内
        if (!DateUtilsCalculation.isRange(queryFormWashSaleReportPageForm.getSoTime())) {
            return Result.success(new Page<FlowWashSaleReportVO>());
        }
        // 获取系统当前时间
        request.setSoTime(soTime);
        request.setYear(StrUtil.toString(DateUtil.year(soTime)));
        request.setMonth(StrUtil.toString(DateUtil.month(soTime) + 1));
        // 机构编码
        request.setCustomerCrmId(queryFormWashSaleReportPageForm.getCrmEnterpriseId());
        // 经销商编码
        request.setCrmId(queryFormWashSaleReportPageForm.getCrmId());
        // 商品编码
        request.setGoodsCode(queryFormWashSaleReportPageForm.getGoodsCode());
        // 流向分类 查询补传月流向的数据
        request.setFlowClassifyList(ListUtil.toList(FlowClassifyEnum.NORMAL.getCode(), FlowClassifyEnum.SUPPLY_TRANS_MONTH_FLOW.getCode()));
//        request.setFlowClassifyList(ListUtil.toList(FlowClassifyEnum.SALE_APPEAL.getCode()));
        // 查询正数
        request.setDataScope(FlowWashSaleReportPageRequest.DataScopeEnum.GT);

        Page<FlowWashSaleReportDTO> page = flowWashReportApi.saleReportPageList(request);
        return Result.success(PojoUtils.map(page, FlowWashSaleReportVO.class));
    }



    @ApiOperation("销量申诉选择流向新增")
    @PostMapping("/saveAppealFlowData")
    public Result<Boolean> saveAppealFlowData(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SelectFlowDataForm form) {
        SaveSelectAppealFlowFormRequest request = PojoUtils.map(form, SaveSelectAppealFlowFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setEmpId(esbEmployeeDTO.getEmpId());
        boolean saveSelectAppealFlowData = saleAppealSelectFlowFormApi.saveSelectAppealFlowData(request);
        if(!saveSelectAppealFlowData){
            return Result.failed("保存失败！");
        }
        return Result.success(true);
    }

    @ApiOperation("删除销售申诉选择流向数据")
    @GetMapping("/removeSelectFlowData")
    public Result<Boolean> removeSelectFlowData(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        RemoveSelectAppealFlowFormRequest request = new RemoveSelectAppealFlowFormRequest();
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(id);
        boolean isSuccess = saleAppealSelectFlowFormApi.removeById(request);
        if (isSuccess) {
            return Result.success(true);
        }
        return Result.failed("删除失败");
    }

    @ApiOperation("销量申诉选择流向新增确认")
    @PostMapping("/saveAppealFlowDataAll")
    public Result<String> saveAppealFlowDataAll(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SelectFlowDataForm form) {
        SaveSelectAppealFlowFormRequest request = PojoUtils.map(form, SaveSelectAppealFlowFormRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        request.setEmpName(esbEmployeeDTO.getEmpName());
        request.setEmpId(esbEmployeeDTO.getEmpId());
        String resultMsg = saleAppealSelectFlowFormApi.saveAppealFlowDataAll(request);
        return Result.success(resultMsg);
    }

}
