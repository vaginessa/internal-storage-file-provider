package com.github.ai.fprovider.data

import com.github.ai.fprovider.entity.FileModel
import com.github.ai.fprovider.entity.Result
import com.github.ai.fprovider.entity.exception.InvalidFileTypeException
import com.github.ai.fprovider.entity.exception.InvalidFileTypeException.Companion.FILE_IS_NOT_A_DIRECTORY
import com.github.ai.fprovider.entity.exception.NoAccessException
import com.github.ai.fprovider.utils.toModel
import com.github.ai.fprovider.utils.toModels
import java.io.File
import java.io.FileNotFoundException

internal class InternalFileSystem(
    private val rootDirPath: String
) : FileSystem {

    override fun getFile(path: String): Result<FileModel> {
        val file = File(rootDirPath + path)
        if (!file.exists()) {
            return Result.Failure(FileNotFoundException(file.path))
        }

        return Result.Success(file.toModel())
    }

    override fun getChildFiles(parentPath: String): Result<List<FileModel>> {
        val file = File(rootDirPath + parentPath)
        if (!file.exists()) {
            return Result.Failure(FileNotFoundException(file.path))
        }

        if (!file.isDirectory) {
            return Result.Failure(
                InvalidFileTypeException(
                    message = String.format(FILE_IS_NOT_A_DIRECTORY, file)
                )
            )
        }

        // TODO: write test for NoAccessException
        val files = file.listFiles()
            ?.toList()
            ?.toModels()
            ?: return Result.Failure(NoAccessException(file))

        return Result.Success(files)
    }
}