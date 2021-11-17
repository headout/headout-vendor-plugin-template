package com.headout.vendor.plugins.ho.api.models

data class ProductCategory(
        val name: String?,
        val description: String?,
        val isGeneral: Boolean
)

data class Product(
        val productId: Long,
        val name: String,
        val shortName: String?,
        val description: String?,
        val shortDescription: String?,
        val image: String?,
        val basePrice: String?,
        val onlyExtras: String?,
        val showCalendar: String?,
        val showEvents: String?,
        val disabledDates: String?,
        val weekDays: String?,
        val displayOrder: String?,
        val status: String?,
        val numbered: String?,
        val productCategory: ProductCategory?
)
