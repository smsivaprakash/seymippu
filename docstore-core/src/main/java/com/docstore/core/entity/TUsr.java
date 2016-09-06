package com.docstore.core.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.validator.constraints.Length;

@Entity
@Table(name = "t_usr", uniqueConstraints = @UniqueConstraint(columnNames = { "usr_id" }))
public class TUsr implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "usr_id", nullable = false, length = 255)
	private Integer usrId;
	
	@Length(max = 200)
	@Column(name = "usr_name", nullable = false, length = 200)
	private String usrName;
	
	@Length(max = 500)
	@Column(name = "email", nullable = false, length = 500)
	private String email;
	
	@Length(max = 20)
	@Column(name = "password", nullable = false, length = 20)
	private String password;
	
	@Length(max = 75)
	@Column(name = "first_name", nullable = false, length = 75)
	private String firstName;
	
	@Length(max = 75)
	@Column(name = "last_name", nullable = false, length = 75)
	private String lastName;

	public Integer getUsrId() {
		return usrId;
	}

	public void setUsrId(final Integer usrId) {
		this.usrId = usrId;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(final String usrName) {
		this.usrName = usrName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(final String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

}
