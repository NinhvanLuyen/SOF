package fossil.sof.sofuser.domain.usecases

import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.domain.services.ApiService
import fossil.sof.sofuser.domain.services.LocalServices
import fossil.sof.sofuser.libs.UseCaseEnvironment
import io.reactivex.schedulers.Schedulers

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class UserUseCase(useCaseEnvironment: UseCaseEnvironment) {
    private val apiServices: ApiService = useCaseEnvironment.apiServices
    private val userRepo = useCaseEnvironment.userRepo

    fun getListUser(page: Int) =
            getListBookMarkUser()
                    .concatMap { listUserBookmarked ->
                        apiServices.getListUser(page).toObservable()
                                .doOnNext {
                                    if (listUserBookmarked.isNotEmpty())
                                        for (user in listUserBookmarked) {
                                            for (u in it.getDatas()) {
                                                if (u.user_id == user.user_id)
                                                    u.isBookmark = user.isBookmark
                                            }
                                        }
                                }


                    }

    fun getListReputation(page: Int, userId: String) = apiServices.getReputation(page, userId).toObservable()
    fun bookMarkUser(user: UserEntity) = userRepo.insert(user).toObservable().subscribeOn(Schedulers.io())
    fun getListBookMarkUser() = userRepo.getAll().toObservable().subscribeOn(Schedulers.io())
    fun unBookMarkUser(user: UserEntity) = userRepo.deleteUser(user).toObservable().subscribeOn(Schedulers.io())
    fun getLiveDataUser() = userRepo.getAllLiveData()


}
