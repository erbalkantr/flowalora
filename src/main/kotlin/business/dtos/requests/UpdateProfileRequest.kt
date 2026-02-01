package org.flowalora.business.dtos.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpdateProfileRequest(val fullName: String)