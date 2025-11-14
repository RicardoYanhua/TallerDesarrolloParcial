package com.yanhua.ms.producto.constans;

public class ApiRoutes { 

// Definición de rutas base para las APIs de transacciones
   public static class Producto {
        public static final String BASE = "/api/productos";

        public static final String ID_PRODUCTO = "/{idProducto}";

        public static final String LISTAR = "/listar";
        public static final String CREAR = "/crear";

        public static final String UPDATE = "/update/{idProducto}";
        public static final String DELETE = "/delete/{idProducto}";
    } 
}