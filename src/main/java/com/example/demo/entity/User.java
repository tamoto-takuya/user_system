package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotEmpty(message = "ログインIDを入力してください！")
	@Pattern(regexp = "^([a-zA-Z0-9]{6,20})$", message ="半角英数字6文字以上、20文字以下にしてください！")
	@Column(name = "login_id")
	private String loginId;

	@Size(min = 1, max = 10, message ="10文字以下で入力してください！")
	@NotEmpty(message = "お名前を入力してください！")
	@Column(name = "name")
	private String name;

//	@Pattern(regexp = "^([a-zA-Z0-9]{6,20})$", message ="半角英数字6文字以上、20文字以下にしてください！")
	@Column(name = "pass")
	private String password;

	@NotNull(message = "所属を選んでください！")
	@OneToOne
	@JoinColumn(name = "branch_id")
	private Branch branch;

	@OneToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@Column(name = "is_active")
	private Integer is_active;


}
