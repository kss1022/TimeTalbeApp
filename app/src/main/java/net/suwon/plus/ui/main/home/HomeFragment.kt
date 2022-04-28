package net.suwon.plus.ui.main.home

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import net.suwon.plus.R
import net.suwon.plus.databinding.FragmentHomeBinding
import net.suwon.plus.ui.base.BaseFragment
import net.suwon.plus.ui.main.home.notice.NoticeCategory
import net.suwon.plus.ui.main.home.notice.NoticeListFragment
import net.suwon.plus.ui.main.home.setting.SettingActivity
import net.suwon.plus.widget.adapter.NoticeListFragmentAdapter
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeViewModel, FragmentHomeBinding>() {

    @Inject
    override lateinit var viewModelFactory: ViewModelProvider.Factory

    override val viewModel: HomeViewModel by viewModels<HomeViewModel> { viewModelFactory }

    override fun getViewBinding(): FragmentHomeBinding = FragmentHomeBinding.inflate(layoutInflater)

    private lateinit var  viewPagerAdapter : NoticeListFragmentAdapter


    override fun initViews() {
        initViewPager()
        initCollegeLink()
        bindViews()
    }




    private fun initViewPager() = with(binding){
        val noticeCategories = NoticeCategory.values()

        if(::viewPagerAdapter.isInitialized.not()){
            val noticeList = noticeCategories.map {
                NoticeListFragment.newInstance(it)
            }

            viewPagerAdapter = NoticeListFragmentAdapter(
                this@HomeFragment,
                noticeList
            )

            viewPager.adapter = viewPagerAdapter
            viewPager.offscreenPageLimit =  noticeCategories.size

            TabLayoutMediator(tabLayout, viewPager){ tab, position->
                tab.setText(noticeCategories[position].categoryNameId)
            }.attach()
        }
    }


    private fun initCollegeLink() = with(binding) {
        collegeLink.collegeScheduleLink.setOnClickListener {
            //https://www.suwon.ac.kr/index.html?menuno=727
            val url = Uri.Builder().scheme("https").authority("www.suwon.ac.kr")
                .appendPath("index.html")
                .appendQueryParameter("menuno", "727")
                .build()

            CustomTabsIntent.Builder().build().also {
                it.launchUrl(requireContext(), url)
            }
        }

        collegeLink.collegeNumberLink.setOnClickListener {
            //https://www.suwon.ac.kr/index.html?menuno=653
            val url = Uri.Builder().scheme("https").authority("www.suwon.ac.kr")
                .appendPath("index.html")
                .appendQueryParameter("menuno", "653")
                .build()

            CustomTabsIntent.Builder().build().also {
                it.launchUrl(requireContext(), url)
            }
        }

        collegeLink.collegeLibraryLink.setOnClickListener {
            val url = Uri.parse("https://lib.suwon.ac.kr/#")
            CustomTabsIntent.Builder().build().also {
                it.launchUrl(requireContext(), url)
            }
        }

        collegeLink.collegeMapLink.setOnClickListener {
            // TODO: 지도 만들기

            val url = Uri.Builder().scheme("https").authority("www.suwon.ac.kr")
                .appendPath("index.html")
                .appendQueryParameter("menuno", "657")
                .build()

            CustomTabsIntent.Builder().build().also {
                it.launchUrl(requireContext(), url)
            }
        }
    }



    private fun bindViews() {
        binding.settingImageView.setOnClickListener {
            startActivity(SettingActivity.newIntent(requireContext()))
            activity?.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    override fun observeData() = Unit

    companion object {
        fun newInstance() = HomeFragment()

        const val TAG = "HomeFragment"
    }
}