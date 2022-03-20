package com.example.suwon_university_community.data.repository.user

interface UserRepository {

    suspend fun saveUserId(userId : String)
}