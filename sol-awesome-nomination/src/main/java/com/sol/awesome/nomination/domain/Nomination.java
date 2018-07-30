package com.sol.awesome.nomination.domain;

import java.sql.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor

public class Nomination {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Date date;

	@ApiModelProperty(required = true)
	@NotNull
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "EMPLOYEE_ID")),
			@AttributeOverride(name = "firstName", column = @Column(name = "EMPLOYEE_FIRST_NAME")),
			@AttributeOverride(name = "lastName", column = @Column(name = "EMPLOYEE_LAST_NAME")) })
	private AwesomeEmployee employee;

	@ApiModelProperty(required = true)
	@NotNull
	@Embedded
	@AttributeOverrides({ @AttributeOverride(name = "id", column = @Column(name = "NOMINATED_BY_ID")),
			@AttributeOverride(name = "firstName", column = @Column(name = "NOMINATED_BY_FIRST_NAME")),
			@AttributeOverride(name = "lastName", column = @Column(name = "NOMINATED_BY_LAST_NAME")) })
	private AwesomeEmployee nominatedBy;
	
	@ApiModelProperty(required = true)
	@NotNull
	// Coma delimeted Solstice principles
	private String principleGroup;

	@ApiModelProperty(required = true)
	private String description;

}
