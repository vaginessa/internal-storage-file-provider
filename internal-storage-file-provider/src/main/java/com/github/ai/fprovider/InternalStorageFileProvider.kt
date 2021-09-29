package com.github.ai.fprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.github.ai.fprovider.entity.Result
import android.os.RemoteException
import com.github.ai.fprovider.data.InternalFileSystem
import com.github.ai.fprovider.domain.Interactor
import com.github.ai.fprovider.domain.MimeTypeProvider
import com.github.ai.fprovider.domain.PathConverter
import com.github.ai.fprovider.domain.usecases.GetMimeTypeUseCase

class InternalStorageFileProvider constructor(): ContentProvider() {

    private lateinit var interactor: Interactor

    internal constructor(interactor: Interactor) : this() {
        this.interactor = interactor
    }

    override fun onCreate(): Boolean {
        val context = this.context ?: return false

        val rootPath = context.filesDir?.parentFile?.path ?: return false
        val fileSystem = InternalFileSystem(rootPath)
        val mimeTypeProvider = MimeTypeProvider()

        interactor = Interactor(
            pathConverter = PathConverter(),
            mimeTypeUseCase = GetMimeTypeUseCase(
                fileSystem = fileSystem,
                mimeTypeProvider = mimeTypeProvider
            )
        )

        return true
    }

    override fun getType(uri: Uri): String {
        val result = interactor.getMimeType(uri)
        if (result.isFailure) {
            throwExceptionFromResult(result)
        }

        return result.getOrThrow()
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        throw RuntimeException()
    }

    override fun insert(
        uri: Uri,
        values: ContentValues?
    ): Uri? {
        throw RuntimeException()
    }

    override fun delete(
        uri: Uri,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw RuntimeException()
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        throw RuntimeException()
    }

    private fun throwExceptionFromResult(result: Result<*>): Nothing {
        val message = result.getExceptionOrThrow().toString()
        throw RemoteException(message)
    }
}