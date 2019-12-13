package com.it.training.addmember.presenter

import com.it.training.home.model.UserListModel
import com.it.training.home.presenter.UserPresenterImpl
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddMemberPresenterImpl(private val view: AddMemberContract.View) :
    AddMemberContract.Presenter {

    companion object {
        const val KEY_USERS = "users"
        const val KEY_USERS_LIST = "userList"
        const val USER_KEY_ID = "ID_"
    }

    private var database = FirebaseDatabase.getInstance()
    private var refUserListChild = database.getReference(KEY_USERS).child(KEY_USERS_LIST)

    override fun addMemberUser(
        userId: String?,
        name: String?,
        weight: String?,
        height: String?
    ) {
        if (!userId.isNullOrBlank()) {
            val queryIdUser = refUserListChild.orderByChild(UserPresenterImpl.KEY_ID_USER)
            queryIdUser.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {}
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val map: MutableMap<String, Any> = HashMap()
                    map["$USER_KEY_ID$userId"] = UserListModel(userId, name, weight, height)
                    refUserListChild.updateChildren(map)
                    view.addMemberSuccess()
                }
            })
        }
    }

}