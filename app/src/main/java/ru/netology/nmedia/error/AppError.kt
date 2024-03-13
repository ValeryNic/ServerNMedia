package ru.netology.nmedia.error

import android.database.SQLException
import java.io.IOException

// sealed устанавливает, что после компиляции создавать наследников данного класса в проекте нельзя
sealed class AppError(var code: String) : RuntimeException() {
    companion object {
        fun from(e: Throwable): AppError = when (e) {
            is AppError -> e
            is SQLException -> DbError
            is IOException -> NetworkError
            else -> UnknownError
        }
    }
}
class ApiError(val status: Int, code: String): AppError(code)
object DbError : AppError("error_db")
object NetworkError : AppError("error_network")
object UnknownError: AppError("error_unknown")