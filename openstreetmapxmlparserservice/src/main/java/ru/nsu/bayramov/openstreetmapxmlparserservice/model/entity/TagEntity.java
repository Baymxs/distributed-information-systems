package ru.nsu.bayramov.openstreetmapxmlparserservice.model.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * @author Bayramov Nizhad
 */
@Data
@Entity
@Table(name = "tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private long id;

    @Column(nullable = false, name = "key")
    private String key;

    @Column(nullable = false, name = "value")
    private String value;

    @ManyToOne
    @JoinColumn(name="node_id", nullable=false)
    private NodeEntity node;
}
