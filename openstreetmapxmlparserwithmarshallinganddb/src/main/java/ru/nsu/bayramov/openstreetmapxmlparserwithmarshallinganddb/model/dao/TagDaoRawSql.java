package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.SQLException;
import java.sql.Statement;

import static ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao.Utils.escape;

/**
 * @author Bayramov Nizhad
 */
public class TagDaoRawSql implements TagDao {
    private final Statement statement;

    public TagDaoRawSql() throws SQLException {
        statement = DbConnection.getConnection().createStatement();
    }

    @Override
    public void putTag(TagEntity tag) throws SQLException {
        statement.execute("INSERT INTO tags (node_id, key, value) " +
                "VALUES ('" + tag.getNodeId() + "', '" + escape(tag.getKey()) + "', '" + escape(tag.getValue()) + "')");
    }
}
