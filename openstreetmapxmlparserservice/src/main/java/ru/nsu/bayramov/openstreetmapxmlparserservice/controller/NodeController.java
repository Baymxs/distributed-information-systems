package ru.nsu.bayramov.openstreetmapxmlparserservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.nsu.bayramov.openstreetmapxmlparserservice.model.dto.NodeDto;
import ru.nsu.bayramov.openstreetmapxmlparserservice.service.NodeService;

import java.util.List;

/**
 * @author Bayramov Nizhad
 */
@RestController
@RequestMapping("/api/v1/node")
@RequiredArgsConstructor
public class NodeController {
    private final NodeService crudService;

    @GetMapping("/{id}")
    NodeDto getNode(@PathVariable("id") long id) {
        return crudService.read(id);
    }

    @PostMapping("/")
    NodeDto createNode(@RequestBody NodeDto node) {
        return crudService.create(node);
    }

    @PutMapping("/{id}")
    NodeDto updateNode(@PathVariable("id") Long id,
                       @RequestBody NodeDto node) {
        return crudService.update(id, node);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        crudService.delete(id);
    }

    @GetMapping("/searchSlow")
    public List<NodeDto> searchSlow(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius
    ) {
        return crudService.searchSlow(latitude, longitude, radius);
    }

    @GetMapping("/search")
    public List<NodeDto> search(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius
    ) {
        return crudService.search(latitude, longitude, radius);
    }
}