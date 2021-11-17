package com.headout.vendor.plugins.ho.api.models

data class Client(val name: String,
                  val legalEntityName: String,
                  val pointOfSaleId: Long)

data class ClientList(val clientList: List<Client>)
