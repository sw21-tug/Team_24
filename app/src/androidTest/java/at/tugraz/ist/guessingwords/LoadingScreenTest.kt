package at.tugraz.ist.guessingwords


import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class LoadingScreenTest {
    @Test
    fun testLoadingScreen() {
        ActivityScenario.launch(SplashScreenActivity::class.java)
        onView(withId(R.id.text_loading)).check(matches(withText("Loading …")))
    }
}