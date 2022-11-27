package me.svbneelmane.imageprocessing_coroutines

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import me.svbneelmane.imageprocessing_coroutines.databinding.ActivityMainBinding
import java.net.URL

class MainActivity : AppCompatActivity() {
    companion object {
        const val IMAGE_URL =
            "https://raw.githubusercontent.com/DevTides/JetpackDogsApp/master/app/src/main/res/drawable/dog.png"
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        coroutineScope.launch {
            val original = coroutineScope.async(Dispatchers.IO) {
//                delay(2000L) - to see loading if downloads fast
                getOriginalBitmap()
            }.await()

            val filtered =
                withContext(coroutineScope.coroutineContext + Dispatchers.Default) {
                    applyFilter(original)
                }

            loadImage(filtered)
        }
    }

    private fun getOriginalBitmap(): Bitmap = URL(IMAGE_URL).openStream().use { inputStream ->
        BitmapFactory.decodeStream(inputStream)
    }

    private fun loadImage(bitmap: Bitmap) {
        binding.apply {
            progressBar.visibility = View.GONE
            imageView.setImageBitmap(bitmap)
            imageView.visibility = View.VISIBLE
        }

    }

    private fun applyFilter(bitmap: Bitmap) = Filter.apply(bitmap)
}