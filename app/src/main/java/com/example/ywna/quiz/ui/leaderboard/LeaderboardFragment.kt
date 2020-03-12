package com.example.ywna.quiz.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.example.ywna.quiz.R
import com.ogaclejapan.smarttablayout.SmartTabLayout
import androidx.viewpager.widget.ViewPager
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems


class LeaderboardFragment : Fragment() {

    private var adapter: FragmentPagerAdapter? = null
    private var viewPager: ViewPager? = null
    private var viewPagerTab: SmartTabLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        adapter = FragmentPagerItemAdapter(
            childFragmentManager, FragmentPagerItems.with(requireActivity())
                .add(getString(R.string.this_week), WeeklyLeaderboardFragment::class.java)
                .add(getString(R.string.all_time), AllTimeLeaderboardFragment::class.java)
                .create()
        )

        viewPager = root.findViewById(R.id.viewpager)
        viewPagerTab = root.findViewById(R.id.viewpagertab)
        setUpViewPager()
        return root
    }

    private fun setUpViewPager() {
        viewPager?.adapter = adapter
        viewPagerTab?.setViewPager(viewPager)
        viewPager?.currentItem

    }

    companion object {
        private const val TAG = "Leaderboard Fragment"
    }
}