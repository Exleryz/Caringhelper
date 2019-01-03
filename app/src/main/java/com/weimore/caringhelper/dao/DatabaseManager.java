package com.weimore.caringhelper.dao;

import android.content.Context;


import com.weimore.caringhelper.dao.helper.ReleaseOpenHelper;
import com.weimore.caringhelper.greendao.gen.DaoMaster;
import com.weimore.caringhelper.greendao.gen.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

/**
 * @author Weimore
 *         2018/11/2.
 *         description:
 */

public class DatabaseManager {

    private static final String DB_NAME = "my_contacts";
    private DaoSession mDaoSession = null;


    private DatabaseManager() {
    }

    public DatabaseManager init(Context context, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        initDao(context,daoClasses);
        return this;
    }

    private static final class Holder {
        private static final DatabaseManager INSTANCE = new DatabaseManager();
    }

    public static DatabaseManager getInstance() {
        return Holder.INSTANCE;
    }

    private void initDao(Context context, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        final ReleaseOpenHelper helper = new ReleaseOpenHelper(context, DB_NAME,daoClasses);
        final Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public static DaoSession getDaoSession() {
        return getInstance().mDaoSession;
    }
}

