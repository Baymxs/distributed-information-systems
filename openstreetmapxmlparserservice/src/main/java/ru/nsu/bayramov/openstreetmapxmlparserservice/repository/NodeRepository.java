package ru.nsu.bayramov.openstreetmapxmlparserservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.nsu.bayramov.openstreetmapxmlparserservice.model.entity.NodeEntity;

import java.util.List;

/**
 * @author Bayramov Nizhad
 */
public interface NodeRepository extends CrudRepository<NodeEntity, Long> {
    @Query(value = "SELECT * FROM nodes " +
            "WHERE earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude)) < ?3 " +
            "ORDER BY earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude)) ASC",
            nativeQuery = true)
    List<NodeEntity> getNodesInRangeOrderByDistAscSlow(double latitude, double longtitude, double radius);

    @Query(value = "SELECT * FROM nodes " +
            "WHERE " +
            "(earth_distance(ll_to_earth(0, 0), ll_to_earth(nodes.latitude, nodes.longitude)) < ?3 + earth_distance(ll_to_earth(?1, ?2), ll_to_earth(0, 0)))\n" +
            "AND (earth_distance(ll_to_earth(0, 90), ll_to_earth(nodes.latitude, nodes.longitude)) < ?3 + earth_distance(ll_to_earth(?1, ?2), ll_to_earth(0, 90)))\n" +
            "AND earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude)) < ?3\n " +
            "ORDER BY earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude)) ASC",
            nativeQuery = true)
    List<NodeEntity> getNodesInRangeOrderByDistAsc(double latitude, double longtitude, double radius);
}
