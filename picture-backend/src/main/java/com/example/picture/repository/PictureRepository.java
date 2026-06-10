package com.example.picture.repository;

import com.example.picture.entity.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, Long> {
    @Query("SELECT p FROM Picture p JOIN p.albums a WHERE a.id = :albumId ORDER BY p.createTime DESC")
    List<Picture> findByAlbumId(@Param("albumId") Long albumId);

    @Query("SELECT p FROM Picture p JOIN p.tags t WHERE t.id = :tagId ORDER BY p.createTime DESC")
    List<Picture> findByTagId(@Param("tagId") Long tagId);

    @Query("SELECT p FROM Picture p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) ORDER BY p.createTime DESC")
    List<Picture> findByNameContainingKeyword(@Param("keyword") String keyword);

    @Query("SELECT COALESCE(SUM(p.size), 0) FROM Picture p")
    Long sumAllSizes();

    @Query("SELECT COALESCE(SUM(p.size), 0) FROM Picture p JOIN p.albums a WHERE a.id = :albumId")
    Long sumSizesByAlbumId(@Param("albumId") Long albumId);

    @Query("SELECT COUNT(p) FROM Picture p JOIN p.albums a WHERE a.id = :albumId")
    Long countByAlbumId(@Param("albumId") Long albumId);

    @Query("SELECT MAX(p.createTime) FROM Picture p JOIN p.albums a WHERE a.id = :albumId")
    java.util.Date findLastUploadTimeByAlbumId(@Param("albumId") Long albumId);

    List<Picture> findAllByOrderByCreateTimeDesc();
}
