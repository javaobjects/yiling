package com.yiling.dataflow.flowcollect.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.agency.dto.CrmSupplierDTO;
import com.yiling.dataflow.agency.service.CrmSupplierService;
import com.yiling.dataflow.crm.entity.CrmEnterpriseDO;
import com.yiling.dataflow.crm.service.CrmEnterpriseService;
import com.yiling.dataflow.flow.dto.FlowEnterpriseConnectMonitorDTO;
import com.yiling.dataflow.flow.service.FlowEnterpriseConnectMonitorService;
import com.yiling.dataflow.flowcollect.bo.FlowMonthUploadRecordBO;
import com.yiling.dataflow.flowcollect.dao.FlowMonthUploadRecordMapper;
import com.yiling.dataflow.flowcollect.dto.FlowMonthUploadRecordDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowMonthUploadPageRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.dto.request.UpdateFlowMonthUploadRecordRequest;
import com.yiling.dataflow.flowcollect.entity.FlowMonthUploadRecordDO;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadCheckStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadDataTypeEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadImportStatusEnum;
import com.yiling.dataflow.flowcollect.enums.FlowMonthUploadTypeEnum;
import com.yiling.dataflow.flowcollect.service.FlowMonthInventoryService;
import com.yiling.dataflow.flowcollect.service.FlowMonthPurchaseService;
import com.yiling.dataflow.flowcollect.service.FlowMonthSalesService;
import com.yiling.dataflow.flowcollect.service.FlowMonthUploadRecordService;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.entity.FlowMonthWashTaskDO;
import com.yiling.dataflow.wash.enums.FlowClassifyEnum;
import com.yiling.dataflow.wash.service.FlowMonthWashControlService;
import com.yiling.dataflow.wash.service.FlowMonthWashTaskService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.common.util.WrapperUtils;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.SjmsUserApi;
import com.yiling.user.system.bo.BaseUser;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.bo.SjmsUser;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 月流向上传记录表 服务实现类
 * </p>
 *
 * @author lun.yu
 * @date 2023-03-03
 */
@Slf4j
@Service
public class FlowMonthUploadRecordServiceImpl extends BaseServiceImpl<FlowMonthUploadRecordMapper, FlowMonthUploadRecordDO> implements FlowMonthUploadRecordService {

    @Autowired
    CrmEnterpriseService crmEnterpriseService;
    @Autowired
    FlowMonthWashControlService flowMonthWashControlService;

    @DubboReference
    SjmsUserApi sjmsUserApi;

    @Autowired
    FlowEnterpriseConnectMonitorService flowEnterpriseConnectMonitorService;

    @DubboReference
    EsbEmployeeApi esbEmployeeApi;

    @Autowired
    CrmSupplierService crmSupplierService;

    @Autowired
    FlowMonthWashTaskService flowMonthWashTaskService;

    @Autowired
    FlowMonthSalesService flowMonthSalesService;

    @Autowired
    FlowMonthPurchaseService flowMonthPurchaseService;

    @Autowired
    FlowMonthInventoryService flowMonthInventoryService;

    @Override
    public Page<FlowMonthUploadRecordBO> queryFlowMonthPage(QueryFlowMonthUploadPageRequest request) {
        QueryWrapper<FlowMonthUploadRecordDO> wrapper = WrapperUtils.getWrapper(request);
        wrapper.lambda().eq(FlowMonthUploadRecordDO::getUploadType, FlowMonthUploadTypeEnum.NORMAL.getCode()).orderByDesc(FlowMonthUploadRecordDO::getCreateTime);
        Page<FlowMonthUploadRecordDO> recordDOPage = this.page(request.getPage(), wrapper);

        Page<FlowMonthUploadRecordBO> boPage = PojoUtils.map(recordDOPage, FlowMonthUploadRecordBO.class);

        List<Long> userIdList = recordDOPage.getRecords().stream().map(FlowMonthUploadRecordDO::getCreateUser).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(userIdList)) {
            Map<Long, SjmsUser> userMap = sjmsUserApi.listByIds(userIdList).stream().collect(Collectors.toMap(BaseUser::getId, Function.identity()));

            boPage.getRecords().forEach(flowMonthUploadRecordBO -> {
                SjmsUser sjmsUser = userMap.getOrDefault(flowMonthUploadRecordBO.getCreateUser(), new SjmsUser());
                flowMonthUploadRecordBO.setCreateUserName(sjmsUser.getName());
                flowMonthUploadRecordBO.setCreateUserNo(sjmsUser.getEmpId());
            });
        }

        return boPage;
    }

    @Override
    public String checkFlowFileName(String fileName) {
        // PM_HHN099_濮阳九州通医药有限公司_20221231.csv    月销售_经销商编码_经销商名称_日期.格式
        if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls")) {
            return "未按照模板文件名称进行命名";
        }
        String[] fileNameArr = fileName.split("_");
        if (fileNameArr.length != 4) {
            return "未按照模板文件名称进行命名";
        }

        Integer dataType = this.getDataType(fileName);
        if (dataType == 0) {
            return "未按照模板文件名称进行命名";
        }

        // 校验经销商编码和名称
        try {
            Long crmEnterpriseId = Long.valueOf(fileNameArr[1]);
            CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(crmEnterpriseId);
            if (Objects.isNull(crmEnterpriseDO)) {
                return "文件名称中的经销商编码不存在";
            }

        } catch (Exception e) {
            return "未按照模板文件名称进行命名";
        }

        String[] dateArr = fileNameArr[3].split("\\.");
        try {
            if (dateArr[0].length() > 8) {
                return "未按照模板文件名称进行命名";
            }
            DateTime date = DateUtil.parse(dateArr[0], "yyyyMMdd");

            FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getWashStatus();
            if (Objects.nonNull(flowMonthWashControlDTO) && (flowMonthWashControlDTO.getYear() != date.year() || flowMonthWashControlDTO.getMonth() != (date.month() + 1))) {
                return "文件名称年月与当前流向收集年月不匹配";
            }

        } catch (Exception e) {
            return "未按照模板文件名称进行命名";
        }

        // 重复校验
        FlowMonthUploadRecordDTO uploadRecordDTO = this.getByFileName(fileName);
        if (Objects.nonNull(uploadRecordDTO)) {
            return "文件名称与当前流向清洗队列中的文件名称重复";
        }

        return null;
    }

    @Override
    public FlowMonthUploadRecordDTO getByFileName(String fileName) {
        LambdaQueryWrapper<FlowMonthUploadRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthUploadRecordDO::getFileName, fileName);
        wrapper.in(FlowMonthUploadRecordDO::getImportStatus, Arrays.asList(FlowMonthUploadImportStatusEnum.SUCCESS.getCode(),FlowMonthUploadImportStatusEnum.IN_PROGRESS.getCode()));
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), FlowMonthUploadRecordDTO.class);
    }

    @Override
    public FlowMonthUploadRecordDTO getByRecordId(Long recordId) {
        LambdaQueryWrapper<FlowMonthUploadRecordDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthUploadRecordDO::getRecordId, recordId);
        wrapper.last("limit 1");
        return PojoUtils.map(this.getOne(wrapper), FlowMonthUploadRecordDTO.class);
    }

    @Override
    public Long saveFlowMonthRecord(SaveFlowMonthUploadRecordRequest request) {
        FlowMonthUploadRecordDO uploadRecordDO = PojoUtils.map(request, FlowMonthUploadRecordDO.class);
        Integer dataType = this.getDataType(request.getFileName());
        uploadRecordDO.setDataType(dataType);
        this.save(uploadRecordDO);
        return uploadRecordDO.getId();
    }

    @Override
    public boolean updateFlowMonthRecord(UpdateFlowMonthUploadRecordRequest request) {
        FlowMonthUploadRecordDO uploadRecordDO = PojoUtils.map(request, FlowMonthUploadRecordDO.class);
        return this.updateById(uploadRecordDO);
    }

    @Override
    public Integer getDataType(String fileName) {
        String[] fileNameArr = fileName.split("_");
        String code = fileNameArr[0];
        int dataType = 0;
        // 月销售（SM开头）   月采购（PM开头）   月库存（IM开头）
        if ("SM".equalsIgnoreCase(code)) {
            dataType = 1;
        } else if ("IM".equalsIgnoreCase(code)) {
            dataType = 2;
        } else if ("PM".equalsIgnoreCase(code)) {
            dataType = 3;
        }
        return dataType;
    }

    @Override
    public String getExcelCodeByFileName(String fileName) {
        Integer dataType = this.getDataType(fileName);
        String excelCode = null;
        switch (dataType) {
            case 1:
                excelCode = "importFlowMonthSales";
                break;
            case 2:
                excelCode = "importFlowMonthInventory";
                break;
            case 3:
                excelCode = "importFlowMonthPurchase";
                break;
        }
        return excelCode;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteRecord(Long id, Long currentUserId) {
        FlowMonthUploadRecordDO uploadRecordDO = new FlowMonthUploadRecordDO();
        uploadRecordDO.setId(id);
        uploadRecordDO.setOpUserId(currentUserId);
        return this.deleteByIdWithFill(uploadRecordDO) > 0;
    }

    @Override
    public String checkFlowFileNameNew(String fileName, CurrentSjmsUserInfo userInfo, boolean isFixUpload) {
        log.info("当前登录人信息userInfo为：{}, 文件名称为：{}", JSON.toJSONString(userInfo), fileName);
        // PM_HHN099_濮阳九州通医药有限公司_20221231.csv    月销售_经销商编码_经销商名称_日期.格式
        if (!fileName.toLowerCase().endsWith(".xlsx") && !fileName.toLowerCase().endsWith(".xls")) {
            return "未按照模板文件名称进行命名";
        }
        String[] fileNameArr = fileName.split("_");
        if(isFixUpload){
            if (fileNameArr.length < 5) {
                return "未按照模板文件名称进行命名";
            }else{
                if(!fileNameArr[4].startsWith("补传")){
                    return "未按照模板文件名称进行命名";
                }
            }
        }
//        else{
//            if (fileNameArr.length != 4) {
//                return "未按照模板文件名称进行命名";
//            }
//        }

        Integer dataType = this.getDataType(fileName);
        if (dataType == 0) {
            return "未按照模板文件名称进行命名";
        }
        // 商业编码
        Long crmEnterpriseId;
        // 校验经销商编码和名称
        try {
            crmEnterpriseId = Long.valueOf(fileNameArr[1]);
            CrmEnterpriseDO crmEnterpriseDO = crmEnterpriseService.getById(crmEnterpriseId);
            if (Objects.isNull(crmEnterpriseDO)) {
                return "文件名称中的经销商编码不存在";
            }
        } catch (Exception e) {
            return "未按照模板文件名称进行命名";
        }

        String[] dateArr = fileNameArr[3].split("\\.");
        try {
            //            if (dateArr[0].length() > 8) {
            //                return "未按照模板文件名称进行命名";
            //            }
            DateTime date = DateUtil.parse(dateArr[0].substring(0, 8), "yyyyMMdd");

            FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlService.getWashStatus();
            if (!isFixUpload && Objects.nonNull(flowMonthWashControlDTO) && (flowMonthWashControlDTO.getYear() != date.year() || flowMonthWashControlDTO.getMonth() != (date.month() + 1))) {
                return "文件名称年月与当前流向收集年月不匹配";
            }

        } catch (Exception e) {
            return "未按照模板文件名称进行命名";
        }

        // 接口实施负责人 和 流向打取人 有的机构没有值，没值不让上传
        // b. 月流向上传需要校验操作人是商业的流向打取人/商务负责人/实施负责人，如流向上传人员与该商业的流向打取人/商务负责人/实施负责人不匹配，
        // 则数据检查状态为“未通过”提示原因为“当前账号与该商业的流向打取人/商务负责人/实施负责人不匹配，无法上传。”导入状态为“导入失败”
        // 实施负责人列表参考：流向清洗>流向收集>上直连接口监控表>接口实施负责人
        // 流向打取人/商务负责人表参考：基础数据管理>机构管理>商业公司档案>流向打取人/商务负责人
        // 注意：流向打取人/商务负责人/实施负责人必须与上述两个表中的商业公司对应，也就是流向打取人/商务负责人/实施负责人只能上传自己负责的商业公司。
        // 通过经销商编码查询流向直连监控
        EsbEmployeeDTO esbEmployeeDTO = esbEmployeeApi.getByEmpId(userInfo.getCurrentUserCode());
        String empName = esbEmployeeDTO.getEmpName();
        // 获取流向直连监控实施负责人
        // TODO 实施人暂时用名称来比对，后期等数据刷出工号之后，再改为实施人用工号来比对
        FlowEnterpriseConnectMonitorDTO byInstallEmployee = flowEnterpriseConnectMonitorService.findByCrmEid(crmEnterpriseId);
        String installEmployee = null;
        if (Objects.nonNull(byInstallEmployee)) {
            installEmployee = byInstallEmployee.getInstallEmployee();
        }
        // 流向打取人比较逻辑为：当前登录人工号与流向打取人工号进行比对
        List<Long> crmEnterIds = Arrays.asList(crmEnterpriseId);
        // 查询当前机构流向打取人工号
        List<CrmSupplierDTO> supplierInfoByCrmEnterId = crmSupplierService.getSupplierInfoByCrmEnterId(crmEnterIds);
        // 获取流向打取人工号
        List<String> flowJobNumberList = supplierInfoByCrmEnterId.stream().map(CrmSupplierDTO::getFlowJobNumber).collect(Collectors.toList());
        // 获取商务负责人工号
        List<String> commerceJobNumberList = supplierInfoByCrmEnterId.stream().map(CrmSupplierDTO::getCommerceJobNumber).collect(Collectors.toList());
        log.info("通过机构编号查询出流向打取人的工号为：{},商务负责人的工号为：{},实施负责人installEmployee为：{}", JSON.toJSONString(flowJobNumberList), JSON.toJSONString(commerceJobNumberList), installEmployee);
        // 获取流向打取人工号和商务负责人工号并集
        flowJobNumberList.addAll(commerceJobNumberList);
        // 获取去重后的工号集合
        List<String> distinctJObNumberList = flowJobNumberList.stream().distinct().collect(Collectors.toList());
        log.info("去重后的工号distinctJObNumberList为：{}", JSON.toJSONString(distinctJObNumberList));
        // 机构档案和直连接口实施人都为空不允许上传
        if (CollUtil.isEmpty(distinctJObNumberList) && Objects.isNull(installEmployee)) {
            log.info("当前登录人的工号CurrentUserCode为：{},当前登录人姓名eName为：{}, 实施负责人和机构流向打取人为空", userInfo.getCurrentUserCode(), empName);
            return "当前账号与该商业的流向打取人/商务负责人/实施负责人不匹配，无法上传。";
        }
        // 机构档案和直连接口实施负责人有一方匹配上就允许上传
        // 如果直连接口实施负责人没有，机构档案流向打取人有却匹配不上的，上传失败
        if (Objects.isNull(installEmployee)) {
            if(CollUtil.isNotEmpty(distinctJObNumberList) && !distinctJObNumberList.contains(userInfo.getCurrentUserCode())){
                log.info("如果直连接口实施负责人没有，机构档案流向打取人有却匹配不上的,当前登录人的工号CurrentUserCode为：{},当前登录人姓名eName为：{},通过机构编号查询出的实施负责人installEmployee为：{},流向打取人工号flowJobNumberList为：{}", userInfo.getCurrentUserCode(), empName, installEmployee, JSON.toJSONString(flowJobNumberList));
                return "当前账号与该商业的流向打取人/商务负责人/实施负责人不匹配，无法上传。";
            }
        }
        // 如果机构档案流向打取人没有，直连接口实施负责人有值却匹配不上的，导入失败
        if (CollUtil.isEmpty(distinctJObNumberList)) {
            if(Objects.nonNull(installEmployee) && !empName.equals(installEmployee)){
                log.info("如果机构档案流向打取人没有，直连接口实施负责人有值却匹配不上的,当前登录人的工号CurrentUserCode为：{},当前登录人姓名eName为：{},通过机构编号查询出的实施负责人installEmployee为：{},流向打取人工号flowJobNumberList为：{}", userInfo.getCurrentUserCode(), empName, installEmployee, JSON.toJSONString(flowJobNumberList));
                return "当前账号与该商业的流向打取人/商务负责人/实施负责人不匹配，无法上传。";
            }
        }
        // 如果机构档案流向打取人有值，直连接口实施负责人有值却都匹配不上的，导入失败
        if((CollUtil.isNotEmpty(distinctJObNumberList) && !distinctJObNumberList.contains(userInfo.getCurrentUserCode()))
                && (Objects.nonNull(installEmployee)) && !empName.equals(installEmployee)){
            log.info("如果机构档案流向打取人有值，直连接口实施负责人有值却都匹配不上的,当前登录人的工号CurrentUserCode为：{},当前登录人姓名eName为：{},通过机构编号查询出的实施负责人installEmployee为：{},流向打取人工号flowJobNumberList为：{}", userInfo.getCurrentUserCode(), empName, installEmployee, JSON.toJSONString(flowJobNumberList));
            return "当前账号与该商业的流向打取人/商务负责人/实施负责人不匹配，无法上传。";
        }
        if(isFixUpload){
            return null;
        }
        // 重复校验
        FlowMonthUploadRecordDTO uploadRecordDTO = this.getByFileName(fileName);
        log.info("文件名称与当前流向清洗队列中的文件名称重复校验逻辑是否进入：{},文件名fileName为：{},查询记录uploadRecordDTO为：{}", Objects.nonNull(uploadRecordDTO), fileName, JSON.toJSONString(uploadRecordDTO));
        if (Objects.nonNull(uploadRecordDTO)) {
            return "文件名称与当前流向清洗队列中的文件名称重复";
        }

        // 月流向上传新增逻辑校验规则：
        // a. 同一个商业、同一类型的的流向（进、销、存）在月流向列表里只能存在一条生效的正常月流向数据，月流向上传时，如该商业在流向列表中已经存在流向数据，
        // 则数据检查状态为“未通过”提示原因为“月流向清洗列表已有流向数据，请删除原流向数据后再进行上传。”导入状态为“导入失败”
        // 年
        Date date = DateUtil.parse(dateArr[0].substring(0, 8), "yyyyMMdd");
        int year = DateUtil.year(date);
        // 月
        int month = DateUtil.month(date) + 1;
        // 文件流向类型
        Integer flowType = this.getFlowType(fileName);
        LambdaQueryWrapper<FlowMonthWashTaskDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(FlowMonthWashTaskDO::getFlowClassify, FlowClassifyEnum.NORMAL.getCode())
                .eq(FlowMonthWashTaskDO::getDelFlag, 0)
                .eq(FlowMonthWashTaskDO::getYear, year)
                .eq(FlowMonthWashTaskDO::getMonth, month)
                .eq(FlowMonthWashTaskDO::getFlowType, flowType)
                .eq(FlowMonthWashTaskDO::getCrmEnterpriseId, fileNameArr[1]);
        List<FlowMonthWashTaskDO> flowMonthWashTaskDOList = flowMonthWashTaskService.list(wrapper);
        log.info("查询出月流向清洗列表已有流向数据,数据flowMonthWashTaskDOList为：{}", JSON.toJSONString(flowMonthWashTaskDOList));
        if(CollUtil.isNotEmpty(flowMonthWashTaskDOList)){
            return "月流向清洗列表已有流向数据，请删除原流向数据后再进行上传";
        }
        return null;
    }

    @Override
    public Integer getFlowType(String fileName) {
        // 流向类型 1-采购 2-销售 3-库存
        String[] fileNameArr = fileName.split("_");
        String code = fileNameArr[0];
        int flowType = 0;
        // 月销售（SM开头）   月采购（PM开头）   月库存（IM开头）
        if ("SM".equalsIgnoreCase(code)) {
            flowType = 2;
        } else if ("IM".equalsIgnoreCase(code)) {
            flowType = 3;
        } else if ("PM".equalsIgnoreCase(code)) {
            flowType = 1;
        }
        return flowType;
    }

    @Override
    public Boolean createWashTask() {
        LambdaQueryWrapper<FlowMonthUploadRecordDO> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(FlowMonthUploadRecordDO::getImportStatus,FlowMonthUploadImportStatusEnum.SUCCESS.getCode()).eq(FlowMonthUploadRecordDO::getCheckStatus, FlowMonthUploadCheckStatusEnum.PASS.getCode());
        wrapper.eq(FlowMonthUploadRecordDO::getUploadType,FlowMonthUploadTypeEnum.FIX.getCode()).eq(FlowMonthUploadRecordDO::getWashTaskId,0);
        List<FlowMonthUploadRecordDO> list = this.list(wrapper);
        if(CollUtil.isNotEmpty(list)){
            list.forEach(l->{
               if(l.getDataType().equals(FlowMonthUploadDataTypeEnum.SALES.getCode()) ){
                   flowMonthSalesService.updateFlowMonthSalesAndTask(0L,l.getId());
               }
                if(l.getDataType().equals(FlowMonthUploadDataTypeEnum.INVENTORY.getCode()) ){
                    flowMonthInventoryService.updateFlowMonthInventoryAndTask(0L,l.getId());
                }
                if(l.getDataType().equals(FlowMonthUploadDataTypeEnum.PURCHASE.getCode()) ){
                    flowMonthPurchaseService.updateFlowMonthPurchaseAndTask(0L,l.getId());
                }
            });
        }
        return true;
    }
}
