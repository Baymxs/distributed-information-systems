package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public interface TagDao extends AutoCloseable {
    void putTag(TagEntity tag) throws SQLException;

    default void close() throws SQLException {
    }
}
