package com.yiling.user.esb.service;

import java.util.List;

import com.yiling.user.esb.bo.EsbBusinessOrgTreeBO;
import com.yiling.user.esb.bo.SimpleEsbBzOrgBO;
import com.yiling.user.esb.dto.request.DeleteBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.SaveBusinessOrganizationRequest;
import com.yiling.user.esb.dto.request.UpdateTargetStatusRequest;
import com.yiling.user.esb.entity.EsbBusinessOrganizationDO;
import com.yiling.framework.common.base.BaseService;
import com.yiling.user.esb.enums.EsbBusinessOrganizationTagTypeEnum;

/**
 * <p>
 * esb业务架构 服务类
 * </p>
 *
 * @author lun.yu
 * @date 2023-04-12
 */
public interface EsbBusinessOrganizationService extends BaseService<EsbBusinessOrganizationDO> {

    /**
     * 生成业务架构
     *
     * @param request
     * @return
     */
    boolean saveBusinessOrg(SaveBusinessOrganizationRequest request);

    /**
     * 设置是否可以指标上传
     *
     * @param request
     * @return
     */
    boolean setTargetStatus(UpdateTargetStatusRequest request);

    /**
     * 获取业务架构树
     *
     * @return
     */
    List<EsbBusinessOrgTreeBO> queryBzOrgTree();

    /**
     * 获取ESB业务架构 事业部/省区/区办 所有的数据
     *
     * @param tagTypeEnum 1-事业部 2-业务省区 3-区办
     * @return
     */
    List<SimpleEsbBzOrgBO> getBzOrgListByTagType(EsbBusinessOrganizationTagTypeEnum tagTypeEnum);

    /**
     * 根据部门ID以及打标类型，获取指定层级业务架构
     *
     * @param orgId 部门ID
     * @param tagTypeEnum 1-事业部 2-业务省区 3-区办
     * @return
     */
    List<SimpleEsbBzOrgBO> getBzOrgListByOrgId(Long orgId, EsbBusinessOrganizationTagTypeEnum tagTypeEnum);

    /**
     * 删除打标
     *
     * @param request
     * @return
     */
    boolean deleteTag(DeleteBusinessOrganizationRequest request);
}
