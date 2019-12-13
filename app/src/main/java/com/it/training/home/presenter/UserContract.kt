package com.it.training.home.presenter

import com.it.training.commom.BaseView
import com.it.training.home.model.UserListModel
import com.it.training.home.model.UserModel

class UserContract {
    interface Presenter {

        fun addDefaultEmail()

        fun loadDataFormFirebase()

        fun removeItemMember(userId: String)
    }

    interface View : BaseView {

        fun updateData(model: UserModel)

        fun updateList(model: List<UserListModel>)

    }
}