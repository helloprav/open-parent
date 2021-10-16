package org.openframework.commons.ofds.service.repository;

import java.util.Date;
import java.util.List;

import org.openframework.commons.ofds.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupRepository extends JpaRepository<Group, Long> {

	@Query("from Group g LEFT JOIN FETCH g.groupFunctions gf LEFT JOIN FETCH gf.function f where g.id = :id")
	List<Group> findByIdWithFunctions(@Param("id") Long id);

	@Query("from Group g where g.groupName = :groupName")
	Group findByGroupName(@Param("groupName") String groupName);

	@Query("from Group g where g.isValid = :isValid")
	Page<Group> findByIsValid(@Param("isValid") Boolean isValid, Pageable paging);

	@Modifying
	@Query("update Group p set p.isValid = :isValid, p.modifiedBy.id = :userId, p.modifiedDate = :modifiedDate where p.id=:id")
	Integer updateStatus(@Param("id") Long id, @Param("isValid") Boolean isValid, @Param("userId") Long userId,
			@Param("modifiedDate") Date modifiedDate);
}
