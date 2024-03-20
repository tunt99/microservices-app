package com.microservices.app.repository;

import com.microservices.app.entity.AnalyticsEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsRepository extends JpaRepository<AnalyticsEntity, UUID>{

    @Query(value = "select e from AnalyticsEntity e where e.word=:word order by e.recordDate")
    List<AnalyticsEntity> getAnalyticsEntitiesByWord(String word, Pageable pageable);
}
