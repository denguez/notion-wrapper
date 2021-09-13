package reifynotion

import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.property.value.PropertyValue
import org.jraf.klibnotion.model.property.SelectOption
import org.jraf.klibnotion.model.richtext.RichTextList

fun Page.property(name: String): PropertyValue<*>? {
    return propertyValues.find { it.name == name }
}

fun Page.url(name: String) = string(name)
fun Page.email(name: String) = string(name)
fun Page.string(name: String): String? {
    return property(name)?.run { value as String }
}

fun Page.number(name: String): Double? {
    return property(name)?.run { value as Double }
}

fun Page.checkbox(name: String): Boolean? {
    return property(name)?.run { value as Boolean }
}

fun Page.text(name: String): RichTextList? {
    return property(name)?.run { value as RichTextList }
}

fun Page.date(name: String): DateOrDateRange? {
    return property(name)?.run { value as DateOrDateRange }
}

fun Page.select(name: String): SelectOption? {
    return property(name)?.run { value as SelectOption }
}

fun Page.multiselect(name: String): List<SelectOption>? {
    return property(name)?.run { value as List<SelectOption> }
}

fun Page.relation(name: String): List<String>? {
    return property(name)?.run { value as List<String> }
}