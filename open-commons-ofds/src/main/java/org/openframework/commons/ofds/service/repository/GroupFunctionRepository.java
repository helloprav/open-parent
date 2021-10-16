package org.openframework.commons.ofds.service.repository;

import org.openframework.commons.ofds.entity.GroupFunction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GroupFunctionRepository extends JpaRepository<GroupFunction, Long> {

	@Modifying
    @Query("delete from GroupFunction gf where gf.group.id=:groupID")
    void deleteByGroupID(@Param("groupID") long groupID);

}
