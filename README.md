
# Proyecto Sistemas Distribuidos 2024-2025

## Navegación

### Capturas de Pantalla
<!-- Añade aquí las capturas de pantalla de las páginas principales de la aplicación -->
1. **Página Principal**
   ![Página Principal](ruta/a/la/captura1.png)

2. **Lista de Usuarios**
   ![Lista de Usuarios](ruta/a/la/captura2.png)

3. **Añadir Usuario**
   ![Añadir Usuario](ruta/a/la/captura3.png)

### Diagrama de Navegación
<!-- Añade aquí el diagrama de navegación de la aplicación -->
![Diagrama de Navegación](ruta/a/diagrama-navegacion.png)

## Instrucciones de Ejecución

### Requisitos
- Java 17
- MySQL 8.0.32
- Maven 3.8.1

### Pasos para Descargar, Construir y Ejecutar la Aplicación

1. **Clonar el repositorio**:
   ```sh
   git clone https://github.com/tu-usuario/tu-repositorio.git
   cd tu-repositorio
   ```

2. **Configurar la base de datos MySQL**:
    - Crear una base de datos en MySQL:
      ```sql
      CREATE DATABASE your_database_name;
      ```
    - Actualizar las credenciales de la base de datos en `src/main/resources/application.properties`:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      ```

3. **Construir la aplicación**:
   ```sh
   mvn clean install
   ```

4. **Ejecutar la aplicación**:
   ```sh
   mvn spring-boot:run
   ```

## Diagrama con las Entidades de la Base de Datos

### Diagrama Entidad-Relación
<!-- Añade aquí el diagrama entidad-relación de la base de datos -->
![Diagrama Entidad-Relación](ruta/a/diagrama-er.png)

### Diagrama UML de las Clases Java
<!-- Añade aquí el diagrama UML de las clases Java -->
![Diagrama UML](ruta/a/diagrama-uml.png)

## Diagrama de Clases y Templates

### Diagrama de Clases
<!-- Añade aquí el diagrama de clases de la aplicación -->
![Diagrama de Clases](ruta/a/diagrama-clases.png)

### Relación de Templates con @Controller
<!-- Añade aquí la relación de los templates con los controladores -->
![Relación de Templates](ruta/a/relacion-templates.png)
