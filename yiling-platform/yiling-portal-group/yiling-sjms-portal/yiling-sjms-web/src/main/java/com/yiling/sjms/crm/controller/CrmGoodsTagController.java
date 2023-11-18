package com.yiling.sjms.crm.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.api.CrmGoodsInfoApi;
import com.yiling.dataflow.crm.api.CrmGoodsTagApi;
import com.yiling.dataflow.crm.bo.CrmGoodsTagRelationBO;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsInfoDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagDTO;
import com.yiling.dataflow.crm.dto.CrmGoodsTagRelationDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsInfoPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagPageRequest;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsTagRelationPageRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsTagRequest;
import com.yiling.dataflow.crm.enums.CrmGoodsTagTypeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.crm.form.QueryCrmGoodsTagPageForm;
import com.yiling.sjms.crm.form.QueryCrmGoodsTagRelationPageForm;
import com.yiling.sjms.crm.form.QueryTagGoodsInfoPageForm;
import com.yiling.sjms.crm.form.SaveCrmGoodsTagRelationForm;
import com.yiling.sjms.crm.form.SaveOrUpdateCrmGoodsTagForm;
import com.yiling.sjms.crm.vo.CrmGoodsTagRelationVO;
import com.yiling.sjms.crm.vo.CrmGoodsTagVO;
import com.yiling.sjms.crm.vo.CrmGoodsTypeTagVO;
import com.yiling.sjms.crm.vo.CrmTagGoodsInfoVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsTagController
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Slf4j
@RestController
@RequestMapping("/crmGoodsTag")
@Api(tags = "商品标签管理")
public class CrmGoodsTagController extends BaseController {
    @DubboReference
    private CrmGoodsTagApi crmGoodsTagApi;

    @DubboReference
    private CrmGoodsInfoApi crmGoodsInfoApi;

    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @ApiOperation(value = "查询标签分页", httpMethod = "POST")
    @PostMapping("/queryTagPage")
    public Result<Page<CrmGoodsTagVO>> queryTagPage(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestBody QueryCrmGoodsTagPageForm form){
        QueryCrmGoodsTagPageRequest request = PojoUtils.map(form,QueryCrmGoodsTagPageRequest.class);
        Page<CrmGoodsTagDTO> page = crmGoodsTagApi.queryTagPage(request);
        Page<CrmGoodsTagVO> voPage = PojoUtils.map(page, CrmGoodsTagVO.class);
        if(CollectionUtil.isNotEmpty(voPage.getRecords())){
            List<Long> tagIds = voPage.getRecords().stream().map(CrmGoodsTagVO::getId).collect(Collectors.toList());
            Map<Long, Long> tagGoodsMap = crmGoodsTagApi.countTagGoods(tagIds);
            voPage.getRecords().forEach(tagVo->{
                Long goodsCount = tagGoodsMap.getOrDefault(tagVo.getId(),0L);
                tagVo.setGoodsCount(goodsCount);
            });
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "获取标签列表", httpMethod = "GET")
    @GetMapping("/tagList")
    public Result<List<CrmGoodsTagVO>> tagList(@RequestParam(value = "type",required = false) Integer type){
        List<CrmGoodsTagDTO> list = crmGoodsTagApi.getTagList(type);
        return Result.success(PojoUtils.map(list,CrmGoodsTagVO.class));
    }

    @ApiOperation(value = "获取各个类型标签", httpMethod = "GET")
    @GetMapping("/tagListByType")
    public Result<CrmGoodsTypeTagVO> tagListByType(){
        List<CrmGoodsTagVO> list = PojoUtils.map(crmGoodsTagApi.getTagList(null), CrmGoodsTagVO.class);
        Map<Integer, List<CrmGoodsTagVO>> typeMap = list.stream().collect(Collectors.groupingBy(CrmGoodsTagVO::getType));
        CrmGoodsTypeTagVO typeTagVO = new CrmGoodsTypeTagVO();
        typeTagVO.setNotLockTagList(typeMap.getOrDefault(CrmGoodsTagTypeEnum.NOT_LOCK.getType(), ListUtil.empty()));
        typeTagVO.setGroupPurchaseTagList(typeMap.getOrDefault(CrmGoodsTagTypeEnum.GROUP_PURCHASE.getType(), ListUtil.empty()));
        return Result.success(typeTagVO);
    }

    @ApiOperation(value = "保存标签", httpMethod = "POST")
    @PostMapping("/saveTag")
    public Result saveTag(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateCrmGoodsTagForm form){
        SaveOrUpdateCrmGoodsTagRequest request = PojoUtils.map(form,SaveOrUpdateCrmGoodsTagRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(null);
        if(StringUtils.isBlank(request.getName())){
            return Result.failed("标签名称为空");
        }
        if(null==request.getType() || request.getType()==0){
            return Result.failed("标签类型为空");
        }
        crmGoodsTagApi.saveOrUpdateTag(request);
        return Result.success();
    }

    @ApiOperation(value = "编辑标签", httpMethod = "POST")
    @PostMapping("/editTag")
    public Result editTag(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveOrUpdateCrmGoodsTagForm form){
        if(null == form.getId()){
            return Result.failed("编辑标签id为空");
        }
        SaveOrUpdateCrmGoodsTagRequest request = PojoUtils.map(form,SaveOrUpdateCrmGoodsTagRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        if(StringUtils.isBlank(request.getName())){
            return Result.failed("标签名称为空");
        }
        crmGoodsTagApi.saveOrUpdateTag(request);
        return Result.success();
    }

    @ApiOperation(value = "删除标签", httpMethod = "GET")
    @GetMapping("/deleteTag")
    public Result deleteTag(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestParam("tagId")Long tagId){
        crmGoodsTagApi.deleteTag(tagId,userInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "id获取标签", httpMethod = "GET")
    @GetMapping("/findById")
    public Result<CrmGoodsTagVO> findById(@RequestParam("tagId")Long tagId){
        CrmGoodsTagDTO tagDTO = crmGoodsTagApi.findById(tagId);
        if(null==tagDTO){
            return Result.failed("标签不存在");
        }
        return Result.success(PojoUtils.map(tagDTO,CrmGoodsTagVO.class));
    }

    @ApiOperation(value = "分页查询标签关联商品", httpMethod = "POST")
    @PostMapping("/queryTagGoodsPage")
    public Result<Page<CrmGoodsTagRelationVO>> queryTagGoodsPage(@CurrentUser CurrentSjmsUserInfo userinfo, @RequestBody QueryCrmGoodsTagRelationPageForm form){
        QueryCrmGoodsTagRelationPageRequest request = new QueryCrmGoodsTagRelationPageRequest();
        if(null == form.getTagId()){
            return Result.failed("标签为空");
        }else {
            request.setTagId( form.getTagId());
        }
        if(StringUtils.isNotBlank(form.getKeyword())){
            if(StringUtils.isNumeric(form.getKeyword())){
                request.setCrmGoodsCode(Long.parseLong(form.getKeyword()));
            }else {
                request.setCrmGoodsName(form.getKeyword());
            }
        }
        request.setCurrent(form.getCurrent());
        request.setSize(form.getSize());
        Page<CrmGoodsTagRelationBO> page = crmGoodsTagApi.queryTagRelationPage(request);
        Page<CrmGoodsTagRelationVO> voPage = PojoUtils.map(page, CrmGoodsTagRelationVO.class);
        if(CollectionUtil.isNotEmpty(voPage.getRecords())){
            List<Long> categoryIds = voPage.getRecords().stream().map(CrmGoodsTagRelationVO::getCrmGoodsCategoryId).collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> categoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
            Map<Long, String> categoryMap = categoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));
            voPage.getRecords().forEach(tagRelation->{
                tagRelation.setCrmGoodsCategory(categoryMap.getOrDefault(tagRelation.getCrmGoodsCategoryId(),""));
            });
        }
        return Result.success(voPage);
    }

    @ApiOperation(value = "保存标签关联商品", httpMethod = "POST")
    @PostMapping("/saveTagRelation")
    public Result saveTagRelation(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCrmGoodsTagRelationForm form){
        if(null==form.getTagId()){
            return Result.failed("标签为空");
        }
        if(null==form.getCrmGoodsId()){
            return Result.failed("商品为空");
        }
        CrmGoodsTagDTO tagDTO = crmGoodsTagApi.findById(form.getTagId());
        if(null == tagDTO){
            return Result.failed("标签不存在");
        }
        CrmGoodsInfoDTO goodsInfoDTO = crmGoodsInfoApi.findById(form.getCrmGoodsId());
        if(null == goodsInfoDTO){
            return Result.failed("绑定的商品不存在");
        }
        if(CrmGoodsTagTypeEnum.GROUP_PURCHASE.getType().equals(tagDTO.getType()) && goodsInfoDTO.getIsGroupPurchase() != 1){
            return Result.failed("商品不是团购商品，无法绑定团购标签");
        }
        crmGoodsTagApi.saveTagRelation(form.getTagId(),form.getCrmGoodsId(),userInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "删除标签关联商品", httpMethod = "POST")
    @PostMapping("/deleteTagRelation")
    public Result deleteTagRelation(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody SaveCrmGoodsTagRelationForm form){
        if(null==form.getId()){
            return Result.failed("删除数据不存在");
        }
        crmGoodsTagApi.deleteTagRelation(form.getId(),userInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "标签弹窗查询商品分页", httpMethod = "POST")
    @PostMapping("/queryPopupGoodsPage")
    public Result<Page<CrmTagGoodsInfoVO>> queryPopupGoodsPage(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryTagGoodsInfoPageForm form){
        if(null == form.getTagId() || form.getTagId()<=0){
            return Result.failed("标签为空");
        }
        CrmGoodsTagDTO tagDTO = crmGoodsTagApi.findById(form.getTagId());
        if(null == tagDTO){
            return Result.failed("标签不存在");
        }
        QueryCrmGoodsInfoPageRequest request = PojoUtils.map(form, QueryCrmGoodsInfoPageRequest.class);
        if(CrmGoodsTagTypeEnum.GROUP_PURCHASE.getType().equals(tagDTO.getType())){
            request.setIsGroupPurchase(1);
        }
        Page<CrmGoodsInfoDTO> page = crmGoodsInfoApi.getPopupPage(request);
        Page<CrmTagGoodsInfoVO> voPage = PojoUtils.map(page, CrmTagGoodsInfoVO.class);
        if(CollectionUtil.isNotEmpty(voPage.getRecords())){
            List<Long> categoryIds = voPage.getRecords().stream().filter(goods -> null != goods.getCategoryId() && goods.getCategoryId() > 0).map(CrmTagGoodsInfoVO::getCategoryId).distinct().collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> categoryDTOS = crmGoodsCategoryApi.findByIds(categoryIds);
            Map<Long, String> categoryMap = categoryDTOS.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));

            List<Long> goodsIds = voPage.getRecords().stream().map(CrmTagGoodsInfoVO::getId).collect(Collectors.toList());
            List<CrmGoodsTagRelationDTO> bindTagGoods = crmGoodsTagApi.getGoodsIdByTag(form.getTagId(), goodsIds);
            List<Long> bindTagGoodsId = bindTagGoods.stream().map(CrmGoodsTagRelationDTO::getCrmGoodsId).collect(Collectors.toList());
            voPage.getRecords().forEach(goodsVO->{
                goodsVO.setCategory(categoryMap.getOrDefault(goodsVO.getCategoryId(),""));
                if(bindTagGoodsId.contains(goodsVO.getId())){
                    goodsVO.setTagFlag(true);
                }else {
                    goodsVO.setTagFlag(false);
                }
            });
        }

        return Result.success(voPage);
    }
}
