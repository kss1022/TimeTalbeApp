package net.suwon.plus.data.repository.user

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultUserRepository @Inject constructor(
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