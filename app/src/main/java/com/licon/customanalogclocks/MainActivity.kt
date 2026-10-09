package com.licon.customanalogclocks

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/** Shows every face from [WatchFaces] as a live clock in a two-column list. */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.clock_list).apply {
            layoutManager = GridLayoutManager(this@MainActivity, SPAN_COUNT)
            adapter = ClockAdapter(WatchFaces.all)
        }
    }

    private companion object {
        const val SPAN_COUNT = 2
    }
}
