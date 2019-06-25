package com.theapphideaway.memo.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.theapphideaway.memo.AddNote
import com.theapphideaway.memo.Model.Note
import com.theapphideaway.memo.NoteAdapter
import com.theapphideaway.memo.ViewModel.NoteViewModel
import com.theapphideaway.memo.R
import kotlinx.android.synthetic.main.content_main.view.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment: Fragment() {

    private var noteAdapter: NoteAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var noteList: ArrayList<Note>? = null
    private val viewModel = NoteViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)

       // MobileAds.initialize(rootView.context, "ca-app-pub-26884270~fake");
//        val mAdView = rootView.findViewById(R.id.ad_view) as AdView
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)

        rootView.fab.setOnClickListener { view ->
            var intent = Intent(rootView.context, AddNote::class.java)
            startActivityForResult(intent, 1)
        }

        noteList = ArrayList()
        layoutManager = LinearLayoutManager(rootView.context)
        noteAdapter = NoteAdapter(noteList!!, rootView.context)

        rootView.note_recycler_view.adapter = noteAdapter
        rootView.note_recycler_view.layoutManager = layoutManager

        viewModel.loadNote("%", noteList!!, context!!)

        noteAdapter!!.notifyDataSetChanged()
        return rootView

    }

    override fun onResume() {
        super.onResume()
        viewModel.loadNote("%", noteList!!, context!!)
        noteAdapter!!.notifyDataSetChanged()
    }
}