package remix.myplayer.db.room.dao

import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import remix.myplayer.db.room.model.PlayList


/**
 * Created by remix on 2019/1/12
 */
@Dao
interface PlayListDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    fun insertPlayList(playlist: PlayList): Long

    @Query("""
    SELECT * FROM PlayList
  """)
    fun selectAll(): List<PlayList>

    @RawQuery
    fun runtimeQuery(sortQuery: SupportSQLiteQuery): List<PlayList>


    @Query("""
    SELECT * FROM PlayList
    WHERE name = :name
  """)
    fun selectByName(name: String): PlayList?

    @Query("""
    SELECT * FROM PlayList
    WHERE id = :id
  """)
    fun selectById(id: Int): PlayList?

    @Query("""
    UPDATE PlayList
    SET audioIds = :audioIds
    WHERE id = :playlistId
  """)
    fun updateAudioIDs(playlistId: Int, audioIds: String): Int

    @Query("""
    UPDATE PlayList
    SET audioIds = :audioIds
    WHERE id = :name
  """)
    fun updateAudioIDs(name: String, audioIds: String): Int


    @Update
    fun update(playlist: PlayList): Int

    @Query("""
    DELETE FROM PlayList
    WHERE id = :id
  """)
    fun deletePlayList(id: Int): Int

    @Query("""
    DELETE FROM PlayList
  """)
    fun clear(): Int

}