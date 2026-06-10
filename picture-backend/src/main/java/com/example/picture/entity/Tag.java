package com.example.picture.entity;

import lombok.Data;
import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer referenceCount = 0;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Picture> pictures = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createTime = new Date();
    }
}
