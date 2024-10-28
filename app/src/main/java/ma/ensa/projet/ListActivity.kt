package ma.ensa.projet

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.SearchView
import androidx.core.app.ShareCompat
import com.bumptech.glide.Glide.init
import ma.ensa.projet.adapter.StarAdapter
import ma.ensa.projet.beans.Star
import ma.ensa.projet.databinding.ActivityListBinding
import ma.ensa.projet.service.StarService

class ListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListBinding
    private lateinit var stars: List<Star>
    private lateinit var service: StarService
    private var starAdapter: StarAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.myToolbar)
        supportActionBar?.title = "STAR RATE"

        stars = ArrayList()
        service = StarService.getInstance()
        initStars()

        stars = service.findAll()
        starAdapter = StarAdapter(this, stars)
        binding.recycleView.adapter = starAdapter
        binding.recycleView.layoutManager = LinearLayoutManager(this)
    }

    private fun filterList(s: String) {
        val filteredList = ArrayList<Star>()
        for (star in stars) {
            if (star.name.lowercase().startsWith(s.lowercase().trim())) {
                filteredList.add(star)
            }
        }
    }

    private fun initStars() {
        if (service.findAll().isEmpty()) {
            service.create(Star("Tour Eiffel", "https://media.tacdn.com/media/attractions-splice-spp-674x446/12/2e/16/f8.jpg", 0.8f))
            service.create(Star("Arc de Triomphe", "https://www.cuddlynest.com/blog/wp-content/uploads/2024/03/arc-de-triomphe.jpg", 1.7f))
            service.create(Star("Musée d'Orsay", "https://upload.wikimedia.org/wikipedia/commons/7/7c/Mus%C3%A9e_d%27Orsay%2C_North-West_view%2C_Paris_7e_140402.jpg", 3.9f))
            service.create(Star("Musée du Louvre", "https://presse.louvre.fr/wp-content/uploads/2022/06/1654857279_cour-napolon-et-pyramide-pyramide-du-louvre-arch-i-m-pei-muse-du-louvre-dist.jpg", 2.5f))
            service.create(Star("Cathédrale Notre-Dame de Paris", "https://cdn.britannica.com/29/255529-050-63A22A3C/notre-dame-de-paris-cathedral-paris-france.jpg", 1.6f))
            service.create(Star("Palais de Versailles", "https://www.vinci-autoroutes.com/static/e6f7eb4e6b4cc227718d268424371624/cc948/site-touristique-vinci-autoroutes-chateau-versailles.jpg", 1.9f))
            service.create(Star("Palais Garnier", "https://res.cloudinary.com/opera-national-de-paris/image/upload/w_768/v1563286913/visites/accueil-garnier.jpg", 1.7f))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                starAdapter?.filter?.filter(newText)
                return true
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.share) {
            val txt = "Stars"
            val mimeType = "text/plain"
            ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Stars")
                .setText(txt)
                .startChooser()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}
