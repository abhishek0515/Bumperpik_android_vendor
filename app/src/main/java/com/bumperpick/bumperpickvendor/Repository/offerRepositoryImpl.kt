package com.bumperpick.bumperpickvendor.Repository

import com.bumperpick.bumperpickvendor.API.Model.success_model
val demoOffers = listOf(
    HomeOffer(
        offerId = "OFF001",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Valid,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
            "https://images.unsplash.com/photo-1560243563-0627e57aaed1?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "20% OFF",
        startDate = "2025-05-01",
        endDate = "2025-06-15",
        offerTitle = "Summer Sale Extravaganza",
        offerTag = "Summer Deal",
        offerDescription = "Get 20% off on all summer clothing collections. Shop now for the latest trends in beachwear, shorts, and sunglasses!",
        termsAndCondition = "Valid on purchases over $50. Cannot be combined with other offers. Excludes clearance items. Offer valid online and in-store until June 15, 2025."
    ),
    HomeOffer(
        offerId = "OFF002",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Expired,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
        ),
        discount = "30% OFF",
        startDate = "2024-12-01",
        endDate = "2024-12-31",
        offerTitle = "Winter Clearance Sale",
        offerTag = "Winter Deal",
        offerDescription = "Save 30% on winter jackets, boots, and accessories. Limited stock available, shop before it’s gone!",
        termsAndCondition = "Offer valid on select winter items only. No rainchecks. Expired on December 31, 2024."
    ),
    HomeOffer(
        offerId = "OFF003",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Valid,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
            "https://images.unsplash.com/photo-1504279577553-1c4197a20491?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "$100 OFF",
        startDate = "2025-05-20",
        endDate = "2025-07-01",
        offerTitle = "Tech Gadget Bonanza",
        offerTag = "Electronics Deal",
        offerDescription = "Save $100 on select electronics, including smartwatches, earbuds, and tablets. Upgrade your tech today!",
        termsAndCondition = "Minimum purchase of $500 required. Offer valid on select brands only. Cannot be combined with other promotions. Valid until July 1, 2025."
    ),
    HomeOffer(
        offerId = "OFF004",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Valid,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1512568400610-62da28bc8a13?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "Buy 1 Get 1 Free",
        startDate = "2025-05-10",
        endDate = "2025-06-10",
        offerTitle = "BOGO Footwear Fiesta",
        offerTag = "Footwear Offer",
        offerDescription = "Buy one pair of shoes and get another pair free. Mix and match your favorite styles!",
        termsAndCondition = "Free item must be of equal or lesser value. Excludes premium brands. Offer valid online only until June 10, 2025."
    ),
    HomeOffer(
        offerId = "OFF005",
        Type = MarketingOption.OFFERS,
        offerValid = OfferValidation.Expired,
        Media_list = listOf(
            "https://images.unsplash.com/photo-1543168256-418811576f69?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"
        ),
        discount = "15% OFF",
        startDate = "2024-11-15",
        endDate = "2024-12-25",
        offerTitle = "Holiday Gift Bundle",
        offerTag = "Holiday Deal",
        offerDescription = "Enjoy 15% off on gift bundles, including skincare, fragrances, and accessories. Perfect for holiday gifting!",
        termsAndCondition = "Valid on gift bundles only. Cannot be combined with other discounts. Expired on December 25, 2024."
    )
)
class offerRepositoryImpl:offerRepository {
    override suspend fun AddOffer(offerModel: OfferModel): Result<success_model> {
        return Result.Success(success_model(code = 200,message = "success", is_registered = 1))
    }

    override suspend fun GetOffers(): Result<List<HomeOffer>> {
        return Result.Success(demoOffers)
    }

    override suspend fun getOfferDetails(id: String): Result<OfferModel> {
        val OfferModel=OfferModel(
            UrlMedialList = arrayListOf(
                "https://images.unsplash.com/photo-1516321318423-f06f85e504b3?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
            "https://images.unsplash.com/photo-1512568400610-62da28bc8a13?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80"

                ),
         UrlBannerIMage =   "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?ixlib=rb-4.0.3&auto=format&fit=crop&w=1350&q=80",
            productTittle = "Product Title",
            productDiscription = "Product Discription",
            termsAndCondition = "Terms and Condition",
            offerStartDate = "23-05-2025",
            offerEndDate = "25-05-2025")

        return Result.Success(OfferModel)
    }

    override suspend fun updateOffer(offerModel: OfferModel): Result<success_model> {
        return Result.Success(success_model(code = 200,message = "success", is_registered = 1))
    }
}