package fossil.sof.sofuser.application.news_feed

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.user_detail.UserDetailActivity
import fossil.sof.sofuser.databinding.FragmentNewsFeedBinding
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.BaseFragment
import fossil.sof.sofuser.libs.RecyclerViewPaginator
import fossil.sof.sofuser.libs.qualifers.RequireFragmentViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers

@RequireFragmentViewModel(NewFeedViewModel.ViewModel::class)
class FragmentNewsFeed : BaseFragment<NewFeedViewModel.ViewModel>(), ItemDelegate {
    override fun bookMarkUser(user: User, isBookmark: Boolean) {
        viewModel!!.input.bookMarkUser(user, isBookmark)
    }

    override fun viewDetailUser(user: User) {
        startActivity(UserDetailActivity.getInstant(activity!!, user))
    }

    companion object {
        fun getInstant(): FragmentNewsFeed {
            var frm = FragmentNewsFeed()
            return frm
        }

    }

    private lateinit var viewDataBinding: FragmentNewsFeedBinding
    private lateinit var recyclerViewPaginator: RecyclerViewPaginator
    private var adapter = UserAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_feed, container, false)
        viewDataBinding.viewModel = viewModel
        viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
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
        }
        return viewDataBinding.root
    }
}