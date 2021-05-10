package ru.nsu.bayramov.openstreetmapxmlparserservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.bayramov.openstreetmapxmlparserservice.exception.ResourceNotFoundException;
import ru.nsu.bayramov.openstreetmapxmlparserservice.model.dto.NodeDto;
import ru.nsu.bayramov.openstreetmapxmlparserservice.model.entity.NodeEntity;
import ru.nsu.bayramov.openstreetmapxmlparserservice.repository.NodeRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Bayramov Nizhad
 */
@Service
@RequiredArgsConstructor
public class NodeService {
    private final NodeRepository nodeRepository;

    public NodeDto create(NodeDto node) {
        NodeEntity nodeEntity = NodeConverter.dtoToEntity(node);
        return NodeConverter.entityToDto(nodeRepository.save(nodeEntity));
    }

    public NodeDto read(long id) {
        Optional<NodeEntity> nodeEntityOptional = nodeRepository.findById(id);
        if (!nodeEntityOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        return NodeConverter.entityToDto(nodeEntityOptional.get());
    }

    @Transactional
    public NodeDto update(long id, NodeDto nodeDto) {
        Optional<NodeEntity> nodeEntityOptional = nodeRepository.findById(id);
        if (!nodeEntityOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        NodeEntity nodeEntity = nodeEntityOptional.get();
        NodeConverter.updateEntityFromDto(nodeEntity, nodeDto);
        nodeEntity = nodeRepository.save(nodeEntity);

        return NodeConverter.entityToDto(nodeEntity);
    }

    public void delete(long id) {
        Optional<NodeEntity> nodeEntityOptional = nodeRepository.findById(id);
        if (!nodeEntityOptional.isPresent()) {
            throw new ResourceNotFoundException();
        }
        nodeRepository.delete(nodeEntityOptional.get());
    }

    public List<NodeDto> searchSlow(double latitude, double longitude, double radius) {
        return nodeRepository.getNodesInRangeOrderByDistAscSlow(latitude, longitude, radius).stream()
                .map(NodeConverter::entityToDto).collect(Collectors.toList());
    }

    public List<NodeDto> search(Double latitude, Double longitude, Double radius) {
        return nodeRepository.getNodesInRangeOrderByDistAsc(latitude, longitude, radius).stream()
                .map(NodeConverter::entityToDto).collect(Collectors.toList());
    }
}