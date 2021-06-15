# MyGrainChainRoute Challenge
##### Este proyecto tiene por objetivo realizar un seguimiento de rutas en tiempo real utilizando el SDK de Google Maps para posteriormente visualizar el listado de las mismas y el detalle de la *Distancia Recorrida, Mapa del Trayecto, Fecha y Tiempo de Recorrido* para poder compartir en diferentes plataformas.

# Modulos

![splash](https://user-images.githubusercontent.com/57393785/121937750-a0731700-cd10-11eb-837a-01d950731588.png)
* Splash Screen

![startroute](https://user-images.githubusercontent.com/57393785/121937647-846f7580-cd10-11eb-9f8d-90bab4bb46dd.png)
![start](https://user-images.githubusercontent.com/57393785/121939379-96eaae80-cd12-11eb-9f8b-23e6f35864bb.png)
![startroutepng](https://user-images.githubusercontent.com/57393785/122055372-2d1fe280-cdae-11eb-95ad-65345d844d7c.png)

* Navegar: El módulo de navegación solicitará al usuario que ingrese el nombre de la ruta que va a iniciar, posteriormente se habilitará el mapa con las opciones de navegación para que pueda iniciar su trayecto e ir haciendo un seguimiento en tiempo real. 

![list](https://user-images.githubusercontent.com/57393785/121961902-9d3a5400-cd2d-11eb-840c-cdd2d7953e7e.png)
![routespng](https://user-images.githubusercontent.com/57393785/122055573-622c3500-cdae-11eb-906c-fba7192ab551.png)

* Mis Rutas: Este módulo mostrará el listado de todas las rutas navegadas con una previsualización de la información de la misma.

![detail](https://user-images.githubusercontent.com/57393785/121961887-97447300-cd2d-11eb-9e6e-b7b801640725.png)
![cdmxpng](https://user-images.githubusercontent.com/57393785/122056122-eaaad580-cdae-11eb-8066-ee803113b587.png)
![peripng](https://user-images.githubusercontent.com/57393785/122056130-eda5c600-cdae-11eb-9ca9-279369dec6ef.png)


* Detalle de Rutas: Este módulo mostrará el Detalle completo de la ruta marcada con una visualización del *mapa del trayecto así como nombre de ruta, distancia y tiempo recorridos.*

![share](https://user-images.githubusercontent.com/57393785/121961855-901d6500-cd2d-11eb-9a22-8c4a77c5031e.png)
![sharing](https://user-images.githubusercontent.com/57393785/121961837-8c89de00-cd2d-11eb-9de7-2b7df1f5f094.png)


* Compartir Información de Ruta: Esta funcionalidad permitirá compartir la información más relevante del trayecto.

# Arquitectura
![arquitecure](https://user-images.githubusercontent.com/57393785/121942940-ac61d780-cd16-11eb-9e32-f6df7392a93a.PNG)

##### Se incorpora en el flujo de navegación el componente Navigation par un óptimo mapeo de flujos de la aplicación

## IU
* Activities: Los Activities que se encargan de la gestión de los procesos son *SplashActivity, MainActivity*
* Fragments: *Route fragment* se encarga de obtener el Nombre de la Ruta, *StartRouteFragment* gestiona la vista del mapeo en tiempo real del trayecto, *MyRoutesFragment* presenta el listado de todas las rutas recorridas, *MyRouteDetailFragment* muestra el detalle de la ruta seleccionada

## ViewModel
* RouteViewModel: Esta capa nos permitirá interactuar con la información requerida en la Interfaz de Usuario a través del Repositorio implementando una clase *Results* que adicionará al estatus de nuestra consulta un *OnSucces* o *onError* para un mayor detalle en los resultados de las consultas (Se puede optimizar aún más integrando una capa de Casos de Uso)
## Repository
* RouteRepository: Esta capa será la encargada de acceder al modelo de datos para proporcionar información a la aplicación a través de consultas a la Base de Datos con corutinas

## Model:
* Esta capa persiste los datos a través de *ROOM* y *SQLite* y su acceso mediante DAO's

## Utilities:
* Esta clase nos ayudará en configuraciones de Fecha, Conversiones de Unidades etc etc.

## Service:
* La clase *RouteTrackingService* se va a encargar de la gestión de la ruta en tiempo real utilizando los *markers, polylines y el location* para el seguimiento.

## Dependencias:

    def nav_version = "2.3.0"
    def room_version = "2.2.5"
    def lifecycle_version = "2.2.0"

    //Lifecycle
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    annotationProcessor "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"

    // Java language implementation
    implementation "androidx.navigation:navigation-fragment:$nav_version"
    implementation "androidx.navigation:navigation-ui:$nav_version"

    // Kotlin
    implementation "androidx.navigation:navigation-fragment-ktx:$nav_version"
    implementation "androidx.navigation:navigation-ui-ktx:$nav_version"

    implementation "androidx.viewpager2:viewpager2:1.0.0"
    implementation 'com.google.android.material:material:1.3.0-alpha01'

    //lifecycle - ViewModel
    implementation "androidx.lifecycle:lifecycle-extensions:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version"
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version"

    //ROOM
    implementation "androidx.room:room-runtime:$room_version"
    kapt "androidx.room:room-compiler:$room_version"
    implementation "androidx.room:room-ktx:$room_version"
    testImplementation "androidx.room:room-testing:$room_version"

    //Google Maps
    implementation 'com.google.android.gms:play-services-maps:17.0.1'
    implementation 'com.google.android.gms:play-services-location:18.0.0'

# Extras

![notification](https://user-images.githubusercontent.com/57393785/121959381-5303a380-cd2a-11eb-847d-cd890f02e5e2.png)


