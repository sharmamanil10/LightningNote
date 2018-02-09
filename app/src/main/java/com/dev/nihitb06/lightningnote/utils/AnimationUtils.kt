package com.dev.nihitb06.lightningnote.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class AnimationUtils {

    companion object {
        fun hamburgerToBackArrow(
                drawerToggle: MyActionBarDrawerToggle,
                mainDrawerLayout: DrawerLayout,
                start: Float,
                end: Float,
                animatorListener: Animator.AnimatorListener?
        ) {
            val anim = ValueAnimator.ofFloat(start, end)

            anim.addUpdateListener { animation: ValueAnimator? ->
                try {
                    drawerToggle.onDrawerAnimate(mainDrawerLayout, animation!!.animatedValue.toString().toFloat())
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            anim.addListener(animatorListener)

            anim.interpolator = AccelerateDecelerateInterpolator()
            anim.duration = 300
            anim.start()
        }

        fun scaleAnimate(animatedView: View, scaleX: Float, scaleY: Float = scaleX) {
            animatedView.animate().scaleX(scaleX).scaleY(scaleY).start()
        }
    }
}