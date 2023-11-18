package com.yiling.data.center.admin.goods.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.common.web.goods.utils.PictureUrlUtils;
import com.yiling.data.center.admin.goods.form.GoodsLineForm;
import com.yiling.data.center.admin.goods.form.QueryGoodsAuditRecordPageForm;
import com.yiling.data.center.admin.goods.form.QueryGoodsPageListForm;
import com.yiling.data.center.admin.goods.form.SaveOrUpdateGoodsForm;
import com.yiling.data.center.admin.goods.form.StandardGoodsInfoForm;
import com.yiling.data.center.admin.goods.form.UpdateGoodsLineForm;
import com.yiling.data.center.admin.goods.vo.GoodsAuditListVO;
import com.yiling.data.center.admin.goods.vo.GoodsDetailsVO;
import com.yiling.data.center.admin.goods.vo.GoodsLineVO;
import com.yiling.data.center.admin.goods.vo.GoodsListItemPageVO;
import com.yiling.data.center.admin.goods.vo.GoodsListItemVO;
import com.yiling.data.center.admin.goods.vo.StandardGoodsAllInfoVO;
import com.yiling.data.center.admin.goods.vo.StandardGoodsCategoryInfoAllVO;
import com.yiling.data.center.admin.goods.vo.StandardGoodsInfoVO;
import com.yiling.data.center.admin.goods.vo.StandardGoodsPicVO;
import com.yiling.data.center.admin.goods.vo.StandardSpecificationInfoVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.enums.ResultCode;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.goods.inventory.api.InventoryApi;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.GoodsAuditApi;
import com.yiling.goods.medicine.bo.GoodsLineBO;
import com.yiling.goods.medicine.bo.GoodsListItemBO;
import com.yiling.goods.medicine.dto.GoodsAuditDTO;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.GoodsFullDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsAuditRecordPageRequest;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateGoodsRequest;
import com.yiling.goods.medicine.dto.request.UpdateGoodsLineRequest;
import com.yiling.goods.medicine.enums.GoodsLineStatusEnum;
import com.yiling.goods.medicine.enums.GoodsStatusEnum;
import com.yiling.goods.standard.api.StandardGoodsApi;
import com.yiling.goods.standard.api.StandardGoodsCategoryApi;
import com.yiling.goods.standard.api.StandardGoodsPicApi;
import com.yiling.goods.standard.api.StandardGoodsSpecificationApi;
import com.yiling.goods.standard.dto.StandardGoodsAllInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsCategoryInfoAllDTO;
import com.yiling.goods.standard.dto.StandardGoodsInfoDTO;
import com.yiling.goods.standard.dto.StandardGoodsPicDTO;
import com.yiling.goods.standard.dto.StandardGoodsSpecificationDTO;
import com.yiling.goods.standard.dto.StandardSpecificationInfoDTO;
import com.yiling.goods.standard.dto.request.StandardGoodsInfoRequest;
import com.yiling.goods.standard.enums.StandardGoodsStatusEnum;
import com.yiling.user.agreement.api.AgreementGoodsApi;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.enterprise.dto.EnterpriseDTO;
import com.yiling.user.enterprise.dto.EnterprisePlatformDTO;
import com.yiling.user.enterprise.enums.ErpSyncLevelEnum;
import com.yiling.user.system.api.StaffApi;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentStaffInfo;
import com.yiling.user.system.dto.UserDTO;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author dexi.yao
 * @date 2021-05-20
 */
@RestController
@Api(tags = "商品管理模块")
@RequestMapping("/data/goods")
public class GoodsController {

    @Autowired
    PictureUrlUtils pictureUrlUtils;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    UserApi userApi;

    @DubboReference
    GoodsAuditApi goodsAuditApi;

    @DubboReference
    InventoryApi inventoryApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    StandardGoodsApi standardGoodsApi;

    @DubboReference
    AgreementGoodsApi agreementGoodsApi;

    @DubboReference
    StandardGoodsCategoryApi standardGoodsCategoryApi;

    @DubboReference
    StandardGoodsPicApi standardGoodsPicApi;

    @DubboReference
    StaffApi staffApi;

    @DubboReference
    StandardGoodsSpecificationApi standardGoodsSpecificationApi;


    @ApiOperation(value = "商品列表", httpMethod = "POST")
    @PostMapping("/list")
    public Result<GoodsListItemPageVO<GoodsListItemVO>> list(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsPageListForm form) {
        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
            request.setEidList(list);
        } else {
            request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
        }
        Page<GoodsListItemBO> page = goodsApi.queryPageListGoods(request);
        if (page != null) {
            GoodsListItemPageVO<GoodsListItemVO> newPage = new GoodsListItemPageVO();
            List<GoodsListItemVO> goodsListItemVOList = PojoUtils.map(page.getRecords(), GoodsListItemVO.class);
            goodsListItemVOList.forEach(e -> {
                e.setPic(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
            newPage.setRecords(goodsListItemVOList);
            newPage.setSize(page.getSize());
            newPage.setCurrent(page.getCurrent());
            newPage.setTotal(page.getTotal());
            EnterpriseDTO customerEnterpriseDTO = enterpriseApi.getById(staffInfo.getCurrentEid());
            newPage.setErpFlag(customerEnterpriseDTO.getErpSyncLevel() > 0 ? 1 : ErpSyncLevelEnum.NOT_DOCKING.getCode());
            return Result.success(newPage);
        }
        return Result.failed(ResultCode.FAILED);
    }

    @ApiOperation(value = "查询商品审核记录列表", httpMethod = "POST")
    @PostMapping("/auditList")
    public Result<Page<GoodsAuditListVO>> auditList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid QueryGoodsAuditRecordPageForm form) {
        QueryGoodsAuditRecordPageRequest request = PojoUtils.map(form, QueryGoodsAuditRecordPageRequest.class);
        Page<GoodsAuditDTO> list = goodsAuditApi.queryPageListGoodsAuditRecord(request);
        Page<GoodsAuditListVO> page = PojoUtils.map(list, GoodsAuditListVO.class);
        List<Long> userIds = list.getRecords().stream().map(e -> e.getUpdateUser()).distinct().filter(p -> p.intValue() > 0).collect(Collectors.toList());
        Map<Long, UserDTO> userDTOMap = MapUtil.newHashMap();
        if (CollUtil.isNotEmpty(userIds)) {
            List<UserDTO> userDTOList = userApi.listByIds(userIds);
            userDTOMap = userDTOList.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity()));
        }
        Map<Long, UserDTO> finalUserDTOMap = userDTOMap;
        page.getRecords().forEach(e -> {
            e.setAuditUser(finalUserDTOMap.get(e.getUpdateUser()) != null ? finalUserDTOMap.get(e.getUpdateUser()).getName() : "");
        });
        return Result.success(page);
    }

    @ApiOperation(value = "批量设置产品线", httpMethod = "POST")
    @PostMapping("/updateGoodsLine")
    @Log(title = "批量设置产品线", businessType = BusinessTypeEnum.UPDATE)
    public Result<Boolean> updateGoodsLine(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid UpdateGoodsLineForm form) {
        List<Long> eidList = ListUtil.empty();
        List<Long> popEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
            eidList = popEids;
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
        }

        List<Long> goodsIds = form.getGoodsLineList().stream().map(e -> e.getGoodsId()).collect(Collectors.toList());

        //查询原始数据
        List<GoodsDTO> goodsInfoDTOList = goodsApi.batchQueryInfo(goodsIds);
        Map<Long, GoodsDTO> goodsDTOMap = goodsInfoDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        //只能修改自己的商品
        for (GoodsDTO goodsDTO : goodsInfoDTOList) {
            if (!eidList.contains(goodsDTO.getEid())) {
                return Result.failed("只能修改自己的商品信息");
            }

            //判断商品是否已经在平台上禁止销售
            if (goodsDTO.getStandardId() != null && goodsDTO.getStandardId() != 0) {
                StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(goodsDTO.getStandardId());
                if (standardGoodsByIdInfoDTO == null || standardGoodsByIdInfoDTO.getBaseInfo().getGoodsStatus().equals(StandardGoodsStatusEnum.FORBID.getCode())) {
                    return Result.failed("该商品已经在平台禁止销售");
                }
            }
        }

        //判断是否开通pop
        if (CollUtil.isNotEmpty(form.getGoodsLineList())) {
            List<Long> sellSpecificationsId = goodsInfoDTOList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
            List<GoodsDTO> goodsSpecificationsList = goodsApi.findGoodsBySellSpecificationsIdAndEid(sellSpecificationsId, popEids);
            List<Long> specificationsIdList = goodsSpecificationsList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
            for (GoodsLineForm goodsLineForm : form.getGoodsLineList()) {
                GoodsDTO goodsDTO = goodsDTOMap.get(goodsLineForm.getGoodsId());
                if (goodsLineForm.getPopFlag() != null && goodsLineForm.getPopFlag() == 1) {
                    if (!popEids.contains(goodsDTO.getEid()) && !specificationsIdList.contains(goodsDTO.getSellSpecificationsId())) {
                        return Result.failed("该商品不能开通POP产品线");
                    }
                }
            }
        }
        UpdateGoodsLineRequest request = PojoUtils.map(form, UpdateGoodsLineRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());
        request.setPopEidList(popEids);
        return Result.success(goodsApi.updateGoodsLine(request));
    }

    @ApiOperation(value = "商品明细", httpMethod = "GET")
    @GetMapping("/detail")
    public Result<GoodsDetailsVO> detail(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam Long goodsId) {
        GoodsDetailsVO resultData = null;
        GoodsFullDTO goodsFullDTO = goodsApi.queryFullInfo(goodsId);
        if (goodsFullDTO != null) {
            resultData = PojoUtils.map(goodsFullDTO, GoodsDetailsVO.class);
            //图片转换
            if (goodsFullDTO.getStandardGoodsAllInfo() != null) {
                StandardGoodsAllInfoVO standardGoodsAllInfoVO = resultData.getStandardGoodsAllInfo();
                if (CollUtil.isNotEmpty(standardGoodsAllInfoVO.getPicBasicsInfoList())) {
                    standardGoodsAllInfoVO.getPicBasicsInfoList().forEach(e -> {
                        e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
                    });
                }
            }

            //获取驳回原因
            GoodsAuditDTO goodsAuditDTO = goodsAuditApi.getGoodsAuditByGidAndAuditStatus(goodsId);
            if (goodsAuditDTO != null) {
                resultData.setRejectMessage(goodsAuditDTO.getRejectMessage());
            }

            //获取产品线
            GoodsLineBO goodsLineBO = goodsApi.getGoodsLineByGoodsIds(ListUtil.toList(goodsId)).get(0);
            GoodsLineVO lineVO = new GoodsLineVO();
            lineVO.setGoodsId(goodsId);
            if(GoodsLineStatusEnum.ENABLED.getCode().equals(goodsLineBO.getMallStatus())){
                lineVO.setMallFlag(1);
            }else {
                lineVO.setMallFlag(0);
            }
            if(GoodsLineStatusEnum.ENABLED.getCode().equals(goodsLineBO.getPopStatus())){
                lineVO.setPopFlag(1);
            }else {
                lineVO.setPopFlag(0);
            }
            resultData.setGoodsLineVO(lineVO);

            return Result.success(resultData);
        } else {
            return Result.failed("商品不存在");
        }
    }

    @ApiOperation(value = "新增或者编辑商品", httpMethod = "POST")
    @PostMapping("/saveOrUpdate")
    @Log(title = "新增或者编辑商品", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> saveOrUpdate(@RequestBody @Valid SaveOrUpdateGoodsForm form, @CurrentUser CurrentStaffInfo staffInfo) {
        List<Long> eidList = ListUtil.empty();
        Long eid = staffInfo.getCurrentEid();
        List<Long> popEids = enterpriseApi.listSubEids(Constants.YILING_EID);
        if (staffInfo.getYilingFlag()) {
            eid = form.getEid();
            eidList = popEids;
        } else {
            eidList = Arrays.asList(staffInfo.getCurrentEid());
            form.setEid(staffInfo.getCurrentEid());
        }
        SaveOrUpdateGoodsRequest request = PojoUtils.map(form, SaveOrUpdateGoodsRequest.class);
        request.setOpUserId(staffInfo.getCurrentUserId());

        if (form.getId() != null && form.getId() != 0) {
            //查询原始数据
            GoodsDTO goodsInfoDTO = goodsApi.queryInfo(form.getId());

            //只能修改自己的商品
            if (!eidList.contains(goodsInfoDTO.getEid())) {
                return Result.failed("只能修改自己的商品信息");
            }

            //判断商品是否已经在平台上禁止销售
            if (goodsInfoDTO.getStandardId() != null && goodsInfoDTO.getStandardId() != 0) {
                StandardGoodsAllInfoDTO standardGoodsByIdInfoDTO = standardGoodsApi.getStandardGoodsById(goodsInfoDTO.getStandardId());
                if (standardGoodsByIdInfoDTO == null || standardGoodsByIdInfoDTO.getBaseInfo().getGoodsStatus().equals(StandardGoodsStatusEnum.FORBID.getCode())) {
                    return Result.failed("该商品已经在平台禁止销售");
                }
            }

            //产品线
            GoodsLineForm goodsLineForm = form.getGoodsLineInfo();
            if (goodsLineForm != null && goodsLineForm.getPopFlag() != null && goodsLineForm.getPopFlag() == 1) {
                if (goodsInfoDTO.getAuditStatus().equals(GoodsStatusEnum.AUDIT_PASS.getCode())) {
                    List<GoodsDTO> goodsSpecificationsList = goodsApi.findGoodsBySellSpecificationsIdAndEid(Arrays.asList(goodsInfoDTO.getSellSpecificationsId()), popEids);
                    List<Long> specificationsIdList = goodsSpecificationsList.stream().map(e -> e.getSellSpecificationsId()).collect(Collectors.toList());
                    if (!popEids.contains(goodsInfoDTO.getEid()) && !specificationsIdList.contains(goodsInfoDTO.getSellSpecificationsId())) {
                        return Result.failed("该商品不能开通POP产品线");
                    }
                }
            }
        } else {
            //新增商品，如果选择到规格的情况
            if (request.getSellSpecificationsId() != null && request.getSellSpecificationsId() != 0) {
                Long gid = goodsApi.queryInfoBySpecIdAndEid(request.getSellSpecificationsId(), eid);
                if (gid > 0) {
                    return Result.failed("该商品已经存在");
                } else {
                    request.setAuditStatus(GoodsStatusEnum.AUDIT_PASS.getCode());
                    request.setRepGoodsId(0L);
                }
            }
        }
        if (form.getEid() != null && form.getEid() != 0) {
            EnterpriseDTO enterpriseDTO = enterpriseApi.getById(form.getEid());
            boolean erpFlag = enterpriseDTO.getErpSyncLevel()>0 && (request.getId() == null || request.getId() == 0);
            if(erpFlag){
                return Result.failed("当前企业已对接ERP如新增商品请在ERP内操作");
            }
            request.setEname(enterpriseDTO.getName());
        }
        //更新商品
        request.setPopEidList(popEids);
        return Result.success(goodsApi.saveGoods(request) > 0);
    }

    /**
     * 获取药品分类列表
     *
     * @return
     */
    @ApiOperation(value = "获取药品分类列表")
    @PostMapping("/getAll")
    public Result<CollectionObject<List<StandardGoodsCategoryInfoAllVO>>> getAllCateInfo() {
        List<StandardGoodsCategoryInfoAllDTO> allCateInfo = standardGoodsCategoryApi.getAllCateInfo();
        List<StandardGoodsCategoryInfoAllVO> voList = PojoUtils.map(allCateInfo, StandardGoodsCategoryInfoAllVO.class);
        return Result.success(new CollectionObject(voList));
    }

    @ApiOperation(value = "获取标准商品列表")
    @PostMapping("/standard/goodsList")
    public Result<Page<StandardGoodsInfoVO>> goodsList(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody StandardGoodsInfoForm form) {
        StandardGoodsInfoRequest request = PojoUtils.map(form, StandardGoodsInfoRequest.class);
        Page<StandardGoodsInfoDTO> goodsInfo = standardGoodsApi.getStandardGoodsInfo(request);
        Page<StandardGoodsInfoVO> page = PojoUtils.map(goodsInfo, StandardGoodsInfoVO.class);
        List<Long> idList = page.getRecords().stream().map(StandardGoodsInfoVO::getId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            List<StandardGoodsSpecificationDTO> standardGoodsSpecificationList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(idList);
            Map<Long, List<StandardGoodsSpecificationDTO>> groupBy = standardGoodsSpecificationList.stream().collect(Collectors.groupingBy(StandardGoodsSpecificationDTO::getStandardId));
            page.getRecords().forEach(e -> {
                if (groupBy.get(e.getId()) != null) {
                    e.setSpecificationsCount(groupBy.get(e.getId()).size());
                }
                e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
        }
        return Result.success(page);
    }

    @ApiOperation(value = "获取标准商品规格信息")
    @GetMapping("/standard/specificationList")
    public Result<CollectionObject<StandardSpecificationInfoVO>> getStandardSpecificationStandardId(@CurrentUser CurrentStaffInfo staffInfo,@RequestParam("id") Long id) {
        List<Long> list = new ArrayList<Long>() {{
            add(id);
        }};
        List<StandardGoodsSpecificationDTO> standardGoodsSpecificationList = standardGoodsSpecificationApi.getListStandardGoodsSpecification(list);
        List<StandardSpecificationInfoVO> result = PojoUtils.map(standardGoodsSpecificationList, StandardSpecificationInfoVO.class);
        result.forEach(e -> {
            List<StandardGoodsPicDTO> standardGoodsPicDTOList = standardGoodsPicApi.getStandardGoodsPic(e.getStandardId(), e.getId());
            List<StandardGoodsPicVO> standardGoodsPicVOList=PojoUtils.map(standardGoodsPicDTOList, StandardGoodsPicVO.class);
            for(StandardGoodsPicVO standardGoodsPicVO:standardGoodsPicVOList){
                standardGoodsPicVO.setPicUrl(pictureUrlUtils.getGoodsPicUrl(standardGoodsPicVO.getPic()));
            }
            e.setPicInfoList(standardGoodsPicVOList);
        });
        return Result.success(new CollectionObject(result));
    }

    /**
     * 获取客户的产品线
     *
     * @return
     */
    @ApiOperation(value = "通过客户ID获取开通产品线")
    @GetMapping("/getGoodsLine")
    public Result<GoodsLineVO> getGoodsLine(@CurrentUser CurrentStaffInfo staffInfo) {
        EnterprisePlatformDTO enterprisePlatformDTO = enterpriseApi.getEnterprisePlatform(staffInfo.getCurrentEid());
        GoodsLineVO goodsLineVO = PojoUtils.map(enterprisePlatformDTO, GoodsLineVO.class);
        return Result.success(goodsLineVO);
    }


    @ApiOperation(value = "通过商品销售规格ID获取标准库信息")
    @GetMapping("/standard/standardGoods")
    public Result<StandardGoodsAllInfoVO> getStandardGoodsBySpecificationId(@RequestParam("specificationId") Long specificationId, @RequestParam("standardId") Long standardId) {
        StandardGoodsAllInfoDTO standardGoodsAllInfoDTO = null;
        if (specificationId != null && specificationId != 0) {
            StandardGoodsSpecificationDTO standardGoodsSpecification = standardGoodsSpecificationApi.getStandardGoodsSpecification(specificationId);
            standardId = standardGoodsSpecification.getStandardId();
            standardGoodsAllInfoDTO = standardGoodsApi.getStandardGoodsById(standardId);
            //赋值标准库图片和规格信息
            List<StandardSpecificationInfoDTO> standardSpecificationInfoDTOList = standardGoodsAllInfoDTO.getSpecificationInfo();
            for (StandardSpecificationInfoDTO standardSpecificationInfoDTO : standardSpecificationInfoDTOList) {
                if (standardSpecificationInfoDTO.getId().equals(specificationId)) {
                    standardGoodsAllInfoDTO.setSpecificationInfo(PojoUtils.map(Arrays.asList(standardSpecificationInfoDTO), StandardSpecificationInfoDTO.class));
                    if (CollUtil.isNotEmpty(standardSpecificationInfoDTO.getPicInfoList())) {
                        standardGoodsAllInfoDTO.setPicBasicsInfoList(standardSpecificationInfoDTO.getPicInfoList());
                    }
                }
            }
        } else {
            standardGoodsAllInfoDTO = standardGoodsApi.getStandardGoodsById(standardId);
        }
        StandardGoodsAllInfoVO standardGoodsAllInfoVO = PojoUtils.map(standardGoodsAllInfoDTO, StandardGoodsAllInfoVO.class);
        if (CollUtil.isNotEmpty(standardGoodsAllInfoVO.getPicBasicsInfoList())) {
            standardGoodsAllInfoVO.getPicBasicsInfoList().forEach(e -> {
                e.setPicUrl(pictureUrlUtils.getGoodsPicUrl(e.getPic()));
            });
        }
        return Result.success(standardGoodsAllInfoVO);
    }

}
