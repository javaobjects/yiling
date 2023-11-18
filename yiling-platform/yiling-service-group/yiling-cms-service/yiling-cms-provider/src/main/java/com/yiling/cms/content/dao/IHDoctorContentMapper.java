package com.yiling.cms.content.dao;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yiling.cms.content.dto.AppContentDTO;
import com.yiling.cms.content.dto.IHDocContentDTO;
import com.yiling.cms.content.dto.request.QueryAppContentPageRequest;
import com.yiling.cms.content.dto.request.QueryIHDocContentPageRequest;
import com.yiling.cms.content.entity.IHDoctorContentDO;
import com.yiling.framework.common.base.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * IH医生端内容表 Dao 接口
 * </p>
 *
 * @author fan.shen
 * @date 2022-11-07
 */
@Repository
public interface IHDoctorContentMapper extends BaseMapper<IHDoctorContentDO> {

    /**
     * 分页查询IHDoc端内容
     *
     * @param page
     * @param request
     * @return
     */
    Page<IHDocContentDTO> listPage(Page<IHDoctorContentDO> page, @Param("request") QueryIHDocContentPageRequest request);

    /**
     * IHDoc端查询内容
     * @param page
     * @param request
     * @return
     */
    Page<AppContentDTO> listAppContentPageBySql(Page<AppContentDTO> page, @Param("request") QueryAppContentPageRequest request);
}
