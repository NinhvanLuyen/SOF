package fossil.sof.sofuser.domain.usecases

import fossil.sof.sofuser.domain.models.User
import fossil.sof.sofuser.domain.services.ApiService
import fossil.sof.sofuser.domain.services.LocalServices
import fossil.sof.sofuser.libs.UseCaseEnvironment

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class UserUseCase(useCaseEnvironment: UseCaseEnvironment) {
    private val apiServices: ApiService = useCaseEnvironment.apiServices
    private val localService: LocalServices = useCaseEnvironment.localService
    private val userRepo = useCaseEnvironment.userRepo

    fun getListUser(page: Int) = apiServices.getListUser(page).toObservable()
    fun getListReputation(page: Int, userId: String) = apiServices.getReputation(page, userId).toObservable()
    fun bookMarkUser(user: User) = userRepo.insert(user).toObservable()
    fun remoBookMarkUser(user: User) = userRepo.deleteUser(user).toObservable()

}
