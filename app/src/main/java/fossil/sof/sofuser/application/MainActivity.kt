package fossil.sof.sofuser.application

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.newsfeed.FragmentBookmarked
import fossil.sof.sofuser.application.newsfeed.FragmentNewsFeed
import fossil.sof.sofuser.databinding.ActivityMainBinding
import fossil.sof.sofuser.libs.BaseActivity
import fossil.sof.sofuser.libs.qualifers.RequireActivityViewModel

@RequireActivityViewModel(MainViewModel.ViewModel::class)
class MainActivity : BaseActivity<MainViewModel.ViewModel>() {

    lateinit var viewDataBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewDataBinding.viewModel = viewModel
        val fragments = ArrayList<Fragment>()
        fragments.add(FragmentNewsFeed.getInstant())
        fragments.add(FragmentBookmarked.getInstant())
        val adapter = PagerAdapter(supportFragmentManager, fragments)
        viewDataBinding.viewPager.adapter = adapter
        viewDataBinding.navigatorButton.createView(null, mutableListOf(R.drawable.ic_user, R.drawable.ic_folder_special), R.color.colorPrimaryDark, R.color.gray, R.color.white, 0)
        viewDataBinding.navigatorButton.setViewPagerListener(viewDataBinding.viewPager)

    }
}
