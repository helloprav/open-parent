package org.openframework.commons.shiksha.service.repository;

import org.openframework.commons.shiksha.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

	@Query("SELECT e FROM Question e JOIN FETCH e.answers WHERE e.id = (:id)")
	public Answer findByIdAndFetchQuestionsEagerly(@Param("id") Long id);

}
