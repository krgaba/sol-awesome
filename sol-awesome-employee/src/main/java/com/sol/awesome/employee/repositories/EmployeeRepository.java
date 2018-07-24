package com.sol.awesome.employee.repositories;

import com.sol.awesome.employee.domain.Employee;
import io.swagger.annotations.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Set;

@Api(tags = "Employee")
@RepositoryRestResource(collectionResourceRel = "employees", path = "employees")
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    @ApiOperation("Find employees by set of ids")
    @ApiResponses({@ApiResponse(code=200, message="Success", response=List.class)})
    List<Employee> findByIdIn(@Param("ids") @ApiParam(value="Employee IDs", required = true) Set<Long> ids);

}
