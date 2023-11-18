package com.yiling.user.agreement.bo;

import java.io.Serializable;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 协议角色BO
 *
 * @author dexi.yao
 * @date 2021-06-11
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AgreementRoleBO implements Serializable {
	private static final long serialVersionUID = -9044217574008714L;

	/**
	 * eid
	 */
	private Long eid;

	/**
	 * 角色 1-乙方 2-丙方
	 */
	private Integer role;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof AgreementRoleBO)) {
			return false;
		}
		AgreementRoleBO that = (AgreementRoleBO) o;
		return eid.equals(that.eid) &&
				role.equals(that.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(eid, role);
	}
}
