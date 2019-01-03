package com.weimore.caringhelper.ui.contract;

import com.weimore.base.IBaseModel;
import com.weimore.base.IBasePresenter;
import com.weimore.base.IBaseView;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public interface ChangePasswordContract {

    interface View extends IBaseView {

        void reLoginSuccess();

        void updateSuccess();
    }

    interface Presenter extends IBasePresenter {

        void reLogin(String phone,String oldPassword);

        void updatePassword(String phone,String password);
    }

    interface Model extends IBaseModel {


    }

}