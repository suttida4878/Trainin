package com.it.training.home

import com.it.training.home.model.UserListModel

interface UserCallback {
    fun onSelectItem(userListModel: UserListModel?)
    fun onSelectItemLongClick(userListModel: UserListModel?)
}