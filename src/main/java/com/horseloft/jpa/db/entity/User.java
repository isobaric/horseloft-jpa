package com.horseloft.jpa.db.entity;

import com.horseloft.jpa.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.Date;

/**
 * Date: 2020/1/7 15:19
 * User: YHC
 * Desc: 用户
 */
@Getter
@Setter
@Entity
@DynamicInsert
@DynamicUpdate //使数据库 update_time 自动更新
@Table(name = "jpa_user")
public class User extends BaseEntity {

	//账号
	private String account;

	//密码
	private String password;

	//密码是否已修改：0未修改，1已修改【首次登陆需要修改密码】
	private Boolean passwordStatus;

	//姓名
	private String realName;

	//账号状态：0无效，1正常【无效账号不可登录】
	private Boolean accountStatus;

	//工作状态：0离职，1在职【离职账号不可登录】
	private Boolean jobStatus;

	//角色ID jpa_role表ID 逗号分隔的字符串，逗号结尾【空不可登录】
	private String roleId;

	//公司ID
	private Long companyId;

	//工厂ID
	private Long factoryId;

	//车间ID
	private Long workshopId;

	//token秘钥
	private String secretKey;

	//token
	private String token;

	//性别：0女，1男
	private Boolean sex;

	//手机号
	private String mobile;

	//户籍：0农村，1城镇
	private Boolean householdRegister;

	//身份证号
	private String IdNumber;

	//生日
	private Date birthday;

	//入职日期
	private Date entryDate;

	//住址
	private String address;

	//备注
	private String remark;

	//银行账户 json格式
	private String bankAccount;

	//登录过期时间|毫秒时间戳
	private Long expireTime;

	//是否为7天自动登陆
	private Integer autoLoginStatus;
}
