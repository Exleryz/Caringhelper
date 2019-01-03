package com.weimore.caringhelper.ui.contract;

import com.weimore.base.IBaseModel;
import com.weimore.base.IBasePresenter;
import com.weimore.base.IBaseView;
import com.weimore.caringhelper.entity.User;

/**
 * @author Weimore
 *         2019/1/1.
 *         description:
 */

public interface LoginContract {

    interface View extends IBaseView {

        void loginSuccess(User user);
    }

    interface Presenter extends IBasePresenter {

        void login(String phone,String password);
    }

    interface Model extends IBaseModel {


    }

}