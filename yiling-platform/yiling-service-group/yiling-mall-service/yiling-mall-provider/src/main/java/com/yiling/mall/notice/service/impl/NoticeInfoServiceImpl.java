package com.yiling.mall.notice.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.framework.common.base.request.QueryPageListRequest;
import com.yiling.framework.common.enums.EnableStatusEnum;
import com.yiling.framework.common.util.Constants;
import com.yiling.framework.common.util.PojoUtils;
import com.yiling.mall.notice.dao.NoticeInfoMapper;
import com.yiling.mall.notice.dto.NoticeBeforeAfterDTO;
import com.yiling.mall.notice.dto.NoticeInfoDTO;
import com.yiling.mall.notice.dto.request.QueryNoticeInfoPageRequest;
import com.yiling.mall.notice.dto.request.SaveNoticeInfoRequest;
import com.yiling.mall.notice.entity.NoticeInfoDO;
import com.yiling.mall.notice.service.NoticeInfoService;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;

/**
 * <p>
 * 公告栏信息 服务实现类
 * </p>
 *
 * @author wei.wang
 * @date 2021-06-15
 */
@Service
public class NoticeInfoServiceImpl extends BaseServiceImpl<NoticeInfoMapper, NoticeInfoDO> implements NoticeInfoService {

    /**
     * 后台获取公告列表
     *
     * @param request
     * @return
     */
    @Override
    public Page<NoticeInfoDTO> getNoticeInfoPage(QueryNoticeInfoPageRequest request) {
        QueryWrapper<NoticeInfoDO> wrapper = new QueryWrapper<>();
        if (request.getState() != null && request.getState() != 0) {
            wrapper.lambda().eq(NoticeInfoDO::getState, request.getState());
        }
        if (StringUtils.isNotEmpty(request.getTitle())) {
            wrapper.lambda().like(NoticeInfoDO::getTitle, request.getTitle());
        }
        if (request.getStartCreateTime() != null) {
            wrapper.lambda().ge(NoticeInfoDO::getCreateTime, DateUtil.beginOfDay(request.getStartCreateTime()));
        }
        if (request.getEndCreateTime() != null) {
            wrapper.lambda().le(NoticeInfoDO::getCreateTime, DateUtil.endOfDay(request.getEndCreateTime()));
        }
        if(request.getEid() != null && request.getEid() != 0){
            wrapper.lambda().eq(NoticeInfoDO::getEid, request.getEid());
        }
        wrapper.lambda().orderByDesc(NoticeInfoDO::getCreateTime);
        Page<NoticeInfoDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<NoticeInfoDTO> dtoPage = PojoUtils.map(page, NoticeInfoDTO.class);
        return dtoPage;
    }

    /**
     * POP前台获取公告信息
     *
     * @param request
     * @return
     */
    @Override
    public Page<NoticeInfoDTO> getAvailableNoticeInfoPage(QueryPageListRequest request) {
        QueryWrapper<NoticeInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NoticeInfoDO::getState, 1)
                .eq(NoticeInfoDO :: getEid,Constants.YILING_EID)
                .le(NoticeInfoDO::getStartTime, new Date())
                .ge(NoticeInfoDO::getEndTime, new Date())
                .orderByDesc(NoticeInfoDO::getCreateTime);
        Page<NoticeInfoDO> page = this.page(new Page<>(request.getCurrent(), request.getSize()), wrapper);
        Page<NoticeInfoDTO> dtoPage = PojoUtils.map(page, NoticeInfoDTO.class);
        return dtoPage;
    }

    /**
     * POP前台首页获取公告
     *
     * @param number
     * @return
     */
    @Override
    public List<NoticeInfoDTO> getAvailableNoticeInfoList(Integer number) {
        QueryWrapper<NoticeInfoDO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(NoticeInfoDO::getState, 1)
                .le(NoticeInfoDO::getStartTime, new Date())
                .ge(NoticeInfoDO::getEndTime, new Date())
                .orderByDesc(NoticeInfoDO::getCreateTime);
        if (-1 != number) {
            String limit = " limit " + number;
            wrapper.last(limit);
        }
        List<NoticeInfoDO> list = list(wrapper);
        return PojoUtils.map(list,NoticeInfoDTO.class);
    }

    @Override
    public Boolean saveNoticeInfo(SaveNoticeInfoRequest request) {
        if(request.getEid() == null){
            request.setEid(Constants.YILING_EID);
        }

        NoticeInfoDO result = PojoUtils.map(request, NoticeInfoDO.class);
        if(Objects.nonNull(request.getStartTime())){
            result.setStartTime(DateUtil.beginOfDay(request.getStartTime()));
        }
        if(Objects.nonNull(request.getEndTime())){
            result.setEndTime(DateUtil.endOfDay(request.getEndTime()));
        }

        if(Objects.nonNull(request.getId())){
            NoticeInfoDO noticeInfoDO = this.getById(request.getId());
            if(Objects.isNull(noticeInfoDO)){
                return false;
            }
            return this.updateById(result);
        }else{
            return save(result);
        }
    }

    @Override
    public NoticeBeforeAfterDTO getBeforeAfterNotice(Long id) {
        NoticeBeforeAfterDTO noticeBeforeAfterDTO = new NoticeBeforeAfterDTO();
        LambdaQueryWrapper<NoticeInfoDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(NoticeInfoDO::getState, EnableStatusEnum.ENABLED.getCode());
        wrapper.eq(NoticeInfoDO::getEid,Constants.YILING_EID);
        wrapper.le(NoticeInfoDO::getStartTime, new Date());
        wrapper.ge(NoticeInfoDO::getEndTime, new Date());
        wrapper.orderByDesc(NoticeInfoDO::getCreateTime);
        List<NoticeInfoDO> list = this.list(wrapper);

        List<Long> idList = list.stream().map(NoticeInfoDO::getId).collect(Collectors.toList());
        Map<Long,String> titleMap = list.stream().collect(Collectors.toMap(NoticeInfoDO::getId,NoticeInfoDO::getTitle,(k1,k2) -> k2));

        if(CollUtil.isNotEmpty(idList)){
            int index = idList.indexOf(id);
            Long afterId = index < idList.size() - 1 ? idList.get(index + 1) : null;
            Long beforeId = index > 0 ? idList.get(index -1) : null;

            String beforeTitle = titleMap.get(beforeId);
            String afterTitle = titleMap.get(afterId);

            noticeBeforeAfterDTO.setBeforeId(beforeId).setBeforeTitle(beforeTitle).setAfterId(afterId).setAfterTitle(afterTitle);
        }
        return noticeBeforeAfterDTO;
    }


}
