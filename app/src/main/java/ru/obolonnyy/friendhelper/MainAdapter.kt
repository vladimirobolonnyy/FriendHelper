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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import ru.obolonnyy.friendhelper.BuildConfig.APPLICATION_ID
import ru.obolonnyy.friendhelper.utils.Constants
import ru.obolonnyy.friendhelper.utils.Constants.ERROR
import ru.obolonnyy.friendhelper.utils.Constants.SUCCESS
import ru.obolonnyy.friendhelper.utils.StandI
import ru.obolonnyy.friendhelper.utils.getColorCompat
import kotlin.coroutines.CoroutineContext


@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class MainAdapter(
    private val elements: List<StandI>,
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

    private fun bindViewHolder(holder: MainViewHolder, elem: StandI) = with(holder) {
        name.text = elem.stringName
        name.setOnClickListener { goToKiosk(elem) }

        version.setOnClickListener { getStandVersion(holder, elem) }
        getStandVersion(holder, elem)

        status.setOnClickListener { standAvailable(holder, elem) }
        standAvailable(holder, elem)

        download_progress.visibility = View.GONE
        download.visibility = View.GONE
    }

    private fun getStandVersion(holder: MainViewHolder, elem: StandI) = launch(Dispatchers.Main) {
        with(holder) {
            version.text = ""
            version_progress.visibility = View.VISIBLE

            val remoteVersion = viewModel.getStandVersion(elem)
            version.text = remoteVersion
            version_progress.visibility = View.GONE

            initDownloading(holder, elem, remoteVersion)
        }
    }

    private fun initDownloading(holder: MainViewHolder, elem: StandI, remoteVersion: String) {
        with (holder){
            download_progress.visibility = View.GONE
            download.visibility = View.VISIBLE

            if (viewModel.fileExists(elem, remoteVersion)){
                download.setImageResource(R.drawable.ic_check_black_24dp)
                download.setOnClickListener { openFolder(elem, remoteVersion) }
            } else {
                download.setOnClickListener { downloadApk(holder, elem, remoteVersion) }
            }
        }
    }

    private fun standAvailable(holder: MainViewHolder, elem: StandI) = launch(Dispatchers.Main) {
        with(holder) {
            status.text = ""
            status_progress.visibility = View.VISIBLE

            val result = viewModel.standAvailable(elem)
            status.text = result.text
            status_progress.visibility = View.GONE
            when (result.color) {
                Constants.GREEN -> status.setTextColor(green)
                Constants.RED -> status.setTextColor(red)
                else -> status.setTextColor(red)
            }
        }
    }

    private fun downloadApk(holder: MainViewHolder, stand: StandI, remoteVersion: String) = launch(Dispatchers.Main) {
        with(holder) {
            download_progress.visibility = View.VISIBLE
            download.visibility = View.INVISIBLE

            val result = viewModel.downloadApk(stand, remoteVersion)
            when (result) {
                SUCCESS -> {
                    download.setImageResource(R.drawable.ic_check_black_24dp)
                    download.setOnClickListener { openFolder(stand, remoteVersion) }
                }
                ERROR -> download.setImageResource(R.drawable.ic_error_outline_black_24dp)
            }
            download_progress.visibility = View.GONE
            download.visibility = View.VISIBLE
        }
    }

    private fun goToKiosk(stand: StandI) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stand.url))
        startActivity(context, browserIntent, null)
    }

    private fun openFolder(stand: StandI, remoteVersion: String) {
        val file = viewModel.getApkFile(stand, remoteVersion)

        val mime = MimeTypeMap.getSingleton()
        val ext = file.name.substring(file.name.lastIndexOf(".") + 1)
        val type = mime.getMimeTypeFromExtension(ext)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context, "$APPLICATION_ID.provider", file)
            intent.setDataAndType(contentUri, type)
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
        startActivity(context, intent, null)
    }
}
