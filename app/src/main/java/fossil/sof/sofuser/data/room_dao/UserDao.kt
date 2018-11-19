package fossil.sof.sofuser.data.room_dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import fossil.sof.sofuser.data.entities.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(userEntity: UserEntity)

    @Query("DELETE FROM user")
    fun deleteAll()

    @Query("DELETE FROM user where `user_id ` =:userID")
    fun deleteUser(userID: Int)

    @Query("SELECT * from user")
    fun getAll(): List<UserEntity>
}