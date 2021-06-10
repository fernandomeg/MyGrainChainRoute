package com.fmg.mygrainchainroute.view.customcontrols

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.fmg.mygrainchainroute.R
import java.util.*

class ModalFragment:DialogFragment() {
    private var viewDialog: View? = null
    private var title: String? = null
        get() = field
        set(value) { field = value }

    private var body: String? = null
        get() = field
       set(value) { field = value }

    private var cancel = ""
        get() = field
        set(value) { field = value }

    private var accept = ""
        get() = field
        set(value) { field = value }

    private var callBack: ModalFragment.CommonDialogFragmentCallBack? = null
    private var isSingle = false
        get() = field
        set(value) { field = value }

    private var isCancel = false
        get() = field
        set(value) { field = value }

    private var isOnTouch = false
        get() = field
        set(value) { field = value }

    private var isError = false
        get() = field
        set(value) { field = value }


    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewDialog = inflater.inflate(R.layout.modal_fragment, container, false)
        Objects.requireNonNull(dialog!!.window)?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initView()
        return viewDialog
    }

    private fun initView() {
        val tvTitle = viewDialog!!.findViewById<TextView>(R.id.tv_title_dialog)
        val tvCuerpo = viewDialog!!.findViewById<TextView>(R.id.tv_body_dialog)
        val linearBotones = viewDialog!!.findViewById<LinearLayout>(R.id.buttons_action_dialog)
        val tvCancelar = viewDialog!!.findViewById<Button>(R.id.btn_cancel)
        val btnAceptar = viewDialog!!.findViewById<Button>(R.id.btn_accept)

        tvCancelar.text = cancel
        btnAceptar.text = accept
        tvCuerpo.text = body
        tvTitle.text = title

        if (isSingle) {
            tvCancelar.visibility = View.GONE
        }
        if (isCancel) {
            tvCancelar.text = getString(R.string.title_cancel)
        }
        btnAceptar.setOnClickListener { view1: View? ->
            if (callBack != null) {
                callBack!!.onAccept()
            }
            if (!isSingle) {
                linearBotones.visibility = View.GONE
            }
            dismiss()
        }
        tvCancelar.setOnClickListener { view2: View? ->
            if (callBack is ModalFragment.CommonDialogFragmentCallBackWithCancel) {
                (callBack as ModalFragment.CommonDialogFragmentCallBackWithCancel).onCancel()
            }
            dismiss()
        }
        isCancelable = isOnTouch
        dialog!!.setCanceledOnTouchOutside(isOnTouch)

        //Config Style when IsErrorDialog or Not
        if (isError) {
            tvCuerpo.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimaryDark))
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Objects.requireNonNull(dialog!!.window)
            ?.getAttributes()?.windowAnimations = R.style.AnimacionModal
    }

    fun setCallBack(callBack: CommonDialogFragmentCallBack?) {
        this.callBack = callBack
    }

    interface CommonDialogFragmentCallBack {
        fun onAccept()
    }

    interface CommonDialogFragmentCallBackWithCancel : CommonDialogFragmentCallBack {
        fun onCancel()
    }

    class DialogBuilder {
        private var title = ""
        private var body = ""
        private var cancel = ""
        private var accept = ""
        private var callBack: CommonDialogFragmentCallBack? = null
        private var isSingle = false
        private var isCancel = false
        private var isOnTouch = false
        private var isError = false

        fun setTitle(title: String): DialogBuilder {
            this.title = title
            return this
        }

        fun setBody(body: String): DialogBuilder {
            this.body = body
            return this
        }

        fun setCancel(cancel: String): DialogBuilder {
            this.cancel = cancel
            return this
        }

        fun setAccept(accept: String): DialogBuilder {
            this.accept = accept
            return this
        }

        fun setSingleButton(isSingle: Boolean): DialogBuilder {
            this.isSingle = isSingle
            return this
        }

        fun ponInterfaceCallBack(callBack: CommonDialogFragmentCallBack?): DialogBuilder {
            this.callBack = callBack
            return this
        }

        fun setInterfaceCallBackWithCancel(callBack: CommonDialogFragmentCallBackWithCancel?): DialogBuilder {
            this.callBack = callBack
            return this
        }

        fun setCanceledOnTouchOutside(isOnTouch: Boolean): DialogBuilder {
            this.isOnTouch = isOnTouch
            return this
        }

        fun addIsError(isError: Boolean): DialogBuilder {
            this.isError = isError
            return this
        }

        fun build(): ModalFragment {
            val commonDialogFragment = ModalFragment()
            estableceParametros(commonDialogFragment)
            return commonDialogFragment
        }

        private fun estableceParametros(modalFragment: ModalFragment) {
            modalFragment.title = title
            modalFragment.body = body
            modalFragment.cancel = cancel
            modalFragment.accept = accept
            modalFragment.setCallBack(callBack)
            modalFragment.isSingle = isSingle
            modalFragment.isCancel = isCancel
            modalFragment.isError = isError
            modalFragment.isOnTouch = isOnTouch
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val fragmentTransaction = manager.beginTransaction()
            fragmentTransaction.add(this, tag)
            fragmentTransaction.commitAllowingStateLoss()
        } catch (e: IllegalStateException) {
            Log.i(ModalFragment::class.java.simpleName, e.message.toString())
        }
    }

}