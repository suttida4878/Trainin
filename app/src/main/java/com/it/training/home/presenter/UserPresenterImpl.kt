package com.it.training.home.presenter

import com.it.training.home.model.UserListModel
import com.it.training.home.model.UserModel
import com.it.training.home.model.UserResponseModel
import com.google.firebase.database.*
import java.math.BigDecimal


class UserPresenterImpl(var view: UserContract.View?) : UserContract.Presenter {

    companion object {
        const val KEY_USERS = "users"
        const val KEY_USERS_LIST = "userList"
        const val KEY_ID_USER = "idUser"
        const val KEY_EMAIL = "email"
    }

    private var database = FirebaseDatabase.getInstance()
    private var refUsersChild = database.getReference(KEY_USERS)
    private var refUserListChild = database.getReference(KEY_USERS).child(KEY_USERS_LIST)

    private var listUser: UserModel? = null

    override fun addDefaultEmail() {
        val map: MutableMap<String, Any> = HashMap()
        map[KEY_EMAIL] = "Pueng4878@gamil.com"
        refUsersChild.updateChildren(map)
    }

    override fun loadDataFormFirebase() {
        refUsersChild.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(UserResponseModel::class.java)
                val model = UserModel()
                model.email = value?.email
                value?.userList?.map {
                    if (!it.value.idUser.isNullOrEmpty()) {
                        val modelList = UserListModel().apply {
                            idUser = it.value.idUser
                            name = it.value.name
                            weight = it.value.weight
                            height = it.value.height
                            bmi = calculateBmi(
                                weight = it.value.weight.coverStringToDouble(),
                                height = it.value.height.coverStringToDouble()
                            )
                        }
                        model.userList.add(modelList)
                    }
                }
                model.email = value?.email
                listUser = model
                view?.updateData(listUser ?: UserModel())
            }

        })
        initEventUserList()
    }


    override fun removeItemMember(userId: String) {
        val queryIdUser = refUserListChild.orderByChild(KEY_ID_USER).equalTo(userId)
        queryIdUser.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach {
                    it.ref.removeValue()
                }
            }
        })
    }

    private fun initEventUserList() {
        refUserListChild.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, value: String?) {}
            override fun onChildChanged(dataSnapshot: DataSnapshot, value: String?) {}
            override fun onChildAdded(dataSnapshot: DataSnapshot, value: String?) {}
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(UserListModel::class.java)
                value?.bmi = calculateBmi(
                    value?.weight.coverStringToDouble(),
                    value?.height.coverStringToDouble()
                )?.substring(IntRange(0, 4))
                listUser?.userList?.remove(value)
                listUser?.userList?.let { list ->
                    view?.updateList(list)
                }
            }
        })
    }

    private fun calculateBmi(weight: Double, height: Double): String? {
        val x = weight
        val y = height * 0.01
        return (x / (y * y)).toString().formatStringDouble()
    }

}

fun String?.coverStringToDouble(): Double = (this ?: "0.0").toDouble()

fun String?.formatStringDouble(digit: Int? = 2): String {
    val format = "%,." + digit + "f"
    if (!this.isNullOrBlank()) {
        val value = this.replace(",".toRegex(), "")
        return try {
            String.format(format, BigDecimal(value))
        } catch (e: Exception) {
            value
        }
    }
    return this ?: ""
}