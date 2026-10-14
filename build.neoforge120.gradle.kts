plugins {
	id("mod-platform")
	id("net.neoforged.moddev.legacyforge")
}

stonecutter {
	replacements.string(current.parsed >= "1.21.11") {
		replace("ResourceLocation", "Identifier")
		replace("location()", "identifier()")
	}
}

platform {
	loader = "neoforge"
	dependencies {
		required("minecraft") {
			forgeLikeVersionRange = prop("deps.minecraft")
		}
		required("neoforge") {
			forgeLikeVersionRange.set("[${prop("deps.neoforge")},)")
		}
		optional("jei") { slug("jei") }
		optional("fairexperience") { slug("fair-experience", "fair-experience") }
		optional("curios") { slug("curios") }
		optional("apotheosis") { slug("apotheosis") }
		optional("quality_equipment") { slug("quality-equipment", "quality-equipment") }
		optional("reskillable") { slug("reskillable-reimagined", "reskillable-reimagined") }
	}
}

legacyForge {
	enable {
		neoForgeVersion = "1.20.1-${prop("deps.neoforge")}"
	}

	validateAccessTransformers = true

	accessTransformers.from(
		rootProject.file("src/main/resources/aw/${sc.current.version}.cfg")
	)

	if (hasProperty("deps.parchment")) parchment {
		val (mc, ver) = prop("deps.parchment").split(':')
		mappingsVersion = ver
		minecraftVersion = mc
	}

	val devJvmArgs = propsList("mod", "dev_jvm_args") +
		propsList("mod", "dev_jvm_args_mixin_debug")
	runs {
		register("client") {
			client()
			gameDirectory = file("run/")
			ideName = "NeoForge Client (${sc.current.version})"
			programArgument("--username=Dev")
			jvmArguments.addAll(devJvmArgs)
		}
		register("server") {
			server()
			gameDirectory = file("run/")
			ideName = "NeoForge Server (${sc.current.version})"
			jvmArguments.addAll(devJvmArgs)
		}
	}

	mods {
		register(prop("mod.id")) {
			sourceSet(sourceSets["main"])
		}
	}
}

mixin {
	add(sourceSets.main.get(), "${prop("mod.id")}.mixins.refmap.json")
	config("${prop("mod.id")}.mixins.json")
}

repositories {
	mavenCentral()
	maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
	maven("https://maven.neoforged.net/releases/") { name = "NeoForged" }
	maven("https://maven.blamejared.com/") { name = "BlameJared" }
	strictMaven("https://api.modrinth.com/maven", "maven.modrinth") { name = "Modrinth" }
}

dependencies {
	annotationProcessor("org.spongepowered:mixin:${libs.versions.mixin.get()}:processor")
	compileOnly(libs.mixinextras.common)
	annotationProcessor(libs.mixinextras.common)
	compileOnly("mezz.jei:jei-${sc.current.version}-forge:15.62.0.219")
}

sourceSets {
	main {
		resources.srcDir(
			"${rootDir}/versions/datagen/${sc.current.version}/src/main/generated"
		)
	}
}

tasks.named("createMinecraftArtifacts") {
	dependsOn(tasks.named("stonecutterGenerate"))
}
