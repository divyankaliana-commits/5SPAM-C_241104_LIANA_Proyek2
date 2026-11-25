package com.veltix.praktikum6

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import coil3.load
import coil3.request.error
import coil3.request.placeholder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private lateinit var progress: ProgressBar
    private lateinit var tvStatus: TextView
    private var request: Call<ApiResponse<Film>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        progress = findViewById(R.id.progress)
        tvStatus = findViewById(R.id.tvStatus)

        findViewById<Button>(R.id.btnBack).setOnClickListener { finish() }
        loadDetail(intent.getIntExtra(EXTRA_ID, -1))
    }

    private fun loadDetail(id: Int) {
        if (id <= 0) {
            progress.visibility = View.GONE
            tvStatus.text = "Id film tidak valid"
            return
        }

        progress.visibility = View.VISIBLE
        tvStatus.text = "Memuat detail film..."

        val apiCall = ApiClient.filmApi.getFilm(id)
        request = apiCall

        apiCall.enqueue(object : Callback<ApiResponse<Film>> {
            override fun onResponse(
                call: Call<ApiResponse<Film>>,
                response: Response<ApiResponse<Film>>
            ) {
                progress.visibility = View.GONE
                val film = response.body()?.data
                if (response.isSuccessful && film != null) {
                    tvStatus.text = ""
                    show(film)
                } else if (response.code() == 404) {
                    tvStatus.text = "Film tidak ditemukan (kode ${response.code()})"
                } else {
                    tvStatus.text = "Gagal memuat detail (kode ${response.code()})"
                }
            }

            override fun onFailure(call: Call<ApiResponse<Film>>, t: Throwable) {
                if (call.isCanceled) return
                progress.visibility = View.GONE
                tvStatus.text = "Tidak dapat terhubung ke server"
                Log.e("Film", "Gagal memuat detail id=$id", t)
            }
        })
    }

    private fun show(film: Film) {
        val info = listOfNotNull(
            film.year?.toString(),
            film.runtime?.let { "$it menit" },
            if (film.rating > 0) "Rating ${film.rating}" else null
        ).joinToString(" • ")

        findViewById<TextView>(R.id.tvTitle).text = film.title
        findViewById<TextView>(R.id.tvInfo).text = info
        findViewById<TextView>(R.id.tvCategories).text =
            film.categories.joinToString(", ") { it.name }
        findViewById<TextView>(R.id.tvTagline).text = film.tagline.orEmpty()
        findViewById<TextView>(R.id.tvOverview).text =
            film.overview?.takeIf { it.isNotBlank() } ?: "Sinopsis belum tersedia"

        findViewById<ImageView>(R.id.ivPoster).load(film.posterUrl) {
            placeholder(android.R.drawable.ic_menu_gallery)
            error(android.R.drawable.ic_menu_gallery)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        request?.cancel()
    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }
}