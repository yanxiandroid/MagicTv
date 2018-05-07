package com.yht.iptv.utils;

import android.content.Context;

import org.xutils.DbManager;
import org.xutils.common.util.KeyValue;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 *
 */
public class DBUtils {

    public static DbManager openDB(Context context) {

        DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
                .setDbName("test.db")
                .setDbVersion(11)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
//                         db.addColumn(...);
//                         db.dropTable(...);
//                         db.dropDb();
                        try {
                            db.dropDb();
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                    }
                });

        DbManager db =  x.getDb(daoConfig);
        return db;
    }

    public static <T> T find(Context context, Class<T> entityType,
                             String columnName, String op, Object value) {
        DbManager db = openDB(context);
        T t = null;
        try {
            t = db.selector(entityType).where(
                    WhereBuilder.b(columnName, op, value)).findFirst();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    public static <T> T find(Context context, Class<T> entityType,
                             WhereBuilder whereBuilder) {
        DbManager db = openDB(context);
        T t = null;
        try {
            t = db.selector(entityType).where(whereBuilder).findFirst();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T find(Context context, Class<T> entityType) {
        DbManager db = openDB(context);
        T t = null;
        try {
            t = db.selector(entityType).findFirst();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    public static <T> List<T> findAll(Context context, Class<T> entityType,
                                      String columnName, String op, Object value) {
        DbManager db = openDB(context);
        List<T> datas = null;
        try {
            datas = db.selector(entityType).where(
                    WhereBuilder.b(columnName, op, value)).findAll();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static <T> List<T> findAll(Context context, Class<T> entityType,
                                      WhereBuilder whereBuilder) {
        DbManager db = openDB(context);
        List<T> datas = null;
        try {
            datas = db.selector(entityType).where(whereBuilder).findAll();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static <T> List<T> findAll(Context context, Class<T> entityType,
                                      WhereBuilder whereBuilder, String orderByStr, Boolean dOra) {
        DbManager db = openDB(context);
        List<T> datas = null;
        try {
            datas = db.selector(entityType).where(whereBuilder)
                    .orderBy(orderByStr, dOra).findAll();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static <T> List<T> findAll(Context context, Class<T> entityType,
                                      WhereBuilder whereBuilder, int limit, int offset) {
        DbManager db = openDB(context);
        List<T> datas = null;
        try {
            datas = db.selector(entityType).where(whereBuilder)
                    .limit(limit).offset(offset).findAll();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static <T> List<T> findAll(Context context, Class<T> entityType,
                                      int limit, int offset) {
        DbManager db = openDB(context);
        List<T> datas = null;
        try {
            datas = db.selector(entityType).limit(limit)
                    .offset(offset).findAll();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static <T> List<T> findAll(Context context, Class<T> entityType) {
        DbManager db = openDB(context);
        List<T> datas = null;
        try {
            datas = db.findAll(entityType);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }



    public static void save(Context context, Object entity) {
        DbManager db = openDB(context);
        try {
            db.save(entity);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAll(Context context, List<?> entities) {
        DbManager db = openDB(context);
        try {
            db.save(entities);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveOrUpdate(Context context, Object entity) {
        DbManager db = openDB(context);
        try {
            db.saveOrUpdate(entity);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void saveOrUpdateAll(Context context, List<?> entities) {
        DbManager db = openDB(context);
        try {
            db.saveOrUpdate(entities);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void deleteById(Context context, Class<T> entityType,
                                      WhereBuilder b) {
        DbManager db = openDB(context);
        try {
            db.deleteById(entityType, b);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(Context context, Class<?> entity) {
        DbManager db = openDB(context);
        try {
            db.delete(entity);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(Context context, Class<?> entity,WhereBuilder b) {
        DbManager db = openDB(context);
        try {
            db.delete(entity,b);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void dropTable(Context context, Class<T> entityType) {
        DbManager db = openDB(context);
        try {
            db.dropTable(entityType);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static <T> void update(Context context,Object entityType,String... updateColumnNames) {
        DbManager db = openDB(context);
        try {
            db.update(entityType,updateColumnNames);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void update(Context context, Class<T> entityType, WhereBuilder whereBuilder, KeyValue... nameValuePairs) {
        DbManager db = openDB(context);
        try {
            db.update(entityType,whereBuilder,nameValuePairs);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> void updateAll(Context context,Object entityType) {
        DbManager db = openDB(context);
        try {
            db.replace(entityType);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
