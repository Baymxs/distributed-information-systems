package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.generated.Node;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class NodeEntity {
    private long id;
    private String user;
    private Double longitude;
    private Double latitude;
    private List<TagEntity> tags;

    public static NodeEntity createFromXml(Node node) {
        return new NodeEntity(node.getId(), node.getUser(), node.getLon(), node.getLat(),
                node.getTag().stream()
                        .map(tag -> TagEntity.createFromXml(tag, node.getId()))
                        .collect(Collectors.toList()));
    }
}

