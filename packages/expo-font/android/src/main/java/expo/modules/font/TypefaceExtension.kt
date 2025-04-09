// Copyright 2015-present 650 Industries. All rights reserved.

package expo.modules.font

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import androidx.core.net.toUri
import expo.modules.core.errors.InvalidArgumentException

internal const val ANDROID_EMBEDDED_URL_BASE_RESOURCE = "file:///android_res/"

@SuppressLint("DiscouragedApi")
internal fun createTypefaceFromResource(context: Context, resourceFilePath: String): Typeface? {
  if (!resourceFilePath.startsWith(ANDROID_EMBEDDED_URL_BASE_RESOURCE)) {
    throw InvalidArgumentException("Invalid resource file path: $resourceFilePath")
  }
  val uri = resourceFilePath.toUri()
  val pathSegments = uri.pathSegments
  if (pathSegments.size < 3) {
    throw InvalidArgumentException("Invalid resource file path: $resourceFilePath")
  }
  val resourceDirectory = pathSegments[1]
  val resourceFilename = pathSegments[2]
  val resourceName = resourceFilename.substringBeforeLast('.', resourceFilename)
  val resourceId = context.resources.getIdentifier(
    resourceName,
    resourceDirectory,
    context.packageName
  )
  if (resourceId == 0) {
    return null
  }
  return ResourcesCompat.getFont(context, resourceId)
}
