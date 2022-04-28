package net.suwon.plus.data.repository.user

interface UserRepository {

    suspend fun saveUserId(userId : String)
}