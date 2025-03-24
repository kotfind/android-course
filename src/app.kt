package org.kotfind.android_course

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.room.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

import android.content.Context
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext

@Composable
fun App() {
    val context = LocalContext.current

    val userModel = remember {
        val db = AppDatabase.getDatabase(context)
        val userDao = db.userDao();
        UserViewModel(userDao)
    }

    LaunchedEffect(Unit) {
        userModel.insert(User(1, "John", "Doe"))
        userModel.insert(User(2, "Jane", "Doe"))
    }

    val users: List<User> = userModel.allUsers.observeAsState(initial = emptyList()).value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(5.dp)
    ) {
        Text(users.joinToString())
    }
}

@Entity
data class User(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "first_name")
    val firstName: String?,

    @ColumnInfo(name = "last_name")
    val lastName: String?,
)

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): LiveData<List<User>>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :firstName AND last_name LIKE :lastName LIMIT 1")
    fun findByName(firstName: String, lastName: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)
}

@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return synchronized(this) {
                if (INSTANCE == null) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "myDatabase"
                    ).build()
                    INSTANCE = instance
                }

                INSTANCE!!
            }
        }
    }
}

class UserViewModel(private val userDao: UserDao) : ViewModel() {
    val allUsers: LiveData<List<User>> = userDao.getAll()

    fun insert(user: User) = viewModelScope.launch {
        userDao.insertAll(user)
    }
}
