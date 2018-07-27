package com.sol.awesome.employee.domain;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "employeeNumber" }))
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@ApiModelProperty(required = true)
	@Size(max = 256)
	private String firstName;
	@ApiModelProperty(required = true)
	@Size(max = 256)
	private String lastName;
	@ApiModelProperty(required = true)
	@Size(max = 50)
	private String employeeNumber;
	@NotNull
	@ApiModelProperty(required = true, allowableValues = "Chicago,NewYork,BA")
	private Office office;

	@Size(max = 100)
	private String title;

	@Email
	@Size(max = 50)
	private String email;

	@Size(max = 2000)
	private String imageUrl;

}
