package com.yiling.admin.data.center.enterprise.controller;


import javax.validation.Valid;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.admin.data.center.enterprise.form.QueryPromotionDownloadRecordPageForm;
import com.yiling.admin.data.center.enterprise.form.SavePromotionDownloadRecordForm;
import com.yiling.admin.data.center.enterprise.vo.EnterprisePromotionDownloadRecordVO;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.base.BaseController;
import com.yiling.framework.common.log.Log;
import com.yiling.framework.common.log.enums.BusinessTypeEnum;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.user.enterprise.api.EnterprisePromotionDownloadRecordApi;
import com.yiling.user.enterprise.bo.EnterprisePromotionDownloadRecordBO;
import com.yiling.user.enterprise.dto.request.QueryPromotionDownloadRecordPageRequest;
import com.yiling.user.enterprise.dto.request.SavePromotionDownloadRecordRequest;
import com.yiling.user.system.bo.CurrentAdminInfo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 企业推广下载记录表 前端控制器
 * </p>
 *
 * @author lun.yu
 * @date 2023-06-01
 */
@Slf4j
@RestController
@Api(tags = "企业推广下载记录接口")
@RequestMapping("/promotionDownloadRecord")
public class EnterprisePromotionDownloadRecordController extends BaseController {

    @DubboReference
    EnterprisePromotionDownloadRecordApi promotionDownloadRecordApi;

    @ApiOperation(value = "查询企业推广下载记录分页列表")
    @PostMapping("/queryListPage")
    public Result<Page<EnterprisePromotionDownloadRecordVO>> queryTagsListPage(@CurrentUser CurrentAdminInfo adminInfo, @RequestBody @Valid QueryPromotionDownloadRecordPageForm form) {
        QueryPromotionDownloadRecordPageRequest request = PojoUtils.map(form, QueryPromotionDownloadRecordPageRequest.class);
        Page<EnterprisePromotionDownloadRecordBO> recordBOPage = promotionDownloadRecordApi.queryListPage(request);

        Page<EnterprisePromotionDownloadRecordVO> voPage = PojoUtils.map(recordBOPage, EnterprisePromotionDownloadRecordVO.class);
        return Result.success(voPage);
    }

    @ApiOperation(value = "新增企业推广下载记录")
    @PostMapping("/insertDownloadRecord")
    @Log(title = "新增企业推广下载记录", businessType = BusinessTypeEnum.INSERT)
    public Result<Boolean> insertDownloadRecord(@RequestBody @Valid SavePromotionDownloadRecordForm form) {
        SavePromotionDownloadRecordRequest request = PojoUtils.map(form, SavePromotionDownloadRecordRequest.class);
        return Result.success(promotionDownloadRecordApi.insertDownloadRecord(request));
    }

}
