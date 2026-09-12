package com.backend.api.repositories;

import com.backend.api.entities.DiscardMaterialStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscardMaterialStatusHistoryRepository extends JpaRepository<DiscardMaterialStatusHistory, Long> {
    List<DiscardMaterialStatusHistory> findByDiscardMaterialIdOrderByChangedAtAsc(Long discardMaterialId);
}
