# NearbyForsquare
An offline-first application wich uses foursquare places api to fetch nearby places around you based on your current lat/long. the list will only updates itself when you move at least 100 meters.

## Usage
In order to make this application able to retrieve responses from foursquare api you need to generate a access token, create a text file named `keystore.properties` like below.

```
ACCESS_TOKEN = "your api access token"
```
paste your access token as the value of `ACCESS_TOKEN` key.

then copy this file into the root folder of this project. the [`AuthHeaderInterceptor`](https://gitlab.com/BlueMeth/nearbyfoursquare/-/blob/master/app/src/main/java/com/example/nearbyfoursquare/data/remote/interceptor/AuthHeaderInterceptor.kt) will read the access token value from generated BuildConfig file.

```
class AuthorizationHeaderInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
        request.addHeader("Authorization", BuildConfig.ACCESS_TOKEN")
        return chain.proceed(request.build())
    }
}
```

## Features & Libraries
* Kotlin
* MVVM
* Repository
* NetworkBoundResource (with Flow)
* Hilt
* Jetpack Compose
* Room
* Offline-First / Local Caching
* Flow
* Navigation
* Coil
