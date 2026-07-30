package yosel.dev.facturascan.core.utils

object Constants {

    //Firestore
    const val BILLS_COLLECTION = "Bills"

    //Room
    const val TABLE_NAME = "table_facturascan"

    //inputs
    const val SERIAL_NUMBER_FIELD = 1
    const val BILL_NUMBER_FIELD = 2
    const val ISSUE_DATE_FIELD = 3
    const val VENDOR_TAX_ID_FIELD = 4
    const val CUSTOMER_TAX_ID = 5
    const val TOTAL_AMOUNT_FIELD = 6
    const val DESCRIPTION_FIELD = 7

    //status bill
    const val DRAFT_STATUS = 0
    const val SAVE_STATUS = 1
}