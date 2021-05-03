package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;


import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.NodeEntity;

import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public interface NodeDao extends AutoCloseable {
    void putNode(NodeEntity node) throws SQLException;

    default void close() throws SQLException {
    }
}
