package org.wit.cloth.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.placemark.helpers.*
import org.wit.placemark.helpers.exists
import org.wit.placemark.helpers.write
import org.wit.placemark.models.TicketModel
import org.wit.placemark.models.TicketStore
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "clothing.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<TicketModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ClothJSONStore(private val context: Context) : TicketStore {

    var cloths = mutableListOf<TicketModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }


    override fun findById(id:Long) : TicketModel? {
        val foundCloth: TicketModel? = cloths.find { it.id == id }
        return foundCloth
    }
    override fun findAll(): MutableList<TicketModel> {
        logAll()
        return cloths
    }

    override fun create(cloth: TicketModel) {
        cloth.id = generateRandomId()
        cloths.add(cloth)
        serialize()
    }


    override fun update(cloth: TicketModel) {
        val clothsList = findAll() as ArrayList<TicketModel>
        var foundCloth: TicketModel? = clothsList.find { p -> p.id == cloth.id }
        if (foundCloth != null) {
            foundCloth.title = cloth.title
            foundCloth.description = cloth.description
            foundCloth.image = cloth.image
        }
        serialize()
    }

    override fun delete(cloth: TicketModel) {
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

