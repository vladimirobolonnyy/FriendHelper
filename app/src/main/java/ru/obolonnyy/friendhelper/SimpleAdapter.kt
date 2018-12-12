package ru.obolonnyy.friendhelper

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.obolonnyy.friendhelper.protect.ServerApi
import ru.obolonnyy.friendhelper.protect.Stand
import kotlin.coroutines.CoroutineContext

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class SimpleAdapter(
        private val elements: Array<Stand>,
        private val context: Context,
        private val apis: Map<Stand, ServerApi>,
        override val coroutineContext: CoroutineContext
) :
        Adapter<SimpleAdapter.MyViewHolder>(),
        CoroutineScope {

    private val green = context.getColorCompat(R.color.green)
    private val red = context.getColorCompat(R.color.red)

    override fun getItemCount() = elements.size

    override fun onCreateViewHolder(group: ViewGroup, position: Int): MyViewHolder {
        val view = LayoutInflater.from(group.context)
                .inflate(R.layout.item_stand, group, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val elem = elements[position]
        with(holder) {
            progress_version.visibility = View.VISIBLE
            progress_status.visibility = View.VISIBLE
            name.text = elem.s
            launch(Dispatchers.Main) {
                version.text = getStandVersion(elem)
                progress_version.visibility = View.GONE
            }
            launch(Dispatchers.Main) {
                val online = standAvalible(elem)
                progress_status.visibility = View.GONE
                if (online) {
                    status.text = "Oneline"
                    status.setTextColor(green)
                } else {
                    status.text = "Off"
                    status.setTextColor(red)
                }
            }
            download.setOnClickListener {
                downLoadApk(elem)
            }
            root.setOnClickListener { toBrowser(elem) }
        }
    }

//    private suspend fun getStandVersion(stand: Stand): String {
//        val task = async {
//            try {
//                val request = apis[stand]!!.getVersion()
//                val version = request.await()
//                version.version
//            } catch (e: Throwable) {
//                Timber.e(e.message)
//                "error"
//            }
//        }
//        return task.await() ?: "error2"
//    }

    private suspend fun getStandVersion(stand: Stand): String {
        val versionDto = apis[stand]!!.getVersion().await()
        return versionDto.toLocal().version
    }

    private suspend fun standAvalible(stand: Stand): Boolean {
        val task = async {
            try {
                if (stand == Stand.DEV) {
                    apis[stand]!!.authDev().await()
                } else {
                    apis[stand]!!.auth().await()
                }
                true
            } catch (e: Throwable) {
                try {
                    val error = (e as HttpException).parseError()
                    error?.exceptionClass?.endsWith("AuthException") ?: false
                } catch (e: Exception) {
                    false
                }
            }
        }
        return task.await()
    }

    private fun downLoadApk(stand: Stand) {
        Toast.makeText(context, "Not implemented yet", Toast.LENGTH_SHORT).show()
    }

    private fun toBrowser(stand: Stand) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stand.url))
        startActivity(context, browserIntent, null)
    }

    inner class MyViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
        val name = root.findViewById<TextView>(R.id.name)!!
        val version = root.findViewById<TextView>(R.id.version)!!
        val progress_version = root.findViewById<ProgressBar>(R.id.progress_version)!!
        val status = root.findViewById<TextView>(R.id.status)!!
        val progress_status = root.findViewById<ProgressBar>(R.id.progress_status)!!
        val download = root.findViewById<TextView>(R.id.download)!!
    }
}
