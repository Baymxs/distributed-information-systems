package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bayramov Nizhad
 */
public class TagDaoBatchSql implements TagDao {
    private final List<TagEntity> tagsToInsert = new ArrayList<>();

    @Override
    public void putTag(TagEntity tag) throws SQLException {
        tagsToInsert.add(tag);
        if (tagsToInsert.size() >= DbConnection.BATCH_SIZE) {
            putTags(tagsToInsert);
            tagsToInsert.clear();
        }
    }

    public void putTags(List<TagEntity> tags) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO tags (node_id, key, value) " +
                "VALUES (?, ?, ?)");
        for (TagEntity tag : tags) {
            statement.setLong(1, tag.getNodeId());
            statement.setString(2, tag.getKey());
            statement.setString(3, tag.getValue());
            statement.addBatch();
        }
        statement.executeBatch();
    }

    @Override
    public void close() throws SQLException {
        putTags(tagsToInsert);
        tagsToInsert.clear();
    }
}
