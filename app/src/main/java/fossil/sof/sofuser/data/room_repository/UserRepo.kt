package fossil.sof.sofuser.data.room_repository

import android.app.Application
import android.content.res.Resources
import fossil.sof.sofuser.data.MyRoomDB
import fossil.sof.sofuser.data.entities.UserEntity
import fossil.sof.sofuser.data.room_dao.UserDao
import io.reactivex.Single

class UserRepo(application: Application) {
    protected var userDao: UserDao

    init {
        var myRoomDataBase = MyRoomDB.getInstance(application)
        userDao = myRoomDataBase!!.getUserDao()
    }

    fun insert(user: UserEntity): Single<Boolean> {
        return Single.create {
            userDao.insert(user)
            it.onSuccess(true)
        }
    }


    fun deleteAll(): Single<Boolean> {
        return Single.create {
            userDao.deleteAll()
            it.onSuccess(true)
        }
    }

    fun deleteUser(user: UserEntity): Single<Boolean> {
        return Single.create {
            userDao.deleteUser(user.user_id)
            it.onSuccess(true)
        }
    }


    fun getAll(): Single<MutableList<UserEntity>> {
        return Single.create {
            it.onSuccess(userDao.getAll().toMutableList())
        }
    }

    fun getAllLiveData() = userDao.getAllLiveData()
}