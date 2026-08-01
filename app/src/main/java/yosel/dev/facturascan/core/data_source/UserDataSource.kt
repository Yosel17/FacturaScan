package yosel.dev.facturascan.core.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import yosel.dev.facturascan.core.models.response.UserResponse
import yosel.dev.facturascan.core.utils.Constants
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    suspend fun getUser(id: String): UserResponse? {
        return firestore.collection(Constants.USERS_COLLECTION)
            .document(id)
            .get()
            .await()
            .toObject(UserResponse::class.java)
    }
}