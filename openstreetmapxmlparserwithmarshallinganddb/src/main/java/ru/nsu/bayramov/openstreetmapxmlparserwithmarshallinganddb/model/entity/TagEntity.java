package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.generated.Tag;

@Data
@AllArgsConstructor
public class TagEntity {
    private long nodeId;
    private String key;
    private String value;

    public static TagEntity createFromXml(Tag tag, long nodeId) {
        return new TagEntity(nodeId, tag.getK(), tag.getV());
    }
}
