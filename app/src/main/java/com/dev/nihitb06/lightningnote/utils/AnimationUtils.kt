package com.dev.nihitb06.lightningnote.utils

import android.animation.Animator
import android.animation.ValueAnimator
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle

class AnimationUtils {

    companion object {
        fun hamburgerToBackArrow(drawerToggle: ActionBarDrawerToggle, mainDrawerLayout: DrawerLayout, start: Float, end: Float) {
            val anim = ValueAnimator.ofFloat(start, end)

            anim.addUpdateListener { animation: ValueAnimator? ->
                try {
                    drawerToggle.onDrawerSlide(mainDrawerLayout, animation!!.animatedValue.toString().toFloat())
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                }
            }

            anim.addListener(object: Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {

                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationStart(animation: Animator?) {

                }
            })
        }
    }
}