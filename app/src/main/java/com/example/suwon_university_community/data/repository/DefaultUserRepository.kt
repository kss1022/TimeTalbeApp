package com.example.suwon_university_community.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class DefaultUserRepository(
    private val fireStore: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {


    override suspend fun saveUserId(userId: String) {
        withContext(ioDispatcher) {
            val user = hashMapOf(
                "id" to userId
            )

            try {
                fireStore.collection("user").add(user)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}