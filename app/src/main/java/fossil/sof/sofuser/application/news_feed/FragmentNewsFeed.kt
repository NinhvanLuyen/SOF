package fossil.sof.sofuser.application.news_feed

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.user_detail.UserDetailActivity
import fossil.sof.sofuser.databinding.FragmentNewsFeedBinding
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.BaseFragment
import fossil.sof.sofuser.libs.RecyclerViewPaginator
import fossil.sof.sofuser.libs.qualifers.RequireFragmentViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers

@RequireFragmentViewModel(NewFeedViewModel.ViewModel::class)
class FragmentNewsFeed : BaseFragment<NewFeedViewModel.ViewModel>(), ItemDelegate {
    private lateinit var userViewDetail: UserEntity
    override fun bookMarkUser(user: UserEntity, isBookmark: Boolean) {
        viewModel!!.input.bookMarkUser(user, isBookmark)
    }

    override fun viewDetailUser(user: UserEntity, position: Int) {
        positionItemViewDetail = position
        userViewDetail = user
        startActivityForResult(UserDetailActivity.getInstant(activity!!, user), REQ_VIEW_DETAIL)
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
    private var positionItemViewDetail: Int = 0
    private var REQ_VIEW_DETAIL = 129

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
        var observable = android.arch.lifecycle.Observer<List<UserEntity>> {
            adapter.searchAndNotifyItemChange(it!!.toMutableList())
        }
        viewModel!!.getListLiveData().observe(this, observable)

        viewDataBinding.swipeRefresh.setOnRefreshListener {
            adapter.removeData()
            recyclerViewPaginator.start()
//            viewDataBinding.recyclerView.recycledViewPool.clear();
            viewModel!!.input.swipeRefresh()
        }

        return viewDataBinding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_VIEW_DETAIL && resultCode == Activity.RESULT_OK) {
            var bd = data!!.getBundleExtra("bd")
            var userRes = bd.getParcelable<UserEntity>("user")
            if (userRes.isBookmark != userViewDetail.isBookmark) {
                //NotifyData changed
                adapter.notifySingleItemChange(userRes, positionItemViewDetail)
            }
        }
    }
}