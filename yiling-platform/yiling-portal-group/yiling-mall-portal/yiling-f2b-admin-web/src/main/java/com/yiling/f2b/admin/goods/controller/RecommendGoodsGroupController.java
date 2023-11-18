package com.yiling.f2b.admin.goods.controller;


import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.f2b.admin.goods.form.DeleteRecommendGoodsGroupForm;
import com.yiling.f2b.admin.goods.form.QueryRecommendGoodsGroupPageListForm;
import com.yiling.f2b.admin.goods.form.QueryRecommendGroupPopGoodsPageListForm;
import com.yiling.f2b.admin.goods.form.SaveOrUpdateRecommendGoodsGroupForm;
import com.yiling.f2b.admin.goods.form.SaveRecommendGoodsGroupRelationForm;
import com.yiling.f2b.admin.goods.vo.RecommendGoodsGroupRelationVO;
import com.yiling.f2b.admin.goods.vo.RecommendGoodsGroupVO;
import com.yiling.f2b.admin.goods.vo.RecommendGroupGoodsListItemVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.goods.medicine.api.GoodsApi;
import com.yiling.goods.medicine.api.PopGoodsApi;
import com.yiling.goods.medicine.api.RecommendGoodsGroupApi;
import com.yiling.goods.medicine.dto.GoodsDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupDTO;
import com.yiling.goods.medicine.dto.RecommendGoodsGroupRelationDTO;
import com.yiling.goods.medicine.dto.request.QueryGoodsPageListRequest;
import com.yiling.goods.medicine.dto.request.QueryRecommendGoodsGroupPageRequest;
import com.yiling.goods.medicine.dto.request.SaveOrUpdateRecommendGoodsGroupRequest;
import com.yiling.user.enterprise.api.EnterpriseApi;
import com.yiling.user.system.bo.CurrentStaffInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author shichen
 * @类名 RecommendGoodsGroupController
 * @描述
 * @创建时间 2023/1/9
 * @修改人 shichen
 * @修改时间 2023/1/9
 **/
@RestController
@Api(tags = "推荐商品组模块")
@RequestMapping("recommendGoodsGroup")
public class RecommendGoodsGroupController extends BaseController {
    @DubboReference
    RecommendGoodsGroupApi recommendGoodsGroupApi;

    @DubboReference
    GoodsApi goodsApi;

    @DubboReference
    EnterpriseApi enterpriseApi;

    @DubboReference
    PopGoodsApi popGoodsApi;

    @ApiOperation(value = "推荐商品组分页", httpMethod = "POST")
    @PostMapping("/groupPage")
    public Result<Page<RecommendGoodsGroupVO>> groupPage(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody QueryRecommendGoodsGroupPageListForm form){
        Long curentEid = staffInfo.getCurrentEid();
        QueryRecommendGoodsGroupPageRequest request = PojoUtils.map(form, QueryRecommendGoodsGroupPageRequest.class);
        request.setEid(curentEid);
        Page<RecommendGoodsGroupDTO> page = recommendGoodsGroupApi.queryGroupPage(request);
        Page<RecommendGoodsGroupVO> groupVOPage = PojoUtils.map(page, RecommendGoodsGroupVO.class);
        List<Long> goodsIdList= ListUtil.toList();
        groupVOPage.getRecords().forEach(group->{
            if(CollectionUtil.isNotEmpty(group.getRelationList())){
                List<Long> groupGoodsIds = group.getRelationList().stream().map(RecommendGoodsGroupRelationVO::getGoodsId).collect(Collectors.toList());
                goodsIdList.addAll(groupGoodsIds);
            }
        });
        List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsDTO> goodsDTOMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        groupVOPage.getRecords().forEach(group->{
            if(CollectionUtil.isNotEmpty(group.getRelationList())){
                group.setRelationNum(group.getRelationList().size());
                group.getRelationList().forEach(relation->{
                    GoodsDTO goodsDTO = goodsDTOMap.get(relation.getGoodsId());
                    if(null!=goodsDTO){
                        relation.setName(goodsDTO.getName());
                        relation.setSellSpecifications(goodsDTO.getSellSpecifications());
                        relation.setLicenseNo(goodsDTO.getLicenseNo());
                        relation.setManufacturer(goodsDTO.getManufacturer());
                    }
                });
            }else {
                group.setRelationNum(0);
            }
        });
        return Result.success(groupVOPage);
    }

    @ApiOperation(value = "id获取组信息(含关联商品信息)", httpMethod = "GET")
    @GetMapping("/getGroup")
    public Result<RecommendGoodsGroupVO> getGroup(@CurrentUser CurrentStaffInfo staffInfo, @RequestParam("groupId")Long groupId){
        RecommendGoodsGroupDTO groupDTO = recommendGoodsGroupApi.getGroupById(groupId);
        if(null == groupDTO){
            return Result.failed("推荐商品组不存在");
        }
        RecommendGoodsGroupVO groupVO = PojoUtils.map(groupDTO, RecommendGoodsGroupVO.class);
        List<Long> goodsIdList = groupVO.getRelationList().stream().map(RecommendGoodsGroupRelationVO::getGoodsId).collect(Collectors.toList());
        List<GoodsDTO> goodsDTOList = goodsApi.batchQueryInfo(goodsIdList);
        Map<Long, GoodsDTO> goodsDTOMap = goodsDTOList.stream().collect(Collectors.toMap(GoodsDTO::getId, Function.identity()));
        groupVO.getRelationList().forEach(relation->{
            GoodsDTO goodsDTO = goodsDTOMap.get(relation.getGoodsId());
            if(null!=goodsDTO){
                relation.setName(goodsDTO.getName());
                relation.setSellSpecifications(goodsDTO.getSellSpecifications());
                relation.setLicenseNo(goodsDTO.getLicenseNo());
                relation.setManufacturer(goodsDTO.getManufacturer());
            }
        });
        groupVO.setRelationNum(groupVO.getRelationList().size());
        return Result.success(groupVO);
    }


    @ApiOperation(value = "保存或修改商品推荐组", httpMethod = "POST")
    @PostMapping("/saveOrUpdate")
    public Result<Long> saveOrUpdate(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody @Valid SaveOrUpdateRecommendGoodsGroupForm form){
        if(StringUtils.isBlank(form.getName())){
            return Result.failed("推荐商品组名称不能为空");
        }
        Long currentEid = staffInfo.getCurrentEid();
        if(CollectionUtil.isNotEmpty(form.getRelationList())&&form.getRelationList().size()>10){
            return Result.failed("推荐商品组数量不能超过10条");
        }
        SaveOrUpdateRecommendGoodsGroupRequest request = PojoUtils.map(form, SaveOrUpdateRecommendGoodsGroupRequest.class);
        request.setEid(currentEid);
        request.setOpUserId(staffInfo.getCurrentUserId());
        Long groupId = recommendGoodsGroupApi.saveOrUpdateGroup(request);
        return Result.success(groupId);
    }

    @ApiOperation(value = "删除推荐组", httpMethod = "POST")
    @PostMapping("/deleteGroup")
    public Result<Boolean> deleteGroup(@CurrentUser CurrentStaffInfo staffInfo, @RequestBody DeleteRecommendGoodsGroupForm form){
        int i = recommendGoodsGroupApi.deleteGroup(form.getGroupId(), staffInfo.getCurrentUserId());
        if(i>0){
            return Result.success(true);
        }
        return Result.success(false);
    }

//    @ApiOperation(value = "pop商品分页（含是否添加当前推荐商品组）", httpMethod = "POST")
//    @PostMapping("/popGoodsPage")
//    public Result<Page<RecommendGroupGoodsListItemVO>> popGoodsPage(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody @Valid QueryRecommendGroupPopGoodsPageListForm form){
//        QueryGoodsPageListRequest request = PojoUtils.map(form, QueryGoodsPageListRequest.class);
//        if (Constants.YILING_EID.equals(staffInfo.getCurrentEid())) {
//            List<Long> list = enterpriseApi.listSubEids(staffInfo.getCurrentEid());
//            request.setEidList(list);
//        } else {
//            request.setEidList(Arrays.asList(staffInfo.getCurrentEid()));
//        }
//        Page<RecommendGroupGoodsListItemVO> page = PojoUtils.map(popGoodsApi.queryPopGoodsPageList(request), RecommendGroupGoodsListItemVO.class);
//        List<Long> goodsIds = page.getRecords().stream().map(RecommendGroupGoodsListItemVO::getId).collect(Collectors.toList());
//        List<RecommendGoodsGroupRelationDTO> relationDTOList = recommendGoodsGroupApi.getGroupRelationByGroupIdsAndGoodsIds(ListUtil.toList(form.getGroupId()), goodsIds);
//        List<Long> relationGoodsList = relationDTOList.stream().map(RecommendGoodsGroupRelationDTO::getGoodsId).collect(Collectors.toList());
//        page.getRecords().forEach(goods->{
//            if(relationGoodsList.contains(goods.getId())){
//                goods.setRecommendGroupFlag(true);
//            }else {
//                goods.setRecommendGroupFlag(false);
//            }
//        });
//        return Result.success(page);
//    }

    @ApiOperation(value = "添加关联商品", httpMethod = "POST")
    @PostMapping("/saveRelationGoods")
    public Result saveRelationGoods(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody @Valid SaveRecommendGoodsGroupRelationForm form){
        if(null==form.getGoodsId() || form.getGoodsId()==0){
            return Result.failed("商品不能为空");
        }
        List<RecommendGoodsGroupRelationDTO> relationDTOList = recommendGoodsGroupApi.getGroupRelationByGroupIdsAndGoodsIds(ListUtil.toList(form.getGroupId()), null);
        if((relationDTOList.size())>9){
            return Result.failed("推荐商品组数量不能超过10条");
        }
        recommendGoodsGroupApi.batchSaveGroupRelation(form.getGroupId(),ListUtil.toList(form.getGoodsId()),staffInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "删除关联商品", httpMethod = "POST")
    @PostMapping("/deleteRelationGoods")
    public Result<Boolean> deleteRelationGoods(@CurrentUser CurrentStaffInfo staffInfo,@RequestBody @Valid SaveRecommendGoodsGroupRelationForm form){
        int i = recommendGoodsGroupApi.batchDeleteGroupRelation(form.getGroupId(), ListUtil.toList(form.getGoodsId()), staffInfo.getCurrentUserId());
        if(i>0){
            return Result.success(true);
        }
        return Result.success(false);
    }
}
