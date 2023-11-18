package com.yiling.sjms.wash.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmEnterpriseDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.utils.NumberCheckUtil;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.api.FlowTerminalOrderApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchTransitDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchTransitPageRequest;
import com.yiling.dataflow.wash.dto.request.SaveFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.dto.request.UpdateFlowGoodsBatchTransitRequest;
import com.yiling.dataflow.wash.enums.FlowGoodsBatchTransitTypeEnum;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryFlowTerminalOrderPageForm;
import com.yiling.sjms.wash.form.SaveFlowTerminalOrderForm;
import com.yiling.sjms.wash.form.SaveFlowTerminalOrderListForm;
import com.yiling.sjms.wash.form.UpdateFlowTerminalOrderForm;
import com.yiling.sjms.wash.vo.FlowTerminalOrderPageDetailVO;
import com.yiling.sjms.wash.vo.FlowTerminalOrderPageVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@RestController
@RequestMapping("/flowWash/terminalOrder")
@Api(tags = "终端库存")
public class FlowGoodsBatchTerminalController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FlowWashReportController.class);

    @DubboReference
    private FlowTerminalOrderApi flowTerminalOrderApi;
    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;
    @DubboReference
    private CrmEnterpriseApi crmEnterpriseApi;
    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;
    @DubboReference
    private UserApi userApi;
    @DubboReference
    private FlowInTransitOrderApi flowInTransitOrderApi;

    @ApiOperation(value = "终端库存列表")
    @PostMapping("/listPage")
    public Result<FlowTerminalOrderPageVO<FlowTerminalOrderPageDetailVO>> listPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryFlowTerminalOrderPageForm form) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-列表");
        String datascopeType = map.get("datascopeType").get(0);

        // 所属年月
        if (form.getGbDetailMonth().length() > 7) {
            String gbDetailMonth = DateUtil.format(DateUtil.parse(form.getGbDetailMonth()), "yyyy-MM");
            form.setGbDetailMonth(gbDetailMonth);
        }

        FlowTerminalOrderPageVO<FlowTerminalOrderPageDetailVO> page = new FlowTerminalOrderPageVO<>();
        QueryFlowGoodsBatchTransitPageRequest request = PojoUtils.map(form, QueryFlowGoodsBatchTransitPageRequest.class);
        // 构建终端库存上报时间信息
        buildInTransitSubmitInfo(page);

        // 根据规格查询标准商品id
        buildCrmGoodsCodeList(form, request);
        request.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
        // 部分权限
        if (ObjectUtil.equal("1", datascopeType)) {
            List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
            List<String> crmProvinceCodeList = map.get("provinceCodes");
            request.setCrmEnterpriseIdList(crmIdList);
            request.setCrmProvinceCodeList(crmProvinceCodeList);
        }
        Page<FlowGoodsBatchTransitDTO> dtoPage = flowTerminalOrderApi.listPage(request);
        if (ObjectUtil.isNull(dtoPage) || CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(page);
        }
        page.setCurrent(dtoPage.getCurrent());
        page.setSize(dtoPage.getSize());
        page.setTotal(dtoPage.getTotal());

        // 操作人信息
        Map<Long, String> userNameMap = getUserNameMap(dtoPage.getRecords());

        // 构建分页列表
        List<FlowTerminalOrderPageDetailVO> list = new ArrayList<>();
        for (FlowGoodsBatchTransitDTO record : dtoPage.getRecords()) {
            buildPageRecords(record, list, userNameMap);
        }
        page.setRecords(list);

        return Result.success(page);
    }

    @ApiOperation(value = "详情", httpMethod = "GET")
    @GetMapping("/detail")
    public Result<FlowTerminalOrderPageDetailVO> detail(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-详情");
        String datascopeType = map.get("datascopeType").get(0);

        FlowGoodsBatchTransitDTO flowGoodsBatchTransitDTO = flowTerminalOrderApi.getById(id);
        if (ObjectUtil.isNull(flowGoodsBatchTransitDTO)) {
            throw new BusinessException(ResultCode.FAILED, "此终端库存信息不存在，请确认");
        }
        // 部分权限
        if (ObjectUtil.equal("1", datascopeType)) {
            List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
            List<String> crmProvinceCodeList = map.get("provinceCodes");
            boolean crmIdListFlag = (CollUtil.isNotEmpty(crmIdList) && crmIdList.contains(flowGoodsBatchTransitDTO.getCrmEnterpriseId())) ? true : false;
            boolean crmProvinceCodeListFlag = (CollUtil.isNotEmpty(crmProvinceCodeList) && crmProvinceCodeList.contains(flowGoodsBatchTransitDTO.getCrmProvinceCode())) ? true : false;
            if (!crmIdListFlag && !crmProvinceCodeListFlag) {
                throw new BusinessException(ResultCode.FORBIDDEN, ResultCode.FORBIDDEN.getMessage());
            }
        }
        FlowTerminalOrderPageDetailVO vo = convertDetailVO(flowGoodsBatchTransitDTO);
        return Result.success(vo);
    }

    @ApiOperation(value = "新增校验", httpMethod = "POST")
    @PostMapping("/saveCheck")
    public Result<List<String>> saveCheck(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFlowTerminalOrderListForm form) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-新增校验");
        String datascopeType = map.get("datascopeType").get(0);
        List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
        List<String> crmProvinceCodeList = map.get("provinceCodes");

        // 所属年月
        if (form.getGbDetailMonth().length() > 7) {
            String gbDetailMonth = DateUtil.format(DateUtil.parse(form.getGbDetailMonth()), "yyyy-MM");
            form.setGbDetailMonth(gbDetailMonth);
        }
        // 保存校验
        List<String> errorMsgList = doSaveCheck(form, datascopeType, crmIdList, crmProvinceCodeList, 1);
        if (CollUtil.isNotEmpty(errorMsgList)) {
            return Result.success(errorMsgList);
        }
        return Result.success(ListUtil.empty());
    }

    @Log(title = "月流向清洗-终端库存-新增", businessType = BusinessTypeEnum.INSERT)
    @ApiOperation(value = "新增", httpMethod = "POST")
    @PostMapping("/save")
    public Result<List<String>> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFlowTerminalOrderListForm form) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-新增");
        String datascopeType = map.get("datascopeType").get(0);
        List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
        List<String> crmProvinceCodeList = map.get("provinceCodes");

        // 所属年月
        if (form.getGbDetailMonth().length() > 7) {
            String gbDetailMonth = DateUtil.format(DateUtil.parse(form.getGbDetailMonth()), "yyyy-MM");
            form.setGbDetailMonth(gbDetailMonth);
        }
        // 保存校验
        List<String> errorMsgList = doSaveCheck(form, datascopeType, crmIdList, crmProvinceCodeList, 2);
        if (CollUtil.isNotEmpty(errorMsgList)) {
            return Result.success(errorMsgList);
        }

        // 经销商
        Long crmEnterpriseId = form.getCrmEnterpriseId();
        String name = form.getName();

        // 明细
        List<SaveFlowTerminalOrderForm> saveList = form.getSaveList();

        // 三者关系
        // 机构id、标准商品编码 map
        Map<Long, Long> crmEnterpriseIdGoodsCodeMap = new HashMap<>();
        // 经销商
        List<Long> getCrmGoodsCodeList = saveList.stream().filter(o -> o.getCrmGoodsCode() > 0).map(SaveFlowTerminalOrderForm::getCrmGoodsCode).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(getCrmGoodsCodeList)) {
            getCrmGoodsCodeList.forEach(goodsCode -> {
                crmEnterpriseIdGoodsCodeMap.put(crmEnterpriseId, goodsCode);
            });
        }
        // 获取三者关系id
        Map<Long, Long> crmEnterpriseIdCersIdMap = flowInTransitOrderApi.getCrmEnterpriseRelationShipMap(crmEnterpriseIdGoodsCodeMap, 0, form.getGbDetailMonth());

        List<SaveFlowGoodsBatchTransitRequest> saveRequesList = new ArrayList<>();
        Date date = new Date();
        for (SaveFlowTerminalOrderForm saveDetail : saveList) {
            // 构建保存明细
            SaveFlowGoodsBatchTransitRequest request = buildSaveRequest(userInfo, date, crmEnterpriseIdCersIdMap, form, saveDetail);
            saveRequesList.add(request);
        }
        flowTerminalOrderApi.batchSave(saveRequesList);
        return Result.success(ListUtil.empty());
    }

    @ApiOperation(value = "编辑校验", httpMethod = "POST")
    @PostMapping("/updateCheck")
    public Result<String> updateCheck(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateFlowTerminalOrderForm form) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-编辑校验");
        String datascopeType = map.get("datascopeType").get(0);
        List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
        List<String> crmProvinceCodeList = map.get("provinceCodes");


        Long id = form.getId();
        FlowGoodsBatchTransitDTO oldData = flowTerminalOrderApi.getById(id);

        // 编辑校验
        String errorMsg = doUpdateCheck(form, oldData, datascopeType, crmIdList, crmProvinceCodeList, 1);
        if (StrUtil.isNotBlank(errorMsg) && !ObjectUtil.equal("0", errorMsg)) {
            return Result.success(errorMsg);
        }
        return Result.success("");
    }

    @Log(title = "月流向清洗-终端库存-编辑", businessType = BusinessTypeEnum.UPDATE)
    @ApiOperation(value = "编辑", httpMethod = "POST")
    @PostMapping("/update")
    public Result<String> update(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid UpdateFlowTerminalOrderForm form) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-编辑");
        String datascopeType = map.get("datascopeType").get(0);
        List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
        List<String> crmProvinceCodeList = map.get("provinceCodes");

        Long id = form.getId();
        FlowGoodsBatchTransitDTO oldData = flowTerminalOrderApi.getById(id);

        // 编辑校验
        String errorMsg = doUpdateCheck(form, oldData, datascopeType, crmIdList, crmProvinceCodeList, 2);
        if (StrUtil.isNotBlank(errorMsg)) {
            if (ObjectUtil.equal("0", errorMsg)) {
                return Result.success("");
            } else {
                return Result.success(errorMsg);
            }
        }

        // 三者关系，编辑后如果标准产品编码未变、机构id未变  则不再重新查询三者关系
        // 机构id、标准商品编码 map
        Map<Long, Long> crmEnterpriseIdGoodsCodeMap = new HashMap<>();
        boolean crmGoodsCodeFlag = ObjectUtil.equal(oldData.getCrmGoodsCode(), oldData.getCrmGoodsCode()) ? true : false;
        if (!ObjectUtil.equal(oldData.getCrmEnterpriseId(), oldData.getCrmEnterpriseId()) || !crmGoodsCodeFlag) {
            // 经销商
            crmEnterpriseIdGoodsCodeMap.put(oldData.getCrmEnterpriseId(), oldData.getCrmGoodsCode());
        }
        Map<Long, Long> crmEnterpriseIdCersIdMap = flowInTransitOrderApi.getCrmEnterpriseRelationShipMap(crmEnterpriseIdGoodsCodeMap, 1, oldData.getGbDetailMonth());

        UpdateFlowGoodsBatchTransitRequest request = buildUpdateRequest(userInfo, crmEnterpriseIdCersIdMap, form, oldData);
        flowTerminalOrderApi.batchUpdate(ListUtil.toList(request));
        return Result.success("");
    }

    @Log(title = "月流向清洗-终端库存-删除", businessType = BusinessTypeEnum.DELETE)
    @ApiOperation(value = "删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result<Integer> delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam(value = "id") Long id) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "终端库存-删除");
        String datascopeType = map.get("datascopeType").get(0);

        FlowGoodsBatchTransitDTO oldData = getOldData(id);

        // 部分权限
        if (ObjectUtil.equal("1", datascopeType)) {
            List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
            List<String> crmProvinceCodeList = map.get("provinceCodes");
            boolean crmIdListFlag = (CollUtil.isNotEmpty(crmIdList) && crmIdList.contains(oldData.getCrmEnterpriseId())) ? true : false;
            boolean crmProvinceCodeListFlag = (CollUtil.isNotEmpty(crmProvinceCodeList) && crmProvinceCodeList.contains(oldData.getCrmProvinceCode())) ? true : false;
            if (!crmIdListFlag && !crmProvinceCodeListFlag) {
                throw new BusinessException(ResultCode.FORBIDDEN, ResultCode.FORBIDDEN.getMessage());
            }
        }

        String gbDetailMonth = oldData.getGbDetailMonth();
        if (StrUtil.isBlank(gbDetailMonth)) {
            throw new BusinessException(ResultCode.FAILED, "此终端库存信息的所属年月为空，不能删除，请确认");
        }
        // 校验对应月份的月流向清洗日程中在途订单上报，是否开启
        checkFlowMonthWashControlByYearMonth(gbDetailMonth);
        return Result.success(flowTerminalOrderApi.deleteById(id, userInfo.getCurrentUserId()));
    }

    private Map<Long, String> getUserNameMap(List<FlowGoodsBatchTransitDTO> list) {
        Map<Long, String> userNameMap = new HashMap<>();
        Set<Long> opUserIdSet = new HashSet<>();
        List<Long> createUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getCreateUser()) && o.getCreateUser() > 0).map(FlowGoodsBatchTransitDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getUpdateUser()) && o.getUpdateUser() > 0).map(FlowGoodsBatchTransitDTO::getUpdateUser).distinct().collect(Collectors.toList());
        opUserIdSet.addAll(createUserIdList);
        if (CollUtil.isNotEmpty(updateUserIdList)) {
            opUserIdSet.addAll(updateUserIdList);
        }
        if (CollUtil.isEmpty(opUserIdSet)) {
            return userNameMap;
        }
        List<UserDTO> userList = userApi.listByIds(ListUtil.toList(opUserIdSet));
        if (CollUtil.isNotEmpty(userList)) {
            userNameMap = userList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o.getName(), (k1, k2) -> k1));
        }
        return userNameMap;
    }

    private void checkFlowMonthWashControlByYearMonth(String gbDetailMonth) {
        String[] split = gbDetailMonth.split("-");
        // 校验对应月份的月流向清洗日程中终端库存上报，是否开启
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowMonthWashControlApi.getByYearAndMonth(Integer.parseInt(split[0]), Integer.parseInt(split[1]));
        if (ObjectUtil.isNotNull(flowMonthWashControlDTO) && ObjectUtil.isNotNull(flowMonthWashControlDTO.getId()) || 0 != flowMonthWashControlDTO.getId().intValue()) {
            Integer goodsBatchStatus = flowMonthWashControlDTO.getWashStatus();
            if (ObjectUtil.isNull(goodsBatchStatus) || goodsBatchStatus!=2) {
                Date startTime = flowMonthWashControlDTO.getGoodsBatchStartTime();
                Date endTime = flowMonthWashControlDTO.getGoodsBatchEndTime();
                String start = DateUtil.format(startTime, "yyyy-MM-dd HH:mm:ss");
                String end = DateUtil.format(endTime, "yyyy-MM-dd HH:mm:ss");
                throw new BusinessException(ResultCode.FAILED, "[终端库存上报]的月流向清洗日程不是进行中、不是手动开启，【" + gbDetailMonth + "】终端库存提交时间为 " + start + " 至 " + end);
            }
        }
    }

    private FlowGoodsBatchTransitDTO getOldData(Long id) {
        FlowGoodsBatchTransitDTO dbFlowGoodsBatchTransit = flowTerminalOrderApi.getById(id);
        if (ObjectUtil.isNull(dbFlowGoodsBatchTransit)) {
            throw new BusinessException(ResultCode.FAILED, "此终端库存信息不存在，请确认");
        }
        return dbFlowGoodsBatchTransit;
    }

    /**
     * 构建编辑明细
     *
     * @param userInfo 当前登录用户
     * @param crmEnterpriseRelationMap 三者关系
     * @param form 编辑信息
     */
    private UpdateFlowGoodsBatchTransitRequest buildUpdateRequest(CurrentSjmsUserInfo userInfo, Map<Long, Long> crmEnterpriseRelationMap, UpdateFlowTerminalOrderForm form, FlowGoodsBatchTransitDTO oldData) {
        /*
        // 所属年月
        String gbDetailMonth = DateUtil.format(DateUtil.parse(form.getGbDetailMonth(), "yyyy-MM"), "yyyy-MM");
        // 经销商
        Long crmEnterpriseId = form.getCrmEnterpriseId();
        String name = form.getName();
        */

        Date date = new Date();
        // 经销商
        Long crmEnterpriseId = oldData.getCrmEnterpriseId();

        UpdateFlowGoodsBatchTransitRequest updateRequest = new UpdateFlowGoodsBatchTransitRequest();
        updateRequest.setId(form.getId());
        /*
        // 所属年月
        updateRequest.setGbDetailMonth(gbDetailMonth);
        // 经销商
        updateRequest.setCrmEnterpriseId(crmEnterpriseId);
        updateRequest.setName(name);
        // 标准商品
        updateRequest.setCrmGoodsCode(form.getCrmGoodsCode());
        updateRequest.setCrmGoodsName(form.getCrmGoodsName());
        updateRequest.setCrmGoodsSpecifications(form.getCrmGoodsSpecifications());
        */
        // 批号
        String gbBatchNo = "";
        if (StrUtil.isNotBlank(form.getGbBatchNo())) {
            gbBatchNo = form.getGbBatchNo();
        }
        updateRequest.setGbBatchNo(gbBatchNo);
        // 库存数量
        updateRequest.setGbNumber(form.getGbNumber());
        // 库存类型
        updateRequest.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
        // 经销商三者关系ID
        Long enterpriseRelationId = crmEnterpriseRelationMap.get(crmEnterpriseId);
        if (ObjectUtil.isNotNull(enterpriseRelationId) && 0 != enterpriseRelationId) {
            updateRequest.setEnterpriseCersId(enterpriseRelationId);
        }
        // 创建人
        updateRequest.setUpdateUser(userInfo.getCurrentUserId());
        updateRequest.setUpdateTime(date);
        return updateRequest;
    }

    /**
     * 编辑校验
     *
     * @param form 编辑后信息
     * @param oldData 编辑前信息
     * @param datascopeType 权限类型：0-全部 1-部分
     * @param crmIdList 数据权限crmID列表
     * @param crmProvinceCodeList 数据权限crm省份编码列表
     * @param type 校验类型：1-校验接口 2-编辑接口
     * @return 未修改："0"。    校验结果：notBlank && !equals("0")
     */
    private String doUpdateCheck(UpdateFlowTerminalOrderForm form, FlowGoodsBatchTransitDTO oldData, String datascopeType, List<Long> crmIdList, List<String> crmProvinceCodeList, int type) {

        if (ObjectUtil.isNull(oldData)) {
            throw new BusinessException(ResultCode.FAILED, "此终端库存信息不存在，请确认");
        }

        // 校验经销商
        Long crmEnterpriseId = oldData.getCrmEnterpriseId();
        String crmProvinceCode = oldData.getCrmProvinceCode();
        // 部分数据权限，是否有数据权限
        if (ObjectUtil.equal("1", datascopeType)) {
            boolean crmIdListFlag = (CollUtil.isNotEmpty(crmIdList) && crmIdList.contains(crmEnterpriseId)) ? true : false;
            boolean crmProvinceCodeListFlag = (CollUtil.isNotEmpty(crmProvinceCodeList) && crmProvinceCodeList.contains(crmProvinceCode)) ? true : false;
            if (!crmIdListFlag && !crmProvinceCodeListFlag) {
                throw new BusinessException(ResultCode.FORBIDDEN, ResultCode.FORBIDDEN.getMessage());
            }
        }

        // 校验是否可以提交终端库存
        String flowMonthWashControlMsg = flowInTransitOrderApi.checkFlowMonthWashControl("终端库存上报", oldData.getGbDetailMonth());
        if (StrUtil.isNotBlank(flowMonthWashControlMsg)) {
            throw new BusinessException(ResultCode.FAILED, flowMonthWashControlMsg);
        }

        // 批号校验
        String gbBatchNo = form.getGbBatchNo();
        if (StrUtil.isNotBlank(gbBatchNo) && gbBatchNo.length() > 50) {
            throw new BusinessException(ResultCode.FAILED, "批号不能超过50个字符");
        }
        // 库存数量校验
        BigDecimal gbNumber = form.getGbNumber();
        if (!NumberCheckUtil.positiveInteger8BitNumber(gbNumber.toString())) {
            throw new BusinessException(ResultCode.FAILED, "库存数量需为大于0的1至8位正整数");
        }
        // 内容是否有修改变动，未改变-true    已改变-false
        //        boolean changeFlag = isChange(form, oldData);
        //        if (changeFlag) {
        //            return "0";
        //        }
        // 重复校验
        if (type == 1) {
            String itemKey = getItemKey(oldData.getCrmEnterpriseId(), oldData.getCrmGoodsCode(), form.getGbBatchNo(), gbNumber);
            List<FlowGoodsBatchTransitDTO> oldFlowGoodsBatchTransitList = flowTerminalOrderApi.listByEnterpriseAndSupplyIdAndGoodsCode(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode(), ListUtil.toList(oldData.getCrmEnterpriseId()), ListUtil.toList(oldData.getCrmGoodsCode()));
            Map<String, FlowGoodsBatchTransitDTO> oldMap = new HashMap<>();
            if (CollUtil.isNotEmpty(oldFlowGoodsBatchTransitList)) {
                oldMap = oldFlowGoodsBatchTransitList.stream().filter(o -> !ObjectUtil.equal(o.getId(), oldData.getId())).collect(Collectors.toMap(o -> getItemKey(o.getCrmEnterpriseId(), o.getCrmGoodsCode(), o.getGbBatchNo(), o.getGbNumber()), o -> o, (k1, k2) -> k1));
            }
            if (MapUtil.isNotEmpty(oldMap)) {
                if (oldMap.containsKey(itemKey)) {
                    Long oldId = oldMap.get(itemKey).getId();
                    return "与已存在数据重复，终端库存主键id：" + oldId;
                }
            }
        }

        return null;
    }

    /**
     * 比较内容是否改变
     *
     * @param form 新内容
     * @param oldData 旧内容
     * @return 未改变-true。    已改变-false
     */
   /* private boolean isChange(UpdateFlowTerminalOrderForm form, FlowGoodsBatchTransitDTO oldData) {
        // 修改提交信息
        StringBuffer formsb = new StringBuffer();
        formsb.append(form.getGbDetailMonth()).append(form.getCrmEnterpriseId()).append(form.getName())
                .append(form.getCrmGoodsCode()).append(form.getCrmGoodsName()).append(form.getCrmGoodsSpecifications())
                .append(form.getGbNumber().stripTrailingZeros().toPlainString());
        if (StrUtil.isNotBlank(form.getGbBatchNo())) {
            formsb.append(form.getGbBatchNo());
        }
//        String formMd5 = SecureUtil.md5(formsb.toString());

        // 修改提交之前信息
        StringBuffer dtosb = new StringBuffer();
        dtosb.append(oldData.getGbDetailMonth()).append(oldData.getCrmEnterpriseId()).append(oldData.getName())
                .append(oldData.getCrmGoodsCode()).append(oldData.getCrmGoodsName()).append(oldData.getCrmGoodsSpecifications())
                .append(oldData.getGbNumber().stripTrailingZeros().toPlainString());
        if (StrUtil.isNotBlank(oldData.getGbBatchNo())) {
            dtosb.append(oldData.getGbBatchNo());
        }
//        String dtoMd5 = SecureUtil.md5(dtosb.toString());

        if (ObjectUtil.equal(formsb.toString(), dtosb.toString())){
            return true;
        }
        return false;
    }*/

    /**
     * 构建保存明细
     *
     * @param userInfo 当前登录用户
     * @param date 当前时间
     * @param crmEnterpriseRelationMap 三者关系
     * @param form 保存表单信息
     * @param saveDetail 保存表单明细
     */
    private SaveFlowGoodsBatchTransitRequest buildSaveRequest(CurrentSjmsUserInfo userInfo, Date date, Map<Long, Long> crmEnterpriseRelationMap,
                                                              SaveFlowTerminalOrderListForm form, SaveFlowTerminalOrderForm saveDetail) {
        Long crmEnterpriseId = form.getCrmEnterpriseId();

        SaveFlowGoodsBatchTransitRequest saveRequest = new SaveFlowGoodsBatchTransitRequest();
        // 所属年月
        saveRequest.setGbDetailMonth(form.getGbDetailMonth());
        // 经销商
        saveRequest.setCrmEnterpriseId(crmEnterpriseId);
        saveRequest.setName(form.getName());
        saveRequest.setCrmProvinceName(form.getCrmProvinceName());
        saveRequest.setCrmProvinceCode(form.getCrmProvinceCode());
        // 标准商品
        saveRequest.setCrmGoodsCode(saveDetail.getCrmGoodsCode());
        saveRequest.setCrmGoodsName(saveDetail.getCrmGoodsName());
        saveRequest.setCrmGoodsSpecifications(saveDetail.getCrmGoodsSpecifications());
        // 批号
        saveRequest.setGbBatchNo(saveDetail.getGbBatchNo());
        // 库存数量
        saveRequest.setGbNumber(saveDetail.getGbNumber());
        // 库存类型
        saveRequest.setGoodsBatchType(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode());
        // 经销商三者关系ID
        Long enterpriseRelationId = crmEnterpriseRelationMap.get(crmEnterpriseId);
        if (ObjectUtil.isNotNull(enterpriseRelationId) && 0 != enterpriseRelationId.intValue()) {
            saveRequest.setEnterpriseCersId(enterpriseRelationId);
        }
        // 创建人
        saveRequest.setCreateUser(userInfo.getCurrentUserId());
        saveRequest.setCreateTime(date);
        return saveRequest;
    }

    /**
     * 新增校验
     *
     * @param form 新增信息
     * @param datascopeType 权限类型：0-全部 1-部分
     * @param crmIdList 数据权限crmID列表
     * @param crmProvinceCodeList 数据权限crm省份编码列表
     * @param type 校验类型：1-校验接口 2-保存接口
     * @return
     */
    private List<String> doSaveCheck(SaveFlowTerminalOrderListForm form, String datascopeType, List<Long> crmIdList, List<String> crmProvinceCodeList, int type) {
        List<String> errorMsgList = new ArrayList();
        // 校验经销商
        Long crmEnterpriseId = form.getCrmEnterpriseId();
        CrmEnterpriseDTO crmEnterpriseDTO = crmEnterpriseApi.getCrmEnterpriseById(crmEnterpriseId);
        if (ObjectUtil.isNull(crmEnterpriseDTO)) {
            throw new BusinessException(ResultCode.FAILED, "经销商不存在，请确认经销商名称");
        }
        // 新增的经销商，是否有数据权限
        if (ObjectUtil.equal("1", datascopeType)) {
            boolean crmIdListFlag = (CollUtil.isNotEmpty(crmIdList) && crmIdList.contains(crmEnterpriseDTO.getId())) ? true : false;
            boolean crmProvinceCodeListFlag = (CollUtil.isNotEmpty(crmProvinceCodeList) && crmProvinceCodeList.contains(crmEnterpriseDTO.getProvinceCode())) ? true : false;
            if (!crmIdListFlag && !crmProvinceCodeListFlag) {
                throw new BusinessException(ResultCode.FORBIDDEN, ResultCode.FORBIDDEN.getMessage());
            }
        }
        String name = form.getName();
        if (!ObjectUtil.equal(crmEnterpriseDTO.getName(), name)) {
            throw new BusinessException(ResultCode.FAILED, "经销商名称不正确，请确认经销商名称");
        }

        // 校验明细条数
        List<SaveFlowTerminalOrderForm> saveList = form.getSaveList();
        if (saveList.size() > 20) {
            throw new BusinessException(ResultCode.FAILED, "提交的数据条数不能超过20行");
        }
        // 校验是否可以提交终端库存
        String flowMonthWashControlMsg = flowInTransitOrderApi.checkFlowMonthWashControl("终端库存上报", form.getGbDetailMonth());
        if (StrUtil.isNotBlank(flowMonthWashControlMsg)) {
            throw new BusinessException(ResultCode.FAILED, flowMonthWashControlMsg);
        }

        // 经销商所属省份
        form.setCrmProvinceName(crmEnterpriseDTO.getProvinceName());
        form.setCrmProvinceCode(crmEnterpriseDTO.getProvinceCode());

        // 明细校验
        // 标准产品
        Map<Long, CrmGoodsInfoDTO> crmGoodsInfoMap = new HashMap<>();
        List<Long> crmGoodsCodeList = saveList.stream().filter(o -> ObjectUtil.isNotNull(o.getCrmGoodsCode()) && o.getCrmGoodsCode() > 0).map(SaveFlowTerminalOrderForm::getCrmGoodsCode).distinct().collect(Collectors.toList());
        if (CollUtil.isNotEmpty(crmGoodsCodeList)) {
            List<CrmGoodsInfoDTO> crmGoodsInfoList = crmGoodsInfoApi.findByCodeList(crmGoodsCodeList);
            if (CollUtil.isNotEmpty(crmGoodsInfoList)) {
                crmGoodsInfoMap = crmGoodsInfoList.stream().collect(Collectors.toMap(o -> o.getGoodsCode(), o -> o, (k1, k2) -> k1));
            }
        }

        // 已存在数据
        List<FlowGoodsBatchTransitDTO> oldFlowGoodsBatchTransitList = flowTerminalOrderApi.listByEnterpriseAndSupplyIdAndGoodsCode(FlowGoodsBatchTransitTypeEnum.TERMINAL.getCode(), ListUtil.toList(crmEnterpriseId), crmGoodsCodeList);
        Map<String, FlowGoodsBatchTransitDTO> oldMap = new HashMap<>();
        if (CollUtil.isNotEmpty(oldFlowGoodsBatchTransitList)) {
            oldMap = oldFlowGoodsBatchTransitList.stream().collect(Collectors.toMap(o -> getItemKey(o.getCrmEnterpriseId(), o.getCrmGoodsCode(), o.getGbBatchNo(), o.getGbNumber()), o -> o, (k1, k2) -> k1));
        }

        Map<String, Integer> keyMap = new HashMap<>();

        for (int i = 0; i < saveList.size(); i++) {
            SaveFlowTerminalOrderForm saveDetail = saveList.get(i);
            // 标准商品校验
            Long crmGoodsCode = saveDetail.getCrmGoodsCode();
            CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoMap.get(crmGoodsCode);
            if (ObjectUtil.isNull(crmGoodsInfoDTO)) {
                String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：标准产品不存在，请确认标准产品编码或名称");
                throw new BusinessException(ResultCode.FAILED, msg);
            }
            String crmGoodsName = saveDetail.getCrmGoodsName();
            if (!ObjectUtil.equal(crmGoodsInfoDTO.getGoodsName(), crmGoodsName)) {
                String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：标准产品名称不正确，请确认标准产品名称");
                throw new BusinessException(ResultCode.FAILED, msg);
            }
            String crmGoodsSpecifications = saveDetail.getCrmGoodsSpecifications();
            if (!ObjectUtil.equal(crmGoodsInfoDTO.getGoodsSpec(), crmGoodsSpecifications)) {
                String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：标准产品规格不正确，请确认标准产品规格");
                throw new BusinessException(ResultCode.FAILED, msg);
            }
            // 批号校验
            String gbBatchNo = saveDetail.getGbBatchNo();
            if (StrUtil.isNotBlank(gbBatchNo) && gbBatchNo.length() > 50) {
                String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：批号不能超过50个字符");
                throw new BusinessException(ResultCode.FAILED, msg);
            }
            // 库存数量校验
            BigDecimal gbNumber = saveDetail.getGbNumber();
            if (ObjectUtil.isNull(gbNumber)) {
                String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：库存数量不能为空");
                throw new BusinessException(ResultCode.FAILED, msg);
            }
            if (!NumberCheckUtil.positiveInteger8BitNumber(gbNumber.toString())) {
                String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：库存数量需为大于0的1至8位正整数");
                throw new BusinessException(ResultCode.FAILED, msg);
            }
            // 重复校验
            if (type == 1) {
                String itemKey = getItemKey(crmEnterpriseId, crmGoodsCode, gbBatchNo, gbNumber);
                if (keyMap.containsKey(itemKey)) {
                    String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：与第").concat(keyMap.get(itemKey).toString()).concat("行重复");
                    errorMsgList.add(msg);
                    continue;
                } else {
                    keyMap.put(itemKey, saveDetail.getItemId());
                }
                FlowGoodsBatchTransitDTO flowGoodsBatchTransitDTO = oldMap.get(itemKey);
                if (ObjectUtil.isNotNull(flowGoodsBatchTransitDTO)) {
                    String msg = "第".concat(saveDetail.getItemId() + "").concat("行数据：与已存在数据重复，终端库存主键id：").concat(flowGoodsBatchTransitDTO.getId().toString());
                    errorMsgList.add(msg);
                    continue;
                }
            }
        }
        return errorMsgList;
    }

    private String getItemKey(Long crmEnterpriseId, Long crmGoodsCode, String gbBatchNo, BigDecimal gbNumber) {
        if (StrUtil.isBlank(gbBatchNo)) {
            gbBatchNo = "";
        }
        String key = crmEnterpriseId.toString().concat("_").concat(crmGoodsCode.toString()).concat("_").concat(gbBatchNo).concat("_").concat(gbNumber.stripTrailingZeros().toPlainString());
        return key;
    }


    private void buildCrmGoodsCodeList(QueryFlowTerminalOrderPageForm form, QueryFlowGoodsBatchTransitPageRequest request) {
        if (ObjectUtil.isNull(form) || StrUtil.isBlank(form.getCrmGoodsSpecifications())) {
            return;
        }
        // 根据规格查询标准商品id
        String crmGoodsSpec = form.getCrmGoodsSpecifications();
        List<CrmGoodsInfoDTO> crmGoodsInfoList = crmGoodsInfoApi.findByNameAndSpec(null, crmGoodsSpec, 0);
        if (CollUtil.isNotEmpty(crmGoodsInfoList)) {
            List<Long> crmGoodsCodeList = crmGoodsInfoList.stream().map(CrmGoodsInfoDTO::getGoodsCode).distinct().collect(Collectors.toList());
            request.setCrmGoodsCodeList(crmGoodsCodeList);
        }
    }

    private void buildInTransitSubmitInfo(FlowTerminalOrderPageVO<FlowTerminalOrderPageDetailVO> page) {
        // 获取是否可以提交终端库存
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowInTransitOrderApi.getGoodsBatchTime();
        if (ObjectUtil.isNull(flowMonthWashControlDTO) || ObjectUtil.isNull(flowMonthWashControlDTO.getId()) || 0 == flowMonthWashControlDTO.getId().intValue()) {
            page.setSubmitFlag(false);
        } else {
            boolean submitFlag = false;
            Integer goodsBatchStatus = flowMonthWashControlDTO.getWashStatus();
            List<Integer> washControlStatusList = ListUtil.toList(FlowMonthWashControlMappingStatusEnum.IN_PROGRESS.getCode(), FlowMonthWashControlMappingStatusEnum.MANUAL_OPEN.getCode());
            if (ObjectUtil.isNotNull(goodsBatchStatus) && goodsBatchStatus==1) {
                submitFlag = true;
            }
            page.setSubmitFlag(submitFlag);
            page.setYear(flowMonthWashControlDTO.getYear());
            page.setMonth(flowMonthWashControlDTO.getMonth());
            page.setGoodsBatchStartTime(flowMonthWashControlDTO.getGoodsBatchStartTime());
            page.setGoodsBatchEndTime(flowMonthWashControlDTO.getGoodsBatchEndTime());
            DateTime currentYearMonthTime = DateUtil.parse(flowMonthWashControlDTO.getYear() + "-" + flowMonthWashControlDTO.getMonth(), "yyyy-MM");
            page.setCurrentYearMonthTime(currentYearMonthTime);
        }
    }

    private void buildPageRecords(FlowGoodsBatchTransitDTO record, List<FlowTerminalOrderPageDetailVO> list, Map<Long, String> userNameMap) {
        if (ObjectUtil.isNull(record)) {
            return;
        }
        FlowTerminalOrderPageDetailVO vo = convertDetailVO(record);
        // 操作时间
        Date createTime = record.getCreateTime();
        Date updateTime = record.getUpdateTime();
        Date opTime;
        if (ObjectUtil.isNull(updateTime) || ObjectUtil.equal("1970-01-01 00:00:00", DateUtil.format(updateTime, "yyyy-MM-dd HH:mm:ss"))) {
            opTime = createTime;
        } else {
            opTime = updateTime;
        }
        vo.setOpTime(opTime);
        // 操作人
        Long createUser = record.getCreateUser();
        Long updateUser = record.getUpdateUser();
        Long opUser = 0L;
        if (ObjectUtil.isNull(updateUser) || 0 == updateUser.intValue()) {
            opUser = createUser;
        } else {
            opUser = updateUser;
        }
        // 用户姓名
        String name = userNameMap.get(opUser);
        vo.setOpUser(name);
        list.add(vo);
    }

    private FlowTerminalOrderPageDetailVO convertDetailVO(FlowGoodsBatchTransitDTO record) {
        FlowTerminalOrderPageDetailVO vo = new FlowTerminalOrderPageDetailVO();
        vo.setId(record.getId());
        vo.setCrmEnterpriseId(record.getCrmEnterpriseId());
        vo.setName(record.getName());
        vo.setCrmGoodsCode(record.getCrmGoodsCode());
        vo.setCrmGoodsName(record.getCrmGoodsName());
        vo.setCrmGoodsSpecifications(record.getCrmGoodsSpecifications());
        vo.setGbBatchNo(record.getGbBatchNo());
        vo.setGbNumber(record.getGbNumber());
        vo.setGbUnit(record.getGbUnit());
        // 所属年月
        Date month = DateUtil.parse(record.getGbDetailMonth(), "yyyy-MM");
        vo.setGbDetailMonth(month);
        return vo;
    }

}
