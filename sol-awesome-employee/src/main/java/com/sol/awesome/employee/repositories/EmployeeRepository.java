package com.sol.awesome.employee.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.sol.awesome.employee.domain.Employee;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "Employee")
@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    @ApiOperation("Find employees by set of ids")
    @ApiResponses({@ApiResponse(code=200, message="Success")})
    List<Employee> findByIdIn(@Param("ids") @ApiParam(value="Employee IDs", required = true) Set<Long> ids);

}
