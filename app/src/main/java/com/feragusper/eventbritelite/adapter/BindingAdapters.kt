package com.feragusper.eventbritelite.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.feragusper.eventbritelite.R
import com.feragusper.eventbritelite.model.Event


@BindingAdapter("imageFromEvent")
fun bindImageFromEvent(view: ImageView, event: Event?) {
    Glide.with(view.context)
        .load(event?.imageURL)
        .error(R.drawable.ic_event)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(view)
}