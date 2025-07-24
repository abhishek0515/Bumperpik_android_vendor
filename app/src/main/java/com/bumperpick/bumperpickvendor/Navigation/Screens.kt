package com.bumperpick.bumperpickvendor.Navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Otp : Screen("otp/{$MOBILE_KEY}") {
        fun withMobile(mobile: String): String {
            return "otp/$mobile"
        }
    }
    object HomePage : Screen("homePage")
    object AddVendorDetails:Screen("addVendorDetails/{$MOBILE_KEY}/{$IS_MOBILE}"){
        fun withMobile(mobile: String,isMobile:Boolean=true): String {
            return "addVendorDetails/$mobile/$isMobile"
        }

    }
    object Subscription:Screen("subscription")
    companion object {
        const val MOBILE_KEY = "mobile"
        const val IS_MOBILE="isMobile"
        const val offerId="offerId"
        const val campaignId="campaignId"
        const val TICKET_ID="ticket_id"
        const val from="from"
        const val eventId="eventId"

    }

    object CreateOfferScreen:Screen("createOffer")
    object EditOffer:Screen("editOffer/{$offerId}"){
        fun withOfferId(offerID:String):String{
            return "editOffer/$offerID"
        }
    }

    object ScanQR:Screen("scanQR")

    object EditAccount:Screen("edit_account")

    object Mysubs:Screen("my_subs")

    object Campaign:Screen("campaign")

    object AddCampaign:Screen("addCampaign")

    object CampaignDetail:Screen("campaignDetail/{$campaignId}"){
        fun withcampaignId(campaignId:String):String{
            return "campaignDetail/$campaignId"
        }
    }

    object EditCampaign:Screen("EditCampaign/{$campaignId}"){
        fun withcampaignId(campaignId:String):String{
            return "EditCampaign/$campaignId"
        }
    }

    object AddEvent:Screen("addEvent")
    object Event:Screen("event")
    object AdsSubscription:Screen("AdsSubs/{$from}"){
        fun from_subs(from: Boolean):String{
            return "AdsSubs/$from"
        }

    }
    object AdsScreen:Screen("AdsScreen")
    object UserAdsSubsScreen:Screen("user_ads_subs")
    object Add_AD:Screen("addAD")
    object AdsEdit:Screen("AdsEditScreen/{$eventId}"){
        fun withAdId(adId:String):String{
            return "AdsEditScreen/$adId"
        }
    }

    object EventEdit:Screen("eventDetail/{$eventId}"){
        fun withEventId(eventId:String):String{
            return "eventDetail/$eventId"
        }
    }

    object Rating:Screen("ratings/{$offerId}"){
        fun withofferId(offerID: String): String{
            return "ratings/$offerID"
        }
    }
    object Faq: Screen("faq")


    object emailadmin: Screen("emailadmin")

    object ticketdetail: Screen("ticket_detail/{$TICKET_ID}"){
        fun withid(id: String): String="ticket_detail/$id"
    }

}

