package com.pab.nutritrack.ui.recipe

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.pab.nutritrack.R
import com.pab.nutritrack.api.remote.APIConfig
import com.pab.nutritrack.data.recipe.RecipeItem
import com.pab.nutritrack.databinding.ActivityDetailRecipeBinding
import com.pab.nutritrack.utils.parcelable

class DetailRecipeActivity : AppCompatActivity() {

    private val onBackPressedCallback: OnBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }

    private lateinit var binding: ActivityDetailRecipeBinding
    private lateinit var recipeItem: RecipeItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding = ActivityDetailRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipeItem = intent.parcelable<RecipeItem>("data")!!

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle("")
        }

        binding.apply {

            Glide.with(this@DetailRecipeActivity)
                .load(APIConfig.URL_IMAGE_RECIPE + recipeItem.foodImg)
                .transform(CenterCrop())
                .into(imgRecipe)

            incContent.apply {

                cpRecipe!!.apply {
                    progressMax = 1000f
                    setProgressWithAnimation(recipeItem.foodKalori.toFloat(), 1000)
                    progressBarWidth = 5f
                    backgroundProgressBarWidth = 7f
                    progressBarColor = ContextCompat.getColor(this@DetailRecipeActivity, R.color.primary)
                }

                pbKarbo!!.apply {
                    max = 100
                    progress = recipeItem.foodKarbo.toInt()
                    ObjectAnimator.ofInt(this, "progress", recipeItem.foodKarbo.toInt()).setDuration(1000).start()
                }

                pbProtein!!.apply {
                    max = 100
                    progress = recipeItem.foodProtein.toInt()
                    ObjectAnimator.ofInt(this, "progress", recipeItem.foodProtein.toInt()).setDuration(1000).start()
                }
                pbLeamk!!.apply {
                    max = 100
                    progress = recipeItem.foodLemak.toInt()
                    ObjectAnimator.ofInt(this, "progress", recipeItem.foodLemak.toInt()).setDuration(1000).start()
                }

                txtTitle!!.text = recipeItem.foodName
                txtDesc!!.text = HtmlCompat.fromHtml(recipeItem.foodDesc, 0)
                txtKarboVal!!.text = "${recipeItem.foodKarbo} gram"
                txtProteinVal!!.text = "${recipeItem.foodProtein} gram"
                txtLemakVal!!.text = "${recipeItem.foodLemak} gram"
                txtKaloriVal!!.text =
                    HtmlCompat.fromHtml("<h7><b>${recipeItem.foodKalori}</b></h7><br>kkal", 0)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressedCallback.handleOnBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}