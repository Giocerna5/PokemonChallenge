package com.example.pokemonchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import java.net.URI


class MainActivity : AppCompatActivity() {
   // var id = null
//https://api.pokemontcg.io/v1/cards
    override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       setContentView(R.layout.activity_main)
       var id = findViewById<TextView>(R.id.card)
       //var pic = findViewById<ImageView>(R.id.imageView1)

       val retrofit = Retrofit.Builder()
           .baseUrl("https://api.pokemontcg.io/v1/")
           .addConverterFactory(GsonConverterFactory.create())
           .build()

       val service = retrofit.create(ApiCall::class.java!!)
       val call: Call<Card> = service.info

       call.enqueue(object : Callback<Card> {
           override fun onFailure(call: Call<Card>, t: Throwable) {
               id.text =  "Failure: " + t.message
           }

           override fun onResponse(call: Call<Card>, response: Response<Card>) {
               if(!response.isSuccessful){
                   id.text = response.code().toString()
                    return
               }
               var cards = response.body()
               id.text =  "success: \n" + cards.toString()

           }
       })

    }
}

class Card{

    var image: String = ""
    var onClick: String = ""
    //@SerializedName("cards")
    //lateinit var text: String
}
interface ApiCall {

    @get:GET("cards")
    var info: Call<Card>
}



