package fossil.sof.sofuser.application.userdetail

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.like.LikeButton
import com.like.OnLikeListener
import fossil.sof.sofuser.R
import fossil.sof.sofuser.databinding.ActivityUserDetailBinding
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.BaseActivity
import fossil.sof.sofuser.libs.RecyclerViewPaginator
import fossil.sof.sofuser.libs.qualifers.RequireActivityViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers
import timber.log.Timber

@RequireActivityViewModel(UserDetailViewModel.ViewModel::class)
class UserDetailActivity : BaseActivity<UserDetailViewModel.ViewModel>() {

    companion object {
        fun getInstant(activity: Activity, user: UserEntity): Intent {
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
        viewDataBinding.likeButton.setOnLikeListener(object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                viewModel!!.input.bookmarkUser()
                viewDataBinding.likeButton1.isLiked =true
            }

            override fun unLiked(p0: LikeButton?) {
                viewModel!!.input.unBookmarkUser()
                viewDataBinding.likeButton1.isLiked =false

            }

        })
        viewDataBinding.likeButton1.setOnLikeListener(object : OnLikeListener {
            override fun liked(p0: LikeButton?) {
                viewModel!!.input.bookmarkUser()
                viewDataBinding.likeButton.isLiked =true

            }

            override fun unLiked(p0: LikeButton?) {
                viewModel!!.input.unBookmarkUser()
                viewDataBinding.likeButton.isLiked =false

            }

        })
        viewDataBinding.btnBack1.setOnClickListener { _ ->
            Timber.e("finish")
            var it =Intent()
            var bundle=Bundle()
            bundle.putParcelable("user", viewModel!!.getUser())
            it.putExtra("bd",bundle)
            setResult(Activity.RESULT_OK, it)
            finish() }
        viewDataBinding.btnBack.setOnClickListener { _ ->
            Timber.e("finish")
            var it =Intent()
            var bundle=Bundle()
            bundle.putParcelable("user", viewModel!!.getUser())
            it.putExtra("bd",bundle)
            setResult(Activity.RESULT_OK, it)
            finish() }
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

    override fun onBackPressed() {
        Timber.e("finish")
        var it =Intent()
        var bundle=Bundle()
        bundle.putParcelable("user", viewModel!!.getUser())
        it.putExtra("bd",bundle)
        setResult(Activity.RESULT_OK, it)
        finish()
    }
}
