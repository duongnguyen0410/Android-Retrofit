package com.example.retrofitdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var retService: AlbumService
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById<TextView>(R.id.textView)

        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(AlbumService::class.java)

        //getRequestWithQueryParameters()
        //getRequestWithPathParameters()
        uploadAlbum()
    }

    private fun getRequestWithQueryParameters(){
        val responseLiveData:LiveData<Response<Albums>> = liveData {
            val response = retService.getSortedAlbum(3)
            emit(response)
        }

        responseLiveData.observe(this, Observer {
            val albumsList = it.body()?.listIterator()
            if(albumsList!=null){
                while(albumsList.hasNext()){
                    val albumsItem = albumsList.next()
                    Log.i("AlbumAPI", "onCreate: ${albumsItem.title}")
                    val result = " " + "Album id: ${albumsItem.id}" + "\n" +
                            " " + "Album title: ${albumsItem.title}" + "\n" +
                            " " + "User id : ${albumsItem.userId}" + "\n\n\n"
                    textView.append(result)
                }
            }
        })
    }

    private fun getRequestWithPathParameters(){
        val pathResponse:LiveData<Response<AlbumsItem>> = liveData {
            val pathResponse = retService.getAlbum(3)
            emit(pathResponse)
        }

        pathResponse.observe(this, Observer {
            val title = it.body()?.title
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show()
        })
    }

    private fun uploadAlbum(){
        val album = AlbumsItem(0, "My title", 3)
        val postResponse : LiveData<Response<AlbumsItem>> = liveData {
            val response = retService.uploadAlbum(album)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedAlbumsItem = it.body()
            val result = " " + "Album id: ${receivedAlbumsItem?.id}" + "\n" +
                    " " + "Album title: ${receivedAlbumsItem?.title}" + "\n" +
                    " " + "User id : ${receivedAlbumsItem?.userId}" + "\n\n\n"
            textView.text = result


        })
    }
}