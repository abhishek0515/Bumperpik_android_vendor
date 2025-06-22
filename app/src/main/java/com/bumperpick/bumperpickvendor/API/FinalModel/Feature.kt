package com.bumperpick.bumperpickvendor.API.FinalModel

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.annotations.JsonAdapter
import java.lang.reflect.Type

data class Feature(
    val id: Int,
    val name: String,
    val type: String,
    val value: FeatureValue
)

sealed class FeatureValue {
    data class BooleanValue(val value: Boolean) : FeatureValue()
    data class StringValue(val value: String) : FeatureValue()
}
class FeatureDeserializer : JsonDeserializer<Feature> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Feature {
        val jsonObject = json.asJsonObject

        val id = jsonObject["id"].asInt
        val name = jsonObject["name"].asString
        val type = jsonObject["type"].asString
        val valueElement = jsonObject["value"]

        val value = when {
            valueElement.isJsonPrimitive && valueElement.asJsonPrimitive.isBoolean ->
                FeatureValue.BooleanValue(valueElement.asBoolean)
            valueElement.isJsonPrimitive && valueElement.asJsonPrimitive.isString ->
                FeatureValue.StringValue(valueElement.asString)
            else -> throw JsonParseException("Unexpected value type")
        }

        return Feature(id, name, type, value)
    }
}
