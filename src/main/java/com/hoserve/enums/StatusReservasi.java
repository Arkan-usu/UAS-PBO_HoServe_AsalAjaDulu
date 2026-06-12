package com.hoserve.enums;

/**
 * Enum untuk status reservasi pasien.
 * Alur: MENUNGGU -> DITERIMA -> SELESAI
 *                  -> DIBATALKAN (dari MENUNGGU atau DITERIMA)
 */
public enum StatusReservasi {
    MENUNGGU,
    DITERIMA,
    SELESAI,
    DIBATALKAN
}
