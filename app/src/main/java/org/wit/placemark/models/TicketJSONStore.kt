package org.wit.ticket.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import org.wit.placemark.helpers.*
import org.wit.placemark.helpers.exists
import org.wit.placemark.helpers.write
import org.wit.ticket.models.TicketModel
import org.wit.ticket.models.TicketStore
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "ticket.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<TicketModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ClothJSONStore(private val context: Context) : TicketStore {

    var tickets = mutableListOf<TicketModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }


    override fun findById(id:Long) : TicketModel? {
        val foundCloth: TicketModel? = tickets.find { it.id == id }
        return foundCloth
    }
    override fun findAll(): MutableList<TicketModel> {
        logAll()
        return tickets
    }

    override fun create(ticket: TicketModel) {
        ticket.id = generateRandomId()
        tickets.add(ticket)
        serialize()
    }


    override fun update(ticket: TicketModel) {
        val ticketsList = findAll() as ArrayList<TicketModel>
        var foundTicket: TicketModel? = ticketsList.find { p -> p.id == ticket.id }
        if (foundTicket != null) {
            foundTicket.title = ticket.title
            foundTicket.description = ticket.description
            foundTicket.image = ticket.image
        }
        serialize()
    }

    override fun delete(ticket: TicketModel) {
        tickets.remove(ticket)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(tickets, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        tickets = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        tickets.forEach { Timber.i("$it") }
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

