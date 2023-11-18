package com.yiling.sales.assistant.commissions.dto.request;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.yiling.framework.common.base.request.BaseRequest;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author dexi.yao
 * @date 2021-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class AddCommissionsToUserRequest extends BaseRequest {
	private static final long serialVersionUID = -6634625819479975912L;

	/**
	 * 用户id
	 */
	@NotNull
	private Long userId;

	/**
	 * 任务id
	 */
	@NotNull
	private Long taskId;

	/**
	 * 任务名称
	 */
	@NotNull
	private String taskName;

	/**
	 * 用户任务id
	 */
	@NotNull
	private Long userTaskId;

	/**
	 * 任务类型：1-交易额 2-交易量 3-新用户推广 4-促销推广 5-会议推广 6-学术推广 7-新人推广 8-会员推广-购买
	 */
	@NotNull
	private Integer finishType;

    /**
     * 佣金来源 1-任务收益 2-下线推广
     */
	@NotNull
	private Integer sources;

	/**
	 * 佣金是否有效 1-无效 2-有效
	 * 任务完成时传2，否则传1
	 */
	@NotNull
	private Integer effectStatus;

	/**
	 * 佣金明细
	 */
	@NotNull
	private List<AddCommToUserDetailRequest> detailList;

	@Data
	@EqualsAndHashCode(callSuper = true)
	@Accessors(chain = true)
	public static  class AddCommToUserDetailRequest extends BaseRequest{

		/**
		 * 佣金金额
		 */
		@NotNull
		private BigDecimal subAmount;

		/**
		 * 订单id
		 * 任务类型：1-交易额 2-交易量时必填
		 */
		private Long orderId;

		/**
		 * 订单编号
		 * 任务类型：1-交易额 2-交易量时必填
		 */
		private String orderCode;

		/**
		 * 拉新企业id
		 * 任务类型： 3-新用户推广时必填
		 */
		private Long newEntId;

		/**
		 * 拉新企业名称
		 * 任务类型： 3-新用户推广时必填
		 */
		private String newEntName;

		/**
		 * 拉新用户id
		 * 任务类型： 7-新人推广时必填
		 */
		private Long newUserId;

		/**
		 * 拉新用户名
		 * 任务类型： 7-新人推广时必填
		 */
		private String newUserName;

		/**
		 * 拉新用户id
		 * 任务类型： 8-购买会员时必填
		 */
		private Long buyMemberEid;

		/**
		 * 拉新用户名
		 * 任务类型： 8-购买会员时必填
		 */
		private String buyMemberName;

		/**
		 * 拉新时间
		 * 拉新类型时必填
		 */
		private Date newTime;

        /**
         * 第一次资料上传时间
         */
        private Date firstUploadTime;

	}

}
