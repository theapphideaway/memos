package com.theapphideaway.memo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.theapphideaway.memo.Database.DbManager
import com.theapphideaway.memo.Model.Note

class NoteAdapter(private val noteList: ArrayList<Note>, private val context: Context):
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): ViewHolder {

        var noteCard = LayoutInflater.from(context).inflate(R.layout.note_card, viewGroup, false)

        return ViewHolder(noteCard)
    }

    fun goToDetails(note: Note){
        var intent = Intent(context, AddNote::class.java)
        intent.putExtra("Id", note.Id)
        intent.putExtra("Title", note.Title)
        intent.putExtra("Content", note.Content)
        startActivity(context, intent, null)
    }

    override fun getItemCount(): Int {
        return noteList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindNote(noteList[position])

        holder.itemView.setOnClickListener { goToDetails(noteList[position]) }

        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(context)

            builder.setTitle("Delete Note")

            builder.setMessage("Are you sure you want to delete this note?")

            builder.setPositiveButton("YES"){dialog, which ->

                var dbManager = DbManager(this.context!!)
                val selectionArgs= arrayOf(noteList[position].Id.toString())
                dbManager.delete("Id=?", selectionArgs )
                noteList.removeAt(position)
                notifyItemRemoved(position)
                true

            }

            builder.setNegativeButton("No"){dialog,which ->
                Toast.makeText(context,"No changes made",Toast.LENGTH_SHORT).show()
            }

            val dialog: AlertDialog = builder.create()

            dialog.show()

            true
        }

    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindNote( note: Note){
            var titleText: TextView = itemView.findViewById(R.id.title) as TextView
            var contentText: TextView = itemView.findViewById(R.id.content) as TextView

            titleText.text = note.Title
            contentText.text = note.Content
        }

    }
}