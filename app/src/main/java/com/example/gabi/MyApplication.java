package com.example.gabi;
/*
La clase MyApplication se utiliza para inicializar configuraciones globales que deben
estar listas tan pronto como se inicie la aplicación.
En este caso, necesitamos inicializar Firebase al inicio de la aplicación
para asegurarnos de que esté disponible en todas las actividades y fragmentos que
necesiten interactuar con Firebase. Al extender Application y sobrescribir el método
onCreate, podemos inicializar Firebase una sola vez y mantener esa instancia
a lo largo del ciclo de vida de la aplicación.
 */
import android.app.Application;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
