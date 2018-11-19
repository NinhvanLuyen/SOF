package fossil.sof.sofuser.application.user_detail

import android.databinding.ObservableField
import android.view.View
import fossil.sof.sofuser.data.api.responses.LoadMoreData
import fossil.sof.sofuser.domain.models.Reputation
import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.libs.ActivityViewModel
import fossil.sof.sofuser.libs.Environment
import fossil.sof.sofuser.libs.tranforms.Transformers
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface UserDetailViewModel {
    class Data {
        var avatar = ObservableField<String>()
        var name = ObservableField<String>()
        var reputation = ObservableField<String>()
        var location = ObservableField<String>()
        var errorMessage = ObservableField<String>()
        var notfound = ObservableField<Boolean>()
        var showLoading = ObservableField<Boolean>()
        var loadDataError = ObservableField<Boolean>()
    }

    class ViewModel(environment: Environment) : ActivityViewModel(), Input, OutPut, Errors {

        var data = Data()
        var input = this
        var output = this
        var errors = this
        var listDataTerm = arrayListOf<Reputation>()
        var userUseCase = environment.userUseCase;
        var canLoadmore = true
        var currentPage = 1
        private lateinit var user: User
        var renderData: BehaviorSubject<Pair<List<Reputation>, Boolean>> = BehaviorSubject.create()
        override fun renderData(): Observable<Pair<List<Reputation>, Boolean>> = renderData
        private var nextPage = PublishSubject.create<Boolean>()
        private var closeSwipe = PublishSubject.create<Boolean>()
        private var callApi = PublishSubject.create<Int>()
        lateinit var loadData: Observable<LoadMoreData<out Reputation>>
        override fun nextPage() {
            nextPage.onNext(true)

        }

        override fun showErrorMessage(): Observable<String> = apiError
                .map {
                    data.showLoading.set(false)
                    if (listDataTerm.size > 0) {
                        renderData.onNext(Pair(emptyList(), false))
                        it.errorMessage
                    } else {
                        data.loadDataError.set(true)
                        data.errorMessage.set(it.errorMessage)
                        ""
                    }
                }

        override fun closeSwipe(): Observable<Boolean> = closeSwipe

        override fun swipeRefresh() {
            closeSwipe.onNext(false)
            listDataTerm.clear()
            data.showLoading.set(true)
            data.loadDataError.set(false)
            renderData.onNext(Pair(emptyList<Reputation>(), false))
            currentPage = 1
            callApi.onNext(currentPage)
            nextPage.onNext(true)

        }

        init {
            intent().subscribe {
                user = it.getParcelableExtra("user")
                data.avatar.set(user.getProfile_image())
                data.name.set(user.getDisplay_name())
                data.location.set(user.getLocation())
                data.reputation.set(user.getReputationFormat())
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
                    .compose(bindTolifecycle())
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

        fun retry(view: View) {
            data.loadDataError.set(false)
            data.showLoading.set(true)
            callApi.onNext(currentPage)
            nextPage.onNext(true)
            loadData.subscribe()
        }

        fun noAction(view: View) {

        }

        fun call(nextPage: Int): Observable<LoadMoreData<out Reputation>> = userUseCase.getListReputation(nextPage, "${user.getUser_id()}")

    }

    interface Input {
        fun swipeRefresh()
        fun nextPage()
    }

    interface OutPut {
        fun renderData(): Observable<Pair<List<Reputation>, Boolean>>
        fun closeSwipe(): Observable<Boolean>


    }

    interface Errors {
        fun showErrorMessage(): Observable<String>
    }

//    class Data {
//        var name = ObservableField<String>()
//        var showError = ObservableField<Boolean>(false)
//        var showLoading = ObservableField<Boolean>(false)
//        var error = ObservableField<String>()
//    }
}