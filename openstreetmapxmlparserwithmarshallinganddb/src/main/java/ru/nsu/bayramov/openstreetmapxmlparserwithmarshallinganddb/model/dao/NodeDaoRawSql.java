package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.NodeEntity;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.SQLException;
import java.sql.Statement;

import static ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao.Utils.escape;

/**
 * @author Bayramov Nizhad
 */
public class NodeDaoRawSql implements NodeDao {
    private final Statement statement;

    public NodeDaoRawSql() throws SQLException {
        statement = DbConnection.getConnection().createStatement();
    }

    @Override
    public void putNode(NodeEntity node) throws SQLException {
        statement.execute("INSERT INTO nodes (id, username, longitude, latitude) " +
                "VALUES ('" + node.getId() + "', '" + escape(node.getUser()) + "', '"
                + node.getLongitude() + "', '" + node.getLatitude() + "')");
        for (TagEntity tagEntity : node.getTags()) {
            DaoProvider.getInstance().getTagDao().putTag(tagEntity);
        }
    }
}
