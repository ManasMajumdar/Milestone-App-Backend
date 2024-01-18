package com.mindit.milestone.data.repository;

import com.mindit.milestone.data.entity.Milestone;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MilestoneRepository extends JpaRepository<Milestone, String> {
  Optional<Milestone> findById(String id);

  @Query(
      value =
          "SELECT * FROM milestone WHERE project_id = :id AND due_date = (SELECT MIN(due_date) FROM milestone WHERE project_id = :id)",
      nativeQuery = true)
  List<Milestone> checkId(String id);

  @Query(
      value = "SELECT * FROM milestone WHERE project_id= :id ORDER BY id ASC",
      nativeQuery = true)
  List<Milestone> check(String id);

  @Query(value = "SELECT name FROM milestone ORDER BY id ASC", nativeQuery = true)
  List<Object> checkName();

  @Query(value = "SELECT * FROM milestone WHERE id = :id", nativeQuery = true)
  List<Milestone> findId(String id);

  @Query(
      value = "SELECT * FROM milestone WHERE project_id = :id order by modified_at asc limit 1",
      nativeQuery = true)
  Optional<Milestone> findByProjectId(String id);
}
