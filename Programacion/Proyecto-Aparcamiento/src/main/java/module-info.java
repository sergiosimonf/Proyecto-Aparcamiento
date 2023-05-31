module dev.sergiosf.proyectoaparcamiento {
    requires javafx.controls;
    requires javafx.fxml;
    requires kotlin.stdlib;
    requires koin.core.jvm;
    requires io.github.microutils.kotlinlogging;


    // Logger
//    requires io.github.microutils.kotlinlogging;
    requires koin.logger.slf4j;
    requires org.slf4j;

    // Gson
    requires com.google.gson;

    // Result
    requires kotlin.result.jvm;
    requires java.sql;

    // SqlDeLight
//    requires runtime.jvm;
//    requires sqlite.driver;

//    // Como no pongas esto te vas a volver loco con los errores
//    requires java.sql;

    // Koin
//    requires koin.core.jvm;

    // Abrimos y exponemos el paquete a JavaFX
    opens dev.sergiosf.proyectoaparcamiento to javafx.fxml;
    exports dev.sergiosf.proyectoaparcamiento;

    // Controladores
    opens dev.sergiosf.proyectoaparcamiento.controllers to javafx.fxml;
    exports dev.sergiosf.proyectoaparcamiento.controllers;

    // Rutas
    opens dev.sergiosf.proyectoaparcamiento.routes to javafx.fxml;
    exports dev.sergiosf.proyectoaparcamiento.routes;

    // Modelos a javafx para poder usarlos en las vistas
    opens dev.sergiosf.proyectoaparcamiento.models to javafx.fxml;
    exports dev.sergiosf.proyectoaparcamiento.models;
}