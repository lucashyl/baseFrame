package com.lucas.shop.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Sets;
import com.lucas.admin.base.DataEntity;
import com.lucas.admin.entity.Menu;
import com.lucas.admin.entity.Role;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

/**
 * <p>
 * 
 * </p>
 *
 */
@TableName("shop_member")
public class ShopMember {

    private static final long serialVersionUID = 1L;

	@TableField(value = "id",strategy= FieldStrategy.IGNORED)
    private Long id;


	@TableField(value = "nick_name",strategy= FieldStrategy.IGNORED)
	private String nickName;

	@TableField(value = "phone",strategy= FieldStrategy.IGNORED)
	private String phone;

	@TableField(value = "status",strategy= FieldStrategy.IGNORED)
	private String status;

	@TableField(value = "create_time",strategy= FieldStrategy.IGNORED)
	private Date createTime;

	@TableField(value = "vx",strategy= FieldStrategy.IGNORED)
	private String vx;

	@TableField(value = "invite_code",strategy= FieldStrategy.IGNORED)
	private String inviteCode;

	@TableField(value = "balancer",strategy= FieldStrategy.IGNORED)
	private BigDecimal balancer;

	@TableField(value = "integral",strategy= FieldStrategy.IGNORED)
	private BigDecimal integral;

	@TableField(value = "true_name",strategy= FieldStrategy.IGNORED)
	private String trueName;

	@TableField(value = "id_card",strategy= FieldStrategy.IGNORED)
	private String idCard;

	@TableField(value = "password",strategy= FieldStrategy.IGNORED)
	private String password;

	@TableField(value = "icon",strategy= FieldStrategy.IGNORED)
	private String icon;

	@TableField(value = "ali_account",strategy= FieldStrategy.IGNORED)
	private String aliAccount;

	@TableField(value = "salt",strategy= FieldStrategy.IGNORED)
	private String salt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getVx() {
		return vx;
	}

	public void setVx(String vx) {
		this.vx = vx;
	}

	public String getInviteCode() {
		return inviteCode;
	}

	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}

	public BigDecimal getBalancer() {
		return balancer;
	}

	public void setBalancer(BigDecimal balancer) {
		this.balancer = balancer;
	}

	public BigDecimal getIntegral() {
		return integral;
	}

	public void setIntegral(BigDecimal integral) {
		this.integral = integral;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getAliAccount() {
		return aliAccount;
	}

	public void setAliAccount(String aliAccount) {
		this.aliAccount = aliAccount;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
}
