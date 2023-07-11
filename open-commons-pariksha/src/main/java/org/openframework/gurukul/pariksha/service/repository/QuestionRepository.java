package org.openframework.gurukul.pariksha.service.repository;

import org.openframework.gurukul.pariksha.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionRepository extends JpaRepository<Question, Long> {

	@Query("SELECT e FROM Question e JOIN FETCH e.answers WHERE e.id = (:id)")
	public Question findByIdAndFetchQuestionsEagerly(@Param("id") Long id);

}
