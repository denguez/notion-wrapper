package reifynotion

import kotlinx.coroutines.runBlocking
import org.jraf.klibnotion.model.block.MutableBlockList
import org.jraf.klibnotion.model.database.query.DatabaseQuery
import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.property.sort.PropertySort
import org.jraf.klibnotion.model.property.spec.PropertySpecList
import org.jraf.klibnotion.model.property.value.PropertyValueList
import org.jraf.klibnotion.model.richtext.RichTextList
import reifydb.DAO
import reifydb.Entity
import reifydb.EntityID
import reifydb.Table

object DatabaseTable : Table() {
    val name = string("database_name")
    val databaseId = string("database_id")
    val parentId = string("database_parent_id")
}

class DatabaseEntity(id: EntityID) : Entity(id) {
    var name by DatabaseTable.name
    var databaseId by DatabaseTable.databaseId
    var parentId by DatabaseTable.parentId

    companion object : DAO<DatabaseEntity>(DatabaseTable) {
        suspend fun finByName(name: String): DatabaseEntity? {
            return readOne { DatabaseTable.name eq name }
        }
    }
}

class DatabaseConfig {
    var name = ""
    var parentId = ""
    internal val propList = PropertySpecList()

    fun properties(block: PropertySpecList.() -> Unit) {
        propList.apply(block)
    }
}

open class NotionDatabase(
        protected val client: NotionClient,
        databaseBuilder: DatabaseConfig.() -> Unit
) {
    val id: String
    init {
        val config = DatabaseConfig().apply(databaseBuilder)
        id = runBlocking {
            val db = DatabaseEntity.finByName(config.name)
            if (db == null) {
                val notiondb = client.createDatabase(
                    parentId = config.parentId,
                    title = RichTextList().text(config.name),
                    props = config.propList
                )
                DatabaseEntity.create {
                    name = config.name
                    parentId = config.parentId
                    databaseId = notiondb.id
                }
                notiondb.id
            } else {
                db.databaseId
            }
        }
    }

    suspend fun query(
            sort: PropertySort.() -> Unit = {},
            query: DatabaseQuery.() -> Unit,
    ): List<Page> {
        return client.queryDatabase(
            id = this.id,
            query = DatabaseQuery().apply(query),
            sort = PropertySort().apply(sort)
        )
    }

    suspend fun updatePage(id: String, properties: PropertyValueList.() -> Unit): Page {
        return client.updatePage(id, PropertyValueList().apply(properties))
    }

    suspend fun addContentToPage(id: String, block: MutableBlockList.() -> Unit) {
        return client.addContentToPage(id, block)
    }
}