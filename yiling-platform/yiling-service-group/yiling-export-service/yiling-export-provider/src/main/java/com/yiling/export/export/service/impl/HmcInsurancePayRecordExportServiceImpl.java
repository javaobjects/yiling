package com.yiling.export.export.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.yiling.export.export.bo.ExportInsurancePayRecordBO;
import com.yiling.export.export.dto.ExportDataDTO;
import com.yiling.export.export.dto.QueryExportDataDTO;
import com.yiling.export.export.service.BaseExportQueryDataService;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.hmc.insurance.api.BackInsuranceRecordPayApi;
import com.yiling.hmc.insurance.api.InsuranceApi;
import com.yiling.hmc.insurance.api.InsuranceCompanyApi;
import com.yiling.hmc.insurance.bo.InsuranceRecordPayBO;
import com.yiling.hmc.insurance.dto.InsurancePageDTO;
import com.yiling.hmc.insurance.dto.request.InsurancePageRequest;
import com.yiling.hmc.insurance.dto.request.QueryInsuranceRecordPayPageRequest;
import com.yiling.hmc.wechat.enums.HmcInsuranceBillTypeEnum;
import com.yiling.hmc.wechat.enums.HmcPolicyStatusEnum;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseTypeEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.Staff;
import com.yiling.user.system.dto.UserDTO;
import com.yiling.user.system.dto.request.QueryStaffListRequest;
import com.yiling.user.system.enums.UserStatusEnum;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 保险销售记录导出
 *
 * @author: gxl
 * @date: 2022/4/19
 */
@Service("insurancePayRecordExportService")
public class HmcInsurancePayRecordExportServiceImpl implements BaseExportQueryDataService<QueryInsuranceRecordPayPageRequest> {
    @DubboReference
    BackInsuranceRecordPayApi backInsuranceRecordPayApi;
    @DubboReference
    InsuranceCompanyApi insuranceCompanyApi;
    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    InsuranceApi insuranceApi;

    @DubboReference
    UserApi userApi;
    @DubboReference
    StaffApi staffApi;


    private static final LinkedHashMap<String, String> FIELD = new LinkedHashMap<String, String>() {
        {
            put("insuranceName", "保险名称");
            put("policyNo", "保司保单号");
            put("orderNo", "平台单号");
            put("billTypeName", "定额方案类型");
            put("terminalName", "保单来源终端");
            put("sourceTypeName", "来源类型");
            put("issueName", "被保人姓名");
            put("issuePhone", "被保人手机号");
            put("proposalTime", "投保时间");
            put("sellerUserName", "销售员姓名");
            put("sellerPhone", "销售员电话");
            put("sellerUserId", "销售员ID标识");
            put("amount", "支付额");
            put("createTime", "创建时间");
            put("updateTime", "最新更新时间");
            put("policyStatus", "保单状态");
            put("cashTerminalName", "保险兑付药店");
            put("expiredTime", "保单终止时间");
            put("totalCashCount", "药品总兑付盒数");
            put("cashTimes", "已兑次数");
            put("cashedTotal", "已兑盒数");
        }
    };

    @Override
    public QueryExportDataDTO queryData(QueryInsuranceRecordPayPageRequest request) {
        request.setExport(true);
        //需要返回的对象
        QueryExportDataDTO result = new QueryExportDataDTO();
        //需要循环调用
        List<Map<String, Object>> data = new ArrayList<>();
        Page<InsuranceRecordPayBO> page = null;
        int current = 1;
        do {

            request.setCurrent(current);
            request.setSize(500);
            page = this.handleData(request, data);
            if (null != page && CollUtil.isEmpty(page.getRecords())) {
                break;
            }
            current = current + 1;
        } while (null != page &&  page.getTotal()>500 && CollUtil.isNotEmpty(page.getRecords()));
        ExportDataDTO exportDataDTO = new ExportDataDTO();
        exportDataDTO.setSheetName("保险销售记录--按交易单导出表");
        exportDataDTO.setFieldMap(FIELD);
        exportDataDTO.setData(data);
        List<ExportDataDTO> sheets = new ArrayList<>();
        sheets.add(exportDataDTO);
        result.setSheets(sheets);
        return result;

    }

    @Override
    public QueryInsuranceRecordPayPageRequest getParam(Map<String, Object> map) {
        QueryInsuranceRecordPayPageRequest recordPageRequest = PojoUtils.map(map, QueryInsuranceRecordPayPageRequest.class);
        //按销售员姓名查询
        String sellerUserName = String.valueOf(map.get("sellerUserName"));
        if (StrUtil.isNotEmpty(sellerUserName)) {
            QueryStaffListRequest queryStaffListRequest = new QueryStaffListRequest();
            queryStaffListRequest.setNameEq(sellerUserName);
            queryStaffListRequest.setStatusNe(UserStatusEnum.DEREGISTER.getCode());
            List<Staff> staffList = staffApi.list(queryStaffListRequest);
            if (CollUtil.isNotEmpty(staffList)) {
                recordPageRequest.setSellerUserIds(staffList.stream().map(Staff::getId).collect(Collectors.toList()));
            }
        }
        //按销售员手机号查询
        String sellerMobile = String.valueOf(map.get("sellerMobile"));
        if (StrUtil.isNotEmpty(sellerMobile)) {
            Staff staff = staffApi.getByMobile(sellerMobile);
            if (Objects.nonNull(staff)) {
                List<Long> userIdList = Lists.newArrayList();
                userIdList.add(staff.getId());
                recordPageRequest.setSellerUserIds(userIdList);
            }

        }

        //按保单来源终端查询
        String terminalName = String.valueOf(map.get("terminalName"));
        if (StrUtil.isNotEmpty(terminalName)) {
            QueryEnterprisePageListRequest request = new QueryEnterprisePageListRequest();
            List<Integer> typeList = Lists.newArrayList();
            typeList.add(EnterpriseTypeEnum.CHAIN_BASE.getCode());
            typeList.add(EnterpriseTypeEnum.HOSPITAL.getCode());
            typeList.add(EnterpriseTypeEnum.CLINIC.getCode());
            typeList.add(EnterpriseTypeEnum.CHAIN_DIRECT.getCode());
            typeList.add(EnterpriseTypeEnum.CHAIN_JOIN.getCode());
            typeList.add(EnterpriseTypeEnum.PHARMACY.getCode());
            request.setHmcType(2);
            request.setInTypeList(typeList);
            request.setName(terminalName);
            Page<EnterpriseDTO> enterpriseDTOPage = enterpriseApi.pageList(request);
            if (enterpriseDTOPage.getTotal() > 0) {
                recordPageRequest.setEidList(enterpriseDTOPage.getRecords().stream().map(EnterpriseDTO::getId).collect(Collectors.toList()));
            }
        }
        String insuranceName = String.valueOf(map.get("insuranceName"));

        //按保险名称查询
        if (StrUtil.isNotEmpty(insuranceName)) {
            InsurancePageRequest request = new InsurancePageRequest();
            request.setInsuranceName(insuranceName);
            Page<InsurancePageDTO> insurancePageDTOPage = insuranceApi.pageList(request);
            if (insurancePageDTOPage.getTotal() > 0) {
                recordPageRequest.setInsuranceIdList(insurancePageDTOPage.getRecords().stream().map(InsurancePageDTO::getId).collect(Collectors.toList()));
            }
        }
        return recordPageRequest;
    }

    private Page<InsuranceRecordPayBO> handleData(QueryInsuranceRecordPayPageRequest request, List<Map<String, Object>> data) {
        Page<InsuranceRecordPayBO> recordBOPage = backInsuranceRecordPayApi.queryPage(request);
        if (recordBOPage.getTotal() == 0) {
            return null;
        }
        List<InsuranceRecordPayBO> records = recordBOPage.getRecords();
        List<Long> sellerUserIdList = records.stream().map(InsuranceRecordPayBO::getSellerUserId).distinct().collect(Collectors.toList());
        List<UserDTO> userDTOS = userApi.listByIds(sellerUserIdList);
        Map<Long, UserDTO> userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        List<Long> eidList = records.stream().map(InsuranceRecordPayBO::getEid).distinct().collect(Collectors.toList());
        List<Long> sellerEidList = records.stream().map(InsuranceRecordPayBO::getSellerEid).distinct().collect(Collectors.toList());
        List<Long> collect = CollUtil.union(eidList, sellerEidList).stream().distinct().collect(Collectors.toList());
        Map<Long, EnterpriseDTO> enterpriseDTOMap = Maps.newHashMap();
        if(CollUtil.isNotEmpty(collect)){
            collect = collect.stream().filter(e->e>0).distinct().collect(Collectors.toList());
            if(CollUtil.isNotEmpty(collect)){
                enterpriseDTOMap = enterpriseApi.getMapByIds(collect);
            }
        }
        Map<Long, EnterpriseDTO> finalEnterpriseDTOMap = enterpriseDTOMap;
        records.forEach(insuranceRecordBO -> {
            ExportInsurancePayRecordBO export = new ExportInsurancePayRecordBO();
            PojoUtils.map(insuranceRecordBO,export);
            export.setBillTypeName(HmcInsuranceBillTypeEnum.getByType(insuranceRecordBO.getBillType()).getName());
            export.setPolicyStatus(HmcPolicyStatusEnum.getByType(insuranceRecordBO.getPolicyStatus()).getName());
            if(insuranceRecordBO.getSellerUserId()==0){
                export.setSourceTypeName("线上渠道");
            }else{
                export.setSourceTypeName("线下终端");
            }

            UserDTO userDTO = userDTOMap.get(insuranceRecordBO.getSellerUserId());
            if (Objects.nonNull(userDTO)) {
                export.setSellerUserName(userDTO.getName()).setSellerPhone(userDTO.getMobile());
            }else{
                export.setSellerUserName(Constants.SEPARATOR_MIDDLELINE).setSellerPhone(Constants.SEPARATOR_MIDDLELINE);
            }
            EnterpriseDTO enterpriseDTO = finalEnterpriseDTOMap.get(insuranceRecordBO.getSellerEid());
            if (Objects.nonNull(enterpriseDTO)) {
                export.setTerminalName(enterpriseDTO.getName());
            }
            EnterpriseDTO terminal = finalEnterpriseDTOMap.get(insuranceRecordBO.getEid());
            if (Objects.nonNull(terminal)) {
                export.setCashTerminalName(terminal.getName());
            }
            Map<String, Object> dataPojo = BeanUtil.beanToMap(export);
            data.add(dataPojo);
        });
        return recordBOPage;
    }
}

