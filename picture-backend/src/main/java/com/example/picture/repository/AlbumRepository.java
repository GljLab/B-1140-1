package com.example.picture.repository;

import com.example.picture.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByIsDefaultTrue();
    List<Album> findAllByOrderByDisplayOrderAscCreateTimeAsc();
    List<Album> findByNameContainingIgnoreCase(String keyword);
    boolean existsByName(String name);
}
