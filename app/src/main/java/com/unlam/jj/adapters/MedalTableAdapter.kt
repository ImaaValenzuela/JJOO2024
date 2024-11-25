package com.unlam.jj.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.unlam.jj.R
import data.Country

class MedalAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<MedalAdapter.MedalViewHolder>() {

    class MedalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val countryName: TextView = itemView.findViewById(R.id.tv_country_name)
        val medals: TextView = itemView.findViewById(R.id.tv_medals)
        val flag: ImageView = itemView.findViewById(R.id.iv_flag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedalViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_prueba, parent, false)//cambie rv_ country por activity_medal_table, creo que esta mal
        return MedalViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedalViewHolder, position: Int) {
        val country = countries[position]
        holder.countryName.text = "${country.position}. ${country}"
        holder.medals.text = """
            🥇 Gold: ${country.goldMedals}
            🥈 Silver: ${country.silverMedals}
            🥉 Bronze: ${country.bronzeMedals}
            Total: ${country.goldMedals + country.silverMedals + country.bronzeMedals}
        """.trimIndent()

        Glide.with(holder.flag.context)
            .load(country.flag) // URL de la bandera
            .into(holder.flag) // Asigna al ImageView
    }

    override fun getItemCount(): Int = countries.size
}
