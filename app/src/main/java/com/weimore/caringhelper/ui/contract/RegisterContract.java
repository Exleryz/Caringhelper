package com.weimore.caringhelper.ui.contract;

import com.weimore.base.IBaseModel;
import com.weimore.base.IBasePresenter;
import com.weimore.base.IBaseView;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public interface RegisterContract {

    interface View extends IBaseView {

        void registerSuccess(String msg);
    }

    interface Presenter extends IBasePresenter {

        void register(String phoneNo,String userName,String password);
    }

    interface Model extends IBaseModel {


    }

}