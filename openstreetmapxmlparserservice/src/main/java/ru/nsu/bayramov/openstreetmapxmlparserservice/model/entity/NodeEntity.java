package ru.nsu.bayramov.openstreetmapxmlparserservice.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author Bayramov Nizhad
 */
@Data
@Entity
@Table(name = "nodes")
public class NodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "longitude")
    private double longitude;

    @Column(nullable = false, name = "latitude")
    private double latitude;

    @OneToMany(mappedBy = "node", cascade = CascadeType.ALL)
    private List<TagEntity> tags;
}
