package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.NodeEntity;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public class NodeDaoPreparedSql implements NodeDao {
    PreparedStatement statement;

    public NodeDaoPreparedSql() throws SQLException {
        statement = DbConnection.getConnection().prepareStatement("INSERT INTO nodes (id, username, longitude, latitude) " +
                "VALUES (?, ?, ?, ?)");
    }

    @Override
    public void putNode(NodeEntity node) throws SQLException {
        statement.setLong(1, node.getId());
        statement.setString(2, node.getUser());
        statement.setDouble(3, node.getLongitude());
        statement.setDouble(4, node.getLatitude());
        statement.executeUpdate();
        for (TagEntity tagEntity : node.getTags()) {
            DaoProvider.getInstance().getTagDao().putTag(tagEntity);
        }
    }
}
