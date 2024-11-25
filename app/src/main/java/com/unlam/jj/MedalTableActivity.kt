package com.unlam.jj

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unlam.jj.adapters.MedalAdapter
import repositories.MedalTableRepository

class MedalTableActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_medal_table)

        val recyclerView: RecyclerView = findViewById(R.id.rv_country)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val countries = MedalTableRepository.get()
        recyclerView.adapter = MedalAdapter(countries)

    }
}