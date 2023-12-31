package com.example.aplikasie_learning.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.tiagohm.markdownview.css.styles.Github
import com.example.aplikasie_learning.databinding.LayoutMarkdownBinding
import com.example.aplikasie_learning.databinding.LayoutYoutubeBinding
import com.example.aplikasie_learning.model.PartsPage
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class PartsPageAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object{
        private const val TYPE_YOUTUBE = 1
        private const val TYPE_MARKDOWN = 2

        private const val YOUTUBE = "youtube"
    }

    var partsPage = mutableListOf<PartsPage>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MarkdownViewHolder(private val markdownBinding: LayoutMarkdownBinding)
        : RecyclerView.ViewHolder(markdownBinding.root) {
        fun bindItem(partPage: PartsPage) {
            val css = Github()
            css.addRule("body", "color: black", "font-family: aksaralontara", "padding: 0px", "background-color: #DEEFEE", "font-size: 24px", "word-spacing: 7px")
            css.addRule("h3", "line-height : 1.8", "font-size: 20px")
            markdownBinding.mdContent.addStyleSheet(css)
            markdownBinding.mdContent.loadMarkdown(partPage.content)
        }

    }

    class YoutubeViewHolder(private val youtubeBinding: LayoutYoutubeBinding)
        : RecyclerView.ViewHolder(youtubeBinding.root) {
        fun bindItem(partPage: PartsPage) {
            youtubeBinding.ytContent.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    super.onReady(youTubePlayer)
                    youTubePlayer.cueVideo(partPage.content.toString(), 0F)
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_YOUTUBE -> {
                val youtubeBinding = LayoutYoutubeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                YoutubeViewHolder(youtubeBinding)
            }
            else -> {
                val markdownBinding = LayoutMarkdownBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MarkdownViewHolder(markdownBinding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (partsPage[position].type){
            YOUTUBE -> TYPE_YOUTUBE
            else -> TYPE_MARKDOWN
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val partPage = partsPage[position]
        when(getItemViewType(position)){
            TYPE_YOUTUBE -> (holder as YoutubeViewHolder).bindItem(partPage)
            TYPE_MARKDOWN -> (holder as MarkdownViewHolder).bindItem(partPage)
        }
    }

    override fun getItemCount(): Int = partsPage.size
}