# 🗺️ MetaMapa - Backend & Microservicios

**Sistema distribuido para la recopilación, normalización y agregación de hechos geolocalizados.**

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)

> 🔗 **Nota Front-end:** Este proyecto fue desarrollado como una aplicación Full-Stack. Este repositorio contiene exclusivamente la arquitectura Backend y las APIs. Para ver la interfaz de usuario interactiva, visitá el [Repositorio del Frontend](https://github.com/matiastort01/metamapa-frontend).

## 📌 Sobre el Proyecto

MetaMapa es un sistema distribuido diseñado para publicar y gestionar hechos/eventos vinculados a coordenadas geográficas específicas. El objetivo principal del backend es centralizar, normalizar y exponer estos eventos complejos provenientes de orígenes heterogéneos, garantizando la integridad de la información en tiempo real.

Se implementó una arquitectura orientada a microservicios para separar la lógica de agregación de datos de la interfaz de usuario, consumiendo APIs externas y servicios internos mediante comunicación asíncrona y reactiva.

## 🏗️ Módulos Principales

El desarrollo se estructuró separando responsabilidades en servicios independientes:

* **Servicio Agregador:** El núcleo del back-end. Centraliza los hechos provenientes de todas las fuentes, aplica filtros de lógica de negocio, evalúa redundancias y expone una API REST unificada.
* **Fuente Estática:** Encargada de la extracción y procesamiento de datos desde orígenes persistentes (datasets en archivos CSV) para alimentar el sistema con información histórica.
* **Fuente Dinámica:** Responsable de la ingesta de hechos en tiempo real mediante aportes de los usuarios.
* **Fuente Proxy:** Componente intermediario que estandariza la comunicación con servicios de terceros, gestionando la transformación de datos externos.
* **Servicio de Estadísticas:** Módulo especializado en el procesamiento analítico de los hechos recolectados para generar métricas y reportes sobre la actividad del sistema.
* **Gestión de Usuarios:** Administra la seguridad de la plataforma, manejando la autenticación, autorización y perfiles, asegurando accesos protegidos.

## ⚙️ Habilidades y Características Técnicas

* **Comunicación Asíncrona:** Implementación de peticiones no bloqueantes utilizando `WebClient` para la comunicación entre microservicios.
* **Persistencia de Datos:** Gestión de base de datos relacional con MySQL, modelado de entidades complejas y optimización de consultas gracias a Hibernate.
* **Cloud & DevOps:** Despliegue de la infraestructura utilizando servicios de nube de AWS.
* **Arquitectura y Calidad:** Aplicación de estándares de calidad ISO 25000 para asegurar un código mantenible, estructurado y gestionado con Maven.
* 
## 🚀 Instalación Local

1. Clonar el repositorio:
   ```bash
   git clone [https://github.com/TU_USUARIO/metamapa-backend.git](https://github.com/TU_USUARIO/metamapa-backend.git)
   ```
   
2. Configurar las credenciales de la base de datos MySQL en application.properties.

3. Compilar el proyecto con Maven:
   ```bash
   mvn clean install
   ```

4. Iniciar el servidor embebido:
   ```bash
   mvn spring-boot:run
   ```
