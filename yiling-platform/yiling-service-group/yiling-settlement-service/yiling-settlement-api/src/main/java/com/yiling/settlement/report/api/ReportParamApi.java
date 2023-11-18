package com.yiling.settlement.report.api;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.goods.ylprice.dto.request.AddGoodsYilingPriceLogRequest;
import com.yiling.settlement.report.dto.ReportLadderGoodsInfoDTO;
import com.yiling.settlement.report.dto.ReportParamDTO;
import com.yiling.settlement.report.dto.ReportParamSubDTO;
import com.yiling.settlement.report.dto.ReportParamSubGoodsDTO;
import com.yiling.settlement.report.dto.ReportYlGoodsCategoryDTO;
import com.yiling.settlement.report.dto.request.AddReportSubParamRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsCategoryRequest;
import com.yiling.settlement.report.dto.request.QueryGoodsInfoRequest;
import com.yiling.settlement.report.dto.request.QueryParamSubGoodsPageListRequest;
import com.yiling.settlement.report.dto.request.QueryReportParPageRequest;
import com.yiling.settlement.report.dto.request.QueryReportParamSubPageListRequest;
import com.yiling.settlement.report.dto.request.SaveOrUpdateParamSubGoodsRequest;

/**
 * @author: gxl
 * @date: 2022/2/22
 */
public interface ReportParamApi {

    /**
     * 报表参数列表
     *
     * @param queryReportParPageRequest
     * @return
     */
    Page<ReportParamDTO> queryReportParamPage(QueryReportParPageRequest queryReportParPageRequest);


    /**
     * 分页查询报表参数的子参数列表
     *
     * @param request
     * @return
     */
    Page<ReportParamSubDTO> queryReportParamSubPageList(QueryReportParamSubPageListRequest request);

    /**
     * 查询报表参数的子参数
     *
     * @param id
     * @return
     */
    ReportParamSubDTO queryReportParamSubById(Long id);

    /**
     * 添加或修改子参数类型
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateReportSubParam(AddReportSubParamRequest request);

    /**
     * 添加或修改子会员参数
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateReportSubMemberParam(AddReportSubParamRequest request);

    /**
     * 分页查询子参属下的商品列表
     *
     * @param request
     * @return
     */
    Page<ReportParamSubGoodsDTO> queryParamSubGoodsPageList(QueryParamSubGoodsPageListRequest request);

    /**
     * 查询子参属下的商品
     *
     * @param id
     * @return
     */
    ReportParamSubGoodsDTO queryParamSubGoodsById(Long id);

    /**
     * 新增或修改分类商品
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateParamSubCategoryGoods(SaveOrUpdateParamSubGoodsRequest request);

    /**
     * 新增或修改 活动商品
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateParamSubActivityGoods(SaveOrUpdateParamSubGoodsRequest request);

    /**
     * 新增或修改阶梯商品
     *
     * @param request
     * @return
     */
    Boolean saveOrUpdateParamSubLadderGoods(SaveOrUpdateParamSubGoodsRequest request);

    /**
     * 删除子参数商品
     *
     * @param id
     * @param opUser
     * @return
     */
    Boolean deleteParSubGoods(Long id, Long opUser);

    /**
     * 查询商品分类信息
     *
     * @param queryGoodsCategoryRequest
     * @return
     */
    List<ReportYlGoodsCategoryDTO> queryCategoryByYlGoodsIds(QueryGoodsCategoryRequest queryGoodsCategoryRequest);

    /**
     * 查询商品阶梯信息
     *
     * @param request
     * @return
     */
    List<ReportLadderGoodsInfoDTO> queryLadderInfo(QueryGoodsInfoRequest request);

    /**
     * 查询商品活动信息
     *
     * @param request
     * @return
     */
    List<ReportParamSubGoodsDTO> queryActivityGoodsInfo(QueryGoodsInfoRequest request);

    /**
     * 新增商品价格日志
     *
     * @param before
     * @param after
     * @param userId
     */
    void addPriceLog(AddGoodsYilingPriceLogRequest before, AddGoodsYilingPriceLogRequest after, Long userId);
}
