package com.example.pokemonchallenge

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
class MainActivity : AppCompatActivity() {

    var arrayListDetails:ArrayList<Model> = ArrayList()
    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    lateinit var recyclerView: RecyclerView


    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        run("https://api.pokemontcg.io/v1/cards")
    }
    fun run(url: String) {
        val request = Request.Builder()
            .url(url)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                var strResponse = response.body()!!.string()
                //creating json object
                val jsonContact:JSONObject = JSONObject(strResponse)
                //creating json array
                val jsonarrayInfo:JSONArray= jsonContact.getJSONArray("cards")
                val size:Int = jsonarrayInfo.length()
                for (i in 0 until size) {
                    val jsonObjectDetail:JSONObject = jsonarrayInfo.getJSONObject(i)
                    val model:Model= Model()
                    model.image = jsonObjectDetail.getString("imageUrl").toString()
                    model.onclick = jsonObjectDetail.getString("imageUrlHiRes").toString()
                    arrayListDetails.add(model)
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
class Model {
    lateinit var image:String
    lateinit var onclick:String
    constructor(image: String, onclick:String) {
        this.image = image
        this.onclick = onclick
    }
    constructor()
}

class CustomAdapter(var list:ArrayList<Model>): RecyclerView.Adapter<CustomAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<ImageView>(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_list, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card :Model = list[position]
        val picasso = Picasso.get()
        picasso.load(card.image)
          .into(holder.name)


    }
}