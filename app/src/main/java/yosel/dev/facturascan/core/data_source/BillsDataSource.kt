package yosel.dev.facturascan.core.data_source

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import yosel.dev.facturascan.core.models.request.BillRequest
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

    suspend fun createBill(request: BillRequest) {
        firestore.collection(Constants.BILLS_COLLECTION)
            .document(request.id)
            .set(request)
            .await()
    }

    suspend fun deleteBill(id: String) {
        firestore.collection(Constants.BILLS_COLLECTION)
            .document(id)
            .delete()
            .await()
    }

    suspend fun updateBill(id: String, updates: Map<String, Any?>) {
        firestore.collection(Constants.BILLS_COLLECTION)
            .document(id)
            .update(updates)
            .await()
    }

}