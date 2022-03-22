package com.example.suwon_university_community.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class LectureListFragmentAdapter(
    fragment : FragmentActivity,
    val fragmentList : List<Fragment>
) : FragmentStateAdapter(fragment){


    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}