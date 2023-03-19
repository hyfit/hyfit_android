package com.example.hyfit_android.community

//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentManager
//import android.support.v4.app.FragmentStatePagerAdapter
//import android.view.ViewGroup
//
//class CommunityAdapter(manager: FragmentManager): FragmentStatePagerAdapter(manager) {
//
//    private var fragmentList: MutableList<Fragment>  = arrayListOf()
//    private var titleList: MutableList<String> = arrayListOf()
//
//    override fun getItem(position: Int): Fragment? {
//        return fragmentList[position]
//    }
//
//    override fun getCount(): Int {
//        return fragmentList.size
//    }
//
//    override fun getPageTitle(position: Int): CharSequence? {
//        return titleList[position]
//    }
//
////    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
////        super.destroyItem(container, position, `object`)
////    }
//
//    fun addFragment(fragment: Fragment, title:String) {
//        fragmentList.add(fragment)
//        titleList.add(title)
//    }
//
//
//}