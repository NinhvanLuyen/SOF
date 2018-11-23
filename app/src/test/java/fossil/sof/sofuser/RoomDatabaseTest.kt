package fossil.sof.sofuser

import android.arch.persistence.room.Room
import fossil.sof.sofuser.data.MyRoomDB
import fossil.sof.sofuser.data.entities.UserEntity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@RunWith(RobolectricTestRunner::class)
@Config(sdk = intArrayOf(21))
open class RoomDatabaseTest {
    private lateinit var myRoomDB: MyRoomDB

    @Before
    fun initDB() {
        myRoomDB = Room.inMemoryDatabaseBuilder(
                RuntimeEnvironment.application.baseContext,
                MyRoomDB::class.java)
                .allowMainThreadQueries()
                .build()
    }

    @After
    fun closeDb() {
        myRoomDB.close()
    }

    @Test
    fun insertUserBookmark() {
        var user = UserEntity()
        user.isBookmark = true
        user.display_name = "name"
        user.user_id = 1234
        myRoomDB.getUserDao().insert(user)
        var listUserBookmarked = myRoomDB.getUserDao().getAll()
        assert(listUserBookmarked.isNotEmpty())
    }

    @Test
    fun deleteUserBookmark() {
        myRoomDB.getUserDao().deleteUser(1234)
        var listUserBookmarked = myRoomDB.getUserDao().getAll()
        assert(listUserBookmarked.isEmpty())
    }
}