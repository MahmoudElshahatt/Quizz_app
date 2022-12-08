package com.example.quizzapp.Adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.quizzapp.Model.Quiz
import com.example.quizzapp.R
import com.example.quizzapp.databinding.ListItemBinding


class QuizListAdapter(private var clickListener: ClickListener) :
    RecyclerView.Adapter<QuizListAdapter.QuizViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(layoutInflater, parent, false)
        return QuizViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.bind(differ.currentList.get(position))
    }

    override fun getItemCount(): Int {
        if (differ.currentList.size == null) {
            return 0
        } else
            return differ.currentList.size
    }

    inner class QuizViewHolder(val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(quiz: Quiz) {
            binding.detailTitle.text = quiz.name
            val imageUrl = quiz.image
            //Images from Firebase Storage
            Glide
                .with(binding.root.context)
                .load(imageUrl)
                .centerCrop()
                .placeholder(R.drawable.placeholder_image)
                .into(binding.detailImage)

            binding.detailDescription.text = quiz.desc
            binding.detailDifficulty.text = quiz.level
            binding.listBtn.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }

    interface ClickListener {
        fun onItemClick(quizPosition: Int)
    }

    private val diffUtilCall = object : DiffUtil.ItemCallback<Quiz>() {
        override fun areItemsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
            return oldItem.quizId == newItem.quizId
        }

        override fun areContentsTheSame(oldItem: Quiz, newItem: Quiz): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffUtilCall)
}