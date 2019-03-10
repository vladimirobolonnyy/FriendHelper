package ru.obolonnyy.friendhelper.main.second

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.obolonnyy.friendhelper.main.R
import ru.obolonnyy.friendhelper.utilsandroid.ScopedFragment

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class SecondFragment : ScopedFragment() {

    val model: SecondViewModel by inject()

    companion object {
        fun newInstance() = SecondFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.second_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    private fun initViews(view: View) {

        val button = view.findViewById<Button>(R.id.add_button)
        var text: String = "213"
        launch(Dispatchers.IO) {
            text = model.getText
        }.invokeOnCompletion {
            button.text = text
        }
        button.setOnClickListener {
            launch(Dispatchers.IO) {
                model.onClick()
            }
            button.text = model.getText

        }

    }
}