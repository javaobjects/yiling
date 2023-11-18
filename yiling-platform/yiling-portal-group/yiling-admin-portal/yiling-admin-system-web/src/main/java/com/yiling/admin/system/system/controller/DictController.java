package com.yiling.admin.system.system.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.system.form.QueryDictTypeForm;
import com.yiling.admin.system.system.form.SaveDictDataForm;
import com.yiling.admin.system.system.form.SaveDictTypeForm;
import com.yiling.admin.system.system.form.UpdateDictDataForm;
import com.yiling.admin.system.system.form.UpdateDictTypeForm;
import com.yiling.admin.system.system.vo.DictDataVO;
import com.yiling.admin.system.system.vo.DictTypeVO;
import com.yiling.admin.system.system.vo.DictVO;
import com.yiling.basic.dict.api.DictApi;
import com.yiling.basic.dict.api.DictDataApi;
import com.yiling.basic.dict.api.DictTypeApi;
import com.yiling.basic.dict.bo.DictBO;
import com.yiling.basic.dict.bo.DictDataBO;
import com.yiling.basic.dict.bo.DictTypeBO;
import com.yiling.basic.dict.bo.request.QueryDictTypeRequest;
import com.yiling.basic.dict.bo.request.SaveDictDataRequest;
import com.yiling.basic.dict.bo.request.SaveDictTypeRequest;
import com.yiling.basic.dict.bo.request.UpdateDictDataRequest;
import com.yiling.basic.dict.bo.request.UpdateDictTypeRequest;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.framework.common.web.rest.BoolObject;
import com.yiling.framework.common.web.rest.CollectionObject;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据字典 Controller
 *
 * @author: xuan.zhou
 * @date: 2021/6/3
 */
@Slf4j
@RestController
@RequestMapping("/system/dict")
@Api(tags = "数据字典接口")
public class DictController extends BaseController {

    @DubboReference
    DictApi dictApi;

    @DubboReference
    DictTypeApi dictTypeApi;

    @DubboReference
    DictDataApi dictDataApi;


    @ApiOperation(value = "获取所有字典数据")
    @GetMapping("/all")
    public Result<CollectionObject<DictVO>> all() {
        List<DictBO> list = dictApi.getEnabledList();
        return Result.success(new CollectionObject<>(PojoUtils.map(list, DictVO.class)));
    }

    @ApiOperation(value = "获取字典类型数据")
    @PostMapping("/get/type")
    public Result<Page<DictTypeVO>> getDictTypePage(@Valid @RequestBody QueryDictTypeForm form) {
        QueryDictTypeRequest request = PojoUtils.map(form, QueryDictTypeRequest.class);
        Page<DictTypeBO> result = dictTypeApi.getDictTypePage(request);
        return Result.success(PojoUtils.map(result, DictTypeVO.class));
    }

    @ApiOperation(value = "获取字典内容数据")
    @GetMapping("/get/data")
    public Result<CollectionObject<DictDataVO>> all(@RequestParam("typeId") Long typeId) {
        List<DictDataBO> list = dictDataApi.getEnabledByTypeIdList(typeId);
        return Result.success(new CollectionObject<>(PojoUtils.map(list, DictDataVO.class)));
    }

    @ApiOperation(value = "修改字典类型数据")
    @PostMapping("/update/type")
    @Log(title = "修改字典类型数据", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateDictTypeById(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody UpdateDictTypeForm form) {
        UpdateDictTypeRequest request = PojoUtils.map(form, UpdateDictTypeRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = dictTypeApi.updateDictTypeById(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "修改字典内容数据")
    @PostMapping("/update/data")
    @Log(title = "修改字典内容数据", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateDictDataById(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody UpdateDictDataForm form) {
        UpdateDictDataRequest request = PojoUtils.map(form, UpdateDictDataRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean data = dictDataApi.updateDictDataById(request);
        return Result.success(new BoolObject(data));
    }

    @ApiOperation(value = "新增字类型数据")
    @PostMapping("/save/type")
    @Log(title = "新增字类型数据", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> saveDictType(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody SaveDictTypeForm form) {
        SaveDictTypeRequest request = PojoUtils.map(form, SaveDictTypeRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = dictTypeApi.saveDictType(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "新增字典内容数据")
    @PostMapping("/save/data")
    @Log(title = "新增字典内容数据", businessType = BusinessTypeEnum.INSERT)
    public Result<BoolObject> saveDictData(@CurrentUser CurrentAdminInfo adminInfo, @Valid @RequestBody SaveDictDataForm form) {
        SaveDictDataRequest request = PojoUtils.map(form, SaveDictDataRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        Boolean result = dictDataApi.saveDictData(request);
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "停用字典内容数据")
    @GetMapping("/stop/data")
    @Log(title = "停用字典内容数据", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateStopDictData(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id") Long id) {
        Boolean result = dictDataApi.updateStopDictData(id, adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "停用字典类型数据")
    @GetMapping("/stop/type")
    @Log(title = "停用字典类型数据", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> updateStopDictType(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id")Long id) {
        Boolean result = dictTypeApi.updateStopDictType(id, adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "启用字典内容数据")
    @GetMapping("/enabled/data")
    @Log(title = "启用字典内容数据", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> enabledDictData(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id")Long id) {
        Boolean result = dictDataApi.enabledDictData(id, adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "启用字典类型数据")
    @GetMapping("/enabled/type")
    @Log(title = "启用字典类型数据", businessType = BusinessTypeEnum.UPDATE)
    public Result<BoolObject> enabledDictType(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("id")Long id) {
        Boolean result = dictTypeApi.enabledDictType(id, adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "删除字典类型")
    @GetMapping("/type/delete")
    @Log(title = "删除字典类型", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> deleteType(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("idList") List<Long> idList) {
        Boolean result = dictTypeApi.deleteType(idList, adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

    @ApiOperation(value = "删除字典数据")
    @GetMapping("/data/delete")
    @Log(title = "删除字典数据", businessType = BusinessTypeEnum.DELETE)
    public Result<BoolObject> deleteData(@CurrentUser CurrentAdminInfo adminInfo, @RequestParam("idList") List<Long> idList) {
        Boolean result = dictDataApi.deleteData(idList, adminInfo.getCurrentUserId());
        return Result.success(new BoolObject(result));
    }

}
