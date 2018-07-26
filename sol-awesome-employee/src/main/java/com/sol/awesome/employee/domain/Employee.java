package com.sol.awesome.employee.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Employee")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 256)
    private String firstName;

    @Size(max = 256)
    private String lastName;

    @Size(max = 50)
    private String employeeNumber;

    @ApiModelProperty(allowableValues = "Chicago,NewYork,BA")
    private Office office;

    @Size(max = 100)
    private String title;

    @Email
    @Size(max = 50)
    private String email;

    @Size(max = 2000)
    private String imageUrl;

}
