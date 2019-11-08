package ru.obolonnyy.friendhelper.main

import android.view.View
import org.junit.Test
import ru.obolonnyy.friendhelper.api.Stand

class StandStateTest {

    //ToDo make more tests =)
    @Test
    fun test() {
        val state = StandState(
            position = 0,
            stand = Stand("a", "a", "a", "a", "a")
        )
        state.changeFileState(FileStatus.NotLoaded)
        state.changeFileState(FileStatus.Error)
        state.changeFileState(FileStatus.Loaded)
        assert(state.fileVisibility == View.VISIBLE)
        assert(state.fileImageResource == R.drawable.ic_downloaded)
        assert(state.fileProgressVisibility == View.GONE)
        assert(state.downloadProgress == null)
    }
}