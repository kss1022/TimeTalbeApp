package net.suwon.plus.widget.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import net.suwon.plus.ui.main.home.notice.NoticeListFragment

class NoticeListFragmentAdapter(
    fragment : Fragment,
    val fragmentList : List<NoticeListFragment>
) : FragmentStateAdapter(fragment){


    override fun getItemCount(): Int = fragmentList.size

    override fun createFragment(position: Int): Fragment = fragmentList[position]
}