package hughjd.xyz.aperio.password

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import io.reactivex.Flowable

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password")
    fun getAll(): Flowable<List<Password>>

    @Query("SELECT * FROM password WHERE id = :id")
    fun getById(id: Long) : Password

    @Insert
    fun insert(password: Password)

    @Insert
    fun insertAll(passwords: List<Password>)

    @Update
    fun update(password: Password)

    @Delete
    fun delete(password: Password)

    @Query("DELETE FROM password")
    fun deleteAll()
}
