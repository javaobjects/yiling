package com.yiling.admin.system.system.controller;

import java.util.HashMap;
import java.util.Map;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.system.system.form.QueryEsWordPageListForm;
import com.yiling.admin.system.system.form.SaveEsWordForm;
import com.yiling.admin.system.system.vo.EsWordAllInfoVO;
import com.yiling.admin.system.system.vo.EsWordVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.exception.BusinessException;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.search.word.api.EsWordExpansionApi;
import com.yiling.search.word.bo.EsWordAllTypeBO;
import com.yiling.search.word.dto.EsWordExpansionDTO;
import com.yiling.search.word.dto.request.QueryEsWordPageListRequest;
import com.yiling.search.word.dto.request.SaveOrUpdateEsWordExpansionRequest;
import com.yiling.search.word.enums.EsWordErrorEnum;
import com.yiling.search.word.enums.EsWordTypeEnum;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shichen
 * @类名 EsWordController
 * @描述
 * @创建时间 2022/4/26
 * @修改人 shichen
 * @修改时间 2022/4/26
 **/
@Slf4j
@RestController
@RequestMapping("/system/esword")
@Api(tags = "es词库接口")
public class EsWordController extends BaseController {

    @DubboReference
    EsWordExpansionApi esWordExpansionApi;

    @ApiOperation(value = "获取词库词语分页")
    @PostMapping("/pageList")
    public Result<Page<EsWordVO>> pageList(@CurrentUser CurrentAdminInfo adminInfo , @RequestBody  QueryEsWordPageListForm form){
        Page<EsWordExpansionDTO> dtoPage = esWordExpansionApi.pageListByType(PojoUtils.map(form, QueryEsWordPageListRequest.class));
        return Result.success(PojoUtils.map(dtoPage,EsWordVO.class));
    }

    @ApiOperation(value = "词语详情")
    @GetMapping("/wordDetail")
    public Result<EsWordAllInfoVO> wordDetail(@CurrentUser CurrentAdminInfo adminInfo ,@RequestParam("word")String word){
        EsWordAllTypeBO allTypeBO = esWordExpansionApi.findByWord(word);
        return Result.success(PojoUtils.map(allTypeBO,EsWordAllInfoVO.class));
    }

    @ApiOperation(value = "保存词语")
    @PostMapping("/saveWord")
    public Result saveWord(@CurrentUser CurrentAdminInfo adminInfo ,@RequestBody SaveEsWordForm form){
        EsWordTypeEnum typeEnum = EsWordTypeEnum.getByType(form.getType());
        if(null==typeEnum){
            throw new BusinessException(EsWordErrorEnum.NOT_FOUND_TYPE);
        }
        SaveOrUpdateEsWordExpansionRequest request = PojoUtils.map(form, SaveOrUpdateEsWordExpansionRequest.class);
        request.setOpUserId(adminInfo.getCurrentUserId());
        esWordExpansionApi.saveOrUpdateWord(request);
        if(form.getUploadFlag()){
            //上传扩展词库文件
            esWordExpansionApi.updateEsWordDic(typeEnum);
            //添加同步任务
            esWordExpansionApi.addSyncJob(form.getType());
        }
        return Result.success();
    }

    @ApiOperation(value = "同步词库到es搜索引擎")
    @GetMapping("/syncWordToEs")
    public Result<String> syncWordToEs(@CurrentUser CurrentAdminInfo adminInfo ,@RequestParam("type")Integer type){
        EsWordTypeEnum typeEnum = EsWordTypeEnum.getByType(type);
        if(null==typeEnum){
            throw new BusinessException(EsWordErrorEnum.NOT_FOUND_TYPE);
        }
        esWordExpansionApi.addSyncJob(type);
        //该方法为异步方法
        esWordExpansionApi.syncGoodsWordByType(typeEnum);
        return Result.success("已成功异步执行同步");
    }

    @ApiOperation(value = "更新上传词库oss文件")
    @GetMapping("/uploadWordDic")
    public Result<String> uploadWord(@CurrentUser CurrentAdminInfo adminInfo ,@RequestParam("type")Integer type){
        EsWordTypeEnum typeEnum = EsWordTypeEnum.getByType(type);
        if(null==typeEnum){
            throw new BusinessException(EsWordErrorEnum.NOT_FOUND_TYPE);
        }
        //上传扩展词库文件
        esWordExpansionApi.updateEsWordDic(typeEnum);
        //添加同步任务
        esWordExpansionApi.addSyncJob(type);
        return Result.success("更新词库文件成功");
    }

    @ApiOperation(value = "获取词库下载地址")
    @GetMapping("/getDicDownloadUrl")
    public Result<Map<String, String>> getDicDownloadUrl(@CurrentUser CurrentAdminInfo adminInfo , @RequestParam("type")Integer type){
        HashMap<String, String> urlMap = new HashMap<>(2);
        String downloadUrl = esWordExpansionApi.getDownloadUrl(type);
        urlMap.put("url",downloadUrl);
        EsWordTypeEnum esWordTypeEnum = EsWordTypeEnum.getByType(type);
        urlMap.put("fileName",esWordTypeEnum.getFileName());
        return Result.success(urlMap);
    }
}
