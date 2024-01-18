package com.mindit.milestone.data.repository;

import com.mindit.milestone.data.entity.Audience;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Integer> {
  @Query("SELECT a.email FROM Audience a WHERE a.type = 'CC'")
  List<String> getAllEmailsWhoseTypeIsCC();

  @Query("SELECT a.email FROM Audience a WHERE a.type = 'TO'")
  List<String> getAllEmailsWhoseTypeIsTO();

  List<Audience> findByEmail(String email);
}
