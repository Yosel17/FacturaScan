package yosel.dev.facturascan.core.data_source

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import yosel.dev.facturascan.core.models.response.BillResponse
import yosel.dev.facturascan.core.utils.Constants
import javax.inject.Inject

class BillsDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    suspend fun getAllBills(): List<BillResponse> {
        return firestore.collection(Constants.BILLS_COLLECTION)
            .get()
            .await()
            .toObjects(BillResponse::class.java)
    }

}