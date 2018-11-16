package fossil.sof.sofuser.data.entities

import fossil.sof.sofuser.domain.models.User

class UserEntity : User {
    private var account_id = 0
    private var is_employee = false
    private var last_modified_date = 0L
    private var last_access_date = 0L
    private var creation_date = 0L
    private var reputation_change_year = 0
    private var reputation_change_quarter = 0
    private var reputation_change_month = 0
    private var reputation_change_week = 0
    private var reputation_change_day = 0
    private var reputation = 0
    private var user_id = 0
    private var accept_rate = 0
    private var user_type = ""
    private var location = ""
    private var website_url = ""
    private var link = ""
    private var profile_image = ""
    private var display_name = ""


    override fun GetAccountId() = account_id
    override fun isEmployee() = is_employee

    override fun getLastModifiedDate() = last_modified_date
    override fun getLastAccessDate() = last_access_date
    override fun getCreationDate() = creation_date
    override fun getReputationChangeYear() = reputation_change_year
    override fun getReputationChangeQuarter() = reputation_change_quarter
    override fun getReputationChangeMonth() = reputation_change_month
    override fun getReputationChangeWeek() = reputation_change_week
    override fun getReputationChangeDay() = reputation_change_day
    override fun getReputation() = reputation
    override fun getUserId() = user_id
    override fun getAcceptRate() = accept_rate
    override fun getUserType() = user_type
    override fun getLocation() = location
    override fun getWebsiteUrl() = website_url
    override fun getLink() = link
    override fun getProfileImage() = profile_image
    override fun getDisplayName() = display_name
}