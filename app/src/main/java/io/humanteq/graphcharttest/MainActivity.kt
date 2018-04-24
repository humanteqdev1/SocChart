package io.humanteq.graphcharttest

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val ch = findViewById<SocChartView>(R.id.chart)

        ch.setOnClickListener {
            ch.invalidate()
        }
    }
}
