
# Proyecto Sistemas Distribuidos 2024-2025

## Integrantes

| Nombre                     | Email                              | GitHub                                     |
|----------------------------|------------------------------------|--------------------------------------------|
| Javier Castellanos Serrano | j.castellanos.2018@alumnos.urjc.es | [GitHub1](https://github.com/Javicastes)   |
| María Martín Mullor        | m.martin.2021@alumnos.urjc.es      | [GitHub2](https://github.com/maria-marmul) |
| Verónica Ramírez Marín     | v.ramirez.2021@alumnos.urjc.es     | [GitHub3](https://github.com/VeroRam1)     |
| Wenhao Zhang               | w.zhang.2022@alumnos.urjc.es       | [GitHub4](https://github.com/wenhiii)      |


## Navegación

### Capturas de Pantalla

1. **Página Principal**
- *Pantalla de presentación desde donde puede comenzar a navegar el usuario, haciendo click en las imágenes para ver los productos, pulsando en el carrito o en el desplegable*
   ![Página Principal](src/main/resources/static/mainpage.jpeg)

2. **Administración de Usuarios**
- *Desde aquí podemos añadir o eliminar usuarios, y ver los usuarios existentes*
   ![Administración de Usuarios](src/main/resources/static/manageusers.jpeg)

3. **Lista de Usuarios**
- *Página para ver la lista de usuarios, donde podremos eliminar a algún usuario de la lista o pulsar el botón de Añadir usuarios  para añadir uno nuevo*
   ![Lista de Usuarios](src/main/resources/static/userslist.jpeg)

4. **Administración de Productos**
- *Aquí podemos añadir productos nuevos*
   ![Administración de Productos](src/main/resources/static/manageproducts.jpeg)

5. **Productos disponibles**
- *En esta página podemos ver los productos disponibles, que aparecen con una imagen, el nombre y el precio. Podemos añadirlos al carrito*
   ![Productos_Disponibles](src/main/resources/static/availableproducts.jpeg)

6. **Lista de Productos**
- *Página para ver la lista de productos de nuestra web, desde donde podremos pulsar otras opciones si nos interesa*
   ![Lista_de_Productos](src/main/resources/static/productslist.jpeg)

7. **Editar Productos**
- *Desde aquí podemos editar cualquier producto que queramos: su nombre, el precio, la imagen y la categoría a la que pertenecen*
   ![Editar_Productos](src/main/resources/static/editproducts.jpeg)

8. **Ver Productos en una Categoría**
- *Podemos ver los productos de cada categoría*
   ![Ver_Productos_en_una_Categoria](src/main/resources/static/productsincategory.jpeg)

9. **Administración de Categorías**
- *Página para añadir categorías nuevas, desde donde también podemos ver las categorías existentes*
   ![Administración de Categorias](src/main/resources/static/managecategories.jpeg)

10. **Lista de Categorías**
   ![Lista_de_Categorías](src/main/resources/static/categorieslist.jpeg)

11. **Pedido**
- *En esta página podemos ver el estado de nuestro pedido*
   ![Pedido](src/main/resources/static/orderspage1.jpeg)

12. **Lista de Pedidos**
   ![Lista_de_Pedidos](src/main/resources/static/orderslist.jpeg)

13. **Carrito**
- *Página del carrito de la compra, desde donde podremos finalizar la compra o seguir comprando*
   ![Carrito](src/main/resources/static/shoppingcart.jpeg)

13. **Página de Error**
    ![Página_de_Error](src/main/resources/static/errorpage.jpeg)

### Diagrama de Navegación

![Diagrama de Navegación](ruta/a/diagrama-navegacion.png)

## Instrucciones de Ejecución

### Requisitos
- Java 17
- MySQL 8.0.32
- Maven 3.8.1

### Pasos para Descargar, Construir y Ejecutar la Aplicación

1. **Clonar el repositorio**:
   ```sh
   https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10.git
   cd cd practica-sistemas-distribuidos-2025-grupo-10
   ```

2. **Configurar la base de datos MySQL**:
    - Crear una base de datos en MySQL:
      ```sql
      CREATE DATABASE shop;
      ```
    - Actualizar las credenciales de la base de datos en `src/main/resources/application.properties`:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/shop
      spring.datasource.username=root
      spring.datasource.password=88888888
      ```

3. **Construir la aplicación**:
   ```sh
   mvn clean install
   ```

4. **Ejecutar la aplicación**:
   ```sh
   mvn spring-boot:run
   ```

## Diagrama UML de las Clases Java

![Diagrama UML](src/main/resources/static/Diagrama_UML_de_clases.png)

## Diagrama de Clases y Templates

![Diagrama de Clases](src/main/resources/static/Diagrama_de_clases_y_templates.png)


### Estructura del Proyecto

#### Controladores
- **UserController**: Maneja las solicitudes relacionadas con los usuarios.
- **CategoryController**: Maneja las solicitudes relacionadas con las categorías.
- **OrderController**: Maneja las solicitudes relacionadas con los pedidos.
- **ProductController**: Maneja las solicitudes relacionadas con los productos.

#### Servicios
- **UserService**: Proporciona operaciones CRUD para los usuarios.
- **CategoryService**: Proporciona operaciones CRUD para las categorías.
- **OrderService**: Proporciona operaciones CRUD para los pedidos.
- **ProductService**: Proporciona operaciones CRUD para los productos.
- **SampleDataService**: Inicializa datos de muestra en la base de datos.

#### Repositorios
- **UserRepository**: Interfaz para las operaciones CRUD en la entidad `User`.
- **CategoryRepository**: Interfaz para las operaciones CRUD en la entidad `Category`.
- **OrderRepository**: Interfaz para las operaciones CRUD en la entidad `Order`.
- **ProductRepository**: Interfaz para las operaciones CRUD en la entidad `Product`.

#### Entidades
- **User**: Representa a un usuario en el sistema.
- **Category**: Representa una categoría de productos.
- **Order**: Representa un pedido realizado por un usuario.
- **Product**: Representa un producto en el sistema.

### Plantillas HTML
- **addCategory.html**: Formulario para añadir una nueva categoría.
- **addProduct.html**: Formulario para añadir un nuevo producto.
- **addUser.html**: Formulario para añadir un nuevo usuario.
- **editProduct.html**: Formulario para editar un producto existente.
- **error.html**: Página de error.
- **index.html**: Página principal.
- **mainPage.html**: Página principal de la aplicación.
- **manageProducts.html**: Página para gestionar productos.
- **products.html**: Muestra la lista de productos.
- **showProduct.html**: Muestra los detalles de un producto.
- **users.html**: Muestra la lista de usuarios.
### Configuración de la Base de Datos
- **application.properties**: Contiene la configuración de la base de datos MySQL.

```properties
spring.application.name=demo1
#Mustache configuration
spring.mustache.suffix=.html
spring.mustache.prefix=classpath:/templates/
#datasource configuration for MySQL
#spring.datasource.url=jdbc:mysql://localhost:3306/shop
#spring.datasource.username=root
#spring.datasource.password=password
#spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
#JPA and hibernate configuration
#spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

# H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

spring.jpa.properties.hibernate.globally_quoted_identifiers=true

spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB


# SQL logging
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

```

## Participación

### Javier Castellanos 
- **Tareas**: mostrar, editar perfil de usuario, puerto 8443, implementar la seguridad, el uso de token, invitado, loging y logout, ver rutina y comentarios, crear rutina, añadir ejercicios en la rutina, algoritmo ordenación, guardar las imágenes en la base de datos y pasarlas a una carpeta.
- **5 commits más significativos**:
   1. [Commit 1](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/5dd21b20343fcaf1e37d8f953e5b0f0642a57fc4)
   2. [Commit 2](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/7181af3d1441ab48e07ea2f7473e2bb9bb2ff741)
   3. [Commit 3](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/c55777c00e9cce2239ceb8e2f7a01adb5b7fe878)
   4. [Commit 4](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/825148a8159113f8ec795c44e53a12dbd9f36190)
   5. [Commit 5](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/7cdcea4476c5ab38c69e8b7be0a8ab25023b5762)


### María Martín
- **Tareas**: novedades (cargar novedades, spinner y paginación), calendario, notificaciones (ver solicitudes, aceptarlas/denegarlas), agregar/buscar amigo, comentar en la rutina, gráfica, borrar usuario administrador, organización de código, error, diagrama de EER, enlazar base de datos, mostrar amigos agregados
- **5 commits más significativos**:
   1. [Commit 1](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/843bb3a20fe8e3c6a407e71b6e006c8735851364)
   2. [Commit 2](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/5307457bd5a9859fb8daecd9cd36fb1567ab1eb6)
   3. [Commit 3](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/a82227cde0b2736ae7fb5f365961c2225888ded5)
   4. [Commit 4](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/72dc52a7e6eea2683c48e8e659b08ba5e055b5ff)
   5. [Commit 5](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/843bb3a20fe8e3c6a407e71b6e006c8735851364)


### Verónica Ramírez
- **Tareas**: registrar usuario, añadir ejercicio administrador, login, paginación lista ejercicios, detalles ejercicios (video, imagen), exportar rutina en PDF, lista ejercicios, buscador de ejercicios, borrar ejercicio administrador, separar header, organización de código, error, diagrama navegación, diagrama clases y templates, logo en página web
- **5 commits más significativos**:
   1. [Commit 1](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/4bd30f19b6619d075d6da5987a40e3d93e62afaf)
   2. [Commit 2](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/ff11e368a3839381852616fb136f4f67a4628481)
   3. [Commit 3](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/30adb43bc690644fa6daa0e76b8cc65662553e14)
   4. [Commit 4](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/2f267fbdc681a344dd2a5cd869b8217f3459cc8b)
   5. [Commit 5](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/1a6b5b713c3ce116bc4105c6dafce95179f3381e)


### Wenhao Zhang
- **Tarea**: enlace a página rutina (sin funcionalidad)
- **5 commits más significativos**:
   1. [Commit 1](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/1ef9813341453f52d092a74f7f8a57ce231c92db)
   2. [Commit 2](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/76aba34e88dd7d574523e527003b28e050e339a5)
   3. [Commit 3](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/57c5923e7fe2de489ce51931316ebccaf73bf933)
   4. [Commit 4](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/1a6b5b713c3ce116bc4105c6dafce95179f3381e )
   5. [Commit 5](https://github.com/SSDD-2025/practica-sistemas-distribuidos-2025-grupo-10/commit/825148a8159113f8ec795c44e53a12dbd9f36190)
