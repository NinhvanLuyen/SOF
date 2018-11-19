package fossil.sof.sofuser.data.room_repository

import android.app.Application
import fossil.sof.sofuser.data.MyRoomDB
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.data.room_dao.UserDao
import fossil.sof.sofuser.domain.models.User
import io.reactivex.Single

class UserRepo(application: Application) {
    protected lateinit var userDao: UserDao

    init {
        var myRoomDataBase = MyRoomDB.getInstance(application)
        userDao = myRoomDataBase!!.getUserDao()
    }

    fun insert(user: User): Single<Boolean> {
        return Single.create {
            userDao.insert(user as UserEntity)
            it.onSuccess(true)
        }
    }


    fun deleteAll(): Single<Boolean> {
        return Single.create {
            userDao.deleteAll()
            it.onSuccess(true)
        }
    }

    fun deleteUser(user: User): Single<Boolean> {
        return Single.create {
            userDao.deleteUser(user.getUser_id())
            it.onSuccess(true)
        }
    }


    fun getAll(): Single<MutableList<out User>> {
        return Single.create {
            if (userDao.getAll().isNotEmpty())
                it.onSuccess(userDao.getAll().toMutableList())
        }
    }
}