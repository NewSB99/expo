package expo.modules.font

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import androidx.core.content.res.ResourcesCompat
import expo.modules.core.errors.InvalidArgumentException
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TypefaceResourceTest {

  @MockK
  lateinit var mockContext: Context

  @MockK
  lateinit var mockResources: Resources

  private val validResourcePath = "file:///android_res/raw/test.ttf"

  @Before
  fun setUp() {
    MockKAnnotations.init(this)
    every { mockContext.packageName } returns "com.example"
    every { mockContext.resources } returns mockResources

    mockkStatic(ResourcesCompat::class)
  }

  @After
  fun tearDown() {
    unmockkAll()
  }

  @Test(expected = InvalidArgumentException::class)
  fun `should throw exception when resource path has invalid prefix`() {
    createTypefaceFromResource(mockContext, "file:///invalid/test.ttf")
  }

  @Test(expected = InvalidArgumentException::class)
  fun `should throw exception when resource path has insufficient segments`() {
    createTypefaceFromResource(mockContext, "file:///invalid/test.ttf")
  }

  @Test
  fun `should return null when resource identifier is not found`() {
    every { mockResources.getIdentifier("test", "raw", "com.example") } returns 0

    val result = createTypefaceFromResource(mockContext, validResourcePath)
    assertNull("Expected null when the resource is not found", result)
  }

  @Test
  fun `should return a Typeface when valid resource exists`() {
    every { mockResources.getIdentifier("test", "raw", "com.example") } returns 12345

    val dummyTypeface = Typeface.DEFAULT
    every { ResourcesCompat.getFont(mockContext, 12345) } returns dummyTypeface

    val result = createTypefaceFromResource(mockContext, validResourcePath)
    assertNotNull("Expected non-null Typeface when resource exists", result)
    assertEquals("Expected the dummy Typeface instance", dummyTypeface, result)
  }
}
