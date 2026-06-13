package com.akeyma.ewallet.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akeyma.ewallet.R
import com.akeyma.ewallet.activity.TransferActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TransferBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_transfer_options, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<View>(R.id.btnTransferBank).setOnClickListener {
            handleTransfer("Bank")
        }

        view.findViewById<View>(R.id.btnTransferGoPay).setOnClickListener {
            handleTransfer("GoPay")
        }

        view.findViewById<View>(R.id.btnTransferDana).setOnClickListener {
            handleTransfer("DANA")
        }

        view.findViewById<View>(R.id.btnTransferOVO).setOnClickListener {
            handleTransfer("OVO")
        }
    }

    private fun handleTransfer(method: String) {
        val intent = Intent(requireContext(), TransferActivity::class.java)
        intent.putExtra("EXTRA_TRANSFER_TYPE", method)
        startActivity(intent)
        dismiss()
    }
}