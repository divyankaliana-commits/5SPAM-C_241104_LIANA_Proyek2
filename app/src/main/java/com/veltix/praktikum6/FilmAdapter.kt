package com.veltix.praktikum6

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import coil3.load
import coil3.request.error
import coil3.request.placeholder

class FilmAdapter(
    private val context: Context,
    private val data: List<Film>
) : BaseAdapter() {

    override fun getCount(): Int = data.size
    override fun getItem(position: Int): Any = data[position]
    override fun getItemId(position: Int): Long = data[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView
            ?: LayoutInflater.from(context).inflate(R.layout.item_film, parent, false)

        val film = data[position]
        val rating = if (film.rating > 0) "Rating ${film.rating}" else "Belum ada rating"

        view.findViewById<TextView>(R.id.tvTitle).text = film.title
        view.findViewById<TextView>(R.id.tvDetail).text = "${film.year ?: "-"} • $rating"
        view.findViewById<TextView>(R.id.tvCategories).text =
            film.categories.joinToString(", ") { it.name }

        view.findViewById<ImageView>(R.id.ivPoster).load(film.posterUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_gallery)
        }
        return view
    }
}