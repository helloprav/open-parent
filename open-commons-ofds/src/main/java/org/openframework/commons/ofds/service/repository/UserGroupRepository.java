package org.openframework.commons.ofds.service.repository;

import org.openframework.commons.ofds.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//TODO Understand why this @Transactional is required
//@Transactional(propagation= Propagation.REQUIRED)
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

	@Modifying
    @Query("delete from UserGroup ug where ug.user.id=:userID")
    void deleteByUserID(@Param("userID") long userID);

}
