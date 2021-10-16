/**
 * 
 */
package org.openframework.commons.ofds.service.repository;

import org.openframework.commons.ofds.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author pmis30
 *
 */
public interface AdminUserRepository extends JpaRepository<User, Long> {

	@Modifying
    //@Query(nativeQuery=true, value = "INSERT INTO user (USERNAME, PASSWORD, FIRST_NAME, CREATED_DATE, IS_VALID, EMAIL, GENDER, STATUS, CREATED_BY) VALUES ('admin', 'uSJ90JFrx+gS7hePWC6R4w==', 'Admin', now(), 1, 'admin@myaapp.com', 'male', 'active', '1')")
	@Query(nativeQuery=true, value = "INSERT INTO user (USERNAME, PASSWORD, FIRST_NAME, IS_VALID, EMAIL, GENDER, STATUS, CREATED_BY, CREATED_DATE, IS_SUPERADMIN) VALUES ('admin', '5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8', 'Admin', 1, 'admin@myaapp.com', 'male', 'active', '1', now(), 1)")
	int createAdminUser();

}
