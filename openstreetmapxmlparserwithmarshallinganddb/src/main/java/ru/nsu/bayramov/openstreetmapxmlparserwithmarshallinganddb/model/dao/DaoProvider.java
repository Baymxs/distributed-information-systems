package ru.nsu.bayramov.openstreetmapxmlparserwithmarshallinganddb.model.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

/**
 * @author Bayramov Nizhad
 */
public class DaoProvider {
    private static final Logger logger = LoggerFactory.getLogger(DaoProvider.class);
    private static final DaoProvider daoProvider = new DaoProvider();
    private NodeDao nodeDao = null;
    private TagDao tagDao = null;

    public static DaoProvider getInstance() {
        return daoProvider;
    }

    public NodeDao getNodeDao() {
        return nodeDao;
    }

    public TagDao getTagDao() {
        return tagDao;
    }

    public void setDbMode(int dbMode) throws SQLException {
        logger.info("Set dbMode to {}", dbMode);
        if (getNodeDao() != null) {
            getNodeDao().close();
        }
        if (getTagDao() != null) {
            getTagDao().close();
        }

        if (dbMode == 0) {
            nodeDao = null;
            tagDao = null;
            return;
        }
        if (dbMode == 1) {
            nodeDao = new NodeDaoPreparedSql();
            tagDao = new TagDaoPreparedSql();
            return;
        }
        if (dbMode == 2) {
            nodeDao = new NodeDaoRawSql();
            tagDao = new TagDaoRawSql();
            return;
        }
        if (dbMode == 3) {
            nodeDao = new NodeDaoBatchSql();
            tagDao = new TagDaoBatchSql();
            return;
        }
        throw new IllegalArgumentException("Only dbMode = 1,2,3 supported (or 0 to close query creators)");
    }
}
