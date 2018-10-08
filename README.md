# MPP Chuck Norris

Chuck Norris can make any phone run the same app but there is only one Chuck.  For the rest of us there's Kotlin. Through the usage of several Kotlin projects it is possible to share a large percentage of code between iOS and Android apps. This is a demo app that utilizes [Kotlin Multiplatform](https://kotlinlang.org/docs/reference/multiplatform.html), [Kotlin Serialization](https://github.com/Kotlin/kotlinx.serialization), [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) and [KTor-Client](https://ktor.io/clients/index.html) to access a backend [api](http://www.icndb.com/api/) and display the results.

![Side by side](https://raw.githubusercontent.com/samus/mpp_chuck_demo/master/screens/sidebyside.png)


## Building the code

 * Make sure the Android SDK is installed
 * In IntelliJ or Android Studio set **Kotlin Updates** to use the `Early Access Preview 1.3` update channel.
 * Open the project in IntelliJ IDEA (2018.2 recommended)
 * Create a file `local.properties` in the root directory of the project, pointing to your Android SDK installation. On Mac OS,
the contents should be `sdk.dir=/Users/<your username>/Library/Android/sdk`. On other OSes, please adjust accordingly.
 * Run `./gradlew build`
 
 ### Building the iOS App

The workspace is setup with an empty framework that contains a `Run Script` phase that executes a gradle task to build and copy the `app` framework to the build directory.  By default it is setup to create a build for running on the simulator.  To build for a device switch out the target preset for iOS in `app/build.gradle` from `presets.iosX64` to `presets.iosArm64`

* Xcode 10.0 or later will work.
* Open the chuck.xcworkspace workspace in Xcode.
* It may be necessary to change the bundle id `com.synappticlabs.mppchuck` to something unique.
* Set the team to a valid team.
* Select Product|Build from the menu.
