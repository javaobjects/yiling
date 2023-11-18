package com.yiling.f2b.admin.procrelation.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.procrelation.form.OperCommonForm;
import com.yiling.f2b.admin.procrelation.form.QueryChannelPartnerPageForm;
import com.yiling.f2b.admin.procrelation.form.QueryDeliveryPageForm;
import com.yiling.f2b.admin.procrelation.form.QueryProcRelationDetailPageForm;
import com.yiling.f2b.admin.procrelation.form.QueryProcRelationGoodsOptionalPageListForm;
import com.yiling.f2b.admin.procrelation.form.QueryProcRelationPageForm;
import com.yiling.f2b.admin.procrelation.form.QueryProcRelationSnapshotDetailPageForm;
import com.yiling.f2b.admin.procrelation.form.SaveProcRelationForm;
import com.yiling.f2b.admin.procrelation.form.SaveProcRelationGoodsForm;
import com.yiling.f2b.admin.procrelation.form.UpdateProcRelationForm;
import com.yiling.f2b.admin.procrelation.vo.ProcChannelEntPageListItemVO;
import com.yiling.f2b.admin.procrelation.vo.ProcRelaYlCustPageListItemVO;
import com.yiling.f2b.admin.procrelation.vo.ProcRelationDetailVO;
import com.yiling.f2b.admin.procrelation.vo.ProcRelationGoodsListItemVO;
import com.yiling.f2b.admin.procrelation.vo.ProcRelationGoodsVO;
import com.yiling.f2b.admin.procrelation.vo.ProcRelationModifyRecordVO;
import com.yiling.f2b.admin.procrelation.vo.ProcRelationPageListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.pojo.vo.SimpleEnterpriseVO;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.user.common.enums.EnterpriseCustomerLineEnum;
import com.yiling.user.enterprise.api.CustomerApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseCustomerDTO;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.request.QueryCustomerPageListRequest;
import com.yiling.user.enterprise.dto.request.QueryEnterprisePageListRequest;
import com.yiling.user.enterprise.enums.EnterpriseChannelEnum;
import com.yiling.user.procrelation.api.ProcurementRelationApi;
import com.yiling.user.procrelation.api.ProcurementRelationGoodsApi;
import com.yiling.user.procrelation.api.ProcurementRelationSnapshotApi;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotDTO;
import com.yiling.user.procrelation.dto.ProcRelationSnapshotGoodsDTO;
import com.yiling.user.procrelation.dto.ProcurementRelationDTO;
import com.yiling.user.procrelation.dto.ProcurementRelationGoodsDTO;
import com.yiling.user.procrelation.dto.request.AddGoodsForProcRelationRequest;
import com.yiling.user.procrelation.dto.request.QueryProcRelationPageRequest;
import com.yiling.user.procrelation.dto.request.SaveProcRelationRequest;
import com.yiling.user.procrelation.dto.request.UpdateProcRelationRequest;
import com.yiling.user.procrelation.enums.PorcRelationDeliveryTypeEnum;
import com.yiling.user.procrelation.enums.ProcurementRelationStatusEnum;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: dexi.yao
 * @date: 2023-05-22
 */
@RestController
@RequestMapping("/procurementRelation")
@Api(tags = "建采管理")
@Slf4j
public class ProcurementRelationController extends BaseController {

    @DubboReference
    ProcurementRelationSnapshotApi relationSnapshotApi;
    @DubboReference
    ProcurementRelationApi procurementRelationApi;
    @DubboReference
    ProcurementRelationGoodsApi relationGoodsApi;
    @DubboReference
    EnterpriseApi enterpriseApi;
    @DubboReference
    PopGoodsApi popGoodsApi;
    @DubboReference
    CustomerApi customerApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "查询工业主体")
    @GetMapping("/mainPart/list")
    public Result<List<SimpleEnterpriseVO>> getMainPartList() {
        List<EnterpriseDTO> list = enterpriseApi.listMainPart();
        return Result.success(PojoUtils.map(list, SimpleEnterpriseVO.class));
    }

    @ApiOperation(value = "查询渠道商列表---用于建采管理页和建采管理新增页查询渠道商")
    @PostMapping("/queryChannelPartnerPage")
    public Result<Page<ProcRelaYlCustPageListItemVO>> queryChannelPartnerPage(@RequestBody @Valid QueryChannelPartnerPageForm form) {
        QueryCustomerPageListRequest request = PojoUtils.map(form, QueryCustomerPageListRequest.class);
        //设置渠道类型
        if (ObjectUtil.isNull(form.getChannelId()) || ObjectUtil.equal(0L, form.getChannelId())) {
            request.setChannelIds(ListUtil.toList(EnterpriseChannelEnum.LEVEL_1.getCode(), EnterpriseChannelEnum.LEVEL_2.getCode(), EnterpriseChannelEnum.KA.getCode(), EnterpriseChannelEnum.Z2P1.getCode()));
        } else {
            request.setChannelIds(ListUtil.toList(form.getChannelId()));
        }
        request.setEids(Collections.singletonList(Constants.YILING_EID));
        request.setUseLine(EnterpriseCustomerLineEnum.POP.getCode());
        Page<EnterpriseCustomerDTO> pageDTO = customerApi.pageList(request);

        Page<ProcRelaYlCustPageListItemVO> page = PojoUtils.map(pageDTO, ProcRelaYlCustPageListItemVO.class);
        //查询渠道类型
        if (CollUtil.isNotEmpty(page.getRecords())) {
            List<Long> eidList = page.getRecords().stream().map(ProcRelaYlCustPageListItemVO::getCustomerEid).collect(Collectors.toList());
            List<EnterpriseDTO> enterpriseDTOList = enterpriseApi.listByIds(eidList);
            if (CollUtil.isNotEmpty(enterpriseDTOList)) {
                Map<Long, Long> channelMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getChannelId));
                Map<Long, String> eNameMap = enterpriseDTOList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
                page.getRecords().forEach(e -> {
                    e.setChannelId(channelMap.getOrDefault(e.getCustomerEid(), 0L));
                    e.setCustomerName(eNameMap.getOrDefault(e.getCustomerEid(), ""));
                });
            }
        }
        return Result.success(page);
    }

    @ApiOperation(value = "查询配送商列表---用于建采管理页和建采管理新增页查询配送商")
    @PostMapping("/queryDeliveryPartnerPage")
    public Result<Page<ProcChannelEntPageListItemVO>> queryDeliveryPartnerPage(@RequestBody @Valid QueryDeliveryPageForm form) {
        QueryEnterprisePageListRequest request = PojoUtils.map(form, QueryEnterprisePageListRequest.class);
        if (NumberUtil.isLong(form.getName())){
            request.setId(NumberUtil.parseLong(form.getName()));
            request.setName(null);
        }
        //设置渠道类型
        request.setChannelIdList(ListUtil.toList(EnterpriseChannelEnum.INDUSTRY.getCode(),EnterpriseChannelEnum.INDUSTRY_DIRECT.getCode(), EnterpriseChannelEnum.LEVEL_1.getCode(), EnterpriseChannelEnum.LEVEL_2.getCode(), EnterpriseChannelEnum.Z2P1.getCode()));
        request.setPopFlag(1);
        Page<EnterpriseDTO> pageDTO = enterpriseApi.pageList(request);
        Page<ProcChannelEntPageListItemVO> page = PojoUtils.map(pageDTO, ProcChannelEntPageListItemVO.class);
        return Result.success(page);
    }

    @ApiOperation(value = "查询采购关系列表")
    @PostMapping("/queryProcRelationPage")
    public Result<Page<ProcRelationPageListItemVO>> queryProcRelationPage(@RequestBody @Valid QueryProcRelationPageForm form) {
        QueryProcRelationPageRequest request = PojoUtils.map(form, QueryProcRelationPageRequest.class);
        List<Long> channelEid = ListUtil.toList();
        //如果传了渠道商的渠道类型
        if (form.getChannelId() != null && form.getChannelId() != 0) {
            channelEid = enterpriseApi.listEidsByChannel(EnterpriseChannelEnum.getByCode(form.getChannelId()));
        } else {
            channelEid = null;
        }
        //如果传了渠道商id
        if (form.getChannelPartnerEid() != null && form.getChannelPartnerEid() != 0) {
            if (channelEid != null && !channelEid.contains(form.getChannelPartnerEid())) {
                return Result.success(form.getPage());
            } else {
                request.setChannelPartnerEid(ListUtil.toList(form.getChannelPartnerEid()));
            }
        } else {
            request.setChannelPartnerEid(channelEid);
        }
        Page<ProcurementRelationDTO> page = procurementRelationApi.queryProcRelationPage(request);
        Page<ProcRelationPageListItemVO> result = PojoUtils.map(page, ProcRelationPageListItemVO.class);
        if (CollUtil.isEmpty(result.getRecords())) {
            return Result.success(result);
        }
        //查询企业信息
        List<Long> eidList = result.getRecords().stream().map(ProcRelationPageListItemVO::getDeliveryEid).collect(Collectors.toList());
        eidList.addAll(result.getRecords().stream().map(ProcRelationPageListItemVO::getChannelPartnerEid).collect(Collectors.toList()));
        eidList = eidList.stream().distinct().collect(Collectors.toList());
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(eidList);

        Map<Long, String> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        Map<Long, Long> enterpriseChannelMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getChannelId));
        List<Long> userIds = result.getRecords().stream().map(ProcRelationPageListItemVO::getCreateUser).collect(Collectors.toList());
        userIds.addAll(result.getRecords().stream().map(ProcRelationPageListItemVO::getUpdateUser).collect(Collectors.toList()));
        userIds = userIds.stream().distinct().collect(Collectors.toList());
        List<UserDTO> list = userApi.listByIds(new ArrayList<>(userIds));
        Map<Long, String> userMap = list.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
        result.getRecords().forEach(e -> {
            e.setDeliveryName(enterpriseMap.getOrDefault(e.getDeliveryEid(), ""));
            e.setChannelPartnerName(enterpriseMap.getOrDefault(e.getChannelPartnerEid(), ""));
            e.setChannelPartnerChannelId(enterpriseChannelMap.getOrDefault(e.getChannelPartnerEid(), 0L));
            e.setCreateUserStr(userMap.getOrDefault(e.getCreateUser(), ""));
            e.setUpdateUserStr(userMap.getOrDefault(e.getUpdateUser(), ""));
        });
        return Result.success(result);
    }

    @ApiOperation(value = "新增采购关系", httpMethod = "POST")
    @PostMapping("/saveProcurementRelation")
    public Result<Long> saveProcurementRelation(@RequestBody @Valid SaveProcRelationForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        if (ObjectUtil.equal(PorcRelationDeliveryTypeEnum.THIRD_ENTERPRISE.getCode(), form.getDeliveryType())) {
            if (ObjectUtil.isNull(form.getDeliveryEid()) || ObjectUtil.equal(0L, form.getDeliveryEid())) {
                return Result.failed("配送商eid不能为空");
            }
            if (StrUtil.isBlank(form.getDeliveryName())) {
                return Result.failed("配送商名称不能为空");
            }
        }
        if (ObjectUtil.equal(form.getDeliveryEid(), form.getChannelPartnerEid())) {
            return Result.failed("渠道商和配送商不能相同");
        }
        SaveProcRelationRequest request = PojoUtils.map(form, SaveProcRelationRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setStartTime(DateUtil.beginOfDay(form.getStartTime()));
        request.setEndTime(DateUtil.offsetMillisecond(DateUtil.endOfDay(form.getEndTime()), -999));
        Long relationId = procurementRelationApi.saveProcurementRelation(request);
        return Result.success(relationId);
    }

    @ApiOperation(value = "更新采购关系", httpMethod = "POST")
    @PostMapping("/updateProcurementRelation")
    public Result<Long> updateProcurementRelation(@RequestBody @Valid UpdateProcRelationForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        if (ObjectUtil.equal(PorcRelationDeliveryTypeEnum.THIRD_ENTERPRISE.getCode(), form.getDeliveryType())) {
            if (ObjectUtil.isNull(form.getDeliveryEid()) || ObjectUtil.equal(0L, form.getDeliveryEid())) {
                return Result.failed("配送商eid不能为空");
            }
            if (StrUtil.isBlank(form.getDeliveryName())) {
                return Result.failed("配送商名称不能为空");
            }
        }
        if (ObjectUtil.equal(form.getDeliveryEid(), form.getChannelPartnerEid())) {
            return Result.failed("渠道商和配送商不能相同");
        }
        UpdateProcRelationRequest request = PojoUtils.map(form, UpdateProcRelationRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setOpTime(new Date());
        request.setStartTime(DateUtil.beginOfDay(form.getStartTime()));
        request.setEndTime(DateUtil.offsetMillisecond(DateUtil.endOfDay(form.getEndTime()), -999));
        Long relationId = procurementRelationApi.updateProcurementRelation(request);
        return Result.success(relationId);
    }

    @ApiOperation(value = "根据采购关系id查询可采商品列表")
    @PostMapping("/queryProcRelationGoodsOptionalPage")
    public Result<Page<ProcRelationGoodsListItemVO>> queryProcRelationGoodsOptionalPage(@RequestBody @Valid QueryProcRelationGoodsOptionalPageListForm form) {
        ProcurementRelationDTO relationDTO = procurementRelationApi.queryProcRelationById(form.getProcRelationId());
        if (ObjectUtil.isNull(relationDTO)) {
            return Result.failed("采购关系不存在");
        }
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(ListUtil.toList(relationDTO.getFactoryEid()));

        Page<ProcRelationGoodsListItemVO> page = PojoUtils.map(popGoodsApi.queryPopGoodsPageList(request), ProcRelationGoodsListItemVO.class);
        List<ProcurementRelationGoodsDTO> procRelationGoodsList = relationGoodsApi.queryGoodsByRelationId(form.getProcRelationId());
        if (CollUtil.isEmpty(procRelationGoodsList)) {
            return Result.success(page);
        }

        List<Long> procRelationGoodsIdList = procRelationGoodsList.stream().map(ProcurementRelationGoodsDTO::getGoodsId).collect(Collectors.toList());
        page.getRecords().forEach(e -> {
            //判断渠道商品是否已经被选了
            if (procRelationGoodsIdList.contains(e.getId())) {
                e.setGoodsSelStatus(true);
            }
        });
        return Result.success(page);
    }

    @ApiOperation(value = "根据采购关系id查询全部可采商品列表")
    @PostMapping("/queryFactoryGoodsList")
    public Result<List<ProcRelationGoodsListItemVO>> queryFactoryGoodsList(@RequestBody @Valid QueryProcRelationGoodsOptionalPageListForm form) {
        ProcurementRelationDTO relationDTO = procurementRelationApi.queryProcRelationById(form.getProcRelationId());
        if (ObjectUtil.isNull(relationDTO)) {
            return Result.failed("采购关系不存在");
        }
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        request.setEidList(ListUtil.toList(relationDTO.getFactoryEid()));

        List<ProcRelationGoodsListItemVO> list = PojoUtils.map(popGoodsApi.queryPopGoodsList(request), ProcRelationGoodsListItemVO.class);
        List<ProcurementRelationGoodsDTO> procRelationGoodsList = relationGoodsApi.queryGoodsByRelationId(form.getProcRelationId());
        if (CollUtil.isEmpty(procRelationGoodsList)) {
            return Result.success(list);
        }

        List<Long> procRelationGoodsIdList = procRelationGoodsList.stream().map(ProcurementRelationGoodsDTO::getGoodsId).collect(Collectors.toList());
        list.forEach(e -> {
            //判断渠道商品是否已经被选了
            if (procRelationGoodsIdList.contains(e.getId())) {
                e.setGoodsSelStatus(true);
            }
        });
        //仅返回可选的商品
        List<ProcRelationGoodsListItemVO> result = list.stream().filter(e -> !procRelationGoodsIdList.contains(e.getId())).collect(Collectors.toList());
        return Result.success(result);
    }

    @ApiOperation(value = "保存采购关系的商品", httpMethod = "POST")
    @PostMapping("/saveProcRelationGoods")
    public Result<Boolean> saveProcRelationGoods(@RequestBody @Valid SaveProcRelationGoodsForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> goodsIdList = form.getGoodsInfoList().stream().map(SaveProcRelationGoodsForm.GoodsInfo::getGoodsId).distinct().collect(Collectors.toList());
        if (ObjectUtil.notEqual(goodsIdList.size(), form.getGoodsInfoList().size())) {
            return Result.failed("商品列表存在重复");
        }
        List<AddGoodsForProcRelationRequest> requests = PojoUtils.map(form.getGoodsInfoList(), AddGoodsForProcRelationRequest.class);
        requests.forEach(e -> {
            e.setRelationId(form.getRelationId());
            e.setOpUserId(staffInfo.getCurrentUserId());
            e.setOpTime(new Date());
        });
        Boolean result = relationGoodsApi.saveGoodsForProcRelation(requests);
        return Result.success(result);
    }

    //    @ApiOperation(value = "根据采购关系id查询商品列表---用于编辑页展示")
    //    @PostMapping("/queryProcRelationGoodsList")
    //    public Result<List<ProcRelationGoodsVO>> queryProcRelationGoodsList(@RequestBody @Valid QueryProcRelationGoodsForm form) {
    //        List<ProcurementRelationGoodsDTO> list = relationGoodsApi.queryGoodsByRelationId(form.getRelationId());
    //        return Result.success(PojoUtils.map(list, ProcRelationGoodsVO.class));
    //    }

    @ApiOperation(value = "根据采购关系id查询采购关系详情")
    @PostMapping("/queryProcRelationDetail")
    public Result<ProcRelationDetailVO<ProcRelationGoodsVO>> queryProcRelationDetail(@RequestBody @Valid QueryProcRelationDetailPageForm form) {
        ProcurementRelationDTO relation = procurementRelationApi.queryProcRelationById(form.getRelationId());
        ProcRelationDetailVO result = PojoUtils.map(relation, ProcRelationDetailVO.class);
        if (ObjectUtil.isNull(result)) {
            return Result.success(result);
        }
        //查询企业信息
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(ListUtil.toList(relation.getDeliveryEid(), relation.getChannelPartnerEid()));
        Map<Long, String> enterpriseMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getName));
        Map<Long, Long> enterpriseChannelMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getChannelId));
        result.setDeliveryName(enterpriseMap.getOrDefault(result.getDeliveryEid(), ""));
        result.setChannelPartnerName(enterpriseMap.getOrDefault(result.getChannelPartnerEid(), ""));
        result.setChannelPartnerChannelId(enterpriseChannelMap.getOrDefault(result.getChannelPartnerEid(), 0L));
        //查询商品列表
        List<ProcurementRelationGoodsDTO> list = relationGoodsApi.queryGoodsByRelationId(form.getRelationId());
        List<ProcRelationGoodsVO> records = PojoUtils.map(list, ProcRelationGoodsVO.class);
        result.setGoodsList(records);
        return Result.success(result);
    }

    @ApiOperation(value = "根据采购关系id查询修改记录")
    @PostMapping("/queryProcRelationSnapshotList")
    public Result<List<ProcRelationModifyRecordVO>> queryProcRelationSnapshotList(@RequestBody @Valid OperCommonForm form) {
        List<ProcRelationSnapshotDTO> list = relationSnapshotApi.queryProcRelationSnapshot(form.getRelationId());
        return Result.success(PojoUtils.map(list, ProcRelationModifyRecordVO.class));
    }

    @ApiOperation(value = "根据采购关系版本id查询快照详情")
    @PostMapping("/queryProcRelationGoodsSnapshotDetail")
    public Result<ProcRelationDetailVO<ProcRelationGoodsVO>> queryProcRelationGoodsSnapshotDetail(@RequestBody @Valid QueryProcRelationSnapshotDetailPageForm form) {
        ProcRelationSnapshotDTO relationSnapshot = relationSnapshotApi.queryProcRelationSnapshotByVersionId(form.getVersionId());
        ProcRelationDetailVO result = PojoUtils.map(relationSnapshot, ProcRelationDetailVO.class);
        if (ObjectUtil.isNull(result)) {
            return Result.success(result);
        }
        //查询企业信息
        List<EnterpriseDTO> enterpriseList = enterpriseApi.listByIds(ListUtil.toList(relationSnapshot.getDeliveryEid(), relationSnapshot.getChannelPartnerEid()));
        Map<Long, Long> enterpriseChannelMap = enterpriseList.stream().collect(Collectors.toMap(EnterpriseDTO::getId, EnterpriseDTO::getChannelId));
        result.setChannelPartnerChannelId(enterpriseChannelMap.getOrDefault(result.getChannelPartnerEid(), 0L));
        //查询商品列表
        List<ProcRelationSnapshotGoodsDTO> list = relationSnapshotApi.queryProcGoodsSnapshotList(relationSnapshot.getId());
        List<ProcRelationGoodsVO> records = PojoUtils.map(list, ProcRelationGoodsVO.class);
        result.setGoodsList(records);
        return Result.success(result);
    }


    @ApiOperation(value = "根据采购关系id停用")
    @PostMapping("/closeProcRelationById")
    public Result<Boolean> closeProcRelationById(@RequestBody @Valid OperCommonForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        ProcurementRelationDTO relation = procurementRelationApi.queryProcRelationById(form.getRelationId());
        if (ObjectUtil.isNull(relation)) {
            return Result.failed("采购关系不存在");
        }
        if (ObjectUtil.notEqual(relation.getProcRelationStatus(), ProcurementRelationStatusEnum.IN_PROGRESS.getCode())) {
            return Result.failed("只有进行中的可以停用");
        }
        Boolean isSuccess = procurementRelationApi.closeProcRelationById(form.getRelationId(), staffInfo.getCurrentUserId());
        return Result.success(isSuccess);
    }

    @ApiOperation(value = "根据采购关系id删除")
    @PostMapping("/deleteProcRelationById")
    public Result<Boolean> deleteProcRelationById(@RequestBody @Valid OperCommonForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        ProcurementRelationDTO relation = procurementRelationApi.queryProcRelationById(form.getRelationId());
        if (ObjectUtil.isNull(relation)) {
            return Result.failed("采购关系不存在");
        }
        if (ObjectUtil.notEqual(relation.getProcRelationStatus(), ProcurementRelationStatusEnum.NOT_STARTED.getCode())) {
            return Result.failed("只有未开始的可以删除");
        }
        Boolean isSuccess = procurementRelationApi.deleteProcRelationById(form.getRelationId(), staffInfo.getCurrentUserId());
        return Result.success(isSuccess);
    }


}
