/**
 * 
 */
package org.openframework.commons.ofds.service.repository;

import java.util.Date;
import java.util.List;

import org.openframework.commons.ofds.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author pmis30
 *
 */
public interface UserRepository extends JpaRepository<User, Long> {

	/**
	 * Refer
	 * [http://stackoverflow.com/questions/2123438/hibernate-how-to-set-null-query-parameter-value-with-hql]
	 * for null parameter at runtime
	 * 
	 * @return
	 */

	@Query("from User u LEFT JOIN FETCH u.userGroups ug LEFT JOIN FETCH ug.group g where u.id = :id")
	List<User> findByIdWithGroups(@Param("id") Long id);

    @Query("from User u where u.username = :username")
	List<User> usernameExists(@Param("username") String username);

    @Query("from User u where u.username = :username and u.id != :id")
	List<User> usernameExists(@Param("username") String username, @Param("id") Long id);

	List<User> findByEmail(@Param("email") String email);

	Page<User> findByIsValid(@Param("isValid") Boolean isValid, Pageable paging);

	Page<User> findByIsValidTrue(Pageable paging);

	List<User> findByIsValidFalse();

	@Query("select u from User u where (u.username is not null and u.username = :username) or (u.email is not null and u.email = :email) or u.mobile is not null and u.mobile=:mobile")
	List<User> findByUsernameOrEmailOrMobile(@Param("email") String email, @Param("username") String username, @Param("mobile") String mobile);

	@Query("from User u left join fetch u.userGroups ug join fetch ug.group where u.id=:id")
	User findUserGroupsAndFunctions(@Param("id") Long id);

	@Modifying
	@Query("update User p set p.isValid = :isValid, p.modifiedBy.id = :userId, p.modifiedDate = :modifiedDate where p.id=:id")
	Integer updateStatus(@Param("id") Long id, @Param("isValid") Boolean isValid, @Param("userId") Long userId,
			@Param("modifiedDate") Date modifiedDate);

}
