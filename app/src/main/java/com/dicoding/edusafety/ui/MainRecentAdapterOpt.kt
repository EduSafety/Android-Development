//package com.dicoding.edusafety.ui
//
//import android.app.Activity
//import android.content.Intent
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.core.app.ActivityOptionsCompat
//import androidx.core.util.Pair
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import com.bumptech.glide.Glide
//import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
//import com.dicoding.edusafety.data.api.response.RecordReportItem
//import com.dicoding.edusafety.databinding.ListItemRecentBinding
//
//class MainRecentAdapterOpt : ListAdapter<RecordReportItem, MainRecentAdapterOpt.MyViewHolder>(ListStoryItemDiffCallback()){
//
//    fun setListUser(data: List<RecordReportItem?>?) {
//        submitList(data)
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        val binding = ListItemRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val item = getItem(position)
//        if (item != null) {
//            holder.bind(item)
//        }
//    }
//
//    class MyViewHolder(private val binding: ListItemRecentBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(data: RecordReportItem){
//            binding.textTitle.text = data.
//            binding.tvDescription.text = "${story.description}"
//            Glide.with(binding.root.context)
//                .load(story.photoUrl)
//                .transition(DrawableTransitionOptions.withCrossFade())
//                .into(binding.ivPhotoOs)
//
//            val imagePhoto = binding.ivPhotoOs
//            val tvName = binding.tvNameStory
//            val tvDescription = binding.tvDescription
//
//            itemView.setOnClickListener {
//                val intent = Intent(itemView.context, DetailStoryActivity::class.java)
//                intent.putExtra("name", story.name)
//                intent.putExtra("photo", story.photoUrl)
//                intent.putExtra("desc", story.description)
//
//                val optionsCompat: ActivityOptionsCompat =
//                    ActivityOptionsCompat.makeSceneTransitionAnimation(
//                        itemView.context as Activity,
//                        Pair(imagePhoto, "imgPhoto"),
//                        Pair(tvName, "name"),
//                        Pair(tvDescription, "description"),
//                    )
//                itemView.context.startActivity(intent, optionsCompat.toBundle())
//            }
//        }
//    }
//
//    class ListStoryItemDiffCallback : DiffUtil.ItemCallback<ListStoryItem>() {
//        override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//            return oldItem.id == newItem.id
//        }
//
//        override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
//            return oldItem == newItem
//        }
//    }
//}