/**
 * 
 */
package org.openframework.commons.ofds.service.repository;

import org.openframework.commons.ofds.entity.UserHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author pmis30
 *
 */
public interface UserHistoryRepository extends JpaRepository<UserHistory, Long> {

	/**
	 * Refer
	 * [http://stackoverflow.com/questions/2123438/hibernate-how-to-set-null-query-parameter-value-with-hql]
	 * for null parameter at runtime
	 * 
	 * @return
	 */

	Page<UserHistory> findByParentId(@Param("userId") Long userId, Pageable paging);

}
