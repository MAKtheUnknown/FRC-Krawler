package com.team2052.frckrawler.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MATCH_DATA".
*/
public class MatchDataDao extends AbstractDao<MatchData, Long> {

    public static final String TABLENAME = "MATCH_DATA";
    private DaoSession daoSession;
    ;
    private Query<MatchData> event_MatchDataListQuery;
    private Query<MatchData> robot_MatchDataListQuery;
    private Query<MatchData> metric_MatchDataListQuery;
    private Query<MatchData> user_MatchDataListQuery;
    private String selectDeep;

    public MatchDataDao(DaoConfig config) {
        super(config);
    }
    
    public MatchDataDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MATCH_DATA\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"EVENT_ID\" INTEGER NOT NULL ," + // 1: event_id
                "\"ROBOT_ID\" INTEGER NOT NULL ," + // 2: robot_id
                "\"USER_ID\" INTEGER," + // 3: user_id
                "\"METRIC_ID\" INTEGER NOT NULL ," + // 4: metric_id
                "\"MATCH_TYPE\" INTEGER NOT NULL ," + // 5: match_type
                "\"MATCH_NUMBER\" INTEGER NOT NULL ," + // 6: match_number
                "\"LAST_UPDATED\" INTEGER," + // 7: last_updated
                "\"DATA\" TEXT);"); // 8: data
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MATCH_DATA\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, MatchData entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getEvent_id());
        stmt.bindLong(3, entity.getRobot_id());
 
        Long user_id = entity.getUser_id();
        if (user_id != null) {
            stmt.bindLong(4, user_id);
        }
        stmt.bindLong(5, entity.getMetric_id());
        stmt.bindLong(6, entity.getMatch_type());
        stmt.bindLong(7, entity.getMatch_number());
 
        java.util.Date last_updated = entity.getLast_updated();
        if (last_updated != null) {
            stmt.bindLong(8, last_updated.getTime());
        }

        String data = entity.getData();
        if (data != null) {
            stmt.bindString(9, data);
        }
    }

    @Override
    protected void attachEntity(MatchData entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public MatchData readEntity(Cursor cursor, int offset) {
        MatchData entity = new MatchData( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // event_id
            cursor.getLong(offset + 2), // robot_id
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // user_id
            cursor.getLong(offset + 4), // metric_id
            cursor.getInt(offset + 5), // match_type
            cursor.getLong(offset + 6), // match_number
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)), // last_updated
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // data
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, MatchData entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setEvent_id(cursor.getLong(offset + 1));
        entity.setRobot_id(cursor.getLong(offset + 2));
        entity.setUser_id(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setMetric_id(cursor.getLong(offset + 4));
        entity.setMatch_type(cursor.getInt(offset + 5));
        entity.setMatch_number(cursor.getLong(offset + 6));
        entity.setLast_updated(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
        entity.setData(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(MatchData entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(MatchData entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "matchDataList" to-many relationship of Event. */
    public List<MatchData> _queryEvent_MatchDataList(long event_id) {
        synchronized (this) {
            if (event_MatchDataListQuery == null) {
                QueryBuilder<MatchData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Event_id.eq(null));
                event_MatchDataListQuery = queryBuilder.build();
            }
        }
        Query<MatchData> query = event_MatchDataListQuery.forCurrentThread();
        query.setParameter(0, event_id);
        return query.list();
    }

    /** Internal query to resolve the "matchDataList" to-many relationship of Robot. */
    public List<MatchData> _queryRobot_MatchDataList(long robot_id) {
        synchronized (this) {
            if (robot_MatchDataListQuery == null) {
                QueryBuilder<MatchData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Robot_id.eq(null));
                robot_MatchDataListQuery = queryBuilder.build();
            }
        }
        Query<MatchData> query = robot_MatchDataListQuery.forCurrentThread();
        query.setParameter(0, robot_id);
        return query.list();
    }

    /** Internal query to resolve the "matchDataList" to-many relationship of Metric. */
    public List<MatchData> _queryMetric_MatchDataList(long metric_id) {
        synchronized (this) {
            if (metric_MatchDataListQuery == null) {
                QueryBuilder<MatchData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.Metric_id.eq(null));
                metric_MatchDataListQuery = queryBuilder.build();
            }
        }
        Query<MatchData> query = metric_MatchDataListQuery.forCurrentThread();
        query.setParameter(0, metric_id);
        return query.list();
    }

    /** Internal query to resolve the "matchDataList" to-many relationship of User. */
    public List<MatchData> _queryUser_MatchDataList(Long user_id) {
        synchronized (this) {
            if (user_MatchDataListQuery == null) {
                QueryBuilder<MatchData> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.User_id.eq(null));
                user_MatchDataListQuery = queryBuilder.build();
            }
        }
        Query<MatchData> query = user_MatchDataListQuery.forCurrentThread();
        query.setParameter(0, user_id);
        return query.list();
    }

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getEventDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getRobotDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T2", daoSession.getUserDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T3", daoSession.getMetricDao().getAllColumns());
            builder.append(" FROM MATCH_DATA T");
            builder.append(" LEFT JOIN EVENT T0 ON T.\"EVENT_ID\"=T0.\"_id\"");
            builder.append(" LEFT JOIN ROBOT T1 ON T.\"ROBOT_ID\"=T1.\"_id\"");
            builder.append(" LEFT JOIN USER T2 ON T.\"USER_ID\"=T2.\"_id\"");
            builder.append(" LEFT JOIN METRIC T3 ON T.\"METRIC_ID\"=T3.\"_id\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }

    protected MatchData loadCurrentDeep(Cursor cursor, boolean lock) {
        MatchData entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        Event event = loadCurrentOther(daoSession.getEventDao(), cursor, offset);
         if(event != null) {
            entity.setEvent(event);
        }
        offset += daoSession.getEventDao().getAllColumns().length;

        Robot robot = loadCurrentOther(daoSession.getRobotDao(), cursor, offset);
         if(robot != null) {
            entity.setRobot(robot);
        }
        offset += daoSession.getRobotDao().getAllColumns().length;

        User user = loadCurrentOther(daoSession.getUserDao(), cursor, offset);
        entity.setUser(user);
        offset += daoSession.getUserDao().getAllColumns().length;

        Metric metric = loadCurrentOther(daoSession.getMetricDao(), cursor, offset);
         if(metric != null) {
            entity.setMetric(metric);
        }

        return entity;
    }

    public MatchData loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();

        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);

        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }

    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<MatchData> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<MatchData> list = new ArrayList<MatchData>(count);

        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<MatchData> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    
    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<MatchData> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }

    /**
     * Properties of entity MatchData.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Event_id = new Property(1, long.class, "event_id", false, "EVENT_ID");
        public final static Property Robot_id = new Property(2, long.class, "robot_id", false, "ROBOT_ID");
        public final static Property User_id = new Property(3, Long.class, "user_id", false, "USER_ID");
        public final static Property Metric_id = new Property(4, long.class, "metric_id", false, "METRIC_ID");
        public final static Property Match_type = new Property(5, int.class, "match_type", false, "MATCH_TYPE");
        public final static Property Match_number = new Property(6, long.class, "match_number", false, "MATCH_NUMBER");
        public final static Property Last_updated = new Property(7, java.util.Date.class, "last_updated", false, "LAST_UPDATED");
        public final static Property Data = new Property(8, String.class, "data", false, "DATA");
    }

}
