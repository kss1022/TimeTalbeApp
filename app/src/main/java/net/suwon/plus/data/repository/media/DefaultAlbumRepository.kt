package net.suwon.plus.data.repository.media

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.provider.MediaStore
import androidx.core.database.getStringOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.suwon.plus.data.entity.media.Album
import net.suwon.plus.util.DeviceUtil
import net.suwon.plus.util.PagingConstants

@SuppressLint("InlinedApi")
class DefaultAlbumRepository  constructor(
    private val context: Context,
)  : AlbumRepository {

    private val contentUri = PagingConstants.getContentUri()

    private val folderMap = LinkedHashMap<Long?, Album>()

    private val projection = arrayListOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.BUCKET_ID,
        MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
        MediaStore.Files.FileColumns.DATE_MODIFIED
    ).apply {
        if (DeviceUtil.isAndroid10Later()) {
            add(MediaStore.Files.FileColumns.RELATIVE_PATH)
        } else {
            add(MediaStore.Files.FileColumns.DATA)
        }
    }.toTypedArray()
    private val selection =
        "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?"

    private val selectionArgs: Array<String> = arrayOf(
        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
    )

    private val sortOrder = "${MediaStore.Files.FileColumns.DATE_MODIFIED} DESC"



    override suspend fun load(): Flow<HashMap<Long?, Album>> {
        return flow {
            //Recent
            val recentMediaCursor: Cursor? = context.contentResolver.query(
                contentUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            if (recentMediaCursor?.moveToNext() == true) {
                folderMap[null] = Album(
                    contentUri = contentUri,
                    recentMediaId = recentMediaCursor.getLong(
                        recentMediaCursor.getColumnIndex(
                            MediaStore.Files.FileColumns._ID
                        )
                    ),
                    bucketId = null,
                    name = "최신목록",
                    count = recentMediaCursor.count,
                    order = PagingConstants.FOLDER_ORDER_RECENT,
                    relativePath = recentMediaCursor.getStringOrNull(
                        recentMediaCursor.getColumnIndex(
                            MediaStore.Files.FileColumns.RELATIVE_PATH
                        )
                    ),
                    data = recentMediaCursor.getStringOrNull(
                        recentMediaCursor.getColumnIndex(
                            MediaStore.Files.FileColumns.DATA
                        )
                    )
                )
            }
            recentMediaCursor?.close()

            //Retrieve folder list

            val cursor: Cursor? = context.contentResolver.query(
                contentUri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
            cursor?.let { c ->
                while (c.moveToNext()) {
                    val bucketId =
                        c.getLong(c.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_ID))
                    if (folderMap.contains(bucketId)) {
                        //hit
                        folderMap[bucketId]?.increaseCount()
                        continue
                    } else {
                        //add
                        val id = c.getLong(c.getColumnIndex(MediaStore.Files.FileColumns._ID))
                        val name =
                            c.getString(c.getColumnIndex(MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME))
                        val relativePath =
                            c.getStringOrNull(c.getColumnIndex(MediaStore.Files.FileColumns.RELATIVE_PATH))
                        val data =
                            c.getStringOrNull(c.getColumnIndex(MediaStore.Files.FileColumns.DATA))

                        var order =
                            c.getLong(c.getColumnIndex(MediaStore.Files.FileColumns.DATE_MODIFIED))

                        if (DeviceUtil.isAndroid10Later()) {
                            if (relativePath == "DCIM/Camera/") {
                                order = PagingConstants.FOLDER_ORDER_CAMERA
                            } else if (relativePath == "Download/") {
                                order = PagingConstants.FOLDER_ORDER_DOWNLOAD
                            }
                        }

                        if (!DeviceUtil.isAndroid10Later()) {
                            if (data == Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DCIM
                                ).toString()
                            ) {
                                order = PagingConstants.FOLDER_ORDER_CAMERA
                            } else if (data == Environment.getExternalStoragePublicDirectory(
                                    Environment.DIRECTORY_DOWNLOADS
                                ).toString()
                            ) {
                                order = PagingConstants.FOLDER_ORDER_DOWNLOAD
                            }

                        }

                        folderMap[bucketId] = Album(
                            contentUri = contentUri,
                            recentMediaId = id,
                            bucketId = bucketId,
                            name = name,
                            count = 1,
                            order = order,
                            relativePath = relativePath,
                            data = data,
                        )
                    }
                    emit(folderMap)
                }
            }
            cursor?.close()
        }
    }
}