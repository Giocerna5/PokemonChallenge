package com.example.pokemonchallenge

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException


class MainActivity : AppCompatActivity() {

    var arrayListDetails:ArrayList<CardImages> = ArrayList()
    //OkHttpClient creates connection pool between client and server
    private val client = OkHttpClient()
    lateinit var recyclerView: RecyclerView


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run("https://api.pokemontcg.io/v1/cards")
    }
    private fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val strResponse = response.body()!!.string()
                //creating json object
                val jsonContact = JSONObject(strResponse)
                //creating json array
                val jsonarrayInfo:JSONArray= jsonContact.getJSONArray("cards")
                val size:Int = jsonarrayInfo.length()
                var counter = 1
                var model= CardImages()

                for (i in 0 until size) {
                    val jsonObjectDetail:JSONObject = jsonarrayInfo.getJSONObject(i)

                    when (counter) {
                        1 -> {
                            model.image = jsonObjectDetail.getString("imageUrl").toString()
                            model.onclick = jsonObjectDetail.getString("imageUrlHiRes").toString()
                        }
                        2 -> {
                            model.image2 = jsonObjectDetail.getString("imageUrl").toString()
                            model.onclick2 = jsonObjectDetail.getString("imageUrlHiRes").toString()
                        }
                        3 -> {
                            model.image3 = jsonObjectDetail.getString("imageUrl").toString()
                            model.onclick3 = jsonObjectDetail.getString("imageUrlHiRes").toString()
                        }
                        else -> {
                            counter = 0
                            model.image4 = jsonObjectDetail.getString("imageUrl").toString()
                            model.onclick4 = jsonObjectDetail.getString("imageUrlHiRes").toString()
                            arrayListDetails.add(model)
                            model = CardImages()
                        }
                    }
                    counter++
                }
                runOnUiThread {
                    recyclerView = findViewById(R.id.views)
                    recyclerView.layoutManager = LinearLayoutManager(this@MainActivity, RecyclerView.VERTICAL, false)
                    val adapter = CustomAdapter(arrayListDetails)
                    recyclerView.adapter = adapter
                }
            }
        })
    }
}
class CardImages {
     var image:String = ""
     var image2:String = ""
     var image3:String = ""
     var image4:String = ""

     var onclick:String = ""
     var onclick2:String = ""
     var onclick3:String = ""
     var onclick4:String = ""
}

class CustomAdapter(private var list:ArrayList<CardImages>): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val card = itemView.findViewById<ImageView>(R.id.imageView)!!
        val card2 = itemView.findViewById<ImageView>(R.id.imageView2)!!
        val card3 = itemView.findViewById<ImageView>(R.id.imageView3)!!
        val card4 = itemView.findViewById<ImageView>(R.id.imageView4)!!

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)

        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card :CardImages = list[position]
        val picasso = Picasso.get()
        if(card.image != "") {
            picasso.load(card.image)
                .into(holder.card)
        }
        if(card.image2 != "") {
            picasso.load(card.image2)
                .into(holder.card2)
        }
        if(card.image3 != "") {
            picasso.load(card.image3)
                .into(holder.card3)
        }
        if(card.image4 != "") {
            picasso.load(card.image4)
                .into(holder.card4)
        }
    }
}