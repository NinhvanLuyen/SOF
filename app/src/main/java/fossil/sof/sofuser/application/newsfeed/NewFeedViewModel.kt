package fossil.sof.sofuser.application.newsfeed

import android.databinding.ObservableField
import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.data.entities.UserEntity
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
        fun bookMarkUser(user: UserEntity, isBookmark: Boolean)
    }

    interface OutPut {
        fun renderData(): Observable<Pair<List<UserEntity>, Boolean>>
        fun closeSwipe(): Observable<Boolean>
        fun notiMyBookmark(): Observable<UserEntity>
        fun renderListUserBookmarked(): Observable<MutableList<UserEntity>>


    }

    interface Errors {
        fun showErrorMessage(): Observable<String>
    }

    class Data {
        var name = ObservableField<String>()
        var showNotFoundData = ObservableField<Boolean>(false)
        var showError = ObservableField<Boolean>(false)
        var showLoading = ObservableField<Boolean>(false)
        var error = ObservableField<String>()
    }

    class ViewModel(environment: Environment) : FragmentViewModel(), Input, OutPut, Errors {
        var data = Data()
        var input = this
        var output = this
        var errors = this
        var listDataTerm = arrayListOf<UserEntity>()
        var listDataUserBookmarkedTerm = arrayListOf<UserEntity>()
        var userUseCase = environment.userUseCase;
        var canLoadmore = true
        var currentPage = 1
        var renderData: BehaviorSubject<Pair<List<UserEntity>, Boolean>> = BehaviorSubject.create()
        var renderDataUserBookmarked: BehaviorSubject<MutableList<UserEntity>> = BehaviorSubject.create()
        override fun renderListUserBookmarked(): Observable<MutableList<UserEntity>> = renderDataUserBookmarked
        override fun renderData(): Observable<Pair<List<UserEntity>, Boolean>> = renderData
        private var nextPage = PublishSubject.create<Boolean>()
        private var closeSwipe = PublishSubject.create<Boolean>()
        private var notyUserBookmark = BehaviorSubject.create<UserEntity>()
        private var callApi = PublishSubject.create<Int>()
        lateinit var loadData: Observable<LoadMoreData<UserEntity>>
        override fun nextPage() {
            nextPage.onNext(true)

        }

        override fun notiMyBookmark(): Observable<UserEntity> = notyUserBookmark

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
            renderData.onNext(Pair(emptyList<UserEntity>(), false))
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
                    renderDataUserBookmarked.onNext(listDataUserBookmarkedTerm)
                }
            }
            loadData = callApi.compose<Int>(Transformers.takeWhen(nextPage))
                    .switchMap(this::call)
                    .doOnError { loadData.subscribe() }
                    .compose(Transformers.pipeApiErrorTo(apiError))
                    .compose(bindToLifecycle())
                    .doOnNext {
                        data.showLoading.set(false)
                        if (it.getHasMore())
                            currentPage++
                        else
                            canLoadmore = false
                        callApi.onNext(currentPage)
                        canLoadmore = (currentPage < 1000)
                        renderData.onNext(Pair(it.getDatas(), canLoadmore))
                        listDataTerm.addAll(it.getDatas())
                    }
            loadData.subscribe()

            userUseCase.getListBookMarkUser()
                    .compose(Transformers.pipeApiErrorTo(apiError))
                    .compose(bindToLifecycle())
                    .subscribe {
                        data.showNotFoundData.set(it.isEmpty())
                        renderDataUserBookmarked.onNext(it)
                        listDataUserBookmarkedTerm.addAll(it)
                    }

        }

        fun getListLiveData() = userUseCase.getLiveDataUser()

        fun call(nextPage: Int): Observable<LoadMoreData<UserEntity>> = userUseCase.getListUser(nextPage)

        override fun bookMarkUser(user: UserEntity, isBookmark: Boolean) {
            if (isBookmark)
                userUseCase.bookMarkUser(user)
                        .subscribe {
                            notyUserBookmark.onNext(user)
                        }
            else
                userUseCase.unBookMarkUser(user)
                        .subscribe {
                            notyUserBookmark.onNext(user)
                        }


        }

    }


}