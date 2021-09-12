package reifynotion

import org.jraf.klibnotion.client.Authentication
import org.jraf.klibnotion.client.ClientConfiguration
import org.jraf.klibnotion.client.HttpConfiguration
import org.jraf.klibnotion.client.HttpLoggingLevel
import org.jraf.klibnotion.client.NotionClient
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList
import org.jraf.klibnotion.model.block.MutableBlockList

open class NotionClient(private val token: String) {

    internal val instance by lazy {
        val http = HttpConfiguration(loggingLevel = HttpLoggingLevel.INFO)
        val config = ClientConfiguration(Authentication(token), http)
        NotionClient.newInstance(config)
    }

    suspend fun getDatabase(id: String) = instance.databases.getDatabase(id)

    suspend fun createDatabase(parentId: String, title: RichTextList, props: PropertySpecList) =
            instance.databases.createDatabase(parentId, title, props)

    suspend fun queryDatabase(id: String, query: DatabaseQuery, sort: PropertySort) =
            instance.databases.queryDatabase(id, query, sort).results

    suspend fun updatePage(id: String, properties: PropertyValueList) =
            instance.pages.updatePage(id, properties)

    suspend fun addContentToPage(id: String, block: MutableBlockList.() -> Unit) {
        instance.blocks.appendBlockList(id, block) 
    }
}