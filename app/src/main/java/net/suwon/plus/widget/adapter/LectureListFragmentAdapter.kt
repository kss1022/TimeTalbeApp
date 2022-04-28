package net.suwon.plus.widget.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.suwon.plus.ui.main.time_table.addTimeTable.addcell.lecturelist.LectureListFragment

class LectureListFragmentAdapter(
    fragment : FragmentActivity,
    val fragmentList : List<LectureListFragment>
) : FragmentStateAdapter(fragment){


    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}