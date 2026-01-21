package org.purboyndradev.rt_rw.helper

enum class MessageSnackbarType {
    SUCCESS,
    ERROR,
    INFO
}

enum class OTPType {
    TELEGRAM,
    EMAIL
}

enum class ActivityType {
    RONDA,
    KERJA_BAKTI,
    RAPAT,
    KEGIATAN_SOSIAL
}


enum class DuesInvoiceStatus {
    PAID,
    UNPAID,
    PARTIAL,
    VOID,
    UNKNOWN;

    companion object {

        fun fromString(status: String?): DuesInvoiceStatus {
            return when (status?.uppercase()) {
                "PAID" -> PAID
                "UNPAID" -> UNPAID
                "VOID" -> VOID
                "PARTIAL" -> PARTIAL
                else -> UNKNOWN
            }
        }
    }
}


enum class PaymentMethod {
    BANK_TRANSFER,
    CASH,
    UNKNOWN;

    companion object {
        fun fromString(method: String?): PaymentMethod {
            return when (method?.uppercase()) {
                "BANK_TRANSFER" -> BANK_TRANSFER
                "CASH" -> CASH
                else -> UNKNOWN
            }
        }
    }

}