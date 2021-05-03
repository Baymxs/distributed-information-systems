package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public class TagDaoPreparedSql implements TagDao {
    PreparedStatement statement;

    public TagDaoPreparedSql() throws SQLException {
        statement = DbConnection.getConnection().prepareStatement("INSERT INTO tags (node_id, key, value) " +
                "VALUES (?, ?, ?)");
    }

    @Override
    public void putTag(TagEntity tag) throws SQLException {
        statement.setLong(1, tag.getNodeId());
        statement.setString(2, tag.getKey());
        statement.setString(3, tag.getValue());
        statement.execute();
    }
}
