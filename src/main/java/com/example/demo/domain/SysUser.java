/*
 * Copyright (C) 2017 IFlyTek. All rights reserved.
 */
package com.example.demo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * <p>
 * <code>SysUser</code>
 * </p>
 * Description:
 *
 * @author Mcchu
 * @date 2018/4/19 19:28
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser {

	@GenericGenerator(name = "sysUserSeqGenerator",
			strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
			parameters = {
					@org.hibernate.annotations.Parameter(name = "sequence_name", value = "SYSUSER_SEQUENCE"),
					@org.hibernate.annotations.Parameter(name = "initial_value", value = "1000"),
					@org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
			})
	@Id
	@GeneratedValue(generator = "sysUserSeqGenerator")
	private String id;

	private String username;

	private String password;

	private String phoneNum;

	private Integer age;

	private Date birthDay;

	public SysUser(String name, String phoneNum) {
		this.username = name;
		this.phoneNum = phoneNum;
	}
}
