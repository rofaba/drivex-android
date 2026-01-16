# DriveX: Aplicación Móvil Android

Una aplicación móvil nativa desarrollada para la gestión integral de alquiler y compraventa de vehículos. El objetivo principal es ofrecer una experiencia de usuario optimizada en la navegación del catálogo y la administración de la cuenta personal del usuario.

## 🛠️ Pila Tecnológica

El proyecto está desarrollado utilizando las siguientes tecnologías:

*   **Lenguaje de Programación:** Kotlin (100%)
*   **Plataforma:** Android Nativo
*   **Gestión de Dependencias:** Gradle (con archivos `.kts` para configuración)

## ✨ Funcionalidades Implementadas

Las siguientes características de la aplicación se encuentran finalizadas o en su fase final de desarrollo:

### 1. Gestión de Autenticación y Perfil

*   **Registro de Usuarios (HU-A01):** Permite a usuarios particulares o profesionales crear una cuenta.
*   **Inicio de Sesión (HU-A02):** Autenticación mediante credenciales o a través de proveedores externos (como Google).
*   **Cierre de Sesión (HU-A03):** Finalización segura de la sesión.
*   **Gestión de Perfil (HU-A05):** Capacidad para que los usuarios actualicen sus datos personales (nombre, teléfono, dirección).
*   **Roles de Usuario (HU-A06):** Implementación de niveles de acceso (comprador, vendedor).

### 2. Catálogo y Búsqueda de Vehículos

*   **Visualización del Catálogo (HU-B01):** Muestra el listado completo de vehículos disponibles.
*   **Filtros Avanzados (HU-B02/B03):** Permite filtrar los resultados por criterios como marca, modelo, año, rango de precio, estado y disponibilidad.
*   **Búsqueda por Palabras Clave (HU-B04):** Funcionalidad de búsqueda por texto.
*   **Ordenamiento y Favoritos (HU-B05):** Los usuarios pueden ordenar los resultados y marcar vehículos como favoritos.

## 🚧 Backlog y Desarrollo Futuro

Las siguientes funcionalidades están planificadas para ser implementadas en iteraciones futuras:

*   **Mejoras UX/UI de Alquiler (HU-B06):** Optimización de la experiencia de usuario en el flujo de alquiler de vehículos.
*   **Gestión de Archivos y Historial (HU-B07):**
    *   Manejo de archivos de procesos de compra/alquiler.
    *   Historial de transacciones de usuario.
    *   Sistema de reseñas y calificaciones.
*   **Recuperación de Contraseña (HU-A04):** Implementación de un sistema de restablecimiento mediante correo electrónico seguro.

## 🚀 Empezar

Para obtener una copia local en funcionamiento, sigue estos sencillos pasos.

### Prerrequisitos

Necesitas tener instalado:

*   [Android Studio](https://developer.android.com/studio)
*   [SDK de Android](https://developer.android.com/studio/install#components)

### Instalación

1.  Clona el repositorio:
    ```bash
    git clone [https://github.com/rofaba/drivex-android.git](https://github.com/rofaba/drivex-android.git)
    ```
2.  Abre el proyecto en Android Studio.
3.  Sincroniza el proyecto con Gradle.

### Ejecución

1.  Abre el proyecto en Android Studio.
2.  Selecciona un emulador o dispositivo físico.
3.  Ejecuta la aplicación presionando el botón **Run** (▶).
