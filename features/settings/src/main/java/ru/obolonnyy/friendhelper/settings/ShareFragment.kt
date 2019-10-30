package ru.obolonnyy.friendhelper.settings

import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment


class ShareFragment : Fragment() {

    lateinit var linkTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.share_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)
    }

    private fun bindView(view: View) {
        linkTextView = view.findViewById(R.id.link)

        view.findViewById<TextView>(R.id.version).apply {
            text = "Версия: " + BuildConfig.VERSION_NAME
        }

        view.setOnClickListener {
            copy(linkTextView.text.toString())
            showToast()
        }
    }

    private fun copy(link: String) {
        val clipboard = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        val clip = android.content.ClipData.newPlainText("Copied Text", link)
        clipboard.primaryClip = clip
    }

    private fun showToast() {
        Toast.makeText(this.context, R.string.link_copied, Toast.LENGTH_LONG).show()
    }
}