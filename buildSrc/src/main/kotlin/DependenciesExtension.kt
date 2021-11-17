import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.headoutInternal(module: String, version: String): String {
    return "${GroupIds.headoutInternal}:headout-$module:$version"
}
