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

import com.yiling.dataflow.crm.api.CrmGoodsCategoryApi;
import com.yiling.dataflow.crm.dto.CrmGoodsCategoryDTO;
import com.yiling.dataflow.crm.dto.request.QueryCrmGoodsCategoryRequest;
import com.yiling.dataflow.crm.dto.request.SaveOrUpdateCrmGoodsCategoryRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.crm.form.QueryCrmGoodsCategoryForm;
import com.yiling.sjms.crm.form.SaveOrUpdateCrmGoodsCategoryForm;
import com.yiling.sjms.crm.vo.CrmGoodsCategoryVO;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 CrmGoodsCategoryController
 * @描述
 * @创建时间 2023/4/10
 * @修改人 shichen
 * @修改时间 2023/4/10
 **/
@Slf4j
@RestController
@RequestMapping("/crmGoodsCategory")
@Api(tags = "商品品种管理")
public class CrmGoodsCategoryController extends BaseController {
    @DubboReference
    private CrmGoodsCategoryApi crmGoodsCategoryApi;

    @ApiOperation(value = "查询品类列表（无搜索条件为树状）", httpMethod = "POST")
    @PostMapping("/queryCategoryTree")
    public Result<List<CrmGoodsCategoryVO>> queryCategoryTree(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmGoodsCategoryForm form){
        Boolean noParam = true;
        if(StringUtils.isNotBlank(form.getCode())){
            noParam=false;
        }
        if(StringUtils.isNotBlank(form.getName())){
            noParam=false;
        }
        if(null != form.getCategoryLevel() && form.getCategoryLevel()>0){
            noParam=false;
        }
        if(StringUtils.isNotBlank(form.getParentName())){
            noParam=false;
        }
        QueryCrmGoodsCategoryRequest request = PojoUtils.map(form, QueryCrmGoodsCategoryRequest.class);
        List<CrmGoodsCategoryDTO> list = crmGoodsCategoryApi.queryCategoryList(request);
        if(CollectionUtil.isNotEmpty(list)){
            List<Long> parentIds = list.stream().map(CrmGoodsCategoryDTO::getParentId).collect(Collectors.toList());
            List<CrmGoodsCategoryDTO> parentList = crmGoodsCategoryApi.findByIds(parentIds);
            Map<Long, String> parentMap = parentList.stream().collect(Collectors.toMap(CrmGoodsCategoryDTO::getId, CrmGoodsCategoryDTO::getName));
            List<Long> idList = list.stream().map(CrmGoodsCategoryDTO::getId).collect(Collectors.toList());
            Map<Long, Long> goodsCountMap = crmGoodsCategoryApi.getGoodsCountByCategory(idList);
            List<CrmGoodsCategoryVO> voList=list.stream().map(categoryDto->{
                CrmGoodsCategoryVO categoryVO = PojoUtils.map(categoryDto, CrmGoodsCategoryVO.class);
                String parentName = parentMap.get(categoryVO.getParentId());
                categoryVO.setParentName(parentName);
                Long goodsCount = goodsCountMap.getOrDefault(categoryVO.getId(),0L);
                categoryVO.setGoodsCount(goodsCount);
                return categoryVO;
            }).collect(Collectors.toList());
            if(noParam){
                return Result.success(this.getCategoryTree(voList));
            }else {
                return Result.success(voList);
            }

        }
        return Result.success(ListUtil.empty());
    }

    @ApiOperation(value = "查询品类基础信息", httpMethod = "POST")
    @PostMapping("/queryCategory")
    public Result<List<CrmGoodsCategoryVO>> queryCategory(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryCrmGoodsCategoryForm form){
        QueryCrmGoodsCategoryRequest request = PojoUtils.map(form, QueryCrmGoodsCategoryRequest.class);
        List<CrmGoodsCategoryDTO> list = crmGoodsCategoryApi.queryCategoryList(request);
        return Result.success(PojoUtils.map(list,CrmGoodsCategoryVO.class));
    }


    @ApiOperation(value = "保存品类", httpMethod = "POST")
    @PostMapping("/saveCategory")
    public Result saveCategory(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody SaveOrUpdateCrmGoodsCategoryForm form){
        SaveOrUpdateCrmGoodsCategoryRequest request = PojoUtils.map(form, SaveOrUpdateCrmGoodsCategoryRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setId(null);
        if(StringUtils.isBlank(request.getCode())){
            return Result.failed("品类编码为空");
        }
        if(StringUtils.isBlank(request.getName())){
            return Result.failed("品类名称为空");
        }
        if(null ==request.getParentId()){
            request.setParentId(0L);
            request.setCategoryLevel(1);
        }
        crmGoodsCategoryApi.saveOrUpdateCategory(request);
        return Result.success();
    }

    @ApiOperation(value = "编辑品类", httpMethod = "POST")
    @PostMapping("/editCategory")
    public Result editCategory(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody SaveOrUpdateCrmGoodsCategoryForm form){
        SaveOrUpdateCrmGoodsCategoryRequest request = PojoUtils.map(form, SaveOrUpdateCrmGoodsCategoryRequest.class);
        request.setOpUserId(userInfo.getCurrentUserId());
        request.setCode(null);
        request.setParentId(null);
        request.setCategoryLevel(null);
        if(StringUtils.isBlank(request.getName())){
            return Result.failed("品类名称为空");
        }
        crmGoodsCategoryApi.saveOrUpdateCategory(request);
        return Result.success();
    }

    @ApiOperation(value = "获取末级品类", httpMethod = "GET")
    @GetMapping("/getFinalStageCategory")
    public Result<List<CrmGoodsCategoryVO>> getFinalStageCategory(@RequestParam(value = "category",required = false)String category){
        List<CrmGoodsCategoryDTO> list = crmGoodsCategoryApi.getFinalStageCategory(category);
        return Result.success(PojoUtils.map(list,CrmGoodsCategoryVO.class));
    }

    @ApiOperation(value = "获取所有品类级别", httpMethod = "GET")
    @GetMapping("/getAllLevel")
    public Result<List<Integer>> getAllLevel(){
        List<Integer> list = crmGoodsCategoryApi.getAllLevel();
        return Result.success(list);
    }

    @ApiOperation(value = "删除品类", httpMethod = "GET")
    @GetMapping("/deleteCategory")
    public Result deleteCategory(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestParam("categoryId")Long categoryId){
        CrmGoodsCategoryDTO categoryDTO = crmGoodsCategoryApi.findById(categoryId);
        if(null==categoryDTO){
            return Result.failed("品类不存在");
        }
        if(categoryDTO.getFinalStageFlag() !=1){
            return Result.failed("品类不是末级,无法删除");
        }
        Map<Long, Long> goodsCountMap = crmGoodsCategoryApi.getGoodsCountByCategory(ListUtil.toList(categoryId));
        if(goodsCountMap.getOrDefault(categoryId,0L)>0){
            return Result.failed("品类已绑定商品,无法删除");
        }
        crmGoodsCategoryApi.deleteCategoryById(categoryId,userInfo.getCurrentUserId());
        return Result.success();
    }

    private List<CrmGoodsCategoryVO> getCategoryTree(List<CrmGoodsCategoryVO> categoryList) {
        TreeNodeConfig config = new TreeNodeConfig();
        //默认为id可以不设置
        config.setIdKey("id");
        //默认为parentId可以不设置
        config.setParentIdKey("parentId");
        config.setNameKey("name");
        //排序字段
        config.setWeightKey("id");

        List<Tree<Long>> treeNodes = TreeUtil.build(categoryList, 0L, config, (treeNode, tree) -> {
            tree.setId(treeNode.getId());
            tree.setParentId(treeNode.getParentId());
            tree.setName(treeNode.getName());
            // 扩展属性
            tree.putExtra("code", treeNode.getCode());
            tree.putExtra("categoryLevel", treeNode.getCategoryLevel());
            tree.putExtra("finalStageFlag", treeNode.getFinalStageFlag());
            tree.putExtra("parentName", treeNode.getParentName());
            tree.putExtra("goodsCount", treeNode.getGoodsCount());
            tree.putExtra("updateTime", treeNode.getUpdateTime());
        });
        return PojoUtils.map(treeNodes,CrmGoodsCategoryVO.class);
    }
}
