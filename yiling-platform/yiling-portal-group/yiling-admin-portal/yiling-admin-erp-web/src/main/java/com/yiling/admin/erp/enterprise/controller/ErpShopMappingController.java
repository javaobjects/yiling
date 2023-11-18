package com.yiling.admin.erp.enterprise.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.erp.enterprise.form.QueryErpShopMappingPageForm;
import com.yiling.admin.erp.enterprise.form.UpdateErpShopMappingPageForm;
import com.yiling.admin.erp.enterprise.vo.ErpShopMappingVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.open.erp.api.ErpShopMappingApi;
import com.yiling.open.erp.dto.ErpShopMappingDTO;
import com.yiling.open.erp.dto.request.QueryErpShopMappingPageRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 ErpShopMappingController
 * @描述
 * @创建时间 2023/3/21
 * @修改人 shichen
 * @修改时间 2023/3/21
 **/
@RestController
@Api(tags = "连锁总店门店对应关系")
@RequestMapping("/erp/shopMapping")
@Slf4j
public class ErpShopMappingController extends BaseController {

    @DubboReference
    private ErpShopMappingApi erpShopMappingApi;


    @ApiOperation(value = "连锁总店门店对应分页列表", httpMethod = "POST")
    @PostMapping("/page")
    public Result<Page<ErpShopMappingVO>> page(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody QueryErpShopMappingPageForm form){
        if(null == form.getMainShopEid() || form.getMainShopEid()==0){
            if(null == form.getShopEid()|| form.getShopEid()==0){
                return Result.success(new Page<>(form.getCurrent(),form.getSize()));
            }
        }
        Page<ErpShopMappingDTO> page = erpShopMappingApi.queryPage(PojoUtils.map(form, QueryErpShopMappingPageRequest.class));
        return Result.success(PojoUtils.map(page,ErpShopMappingVO.class));
    }

    @ApiOperation(value = "删除对应关系", httpMethod = "POST")
    @PostMapping("/delete")
    public Result delete(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateErpShopMappingPageForm form){
        ErpShopMappingDTO shopMappingDTO = erpShopMappingApi.findById(form.getId());
        if(null==shopMappingDTO){
            return Result.failed("删除的对应关系不存在");
        }
        erpShopMappingApi.deleteById(form.getId(),adminInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "批量删除对应关系", httpMethod = "POST")
    @PostMapping("/deleteByQuery")
    public Result deleteByQuery(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateErpShopMappingPageForm form){
        if(null == form.getMainShopEid() || form.getMainShopEid()==0){
            if(null == form.getShopEid()|| form.getShopEid()==0){
                return Result.failed("总店ID和门店ID都为空，请查询后再进行批量操作");
            }
        }
        QueryErpShopMappingPageRequest request = PojoUtils.map(form, QueryErpShopMappingPageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        erpShopMappingApi.batchDeleteByQuery(request);
        return Result.success();
    }

    @ApiOperation(value = "修改对应关系同步状态", httpMethod = "POST")
    @PostMapping("/updateSyncStatus")
    public Result updateSyncStatus(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateErpShopMappingPageForm form){
        ErpShopMappingDTO shopMappingDTO = erpShopMappingApi.findById(form.getId());
        if(null==shopMappingDTO){
            return Result.failed("更新的对应关系不存在");
        }
        erpShopMappingApi.updateSyncById(form.getId(),form.getSyncStatus(),adminInfo.getCurrentUserId());
        return Result.success();
    }

    @ApiOperation(value = "批量修改对应关系同步状态", httpMethod = "POST")
    @PostMapping("/updateSyncStatusByQuery")
    public Result updateSyncStatusByQuery(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody UpdateErpShopMappingPageForm form){
        if(null == form.getMainShopEid() || form.getMainShopEid()==0){
            if(null == form.getShopEid()|| form.getShopEid()==0){
                return Result.failed("总店ID和门店ID都为空，请查询后再进行批量操作");
            }
        }
        QueryErpShopMappingPageRequest request = PojoUtils.map(form, QueryErpShopMappingPageRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        erpShopMappingApi.batchUpdateSyncByQuery(request,form.getSyncStatus());
        return Result.success();
    }
}
