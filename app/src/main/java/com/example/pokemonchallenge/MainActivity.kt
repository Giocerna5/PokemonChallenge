package com.example.pokemonchallenge

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
class MainActivity : AppCompatActivity() {
    lateinit var test: TextView
    lateinit var test2: ImageView
    var arrayListDetails:ArrayList<Model> = ArrayList()
    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        test = this.findViewById(R.id.card)
        test2 = this.findViewById(R.id.card1)
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
                var jsonarrayInfo:JSONArray= jsonContact.getJSONArray("cards")
                var i:Int = 0
                var size:Int = jsonarrayInfo.length()
                for (i in 0 until size) {
                    var jsonObjectDetail:JSONObject = jsonarrayInfo.getJSONObject(i)
                    var model:Model= Model()
                    model.image = jsonObjectDetail.getString("imageUrl").toString()
                    model.onclick = jsonObjectDetail.getString("imageUrlHiRes").toString()
                    arrayListDetails.add(model)
                }
                runOnUiThread {
                    // Stuff that updates the UI
                    test.text = arrayListDetails.size.toString()
                    val picasso = Picasso.get()
                    picasso.load(arrayListDetails.get(0).image)
                        .into(test2)

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
