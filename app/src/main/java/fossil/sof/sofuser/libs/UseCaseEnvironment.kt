package fossil.sof.sofuser.libs

import fossil.sof.sofuser.data.room_repository.UserRepo
import fossil.sof.sofuser.domain.services.ApiService
import fossil.sof.sofuser.domain.services.LocalServices

/**
 * Created by ninhvanluyen on 16/11/18.
 */
class UseCaseEnvironment(val apiServices: ApiService,
                         val localService: LocalServices,
                         val userRepo: UserRepo)