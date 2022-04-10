package gerber.apress.viewbinding

import android.content.ContentUris
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import gerber.apress.viewbinding.MainActivity.Companion.idArray

class MyAdapter(): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.my_row, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val name: TextView = holder.view.findViewById(R.id.nameTextView)
        val phoneNumber: TextView = holder.view.findViewById(R.id.numberTextView)
        val callButton: Button = holder.view.findViewById(R.id.callButton)
        val smsButton: Button = holder.view.findViewById(R.id.smsButton)

        name.setOnLongClickListener(object: View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean{

                val uri: Uri = ContentUris.withAppendedId(
                    ContactsContract.Contacts.CONTENT_URI,
                    idArray[position].toLong())
                val action = Intent.ACTION_EDIT
                val intent = Intent()
                intent.data = uri
                intent.action = action
                startActivity(holder.itemView.context, intent, null)
                return false
            }
        })

        smsButton.setOnClickListener {
            val smsIntent = Intent()
            if(MainActivity.numberArray[position] == "Brak numeru!"){
                Toast.makeText(holder.itemView.context, "Brak numeru!", Toast.LENGTH_SHORT).show()
            } else {
                smsIntent.data = Uri.parse("sms:" + MainActivity.numberArray[position])
                smsIntent.action = Intent.ACTION_VIEW
                startActivity(holder.itemView.context, smsIntent, null)
            }
        }

        callButton.setOnClickListener {
            val callIntent = Intent()
            if(MainActivity.numberArray[position] == "Brak numeru!"){
                Toast.makeText(holder.itemView.context, "Brak numeru!", Toast.LENGTH_SHORT).show()
            } else {
                callIntent.data = Uri.parse("tel:" + MainActivity.numberArray[position])
                callIntent.action = Intent.ACTION_DIAL
                startActivity(holder.itemView.context, callIntent, null)
            }
        }

        name.text = ""
        if(MainActivity.nameArray[position].length>10){
            var x: Int = 0
            while (x<10){
                if(x<7) {
                    var znak: Char = MainActivity.nameArray[position].get(x)
                    name.append(znak.toString())
                } else {
                    name.append(".")
                }
                x++
            }

        } else {
            name.text = MainActivity.nameArray[position]
        }
        phoneNumber.text = (MainActivity.numberArray[position])
    }

    override fun getItemCount(): Int {
        return MainActivity.nameArray.size
    }
}

class MyViewHolder(val view: View): RecyclerView.ViewHolder(view) {}