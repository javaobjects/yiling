package com.yiling.sjms.wash.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.dubbo.config.annotation.DubboReference;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmEnterpriseApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.wash.api.FlowGoodsBatchSafeApi;
import com.yiling.dataflow.wash.api.FlowInTransitOrderApi;
import com.yiling.dataflow.wash.api.FlowMonthWashControlApi;
import com.yiling.dataflow.wash.dto.FlowGoodsBatchSafeDTO;
import com.yiling.dataflow.wash.dto.FlowMonthWashControlDTO;
import com.yiling.dataflow.wash.dto.request.QueryFlowGoodsBatchSafePageRequest;
import com.yiling.dataflow.wash.enums.FlowMonthWashControlMappingStatusEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.wash.form.QueryFlowGoodsBatchSafePageForm;
import com.yiling.sjms.wash.vo.FlowGoodsBatchSafePageDetailVO;
import com.yiling.sjms.wash.vo.FlowGoodsBatchSafePageVO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author: houjie.sun
 * @date: 2023/3/8
 */
@RestController
@RequestMapping("/flowWash/goodsBatchSafe")
@Api(tags = "安全库存")
public class FlowGoodsBatchSafeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FlowWashReportController.class);

    @DubboReference
    private FlowGoodsBatchSafeApi flowGoodsBatchSafeApi;
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

    @ApiOperation(value = "安全库存列表")
    @PostMapping("/listPage")
    public Result<FlowGoodsBatchSafePageVO<FlowGoodsBatchSafePageDetailVO>> listPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryFlowGoodsBatchSafePageForm form) {
        // 数据权限
        Map<String, List<String>> map = flowInTransitOrderApi.buildUserDatascope(userInfo.getCurrentUserCode(), "安全库存-列表");
        String datascopeType = map.get("datascopeType").get(0);

        FlowGoodsBatchSafePageVO<FlowGoodsBatchSafePageDetailVO> page = new FlowGoodsBatchSafePageVO<>();
        QueryFlowGoodsBatchSafePageRequest request = PojoUtils.map(form, QueryFlowGoodsBatchSafePageRequest.class);
        // 构建安全库存上报时间信息
        //        buildInTransitSubmitInfo(page);

        // 根据规格查询标准商品id
        buildCrmGoodsCodeList(form, request);

        // 部分权限
        if (ObjectUtil.equal("1", datascopeType)) {
            List<Long> crmIdList = Convert.toList(Long.class, map.get("crmIds"));
            List<String> crmProvinceCodeList = map.get("provinceCodes");
            request.setCrmEnterpriseIdList(crmIdList);
            request.setCrmProvinceCodeList(crmProvinceCodeList);
        }

        Page<FlowGoodsBatchSafeDTO> dtoPage = flowGoodsBatchSafeApi.listPage(request);
        if (ObjectUtil.isNull(dtoPage) || CollUtil.isEmpty(dtoPage.getRecords())) {
            return Result.success(page);
        }
        page.setCurrent(dtoPage.getCurrent());
        page.setSize(dtoPage.getSize());
        page.setTotal(dtoPage.getTotal());

        // 操作人信息
        Map<Long, String> userNameMap = getUserNameMap(dtoPage.getRecords());

        // 构建分页列表
        List<FlowGoodsBatchSafePageDetailVO> list = new ArrayList<>();
        for (FlowGoodsBatchSafeDTO record : dtoPage.getRecords()) {
            buildPageRecords(record, list, userNameMap);
        }
        page.setRecords(list);

        return Result.success(page);
    }

    private Map<Long, String> getUserNameMap(List<FlowGoodsBatchSafeDTO> list) {
        Map<Long, String> userNameMap = new HashMap<>();
        Set<Long> opUserIdSet = new HashSet<>();
        List<Long> createUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getCreateUser()) && o.getCreateUser() > 0).map(FlowGoodsBatchSafeDTO::getCreateUser).distinct().collect(Collectors.toList());
        List<Long> updateUserIdList = list.stream().filter(o -> ObjectUtil.isNotNull(o.getUpdateUser()) && o.getUpdateUser() > 0).map(FlowGoodsBatchSafeDTO::getUpdateUser).distinct().collect(Collectors.toList());
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

    private void buildInTransitSubmitInfo(FlowGoodsBatchSafePageVO<FlowGoodsBatchSafePageDetailVO> page) {
        // 获取是否可以提交终端库存
        FlowMonthWashControlDTO flowMonthWashControlDTO = flowInTransitOrderApi.getGoodsBatchTime();
        if (ObjectUtil.isNull(flowMonthWashControlDTO) || ObjectUtil.isNull(flowMonthWashControlDTO.getId()) || 0 == flowMonthWashControlDTO.getId().intValue()) {
            page.setSubmitFlag(false);
        } else {
            boolean submitFlag = false;
            Integer goodsBatchStatus = flowMonthWashControlDTO.getWashStatus();
            if (ObjectUtil.isNotNull(goodsBatchStatus) && 1==goodsBatchStatus) {
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

    private void buildPageRecords(FlowGoodsBatchSafeDTO record, List<FlowGoodsBatchSafePageDetailVO> list, Map<Long, String> userNameMap) {
        if (ObjectUtil.isNull(record)) {
            return;
        }
        FlowGoodsBatchSafePageDetailVO vo = new FlowGoodsBatchSafePageDetailVO();
        vo.setId(record.getId());
        vo.setCrmEnterpriseId(record.getCrmEnterpriseId());
        vo.setName(record.getName());
        vo.setCrmGoodsCode(record.getCrmGoodsCode());
        vo.setCrmGoodsName(record.getCrmGoodsName());
        vo.setCrmGoodsSpecifications(record.getCrmGoodsSpecifications());
        vo.setGbNumber(record.getGbNumber());
        vo.setGbUnit(record.getGbUnit());
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

    private void buildCrmGoodsCodeList(QueryFlowGoodsBatchSafePageForm form, QueryFlowGoodsBatchSafePageRequest request) {
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

}
