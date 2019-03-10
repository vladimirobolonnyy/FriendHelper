package ru.obolonnyy.friendhelper.main.main

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import retrofit2.HttpException
import ru.obolonnyy.friendhelper.api.interfaces.ApiInteractorInterface
import ru.obolonnyy.friendhelper.utils.constants.Constants
import ru.obolonnyy.friendhelper.utils.data.MyResult
import ru.obolonnyy.friendhelper.utils.local.StandI
import timber.log.Timber
import java.io.File
import kotlin.coroutines.CoroutineContext

class MainModel(
    val interactor: ApiInteractorInterface,
    val filesDir: File
) : CoroutineScope {

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    suspend fun getStandVersion(state: StandI): MyResult<String> {
        return try {
            val response = interactor.getVersion(state).await()
            MyResult.Success(response.version!!)
        } catch (ex: Exception) {
            Timber.e(ex)
            MyResult.Error(ex, Constants.ERROR)
        }
    }

    suspend fun getStandStatus(stand: StandI): MyResult<String> {
        //ToDo вот с этим что-то надо сделать
        return try {
            val response = interactor.sendEmailTemporaryCode(stand).await()
            MyResult.Success(Constants.ONLINE)
        } catch (ex: Exception) {
            Timber.e(ex)
            when (ex) {
                is HttpException -> {
                    //it's ok. I expect here 404 error
                    if (ex.code() == 404) {
                        return MyResult.Success(Constants.ONLINE)
                    }
                    //ToDo check this
                    if (ex.message().contains("AuthException"))
                        return MyResult.Success(Constants.ONLINE)
                    if (ex.message().contains("power")) {
                        return MyResult.Error(ex, Constants.DATA_POWER_ERROR)
                    } else {
                        return MyResult.Error(ex, Constants.UNKNOWN_ERROR)
                    }
                }
                else -> MyResult.Error(ex, Constants.NOT_HTTP_ERROR)
            }
        }
    }
}