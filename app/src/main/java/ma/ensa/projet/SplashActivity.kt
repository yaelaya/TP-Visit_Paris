package ma.ensa.projet

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ma.ensa.projet.databinding.SplashActivityBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: SplashActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.splash_activity)

        binding.image.animate().rotation(360f).setDuration(2000)
        binding.image.animate().scaleX(0.5f).scaleY(0.5f).setDuration(3000)
        binding.image.animate().translationYBy(1000f).setDuration(2000)
        binding.image.animate().alpha(0f).setDuration(6000)

        Thread {
            try {
                Thread.sleep(2000)
                val intent = Intent(this@SplashActivity, ListActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }
}



