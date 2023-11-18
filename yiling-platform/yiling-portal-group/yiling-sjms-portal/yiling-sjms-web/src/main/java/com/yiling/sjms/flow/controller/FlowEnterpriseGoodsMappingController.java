package com.yiling.sjms.flow.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.flow.api.FlowEnterpriseGoodsMappingApi;
import com.yiling.dataflow.flow.dto.FlowEnterpriseGoodsMappingDTO;
import com.yiling.dataflow.flow.dto.request.SaveFlowEnterpriseGoodsMappingRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.base.BaseDTO;
import com.yiling.framework.common.base.EsAggregationDTO;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpClientApi;
import com.yiling.search.flow.api.EsFlowEnterpriseGoodsMappingApi;
import com.yiling.search.flow.dto.EsFlowEnterpriseGoodsMappingDTO;
import com.yiling.search.flow.dto.request.EsFlowEnterpriseGoodsMappingSearchRequest;
import com.yiling.sjms.flow.form.BatchSaveGoodsMappingForm;
import com.yiling.sjms.flow.form.QueryFlowEnterpriseGoodsMappingPageForm;
import com.yiling.sjms.flow.form.SaveFlowEnterpriseGoodsMappingForm;
import com.yiling.sjms.flow.vo.FlowEnterpriseGoodsMappingVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shichen
 * @类名 FlowEnterpriseGoodsMappingController
 * @描述 商品对照关系controller
 * @创建时间 2023/3/1
 * @修改人 shichen
 * @修改时间 2023/3/1
 **/
@Slf4j
@RestController
@RequestMapping("/flowEnterpriseGoodsMapping")
@Api(tags = "产品对照关系管理")
public class FlowEnterpriseGoodsMappingController extends BaseController {

    @DubboReference
    private EsFlowEnterpriseGoodsMappingApi esFlowEnterpriseGoodsMappingApi;

    @DubboReference
    private FlowEnterpriseGoodsMappingApi flowEnterpriseGoodsMappingApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private FlowMonthWashControlApi flowMonthWashControlApi;

    @DubboReference
    private UserApi userApi;

    @DubboReference
    ErpClientApi erpClientApi;

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;

    private static final BigDecimal MAX_CONVERT_NUMBER = new BigDecimal("99999.99");

    @ApiOperation(value = "产品对照查询", httpMethod = "POST")
    @PostMapping("/searchGoodsMapping")
    public Result<Page<FlowEnterpriseGoodsMappingVO>> search(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowEnterpriseGoodsMappingPageForm form) {
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("产品对照查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if (null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())) {
            return Result.success(new Page<>());
        }
        if (null != form.getStartUpdateTime() || null != form.getEndUpdateTime()) {
            if (null != form.getStartUpdateTime()) {
                if (null == form.getEndUpdateTime()) {
                    return Result.failed("最后操作时间结束为空");
                }
            } else {
                return Result.failed("最后操作时间开始为空");
            }
            form.setStartUpdateTime(DateUtil.beginOfDay(form.getStartUpdateTime()));
            form.setEndUpdateTime(DateUtil.endOfDay(form.getEndUpdateTime()));
            if (form.getStartUpdateTime().getTime() > form.getEndUpdateTime().getTime()) {
                return Result.failed("开始时间必须小于结束时间");
            }
        }
        EsFlowEnterpriseGoodsMappingSearchRequest request = PojoUtils.map(form, EsFlowEnterpriseGoodsMappingSearchRequest.class);
        request.setSortField("update_time");
        request.setOrgDatascope(userDatascopeBO.getOrgDatascope());
        if (null != userDatascopeBO.getOrgPartDatascopeBO()) {
            request.setCrmEnterpriseIds(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setProvinceCodes(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
        }
        EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> esAggregationDTO = esFlowEnterpriseGoodsMappingApi.searchFlowEnterpriseGoodsMapping(request);
        Page<FlowEnterpriseGoodsMappingVO> page = this.toPageVO(esAggregationDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "产品未对照查询", httpMethod = "POST")
    @PostMapping("/searchGoodsUnmapped")
    public Result<Page<FlowEnterpriseGoodsMappingVO>> searchGoodsUnmapped(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid QueryFlowEnterpriseGoodsMappingPageForm form) {
        SjmsUserDatascopeBO userDatascopeBO = sjmsUserDatascopeApi.getByEmpId(userInfo.getCurrentUserCode());
        log.info("产品未对照查询权限:userDatascopeBO={}", JSONUtil.toJsonStr(userDatascopeBO));
        //代表没权限返回空
        if (null == userDatascopeBO || OrgDatascopeEnum.NONE.getCode().equals(userDatascopeBO.getOrgDatascope())) {
            return Result.success(new Page<>());
        }
        if (null != form.getStartLastUploadTime() || null != form.getEndLastUploadTime()) {
            if (null != form.getStartLastUploadTime()) {
                if (null == form.getEndLastUploadTime()) {
                    return Result.failed("最后上传时间结束为空");
                }
            } else {
                return Result.failed("最后上传时间开始为空");
            }
            form.setStartLastUploadTime(DateUtil.beginOfDay(form.getStartLastUploadTime()));
            form.setEndLastUploadTime(DateUtil.endOfDay(form.getEndLastUploadTime()));
            if (form.getStartLastUploadTime().getTime() > form.getEndLastUploadTime().getTime()) {
                return Result.failed("开始时间必须小于结束时间");
            }
        }
        EsFlowEnterpriseGoodsMappingSearchRequest request = PojoUtils.map(form, EsFlowEnterpriseGoodsMappingSearchRequest.class);
        request.setSortField("last_upload_time");
        request.setCrmGoodsCode(0L);
        //部分权限
        request.setOrgDatascope(userDatascopeBO.getOrgDatascope());
        if (null != userDatascopeBO.getOrgPartDatascopeBO()) {
            request.setCrmEnterpriseIds(userDatascopeBO.getOrgPartDatascopeBO().getCrmEids());
            request.setProvinceCodes(userDatascopeBO.getOrgPartDatascopeBO().getProvinceCodes());
        }
        EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> esAggregationDTO = esFlowEnterpriseGoodsMappingApi.searchFlowEnterpriseGoodsMapping(request);
        Page<FlowEnterpriseGoodsMappingVO> page = this.toPageVO(esAggregationDTO);
        return Result.success(page);
    }

    @ApiOperation(value = "产品对照删除", httpMethod = "GET")
    @GetMapping("/delete")
    public Result delete(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam("id") Long id) {
        FlowEnterpriseGoodsMappingDTO goodsMappingDTO = flowEnterpriseGoodsMappingApi.findById(id);
        if (null == goodsMappingDTO) {
            return Result.failed("删除的数据不存在");
        }
        Boolean b = flowEnterpriseGoodsMappingApi.deleteById(id, userInfo.getCurrentUserId());
        if (b) {
            Date currentDate = new Date();
            Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
            int lastMonth = DateUtil.month(lastMonthTime) + 1;
            int lastYear = DateUtil.year(lastMonthTime);
            FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
            if (null != controlDTO && 2 == controlDTO.getWashStatus()) {
                //发送删除前数值给上月月流向刷新
                flowEnterpriseGoodsMappingApi.sendRefreshGoodsFlowMq(ListUtil.toList(goodsMappingDTO));
            }
            return Result.success();
        } else {
            return Result.failed("删除失败");
        }
    }

    @ApiOperation(value = "产品对照编辑", httpMethod = "POST")
    @PostMapping("/edit")
    public Result edit(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFlowEnterpriseGoodsMappingForm form) {
        FlowEnterpriseGoodsMappingDTO goodsMappingDTO = flowEnterpriseGoodsMappingApi.findById(form.getId());
        if (null == goodsMappingDTO) {
            return Result.failed("产品对照关系不存在");
        }
        if (null == form.getConvertUnit()) {
            if (null == form.getConvertNumber() || BigDecimal.ZERO.equals(form.getConvertNumber())) {
                form.setConvertUnit(1);
                form.setConvertNumber(BigDecimal.ONE);
            } else {
                return Result.failed("转换单位为空");
            }
        } else {
            if (form.getConvertUnit() != 1 && form.getConvertUnit() != 2) {
                return Result.failed("转换单位不正确");
            }
            if (null != form.getConvertNumber()) {
                if (form.getConvertNumber().compareTo(BigDecimal.ZERO) <= 0) {
                    return Result.failed("转换系数必须大于0");
                }
                if (form.getConvertNumber().scale() > 2) {
                    return Result.failed("转换系数最多两位小数");
                }
                if (form.getConvertNumber().compareTo(MAX_CONVERT_NUMBER) > 0) {
                    return Result.failed("转换系数最大为99999.99");
                }
            }
        }

        CrmGoodsInfoDTO crmGoodsInfoDTO = crmGoodsInfoApi.findByCodeAndName(form.getCrmGoodsCode(), form.getGoodsName());
        if (null == crmGoodsInfoDTO) {
            return Result.failed("标准产品编码和标准产品名称不存在对应标准产品");
        }
        SaveFlowEnterpriseGoodsMappingRequest request = PojoUtils.map(form, SaveFlowEnterpriseGoodsMappingRequest.class);
        request.setGoodsSpecification(crmGoodsInfoDTO.getGoodsSpec());
        request.setOpUserId(userInfo.getCurrentUserId());
        flowEnterpriseGoodsMappingApi.save(request);
        Date currentDate = new Date();
        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth = DateUtil.month(lastMonthTime) + 1;
        int lastYear = DateUtil.year(lastMonthTime);
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        if (null != controlDTO && 2 == controlDTO.getWashStatus()) {
            //发送更新前数值给上月月流向刷新
            flowEnterpriseGoodsMappingApi.sendRefreshGoodsFlowMq(ListUtil.toList(goodsMappingDTO));
        }
        return Result.success();
    }

    @ApiOperation(value = "产品对照批量保存编辑", httpMethod = "POST")
    @PostMapping("/batchSave")
    public Result batchSave(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid BatchSaveGoodsMappingForm form) {
        if (CollectionUtil.isEmpty(form.getSaveList())) {
            return Result.failed("提交更新数据为空，提交失败！");
        }
        List<SaveFlowEnterpriseGoodsMappingRequest> requestList = PojoUtils.map(form.getSaveList(), SaveFlowEnterpriseGoodsMappingRequest.class);
        List<Long> codeList = requestList.stream().map(SaveFlowEnterpriseGoodsMappingRequest::getCrmGoodsCode).collect(Collectors.toList());
        List<CrmGoodsInfoDTO> crmGoodsList = crmGoodsInfoApi.findByCodeList(codeList);
        Map<Long, CrmGoodsInfoDTO> goodsMap = crmGoodsList.stream().collect(Collectors.toMap(CrmGoodsInfoDTO::getGoodsCode, Function.identity(), (e1, e2) -> e2));
        for (SaveFlowEnterpriseGoodsMappingRequest request : requestList) {
            if (null == request.getId() || request.getId() <= 0) {
                return Result.failed("提交数据异常，提交失败！");
            }
            CrmGoodsInfoDTO goodsInfoDTO = goodsMap.get(request.getCrmGoodsCode());
            if (null == goodsInfoDTO) {
                return Result.failed("您输入的标准商品编码" + request.getCrmGoodsCode() + "不存在");
            }
            if (!goodsInfoDTO.getGoodsName().equals(request.getGoodsName())) {
                return Result.failed("您输入的标准商品名称" + request.getGoodsName() + "不正确");
            }
            if (null == request.getConvertUnit()) {
                if (null == request.getConvertNumber() || BigDecimal.ZERO.equals(request.getConvertNumber())) {
                    request.setConvertUnit(1);
                    request.setConvertNumber(BigDecimal.ONE);
                } else {
                    return Result.failed("转换单位为空");
                }
            } else {
                if (request.getConvertUnit() != 1 && request.getConvertUnit() != 2) {
                    return Result.failed("转换单位不正确");
                }
                if (null != request.getConvertNumber()) {
                    if (request.getConvertNumber().compareTo(BigDecimal.ZERO) <= 0) {
                        return Result.failed("转换系数必须大于0");
                    }
                    if (request.getConvertNumber().scale() > 2) {
                        return Result.failed("转换系数最多两位小数");
                    }
                    if (request.getConvertNumber().compareTo(MAX_CONVERT_NUMBER) > 0) {
                        return Result.failed("转换系数最大为99999.99");
                    }
                }
            }
            request.setGoodsSpecification(goodsInfoDTO.getGoodsSpec());
            request.setOpUserId(userInfo.getCurrentUserId());
        }
        Boolean b = flowEnterpriseGoodsMappingApi.batchUpdateById(requestList);
        if (!b) {
            return Result.failed("更新标准商品信息失败！");
        }
        Date currentDate = new Date();
        HashMap<String, String> resultMap = new HashMap<>();
        String currentDateFormat = DateUtil.format(currentDate, "yyyy.MM.dd");
        String startDayFormat = DateUtil.format(currentDate, "yyyy.MM") + ".01";
        resultMap.put("dayFlowRefreshMsg", "系统将自动刷新【" + startDayFormat + "-" + currentDateFormat + "】期间的日流向 ！");

        Date lastMonthTime = DateUtil.offsetMonth(currentDate, -1);
        int lastMonth = DateUtil.month(lastMonthTime) + 1;
        int lastYear = DateUtil.year(lastMonthTime);
        FlowMonthWashControlDTO controlDTO = flowMonthWashControlApi.getByYearAndMonth(lastYear, lastMonth);
        String controlMsg = "【" + lastYear + "." + lastMonth + "】月流向的商品对照清洗日程不存在，无法更新！";
        if (null != controlDTO) {
//            FlowMonthWashControlMappingStatusEnum controlStatusEnum = FlowMonthWashControlMappingStatusEnum.getByCode(controlDTO.getWashStatus());
            if(controlDTO.getWashStatus()==2) {
                List<FlowEnterpriseGoodsMappingDTO> refreshList = requestList.stream().map(request -> {
                    FlowEnterpriseGoodsMappingDTO mappingDTO = new FlowEnterpriseGoodsMappingDTO();
                    mappingDTO.setId(request.getId());
                    mappingDTO.setCrmGoodsCode(0L);
                    return mappingDTO;
                }).collect(Collectors.toList());
                flowEnterpriseGoodsMappingApi.sendRefreshGoodsFlowMq(refreshList);
                controlMsg = "系统将自动刷新【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】的月流向！";
            }else if(controlDTO.getWashStatus()==3){
                controlMsg = "【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】月流向的商品对照阶段已手动关闭，无法更新！";
            }else{
                controlMsg = "【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】月流向的商品对照阶段未开启，无法更新！";
            }
//            switch (controlStatusEnum) {
//                case IN_PROGRESS:
//                case MANUAL_OPEN:
//                    //发送mq
//                    List<FlowEnterpriseGoodsMappingDTO> refreshList = requestList.stream().map(request -> {
//                        FlowEnterpriseGoodsMappingDTO mappingDTO = new FlowEnterpriseGoodsMappingDTO();
//                        mappingDTO.setId(request.getId());
//                        mappingDTO.setCrmGoodsCode(0L);
//                        return mappingDTO;
//                    }).collect(Collectors.toList());
//                    flowEnterpriseGoodsMappingApi.sendRefreshGoodsFlowMq(refreshList);
//                    controlMsg = "系统将自动刷新【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】的月流向！";
//                    break;
//                case CLOSE:
//                    controlMsg = "【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】月流向的商品对照阶段已过期关闭，无法更新！";
//                    break;
//                case MANUAL_CLOSE:
//                    controlMsg = "【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】月流向的商品对照阶段已手动关闭，无法更新！";
//                    break;
//                default:
//                    controlMsg = "【" + controlDTO.getYear() + "." + controlDTO.getMonth() + "】月流向的商品对照阶段未开启，无法更新！";
//                    break;
//            }
        }
        resultMap.put("monthFlowRefreshMsg", controlMsg);
        return Result.success(resultMap);
    }

    private Page<FlowEnterpriseGoodsMappingVO> toPageVO(EsAggregationDTO<EsFlowEnterpriseGoodsMappingDTO> esAggregationDTO) {
        Page<FlowEnterpriseGoodsMappingVO> page = new Page<>();
        page.setTotal(esAggregationDTO.getTotal());
        page.setCurrent(esAggregationDTO.getCurrent());
        page.setSize(esAggregationDTO.getSize());
        LinkedList<FlowEnterpriseGoodsMappingVO> pageData = ListUtil.toLinkedList();
        if (CollectionUtil.isNotEmpty(esAggregationDTO.getData())) {
            List<Long> idList = esAggregationDTO.getData().stream().map(BaseDTO::getId).collect(Collectors.toList());
            List<FlowEnterpriseGoodsMappingDTO> dtoList = flowEnterpriseGoodsMappingApi.findByIds(idList);
            if (CollectionUtil.isNotEmpty(dtoList)) {
                Map<Long, FlowEnterpriseGoodsMappingDTO> dtoMap = dtoList.stream().collect(Collectors.toMap(FlowEnterpriseGoodsMappingDTO::getId, Function.identity()));
                //获取操作人
                List<Long> updateUserIds = dtoList.stream().map(FlowEnterpriseGoodsMappingDTO::getUpdateUser).filter(userId -> userId > 0).distinct().collect(Collectors.toList());
                Map<Long, String> userMap;
                if (CollectionUtil.isNotEmpty(updateUserIds)) {
                    List<UserDTO> userList = userApi.listByIds(updateUserIds);
                    userMap = userList.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
                } else {
                    userMap = MapUtil.newHashMap();
                }
                //获取实施负责人
                List<Long> crmEnterpriseIds = dtoList.stream().map(FlowEnterpriseGoodsMappingDTO::getCrmEnterpriseId).distinct().collect(Collectors.toList());
                Map<Long, String> implementerMap = erpClientApi.getInstallEmployeeByCrmEnterpriseIdList(crmEnterpriseIds);
                idList.forEach(id -> {
                    FlowEnterpriseGoodsMappingDTO mappingDTO = dtoMap.get(id);
                    if (null != mappingDTO) {
                        FlowEnterpriseGoodsMappingVO mappingVO = PojoUtils.map(mappingDTO, FlowEnterpriseGoodsMappingVO.class);
                        //操作人赋值
                        mappingVO.setOperator(userMap.getOrDefault(mappingDTO.getUpdateUser(), "admin"));
                        //实施负责人赋值
                        mappingVO.setImplementer(implementerMap.getOrDefault(mappingDTO.getCrmEnterpriseId(), ""));
                        pageData.add(mappingVO);
                    }
                });
            }
        }
        page.setRecords(pageData);
        return page;
    }
}
