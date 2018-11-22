package fossil.sof.sofuser.application.news_feed

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fossil.sof.sofuser.R
import fossil.sof.sofuser.application.book_mark.BookmarkAdapter
import fossil.sof.sofuser.application.user_detail.UserDetailActivity
import fossil.sof.sofuser.databinding.FragmentNewsFeedBinding
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.databinding.FragmentBookmarkedBinding
import fossil.sof.sofuser.libs.BaseFragment
import fossil.sof.sofuser.libs.RecyclerViewPaginator
import fossil.sof.sofuser.libs.qualifers.RequireFragmentViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers
import timber.log.Timber

@RequireFragmentViewModel(BookmarkedViewModel.ViewModel::class)
class FragmentBookmarked : BaseFragment<BookmarkedViewModel.ViewModel>(), ItemDelegate {
    override fun bookMarkUser(user: UserEntity, isBookmark: Boolean) {
        viewModel!!.input.bookMarkUser(user, isBookmark)
    }

    override fun viewDetailUser(user: UserEntity,position:Int) {
        startActivity(UserDetailActivity.getInstant(activity!!, user))
    }

    companion object {
        fun getInstant(): FragmentBookmarked {
            var frm = FragmentBookmarked()
            return frm
        }

    }

    private lateinit var viewDataBinding: FragmentBookmarkedBinding
    private var adapter = BookmarkAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmarked, container, false)
        viewDataBinding.viewModel = viewModel
        viewDataBinding.recyclerView.layoutManager = LinearLayoutManager(activity)
        viewDataBinding.recyclerView.adapter = adapter
        viewModel!!.output.renderData
                .compose(Transformers.observeForUI())
                .compose(bindToLifecycle())
                .subscribe {
                    adapter.addData(it)
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
            adapter.removeData()
            adapter.addData(it!!.toMutableList())
        }
        viewModel!!.getListLiveData().observe(this, observable)
        viewDataBinding.swipeRefresh.setOnRefreshListener {
            adapter.removeData()
//            viewDataBinding.recyclerView.recycledViewPool.clear();
            viewModel!!.input.swipeRefresh()
        }
        return viewDataBinding.root
    }
}