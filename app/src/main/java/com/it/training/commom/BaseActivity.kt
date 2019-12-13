package com.it.training.commom

import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.it.training.R

open class BaseActivity : AppCompatActivity(), BaseView {

    private var progressDialog: ProgressDialog? = null

    override fun showLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
            progressDialog?.apply {
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                isIndeterminate = true
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                setTitle(R.string.loading)
                show()
            }
        }
    }

    override fun hideLoading() {
        if (progressDialog != null) {
            try {
                this@BaseActivity.runOnUiThread {
                    progressDialog?.dismiss()
                    progressDialog = null
                }
            } catch (e: Exception) {
                Log.e("Exception", "Error -> " + e.message)
            }
        }
    }
}