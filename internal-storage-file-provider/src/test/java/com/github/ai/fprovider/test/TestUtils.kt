package com.github.ai.fprovider.test

import android.net.Uri
import com.github.ai.fprovider.entity.FileModel
import io.mockk.every
import io.mockk.mockk
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

internal fun createMockedFile(
    path: String,
    name: String,
    length: Long,
    isDirectory: Boolean
): File {
    return mockk<File>()
        .also {
            every { it.path }.returns(path)
            every { it.name }.returns(name)
            every { it.length() }.returns(length)
            every { it.isDirectory }.returns(isDirectory)
        }
}

internal fun mockUri(
    path: String?
): Uri {
    return mockk<Uri>()
        .also {
            every { it.path }.returns(path)
        }
}

internal fun FileModel.toUri(
    authority: String = TestData.AUTHORITY
): Uri {
    return Uri.parse("content://$authority$path")
}

internal fun FileModel.toFile(): File {
    return File(path)
}

internal fun File.createWithContent(content: String) {
    val parentFile = parentFile ?: throw IllegalStateException()

    if (!parentFile.exists()) {
        parentFile.mkdirs()
    }

    BufferedWriter(OutputStreamWriter(FileOutputStream(this)))
        .apply {
            write(content)
            flush()
            close()
        }
}

fun createUri(
    scheme: String = "content",
    authority: String = TestData.AUTHORITY,
    path: String
): Uri {
    return Uri.parse("$scheme://$authority$path")
}