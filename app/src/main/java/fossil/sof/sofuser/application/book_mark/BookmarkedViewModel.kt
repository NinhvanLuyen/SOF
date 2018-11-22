package fossil.sof.sofuser.application.news_feed

import android.databinding.ObservableField
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.libs.Environment
import fossil.sof.sofuser.libs.FragmentViewModel
import fossil.sof.sofuser.libs.tranforms.Transformers
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface BookmarkedViewModel {
    interface Input {
        fun swipeRefresh()
        fun bookMarkUser(user: UserEntity, isBookmark: Boolean)
    }

    interface OutPut {
        fun renderData(): Observable<MutableList<UserEntity>>
        fun closeSwipe(): Observable<Boolean>
        fun notiMyBookmark(): Observable<UserEntity>


    }

    interface Errors {
        fun showErrorMessage(): Observable<String>
    }

    class Data {
        var name = ObservableField<String>()
        var showNotFoundData = ObservableField<Boolean>(false)
        var showLoading = ObservableField<Boolean>(false)
    }

    class ViewModel(environment: Environment) : FragmentViewModel(), Input, OutPut, Errors {


        var data = Data()
        var input = this
        var output = this
        var errors = this
        var listDataTerm = mutableListOf<UserEntity>()
        var userUseCase = environment.userUseCase;
        var renderData: BehaviorSubject<MutableList<UserEntity>> = BehaviorSubject.create()
        override fun renderData(): Observable<MutableList<UserEntity>> = renderData
        private var closeSwipe = PublishSubject.create<Boolean>()
        private var notyUserBookmark = BehaviorSubject.create<UserEntity>()
        lateinit var loadData: Observable<MutableList<UserEntity>>

        override fun notiMyBookmark(): Observable<UserEntity> = notyUserBookmark

        override fun showErrorMessage(): Observable<String> = apiError
                .map {
                    data.showLoading.set(false)
                    if (listDataTerm.size > 0) {
                        renderData.onNext(mutableListOf())
                        it.errorMessage
                    } else ""
                }

        override fun closeSwipe(): Observable<Boolean> = closeSwipe

        override fun swipeRefresh() {
            closeSwipe.onNext(false)
            listDataTerm.clear()
            data.showLoading.set(true)
            data.showNotFoundData.set(false)
            renderData.onNext(mutableListOf())
            loadData.subscribe()

        }

        init {
            arguments().subscribe {
                data.name.set(it.getString("params", ""))
                if (listDataTerm.isEmpty()) {
                    data.showLoading.set(true)
                } else {
                    renderData.onNext(listDataTerm)
                }
            }


            loadData = userUseCase.getListBookMarkUser()
                    .doOnError { loadData.subscribe() }
                    .compose(Transformers.pipeApiErrorTo(apiError))
                    .compose(bindToLifecycle())
                    .doOnNext {
                        data.showNotFoundData.set(it.isEmpty())
                        data.showLoading.set(false)
                        renderData.onNext(it)
                        listDataTerm.addAll(it)
                    }
            loadData.subscribe()

        }

        fun getListLiveData() = userUseCase.getLiveDataUser()

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