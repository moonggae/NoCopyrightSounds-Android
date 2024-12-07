package com.ccc.ncs

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withGroovyBuilder

internal fun Project.configureGenerateSymbols(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    tasks.register("generateSymbolsZip") {
        doLast {
            // DS_Store 파일 삭제
            delete(fileTree("build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/").matching {
                include("**/.DS_Store")
            })

            // __MACOSX 디렉토리 삭제
            delete(fileTree("build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/").matching {
                include("**/__MACOSX")
            })

            // symbols.zip 생성
            ant.withGroovyBuilder {
                "zip"("destfile" to "../symbols.zip",
                    "basedir" to "build/intermediates/merged_native_libs/release/mergeReleaseNativeLibs/out/lib/")
            }
        }
    }

    tasks.whenTaskAdded {
        if (name == "bundleRelease") {
            finalizedBy("generateSymbolsZip")
        }
    }
}