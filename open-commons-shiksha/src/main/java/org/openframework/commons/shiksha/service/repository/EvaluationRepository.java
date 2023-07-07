package org.openframework.commons.shiksha.service.repository;

import java.util.Date;
import java.util.List;

import org.openframework.commons.shiksha.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

	@Query("SELECT e FROM Evaluation e WHERE e.isValid=true")
	public List<Evaluation> findAllActive();

	@Query("SELECT e FROM Evaluation e JOIN FETCH e.questions WHERE e.id = (:id)")
	public Evaluation findByIdAndFetchQuestionsEagerly(@Param("id") Long id);

	@Modifying
	@Query("update Evaluation e set e.isValid = :isValid, e.modifiedBy.id = :userId, e.modifiedDate = :modifiedDate where e.id=:id")
	Integer updateStatusById(@Param("id") Long id, @Param("isValid") Boolean isValid, @Param("userId") Long userId,
			@Param("modifiedDate") Date modifiedDate);

	@Query("SELECT e FROM Evaluation e WHERE e.isValid=true and e.evalGroup IN (:evalGroups)")
	public List<Evaluation> findEvaluationsByGroups(@Param("evalGroups") List<String> evalGroups);

}
