package quo.vadis.readio.presentation

internal fun routeWithoutArgs(route: String?): String {
    if (route == null) return ""
    return route.substringBefore('/')
}

