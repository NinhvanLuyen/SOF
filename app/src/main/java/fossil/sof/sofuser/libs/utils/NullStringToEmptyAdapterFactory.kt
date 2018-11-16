package fossil.sof.sofuser.libs.utils

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import com.google.gson.internal.`$Gson$Types`.getRawType
import com.google.gson.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.TypeAdapterFactory




/**
 * Created by ninhvanluyen on 2/7/18.
 */

class NullStringToEmptyAdapterFactory() : TypeAdapterFactory {
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {

        val rawType = type.rawType as Class<T>
        return if (rawType != String::class.java) {
            null
        } else StringAdapter() as TypeAdapter<T>
    }
}
internal class StringAdapter : TypeAdapter<String>() {
    @Throws(IOException::class)
    override fun read(reader: JsonReader): String {
        if (reader.peek() === JsonToken.NULL) {
            reader.nextNull()
            return ""
        }
        return reader.nextString()
    }

    @Throws(IOException::class)
    override fun write(writer: JsonWriter, value: String?) {
        if (value == null) {
            writer.nullValue()
            return
        }
        writer.value(value)
    }
}