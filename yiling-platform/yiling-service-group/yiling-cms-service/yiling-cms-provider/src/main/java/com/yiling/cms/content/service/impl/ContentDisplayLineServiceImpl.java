package com.yiling.cms.content.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yiling.cms.content.dto.request.ContentRankRequest;
import org.springframework.stereotype.Service;

import com.yiling.cms.content.service.ContentDisplayLineService;
import com.yiling.framework.common.base.BaseServiceImpl;
import com.yiling.cms.content.dao.ContentDisplayLineMapper;
import com.yiling.cms.content.entity.ContentDisplayLineDO;

import java.util.Objects;

/**
 * <p>
 * 内容引用业务线 服务实现类
 * </p>
 *
 * @author gxl
 * @date 2022-03-24
 */
@Service
public class ContentDisplayLineServiceImpl extends BaseServiceImpl<ContentDisplayLineMapper, ContentDisplayLineDO> implements ContentDisplayLineService {

}
