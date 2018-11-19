package fossil.sof.sofuser.application.user_detail

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.MainViewModel
import fossil.sof.sofuser.application.PagerAdapter
import fossil.sof.sofuser.application.news_feed.FragmentNewsFeed
import fossil.sof.sofuser.databinding.ActivityMainBinding
import fossil.sof.sofuser.databinding.ActivityUserDetailBinding
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.BaseActivity
import fossil.sof.sofuser.libs.RecyclerViewPaginator
import fossil.sof.sofuser.libs.qualifers.RequireActivityViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers

@RequireActivityViewModel(UserDetailViewModel.ViewModel::class)
class UserDetailActivity : BaseActivity<UserDetailViewModel.ViewModel>() {

    companion object {
        fun getInstant(activity: Activity, user: User): Intent {
            var it = Intent(activity, UserDetailActivity::class.java)
            it.putExtra("user", user)
            return it
        }
    }

    lateinit var viewDataBinding: ActivityUserDetailBinding
    private lateinit var recyclerViewPaginator: RecyclerViewPaginator
    private var adapter = ReputationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_user_detail)
//        setSupportActionBar(viewDataBinding.toolbar)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        viewDataBinding.toolbar.setNavigationOnClickListener { _ -> onBackPressed() }
//        viewDataBinding.toolbar.setNavigationIcon(R.drawable.ic_back)
//        viewDataBinding.collapsingToolbar.setExpandedTitleColor(resources.getColor(android.R.color.transparent))
//        viewDataBinding.collapsingToolbar.title = "User detail"
        viewDataBinding.btnBack1.setOnClickListener { _ -> onBackPressed() }
        viewDataBinding.btnBack.setOnClickListener { _ -> onBackPressed() }
        viewDataBinding.viewModel = viewModel
        viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewDataBinding.recyclerView.adapter = adapter
        recyclerViewPaginator = RecyclerViewPaginator(viewDataBinding.recyclerView, { viewModel!!.input.nextPage() })
        viewModel!!.output.renderData
                .compose(Transformers.observeForUI())
                .compose(bindToLifecycle())
                .subscribe {
                    adapter.addData(it.first, it.second)
                }
        viewModel!!.errors.showErrorMessage()
                .compose(Transformers.observeForUI())
                .compose(bindToLifecycle())
                .subscribe(this::showErrorMessage)
        viewModel!!.output.closeSwipe()
                .compose(bindToLifecycle())
                .compose(Transformers.observeForUI())
                .subscribe { viewDataBinding.swipeRefresh.isRefreshing = it }
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            adapter.removeData()
            recyclerViewPaginator.start()
//            viewDataBinding.recyclerView.recycledViewPool.clear();
            viewModel!!.input.swipeRefresh()
//        }

        }
    }
}
