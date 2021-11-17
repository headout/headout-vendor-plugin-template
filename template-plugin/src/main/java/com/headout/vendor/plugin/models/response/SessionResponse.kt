package com.headout.vendor.plugins.experticket.models.response

data class SessionResponse (
    val sessionsGroupProfiles: List<SessionsGroupProfile>,
    val success: Boolean,
    val errorMessage: String ?= null
)

data class SessionsGroupProfile (
    val sessionsGroupProfileId: String,
    val sessionsGroups: List<SessionsGroup>
)

data class SessionsGroup (
    val sessionsGroupId: String,
    val sessions: List<Session>
)

data class Session (
    val sessionId: String,
    val sessionTime: String,
    val availableCapacity: Long
)
