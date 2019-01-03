package com.weimore.caringhelper.dao.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.weimore.caringhelper.greendao.gen.DaoMaster;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.database.Database;

/**
 * @author Weimore
 *         2018/11/2.
 *         description:
 */

public class ReleaseOpenHelper extends DaoMaster.OpenHelper {

    Class<? extends AbstractDao<?,?>>[] daoClasses;

    public ReleaseOpenHelper(Context context, String name, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        super(context, name);
        this.daoClasses = daoClasses;
    }

    public ReleaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, Class<? extends AbstractDao<?, ?>>... daoClasses) {
        super(context, name, factory);
        this.daoClasses = daoClasses;
    }

    @Override
    public void onCreate(Database db) {
        super.onCreate(db);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
//        super.onUpgrade(db, oldVersion, newVersion);
        if(oldVersion < newVersion){
            MigrationHelper.migrate(db,daoClasses);
        }
    }
}
