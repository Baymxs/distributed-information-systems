package ru.nsu.bayramov.openstreetmapxmlparserservice.service;

import ru.nsu.bayramov.openstreetmapxmlparserservice.model.dto.NodeDto;
import ru.nsu.bayramov.openstreetmapxmlparserservice.model.entity.NodeEntity;
import ru.nsu.bayramov.openstreetmapxmlparserservice.model.entity.TagEntity;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author Bayramov Nizhad
 */
public class NodeConverter {
    static void updateEntityFromDto(NodeEntity nodeEntity, NodeDto nodeDto) {
        nodeEntity.setLatitude(nodeDto.getLatitude());
        nodeEntity.setLongitude(nodeDto.getLongitude());
        nodeEntity.setUsername(nodeDto.getUsername());
        if (nodeDto.getTags() != null) {
            nodeEntity.setTags(nodeDto.getTags().entrySet().stream().map(keyAndValue -> {
                TagEntity tagEntity = new TagEntity();
                tagEntity.setNode(nodeEntity);
                tagEntity.setKey(keyAndValue.getKey());
                tagEntity.setValue(keyAndValue.getValue());
                return tagEntity;
            }).collect(Collectors.toList()));
        } else {
            nodeEntity.setTags(new ArrayList<>());
        }
    }

    static NodeEntity dtoToEntity(NodeDto nodeDto) {
        NodeEntity nodeEntity = new NodeEntity();
        updateEntityFromDto(nodeEntity, nodeDto);
        return nodeEntity;
    }

    static NodeDto entityToDto(NodeEntity nodeEntity) {
        NodeDto nodeDto = new NodeDto();
        nodeDto.setId(nodeEntity.getId());
        nodeDto.setLatitude(nodeEntity.getLatitude());
        nodeDto.setLongitude(nodeEntity.getLongitude());
        nodeDto.setUsername(nodeEntity.getUsername());
        nodeDto.setTags(nodeEntity.getTags().stream().collect(Collectors.toMap(TagEntity::getKey, TagEntity::getValue)));

        return nodeDto;
    }
}
