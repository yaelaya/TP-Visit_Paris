package ma.ensa.projet.adapter
import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.compose.ui.graphics.Color
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import ma.ensa.projet.service.StarService
import ma.ensa.projet.R
import ma.ensa.projet.beans.Star
import ma.ensa.projet.databinding.StarEditItemBinding
import ma.ensa.projet.databinding.StarItemBinding

class StarAdapter(
    private val context: Context,
    private var stars: List<Star>
) : RecyclerView.Adapter<StarAdapter.StarViewHolder>(), Filterable {

    private var starsFilter: MutableList<Star> = ArrayList(stars)
    private val originalStars: List<Star> = stars
    private val mFilter: NewFilter = NewFilter(this)

    fun setFilteredList(filteredList: List<Star>) {
        starsFilter.clear()
        starsFilter.addAll(filteredList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StarViewHolder {
        val binding: StarItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.star_item, parent, false
        )
        return StarViewHolder(binding).apply {
            itemView.setOnClickListener {
                showEditDialog(this)
            }
        }
    }

    private fun showEditDialog(holder: StarViewHolder) {
        val binding: StarEditItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.star_edit_item,
            null,
            false
        )

        val currentStar = holder.binding.star
        if (currentStar != null) {
            binding.star = currentStar

            Glide.with(context)
                .load(currentStar.img)
                .into(binding.img)

            binding.idss.text = currentStar.id.toString()
            binding.name.text = currentStar.name

            AlertDialog.Builder(context, R.style.MyDialogStyle)
                .setTitle("Place Evaluation")
                .setView(binding.root)
                .setPositiveButton("Confirm") { _, _ ->

                    val rating = binding.ratingBar.rating
                    currentStar.star = rating

                    StarService.getInstance().update(currentStar)
                    notifyItemChanged(holder.adapterPosition)
                }
                .setNegativeButton("Cancel", null)
                .create()
                .show()
        } else {
            Log.e(TAG, "Current star is null, cannot show dialog.")
        }
    }



    override fun onBindViewHolder(holder: StarViewHolder, position: Int) {
        val currentStar = starsFilter[position]
        holder.binding.star = currentStar

        Glide.with(holder.itemView.context)
            .load(currentStar.img)
            .into(holder.binding.img)

        holder.binding.ratingBar.rating = currentStar.star

        holder.binding.executePendingBindings()
    }


    override fun getItemCount(): Int = starsFilter.size

    override fun getFilter(): Filter {
        return mFilter
    }

    inner class StarViewHolder(val binding: StarItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class NewFilter(private val adapter: StarAdapter) : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Star> = ArrayList()
            if (constraint.isNullOrEmpty()) {
                filteredList.addAll(originalStars)
            } else {
                val filterPattern = constraint.toString().lowercase().trim()
                for (item in originalStars) {
                    if (item.name.lowercase().startsWith(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }
            return FilterResults().apply {
                values = filteredList
                count = filteredList.size
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            starsFilter.clear()
            results?.values?.let {
                starsFilter.addAll(it as List<Star>)
            }
            adapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val TAG = "StarAdapter"
    }
}
