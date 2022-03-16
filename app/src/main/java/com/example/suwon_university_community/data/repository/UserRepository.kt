package com.example.suwon_university_community.data.repository

interface UserRepository {

    suspend fun saveUserId(userId : String)
}