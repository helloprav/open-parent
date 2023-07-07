package org.openframework.commons.shiksha.service.repository;

import java.util.List;

import org.openframework.commons.shiksha.entity.EvalStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvalStatsRepository extends JpaRepository<EvalStats, Long> {

	@Query("SELECT es FROM EvalStats es WHERE es.user.id = :userId order by es.evalEndDateTime desc")
	List<EvalStats> getByUserId(@Param("userId") Long userId);

}
