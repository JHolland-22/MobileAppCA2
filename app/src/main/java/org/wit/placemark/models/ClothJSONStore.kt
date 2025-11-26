package org.wit.cloth.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.placemark.helpers.*
import org.wit.placemark.helpers.exists
import org.wit.placemark.helpers.write
import org.wit.placemark.models.ClothModel
import org.wit.placemark.models.ClothStore
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "clothing.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<ClothModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ClothJSONStore(private val context: Context) : ClothStore {

    var cloths = mutableListOf<ClothModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }


    override fun findById(id:Long) : ClothModel? {
        val foundCloth: ClothModel? = cloths.find { it.id == id }
        return foundCloth
    }
    override fun findAll(): MutableList<ClothModel> {
        logAll()
        return cloths
    }

    override fun create(cloth: ClothModel) {
        cloth.id = generateRandomId()
        cloths.add(cloth)
        serialize()
    }


    override fun update(cloth: ClothModel) {
        val clothsList = findAll() as ArrayList<ClothModel>
        var foundCloth: ClothModel? = clothsList.find { p -> p.id == cloth.id }
        if (foundCloth != null) {
            foundCloth.title = cloth.title
            foundCloth.description = cloth.description
            foundCloth.image = cloth.image
        }
        serialize()
    }

    override fun delete(cloth: ClothModel) {
        cloths.remove(cloth)
        serialize()
    }



    private fun serialize() {
        val jsonString = gsonBuilder.toJson(cloths, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        cloths = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        cloths.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }


}

