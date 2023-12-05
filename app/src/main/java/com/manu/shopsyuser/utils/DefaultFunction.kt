package com.manu.shopsyuser.utils

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.manu.shopsyuser.R
import com.manu.shopsyuser.databinding.DialogDesignBinding
import com.manu.shopsyuser.databinding.DialogLoadingBinding
import kotlinx.coroutines.flow.combine

class DefaultFunction {
    companion object {

        fun loading(context: Context): AlertDialog {
            val progress = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
            val processLayout = DialogLoadingBinding.inflate(LayoutInflater.from(context))
            progress.setView(processLayout.root)
            progress.setCancelable(false)

            return progress
        }

        fun dialog(context: Context, title: String, subTitle: String, confirmBtText: String, onClick:() -> Unit): AlertDialog {
            val dialog = AlertDialog.Builder(context, R.style.CustomAlertDialog).create()
            val dialogLayout = DialogDesignBinding.inflate(LayoutInflater.from(context))
            dialog.setView(dialogLayout.root)
            dialog.setCancelable(true)
            dialogLayout.dialogTitle.text = title
            dialogLayout.dialogBody.text = subTitle
            dialogLayout.btConfirm.text = confirmBtText
            dialogLayout.btCancle.setOnClickListener {
                dialog.dismiss()
            }
            dialogLayout.btConfirm.setOnClickListener {
                onClick()
                dialog.dismiss()
            }
            dialog.show()

            return dialog
        }
    }
}