package com.ccc.ncs.ui.model

import com.ccc.ncs.model.Music

val Music.downloadUrl: String
    get() = "https://ncs.io/track/download/$id"