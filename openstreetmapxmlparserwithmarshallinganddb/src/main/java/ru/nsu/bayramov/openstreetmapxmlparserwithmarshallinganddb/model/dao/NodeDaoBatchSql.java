package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.NodeEntity;
import ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.entity.TagEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bayramov Nizhad
 */
public class NodeDaoBatchSql implements NodeDao {
    private final List<NodeEntity> nodesToInsert = new ArrayList<>();

    @Override
    public void putNode(NodeEntity node) throws SQLException {
        nodesToInsert.add(node);
        if (nodesToInsert.size() >= DbConnection.BATCH_SIZE) {
            putNodes(nodesToInsert);
            nodesToInsert.clear();
        }
    }

    private void putNodes(List<NodeEntity> nodes) throws SQLException {
        Connection connection = DbConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement("INSERT INTO nodes (id, username, longitude, latitude) " +
                "VALUES (?, ?, ?, ?)");
        for (NodeEntity node : nodes) {
            statement.setLong(1, node.getId());
            statement.setString(2, node.getUser());
            statement.setDouble(3, node.getLongitude());
            statement.setDouble(4, node.getLatitude());
            statement.addBatch();
        }
        statement.executeBatch();

        List<TagEntity> tags = nodes.stream()
                .flatMap(node -> node.getTags().stream())
                .collect(Collectors.toList());

        ((TagDaoBatchSql) DaoProvider.getInstance().getTagDao()).putTags(tags);
    }

    @Override
    public void close() throws SQLException {
        putNodes(nodesToInsert);
        nodesToInsert.clear();
    }
}