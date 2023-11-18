package com.yiling.sjms.flowcollect.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.beust.jcommander.internal.Lists;
import com.yiling.dataflow.flowcollect.api.FlowCollectSummaryStatisticsApi;
import com.yiling.dataflow.flowcollect.api.FlowCollectUnuploadReasonApi;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsBO;
import com.yiling.dataflow.flowcollect.bo.FlowDayHeartStatisticsDetailBO;
import com.yiling.dataflow.flowcollect.dto.FlowCollectUnuploadReasonDTO;
import com.yiling.dataflow.flowcollect.dto.request.QueryDayCollectStatisticsRequest;
import com.yiling.dataflow.flowcollect.dto.request.QueryFlowCollectUnUploadRequest;
import com.yiling.dataflow.flowcollect.dto.request.SaveFlowCollectUnuploadReasonRequest;
import com.yiling.dataflow.sjms.api.SjmsUserDatascopeApi;
import com.yiling.dataflow.sjms.bo.SjmsUserDatascopeBO;
import com.yiling.dataflow.sjms.enums.OrgDatascopeEnum;
import com.yiling.framework.common.annotations.CurrentUser;
import com.yiling.framework.common.pojo.Result;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.sjms.flowcollect.form.QueryDayCollectStatisticsForm;
import com.yiling.sjms.flowcollect.form.QueryFlowDayUnUnploadForm;
import com.yiling.sjms.flowcollect.form.SaveFlowCollectUnuploadReasonForm;
import com.yiling.sjms.flowcollect.vo.FlowCollectUnuploadReasonVO;
import com.yiling.sjms.flowcollect.vo.FlowDayHeartStatisticsVO;
import com.yiling.sjms.flowcollect.vo.FlowDayStatisticsTableVO;
import com.yiling.user.esb.api.EsbEmployeeApi;
import com.yiling.user.esb.dto.EsbEmployeeDTO;
import com.yiling.user.system.api.UserApi;
import com.yiling.user.system.bo.CurrentSjmsUserInfo;
import com.yiling.user.system.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 日流向收集心跳统计
 */
@Slf4j
@RestController
@RequestMapping(value = "/flow/collect/upload")
@Api(tags = "日流向未上传统计原因")
public class FlowDayUnUploadController {

    @DubboReference
    SjmsUserDatascopeApi sjmsUserDatascopeApi;
    @DubboReference
    private FlowCollectUnuploadReasonApi flowCollectUnuploadReasonApi;
    @DubboReference
    EsbEmployeeApi esbEmployeeApi;
    @DubboReference
    UserApi userApi;

    @ApiOperation(value = "列表", httpMethod = "post")
    @PostMapping("list")
    public Result<List<FlowCollectUnuploadReasonVO>> list(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody QueryFlowDayUnUnploadForm form) {
        QueryFlowCollectUnUploadRequest request = PojoUtils.map(form, QueryFlowCollectUnUploadRequest.class);
        List<FlowCollectUnuploadReasonDTO> flowCollectUnuploadReasonDTOS = flowCollectUnuploadReasonApi.listByCrmIdAndDate(request);
        return Result.success(PojoUtils.map(flowCollectUnuploadReasonDTOS, FlowCollectUnuploadReasonVO.class));
    }

    @ApiOperation(value = "保存", httpMethod = "post")
    @PostMapping("save")
    public Result<Long> save(@CurrentUser CurrentSjmsUserInfo userInfo, @RequestBody @Valid SaveFlowCollectUnuploadReasonForm form) {
        SaveFlowCollectUnuploadReasonRequest request = new SaveFlowCollectUnuploadReasonRequest();
        PojoUtils.map(form, request);
        request.setOpUserId(userInfo.getCurrentUserId());
        List<UserDTO> userDTOS = userApi.listByIds(Arrays.asList(userInfo.getCurrentUserId()));
        request.setOptName(CollUtil.isNotEmpty(userDTOS) ? userDTOS.get(0).getName() : "");
        Long aLong = flowCollectUnuploadReasonApi.saveOrUpdate(request);
        return Result.success(aLong);

    }

    @ApiOperation(value = "详情", httpMethod = "post")
    @PostMapping("detail")
    public Result<FlowCollectUnuploadReasonVO> detail(@CurrentUser CurrentSjmsUserInfo userInfo,@RequestBody QueryFlowDayUnUnploadForm form) {
        QueryFlowCollectUnUploadRequest request = PojoUtils.map(form, QueryFlowCollectUnUploadRequest.class);
        if(Objects.isNull(form.getCrmEnterpriseId()) || Long.valueOf(0).equals(form.getCrmEnterpriseId())|| Objects.isNull(form.getStatisticsTime())){
            return Result.failed("机构编码或未上传时间不能未空");
        }
        if(Objects.isNull(userInfo)){
            return Result.failed("请登陆");
        }
        request.setOpUserId(userInfo.getCurrentUserId());
        List<FlowCollectUnuploadReasonDTO> flowCollectUnuploadReasonDTOS = flowCollectUnuploadReasonApi.listByCrmIdAndDate(request);
        if (CollUtil.isEmpty(flowCollectUnuploadReasonDTOS)) {
//            FlowCollectUnuploadReasonVO vo=    new FlowCollectUnuploadReasonVO();
//            vo.setCrmEnterpriseId(form.getCrmEnterpriseId());
//            vo.setStatisticsTime(form.getStatisticsTime());
            return Result.success(null);
        }
        return Result.success(PojoUtils.map(flowCollectUnuploadReasonDTOS.get(0), FlowCollectUnuploadReasonVO.class));
    }
}
