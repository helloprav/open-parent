package org.openframework.commons.ofds.service.repository;

import java.util.List;

import org.openframework.commons.ofds.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FunctionRepository extends JpaRepository<Function, Long> {

	@Modifying
	@Query(nativeQuery=true, value = "INSERT INTO FUNCTIONS (FUNCTION_NAME, IS_VALID, CREATED_BY, CREATED_DATE) VALUES ('MANAGE USERS', 1, '1', now())")
	int createFunctionForUser();

	@Modifying
	@Query(nativeQuery=true, value = "INSERT INTO FUNCTIONS (FUNCTION_NAME, IS_VALID, CREATED_BY, CREATED_DATE) VALUES ('MANAGE GROUPS', 1, '1', now())")
	int createFunctionForGroup();

	@Query("from Function f where f.isValid = :isValid")
	List<Function> findByIsValid(@Param("isValid") Boolean isValid);
}
