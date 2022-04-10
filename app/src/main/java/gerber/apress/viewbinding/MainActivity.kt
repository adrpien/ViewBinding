package gerber.apress.viewbinding

import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues.TAG
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import gerber.apress.viewbinding.databinding.ActivityMainBinding
import java.lang.IllegalArgumentException
import kotlin.random.Random

class MainActivity : Activity(){

    companion object{
        var nameArray: ArrayList<String> = arrayListOf()
        var numberArray: ArrayList<String> = arrayListOf()
        var idArray: ArrayList<String> = arrayListOf()
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState != null){
            val savedRandom = savedInstanceState.getString("savedRandom")
            binding.randomNumerTextView.setText(savedRandom)
        } else {
            val savedRandom = Random.nextInt(100).toString()
            binding.randomNumerTextView.setText(savedRandom)
        }


        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MyAdapter()

        val contentResolver = contentResolver
        val cursor: Cursor? = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null, null)

        try {
            if (cursor != null){
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    val id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.CONTACT_ID))
                    idArray.add(id)
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY))
                    nameArray.add(name)
                    val number = readNumber(contentResolver, id)
                    numberArray.add(number)
                    cursor.moveToNext()
                }

            }
        } finally {
            if (cursor != null) cursor.close()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState?.putString("savedRandom", binding.randomNumerTextView.text.toString())
    }
}

fun readNumber(contentResolver: ContentResolver, id: String): String {
    var number: String = ""
    val cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID  +"= ?" ,
        arrayOf(id),
        null,
        null
    )
    if (cursor != null){
        cursor.moveToFirst()
        number = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER))
    } else {
        number = "Brak numeru!"
    }


    return number
}
