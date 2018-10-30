# BattleRoyaleArgentumOnline
## Proyecto descontinuado :(
## Algunas Imágenes
![IMG1](https://i.imgur.com/xROUsFW.png)
![IMG2](https://i.imgur.com/4kCAmx8.png)
![IMG3](https://i.imgur.com/wbSfWC5.png)
![IMG4](https://i.imgur.com/F0DdoSB.png)

## GIFs
![](https://i.imgur.com/EBrBJ4z.gif)
https://i.imgur.com/EBrBJ4z.gif

## Características
El engine fue creado desde cero usando libGDX, y sus sub-librerías de box2D, box2Dlighting y FreeTypeFont (no tiene soporte para joysticks, no usamos la librería Controller)
La parte de networking también fue hecha desde cero utilizando [KryoNet](https://github.com/EsotericSoftware/kryonet) y conectando con MySQL mediante [JDBC Connector](https://dev.mysql.com/downloads/connector/j/5.1.html); el envío de paquetes probablemente puede ser más refinado, fue hecho sin experiencia previa y aprendiendo sobre la marcha.

## Bugs conocidos
• Al tirar los items no se muestra la luz correspondiente según la rareza.<br>
• A veces se bugea el Dialog de tirar pociones, y no muestra los textos.

## Setup
### Eclipse
Clonen el repo e importenlo como un *proyecto Gradle existente*. Antes de importarlo, van a tener que crear un archivo **local.properties**, que contenga un path al SDK de android.
Mi **local.properties** es así:

    # Location of the android SDK
    sdk.dir= C:/Users/manud/AppData/Local/Android/sdk

También, van a tener que montar en localhost una BD **"test"**, que tenga sólo una tabla: *users*. Esta tabla tiene 4 columnas, *ID* (primaria), *username* (varchar), *password* (varchar) y *default_body* (int), que puede valer **1** (cuerpo desnudo) o **2** (túnica de druida).
Creen un usuario dentro de la BD, y ya van a poder logear.
P.D: en honor a Argentum original, usamos los puertos 7666 (TCP) y 7667 (UDP), asegurense de que estén libres.


