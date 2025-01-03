package com.pab.nutritrack.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.pab.nutritrack.api.APIConfig
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.databinding.ItemRecipeBinding
import com.pab.nutritrack.ui.recipe.DetailRecipeActivity

class RecipeAdapter(val recipeList: List<RecipeItem>) :
    RecyclerView.Adapter<RecipeAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            val data = recipeList.get(position)
            binding.apply {
                txtTitle.text = data.foodName
                txtKalori.text = "± ${data.foodKalori} Kalori"
                Glide.with(itemView.context)
                    .load(APIConfig.URL_IMAGE_RECIPE + data.foodImg)
                    .transform(CenterCrop())
                    .into(imgRecipe)

                root.setOnClickListener {
                    itemView.context.startActivity(
                        Intent(
                            itemView.context,
                            DetailRecipeActivity::class.java
                        )
                            .putExtra("data", data)
                    )
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return recipeList.size
    }
}