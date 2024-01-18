package com.mindit.milestone.data.repository;

import com.mindit.milestone.contants.enums.UserRole;
import com.mindit.milestone.data.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {

  Optional<User> findByEmail(String email);

  @Query(
      "SELECT u FROM User u WHERE "
          + "(:text IS NULL OR :text = '' OR "
          + "lower(u.firstName) LIKE lower(concat('%', :text, '%')) OR "
          + "lower(u.email) LIKE lower(concat('%', :text, '%'))) "
          + "AND u.role = :role "
          + "AND u.isDeleted = false")
  List<User> findByFirstNameOrEmailContainingIgnoreCaseAndRoleAndNotDeleted(
      @Param("text") String text, @Param("role") UserRole role);

  Optional<User> findById(String id);
}
