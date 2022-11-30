package com.example.igeniusandroidtest.data.source.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/*
 * Store just some data. 
 */
@JsonClass(generateAdapter = true)
data class AuthUserRepos(
    var repos: List<Repository>?
)

@Entity
@JsonClass(generateAdapter = true)
data class Repository(
    @Json(name = "created_at")
    var createdAt: String?,
    @Json(name = "default_branch")
    var defaultBranch: String?,
    var description: String?,
    @Json(name = "forks_count")
    var forksCount: Int?,
    @Json(name = "full_name")
    var fullName: String?,
    @Json(name = "git_url")
    var gitUrl: String?,
    var language: Any?,
    var name: String?,
    @Json(name = "open_issues_count")
    var openIssuesCount: Int?,
    @Embedded var owner: Owner?,
    var private: Boolean?,
    @Json(name = "subscribers_count")
    var subscribersCount: Int?,
    @PrimaryKey var id: Int?,
) {
    constructor() : this(
        createdAt = null,
        defaultBranch = null,
        description = null,
        forksCount = null,
        fullName = null,
        gitUrl = null,
        language = null,
        name = null,
        openIssuesCount = null,
        owner = null,
        private = null,
        subscribersCount = null,
        id = null
    )
}

@JsonClass(generateAdapter = true)
data class Owner(
    @Json(name = "avatar_url")
    var avatar_url: String?,
    var login: String?,
    var type: String?,
    var url: String?
)

