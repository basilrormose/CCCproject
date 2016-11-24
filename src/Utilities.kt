/**
 * Created by rbasil on 20/11/2016.
 */

package com.rormose.capstone

import java.io.File


fun createDirKeyPrefix(item: File): String {
    return item.absolutePath
}
