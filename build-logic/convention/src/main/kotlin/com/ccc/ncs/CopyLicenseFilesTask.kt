package com.ccc.ncs

import org.gradle.api.Project


internal fun Project.configureCopyLicenseFilesTask() {
    tasks.register("copyLicenseFiles") {
        doLast {
            copy {
                from("${layout.buildDirectory.get()}/generated/third_party_licenses/release/res/raw")
                into("${layout.buildDirectory.get()}/generated/res/resValues/release/raw")
                include("*")
            }
        }
    }

    tasks.whenTaskAdded {
        if (name == "releaseOssLicensesTask") {
            finalizedBy("copyLicenseFiles")
        }
        if (name == "mergeReleaseResources") {
            dependsOn("copyLicenseFiles")
            mustRunAfter("copyLicenseFiles")
        }
    }
}