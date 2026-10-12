plugins {
	alias(libs.plugins.stonecutter)
	alias(libs.plugins.mod.publish.plugin)
	alias(libs.plugins.loom.back.compat).apply(false)
	alias(libs.plugins.neoforged.moddev).apply(false)
	alias(libs.plugins.jsonlang.postprocess).apply(false)
	alias(libs.plugins.kotlin.jvm).apply(false)
	alias(libs.plugins.devtools.ksp).apply(false)
	alias(libs.plugins.fletching.table).apply(false)
	alias(libs.plugins.legacyforge.moddev).apply(false)
	id("mod-root")
}

stonecutter active file(".sc_active_version")

stonecutter parameters {
	constants.match(current.project.substringAfterLast('-'), "fabric", "neoforge", "forge")
	swaps["mod_version"] = "\"${properties.get<String>("mod.version")}\";"
	swaps["mod_id"] = "\"${properties.get<String>("mod.id")}\";"
	swaps["mod_name"] = "\"${properties.get<String>("mod.name")}\";"
	swaps["mod_group"] = "\"${properties.get<String>("mod.group")}\";"
	swaps["minecraft"] = "\"${current.version}\";"
	constants["release"] = properties.get<String>("mod.id") != "modtemplate"
}

tasks.register<Copy>("collectDebugJars") {
	group = "build"
	description = "Collects all compiled mod jars into debug-jars/ for local testing without publishing"
	from("versions") {
		include("*/build/libs/*.jar")
		exclude { it.file.name.contains("sources") || it.file.name.contains("javadoc") }
		eachFile {
			path = name
		}
	}
	into(layout.projectDirectory.dir("debug-jars"))
	includeEmptyDirs = false
	duplicatesStrategy = DuplicatesStrategy.INCLUDE
	doLast {
		logger.lifecycle("==================================================")
		logger.lifecycle("All testing jars ready in: debug-jars/")
		logger.lifecycle("You can copy these into your .minecraft/mods folder.")
		logger.lifecycle("==================================================")
	}
}

tasks.register("debugBuild") {
	group = "build"
	description = "Compiles all target versions and collects jars into debug-jars/ for local testing"
	dependsOn(stonecutter.tasks.named("jar"))
	finalizedBy("collectDebugJars")
}


