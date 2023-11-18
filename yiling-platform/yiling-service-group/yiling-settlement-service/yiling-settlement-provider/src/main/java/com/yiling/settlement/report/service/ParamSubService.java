package com.yiling.settlement.report.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseService;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.request.AddReportSubParamRequest;
import com.yiling.settlement.report.dto.request.QueryReportParamSubPageListRequest;
import com.yiling.settlement.report.entity.ParamSubDO;

/**
 * <p>
 * 子参数表 服务类
 * </p>
 *
 * @author dexi.yao
 * @date 2022-05-17
 */
public interface ParamSubService extends BaseService<ParamSubDO> {


    /**
     * 分页查询报表参数的子参数列表
     *
     * @param request
     * @return
     */
    Page<ReportParamSubDTO> queryReportParamSubPageList(QueryReportParamSubPageListRequest request);

    /**
     * 根据id批量查询子参数
     *
     * @param ids
     * @return
     */
    List<ReportParamSubDTO> queryReportParamSubInfoByIds(List<Long> ids);

    /**
     * 查询阶梯最大range
     *
     * @param
     * @return
     */
    Integer queryLadderMaxRange();

    /**
     * 添加或修改子会员参数
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateReportSubMemberParam(AddReportSubParamRequest request);

    /**
     * 根据eid查询会员类型返利参数
     *
     * @param eid
     * @return
     */
    List<ReportParamSubDTO> queryMemberParInfoByEid(Long eid);
}
