package com.headout.vendor.plugin

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyDescription
import com.headout.vendor.api.models.IInventoryThreshold
import tourlandish.common.enums.inventory.PaxType

enum class GenericFieldType {
    FULLNAME, FIRSTNAME, LASTNAME, EMAIL, PHONE;

    companion object {

        @JvmStatic @JsonCreator
        fun fromString(value: String): GenericFieldType {
            val names = values().associateBy { it.name }
            return names[value] ?: throw Exception("Unable to deserialize string: $value to Generic Field Type Enum")
        }
    }
}

data class TemplateProductCode(
    val productId: Long,
    val posId: Long,
    val venueIdToCapacityId: Map<Long, Long> = emptyMap(),
    val personTypes: Map<PaxType, Long>,

    @get:JsonPropertyDescription("Use: For headout general fields, like name, lastname etc which we by default ask in a booking <br>\n" +
                "<b>Key: String </b> -> Headout Field Name (Possible Values: FULLNAME, FIRSTNAME, LASTNAME, EMAIL, PHONE) <br>\n" +
                "<b>Value: Integer </b> -> ho Generic Field Id <br>\n")
        @get:JsonProperty(required = false)
        val genericFields: Map<GenericFieldType, Long> = emptyMap(),

    @get:JsonPropertyDescription("Use: When we have a custom field in our site like Country required in a ho product <br>\n" +
                "<b>Key: Integer </b> -> Headout Custom Field Id <br>\n" +
                "<b>Value: Integer </b> ->  ho Generic Field Id <br>\n")
        @get:JsonProperty(required = false)
        val customFields: Map<Long, Long> = emptyMap(),

    @get:JsonPropertyDescription("Use: When some field which is meant as a generic field by ho is mapped to a constant value for a TID. <br>\n" +
                "<b>Key: Integer </b> -> ho Generic Field Id <br>\n" +
                "<b>Value: String </b> -> Constant Field Data <br>\n")
        @get:JsonProperty(required = false)
        val constantFields: Map<Long, String> = emptyMap(),
    override val threshold: Int = 0
): IInventoryThreshold