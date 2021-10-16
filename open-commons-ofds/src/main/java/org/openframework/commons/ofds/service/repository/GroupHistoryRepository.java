package org.openframework.commons.ofds.service.repository;

import java.util.List;

import org.openframework.commons.ofds.entity.GroupHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface GroupHistoryRepository extends JpaRepository<GroupHistory, Long> {

	List<GroupHistory> findByParentId(@Param("parentId") Long parentId);
}
