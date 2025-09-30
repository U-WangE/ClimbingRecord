/**
 * Precompiled [climbingrecord.android.application.gradle.kts][Climbingrecord_android_application_gradle] script plugin.
 *
 * @see Climbingrecord_android_application_gradle
 */
public
class Climbingrecord_android_applicationPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Climbingrecord_android_application_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
