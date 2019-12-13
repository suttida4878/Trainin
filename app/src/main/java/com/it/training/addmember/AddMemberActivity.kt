package com.it.training.addmember

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.it.training.R
import com.it.training.addmember.presenter.AddMemberContract
import com.it.training.addmember.presenter.AddMemberPresenterImpl
import com.it.training.commom.BaseActivity
import com.it.training.home.model.UserListModel
import kotlinx.android.synthetic.main.activity_add_member.*

class AddMemberActivity : BaseActivity(), AddMemberContract.View {

    companion object {
        const val ARGUMENT_EDIT_USER = "edit_user_model"

        @JvmStatic
        fun getStartIntent(context: Context, editModel: UserListModel?): Intent {
            return Intent(context, AddMemberActivity::class.java).apply {
                putExtra(ARGUMENT_EDIT_USER, editModel)
            }
        }
    }

    private var editModel: UserListModel? = null
    private lateinit var presenter: AddMemberPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_member)
        intent?.let {
            editModel = it.extras?.getParcelable<UserListModel>(ARGUMENT_EDIT_USER)
        }
        presenter = AddMemberPresenterImpl(this@AddMemberActivity)
        initView()
    }

    override fun addMemberSuccess() {
        setResult(Activity.RESULT_OK)
        finish()
    }

    private fun initView() {
        if (editModel != null) {
            edt_id?.apply {
                setText(editModel?.idUser)
                isEnabled = false
            }
            edt_name?.apply {
                setText(editModel?.name)
            }
            edt_weight?.apply {
                setText(editModel?.weight)
            }
            edt_height?.apply {
                setText(editModel?.height)
            }
        }
        btn_add_member?.setOnClickListener {
            presenter.addMemberUser(
                userId = edt_id?.text?.trim().toString(),
                name = edt_name?.text?.trim().toString(),
                weight = edt_weight?.text?.trim().toString(),
                height = edt_height?.text?.trim().toString()
            )
        }
    }
}