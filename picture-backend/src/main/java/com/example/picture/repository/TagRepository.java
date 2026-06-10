package com.example.picture.repository;

import com.example.picture.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    List<Tag> findByNameContainingIgnoreCaseOrderByReferenceCountDesc(String keyword);
    List<Tag> findAllByOrderByReferenceCountDescCreateTimeDesc();
    @Query("SELECT t FROM Tag t WHERE t.referenceCount = 0")
    List<Tag> findUnusedTags();
    @Query("SELECT t FROM Tag t ORDER BY t.referenceCount DESC, t.createTime DESC")
    List<Tag> findTop10ByOrderByReferenceCountDesc(org.springframework.data.domain.Pageable pageable);
}
