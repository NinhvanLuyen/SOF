package fossil.sof.sofuser.application.news_feed

import android.databinding.ObservableField
import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.Environment
import fossil.sof.sofuser.libs.FragmentViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface NewFeedViewModel {
    interface Input {
        fun swipeRefresh()
        fun nextPage()
        fun bookMarkUser(user: User, isBookmark: Boolean)
    }

    interface OutPut {
        fun renderData(): Observable<Pair<List<User>, Boolean>>
        fun closeSwipe(): Observable<Boolean>
        fun notiMyBookmark(): Observable<User>


    }

    interface Errors {
        fun showErrorMessage(): Observable<String>
    }

    class Data {
        var name = ObservableField<String>()
        var showError = ObservableField<Boolean>(false)
        var showLoading = ObservableField<Boolean>(false)
        var error = ObservableField<String>()
    }

    class ViewModel(environment: Environment) : FragmentViewModel(), Input, OutPut, Errors {


        var data = Data()
        var input = this
        var output = this
        var errors = this
        var listDataTerm = arrayListOf<User>()
        var userUseCase = environment.userUseCase;
        var canLoadmore = true
        var currentPage = 1
        var renderData: BehaviorSubject<Pair<List<User>, Boolean>> = BehaviorSubject.create()
        override fun renderData(): Observable<Pair<List<User>, Boolean>> = renderData
        private var nextPage = PublishSubject.create<Boolean>()
        private var closeSwipe = PublishSubject.create<Boolean>()
        private var notyUserBookmark = BehaviorSubject.create<User>()
        private var callApi = PublishSubject.create<Int>()
        lateinit var loadData: Observable<LoadMoreData<out User>>
        override fun nextPage() {
            nextPage.onNext(true)

        }

        override fun notiMyBookmark(): Observable<User> = notyUserBookmark

        override fun showErrorMessage(): Observable<String> = apiError
                .map {
                    data.showLoading.set(false)
                    if (listDataTerm.size > 0) {
                        renderData.onNext(Pair(emptyList(), false))
                        it.errorMessage
                    } else {
                        data.showError.set(true)
                        data.error.set(it.errorMessage)
                        ""
                    }
                }

        override fun closeSwipe(): Observable<Boolean> = closeSwipe

        override fun swipeRefresh() {
            closeSwipe.onNext(false)
            listDataTerm.clear()
            data.showLoading.set(true)
            data.showError.set(false)
            renderData.onNext(Pair(emptyList<User>(), false))
            currentPage = 1
            callApi.onNext(currentPage)
            nextPage.onNext(true)

        }

        init {
            arguments().subscribe {
                data.name.set(it.getString("params", ""))
                if (listDataTerm.isEmpty()) {
                    data.showLoading.set(true)
                    callApi.onNext(currentPage)
                    nextPage.onNext(true)
                } else {
                    renderData.onNext(Pair(listDataTerm, canLoadmore))
                }
            }
            loadData = callApi.compose<Int>(Transformers.takeWhen(nextPage))
                    .switchMap(this::call)
                    .doOnError { loadData.subscribe() }
                    .compose(Transformers.pipeApiErrorTo(apiError))
                    .compose(bindToLifecycle())
                    .doOnNext {
                        data.showLoading.set(false)
                        currentPage++
                        callApi.onNext(currentPage)
                        canLoadmore = (currentPage < 1000)
                        renderData.onNext(Pair(it.getDatas(), canLoadmore))
                        listDataTerm.addAll(it.getDatas())
                    }
            loadData.subscribe()

        }

        fun call(nextPage: Int): Observable<LoadMoreData<out User>> = userUseCase.getListUser(nextPage)

        override fun bookMarkUser(user: User, isBookmark: Boolean) {
            if (isBookmark)
                userUseCase.bookMarkUser(user)
                        .subscribe {
                            notyUserBookmark.onNext(user)
                        }
            else
                userUseCase.remoBookMarkUser(user)
                        .subscribe {
                            notyUserBookmark.onNext(user)
                        }


        }

    }


}