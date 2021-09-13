package reifynotion

import org.jraf.klibnotion.model.page.Page
import org.jraf.klibnotion.model.date.DateOrDateRange
import org.jraf.klibnotion.model.property.value.PropertyValue

fun Page.property(name: String): PropertyValue<*>? {
    return propertyValues.find { it.name == name }
}

fun Page.date(name: String): DateOrDateRange? {
    return property(name)?.run { value as DateOrDateRange }
}