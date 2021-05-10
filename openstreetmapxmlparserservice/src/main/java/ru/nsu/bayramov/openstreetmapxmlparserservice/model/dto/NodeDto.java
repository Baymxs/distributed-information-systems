package ru.nsu.bayramov.openstreetmapxmlparserservice.model.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author Bayramov Nizhad
 */
@Data
public class NodeDto {
    private long id;

    private String username;

    private double longitude;

    private double latitude;

    private Map<String, String> tags;
}