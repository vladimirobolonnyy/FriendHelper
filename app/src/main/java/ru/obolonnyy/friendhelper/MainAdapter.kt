package ru.obolonnyy.friendhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.content.FileProvider
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import kotlinx.coroutines.*
import ru.obolonnyy.friendhelper.protect.Stand
import ru.obolonnyy.friendhelper.utils.Constants
import ru.obolonnyy.friendhelper.utils.Constants.ERROR
import ru.obolonnyy.friendhelper.utils.Constants.SUCCESS
import ru.obolonnyy.friendhelper.utils.getColorCompat
import kotlin.coroutines.CoroutineContext


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainAdapter(
    private val elements: Array<Stand>,
    private val context: Context,
    override val coroutineContext: CoroutineContext,
    private val viewModel: MainViewModel
) :
    Adapter<MainViewHolder>(),
    CoroutineScope {

    private val green = context.getColorCompat(R.color.green)
    private val red = context.getColorCompat(R.color.red)

    override fun getItemCount() = elements.size

    override fun onCreateViewHolder(group: ViewGroup, position: Int): MainViewHolder {
        val view = LayoutInflater.from(group.context)
            .inflate(R.layout.item_stand, group, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        bindViewHolder(holder, elements[position])
    }

    private fun bindViewHolder(holder: MainViewHolder, elem: Stand) = with(holder) {
        name.text = elem.stringName
        name.setOnClickListener { goToKiosk(elem) }

        version.setOnClickListener { getStandVersion(holder, elem) }
        getStandVersion(holder, elem)

        status.setOnClickListener { standAvailable(holder, elem) }
        standAvailable(holder, elem)

        ///ToDo чекать локальный файл
        download_progress.visibility = View.GONE
        if (viewModel.getApkFile(elem).exists()) {
            download.setImageResource(R.drawable.ic_check_black_24dp)
            download.setOnClickListener { openFolder(elem) }
        } else {
            download.setOnClickListener { downloadApk(holder, elem) }
        }
    }

    private fun getStandVersion(holder: MainViewHolder, elem: Stand) = launch(Dispatchers.Main) {
        with(holder) {
            version.text = ""
            version_progress.visibility = View.VISIBLE

            version.text = viewModel.getStandVersion(elem)
            version_progress.visibility = View.GONE
        }
    }

    private fun standAvailable(holder: MainViewHolder, elem: Stand) = launch(Dispatchers.Main) {
        with(holder) {
            status.text = ""
            status_progress.visibility = View.VISIBLE

            val result = viewModel.standAvailable(elem)
            status.text = result
            status_progress.visibility = View.GONE
            //ToDo rework
            if (result == Constants.ONLINE) {
                status.setTextColor(green)
            } else {
                status.setTextColor(red)
            }
        }
    }

    private fun downloadApk(holder: MainViewHolder, stand: Stand) = launch(Dispatchers.Main) {
        with(holder) {
            download_progress.visibility = View.VISIBLE
            download.visibility = View.INVISIBLE

            val result = if (viewModel.getApkFile(stand).exists()) {
                SUCCESS
            } else {
                viewModel.downloadApk(stand)
            }
            when (result) {
                SUCCESS -> {
                    download.setImageResource(R.drawable.ic_check_black_24dp)
                    download.setOnClickListener { openFolder(stand) }
                }
                ERROR -> download.setImageResource(R.drawable.ic_error_outline_black_24dp)
            }
            download_progress.visibility = View.GONE
            download.visibility = View.VISIBLE
        }
    }

    //ToDo remove?
    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun goToKiosk(stand: Stand) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stand.url))
        startActivity(context, browserIntent, null)
    }

    private fun openFolder2(stand: Stand) {
        val file = viewModel.getApkFile(stand)
        val intent = Intent(
            Intent.ACTION_VIEW,
            FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file)
        )
//        intent.setDataAndType(Uri.fromFile(file), "*/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(context, intent, null)
    }

    private fun openFolder(stand: Stand) {
        val file = viewModel.getApkFile(stand)

        val mime = MimeTypeMap.getSingleton()
        val ext = file.name.substring(file.name.lastIndexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(ext)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context, "ru.obolonnyy.friendhelper.provider", file)
            intent.setDataAndType(contentUri, type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        startActivity(context, intent, null)
    }
}
