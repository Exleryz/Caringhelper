package com.weimore.caringhelper.base;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.weimore.base.BaseApplication;
import com.tencent.bugly.crashreport.CrashReport;
import com.weimore.caringhelper.dao.DatabaseManager;
import com.weimore.caringhelper.entity.Contact;
import com.weimore.caringhelper.greendao.gen.ContactDao;

/**
 * @author Weimore
 *         2018/12/6.
 *         description:
 */

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "596ea85e0b", true);
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        DatabaseManager.getInstance().init(this, ContactDao.class);
    }


}
