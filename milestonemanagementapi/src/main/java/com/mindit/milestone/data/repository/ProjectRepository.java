package com.mindit.milestone.data.repository;

import com.mindit.milestone.data.entity.Project;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, String> {
  List<Project> findAll();

  @Query(value = "SELECT * FROM project WHERE owner =:email", nativeQuery = true)
  List<Project> findByOwner(String email);

  Optional<Project> findById(String id);

  @Query(value = "Select * from project where id=:id", nativeQuery = true)
  Project checkId(String id);

  @Query(value = "SELECT * FROM project  WHERE name LIKE %:name%", nativeQuery = true)
  List<Project> findByName(@Param("name") String name);
}
