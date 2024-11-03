package com.cred.cool.core

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

object Utility {

    fun getCalculatedHeight(context: Context, reductionFactor: Int): Int {
        val displayMetrics = context.resources.displayMetrics
        val screenHeight = displayMetrics.heightPixels
        val reductionPx = (reductionFactor * displayMetrics.density).toInt()
        return screenHeight - reductionPx
    }

    fun formatNumberWithCommas(number: String): String {
        return try {
            val amount = number.toLong()
            val formatter = NumberFormat.getInstance(Locale("en", "IN")) as DecimalFormat
            formatter.format(amount)
        } catch (e: NumberFormatException) {
            "Invalid number"
        }
    }

    fun String.toCurrencyFormat(): String {
        val amount = this.toLongOrNull() ?: return "Invalid number"
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")) as DecimalFormat
        return formatter.format(amount)
    }

    fun View.fadeInAndSlideUp(duration: Long = 250) {
        // Create a fade-in animation
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = duration

        // Create a slide-up animation
        val slideUp = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f
        )
        slideUp.duration = duration

        // Combine fade and slide animations
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeIn)
        animationSet.addAnimation(slideUp)

        // Start the animation
        this.startAnimation(animationSet)

        // Set the view to visible at the end of the animation
        this.visibility = View.VISIBLE
    }

    fun View.fadeIn(duration: Long = 200) {
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            this.duration = duration
            fillAfter = true  // Keeps the view visible after the animation
        }
        this.startAnimation(fadeIn)
        this.visibility = View.VISIBLE
    }

    fun View.fadeOut(duration: Long = 200) {
        val fadeOut = AlphaAnimation(1f, 0f).apply {
            this.duration = duration
            fillAfter = true
        }
        this.startAnimation(fadeOut)

        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                this@fadeOut.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun View.fadeOutAndSlideDown(duration: Long = 100) {
        // Create a fade-out animation
        val fadeOut = AlphaAnimation(1f, 0f)
        fadeOut.duration = duration

        // Create a slide-down animation
        val slideDown = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f
        )
        slideDown.duration = duration

        // Combine fade-out and slide-down animations
        val animationSet = AnimationSet(true)
        animationSet.addAnimation(fadeOut)
        animationSet.addAnimation(slideDown)
        animationSet.fillAfter = true  // Keep view in the final position

        // Start the animation
        this.startAnimation(animationSet)

        // Set the view to gone at the end of the animation
        animationSet.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                this@fadeOutAndSlideDown.visibility = View.GONE
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }
}