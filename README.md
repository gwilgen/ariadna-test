# ariadna-test

He subido [un video](https://youtu.be/qL7cqu1GDWI) en el que se ve la ejecución del resultado

## Requisitos

La descripción de la prueba es la siguiente:

Una de las partes mas importantes en el producto de Ariadna es trabajar con grandes cantidades de eventos, que simplificando un poco, tienen un origen, un timestamp y un valor.

El proyecto consiste en implementar una aplicación Java básica para trabajar con esos eventos. Para ello puedes asumir las siguientes estructuras:

    Fuente (id, nombre)
    Evento (id, fuente_id, timestamp, valor)

La métodos que hay que implementar son:
  - Buscar eventos por lista de timestamps.
  - Buscar eventos por fuente_id.
  - Buscar eventos dentro de un rango de valores (valor min, valor max). 

Las búsquedas por timestamp y rango de valor deberían ser lo mas eficientes que sea posible (dentro de lo razonable, es solo una prueba).

### Consideraciones

 - En el mundo real hubiera pedido algo de feedback para concretar dudas sobre los requisitos. Pero he querido aprovechar para plantear los diferentes escenarios, y así demostrar los pros y contras de cada alternativa.
 - Habrá una forma de persistir esta información (para que hacer la misma consulta en dos momentos del tiempo te de el mismo resultado). Para ello he optado por usar el motor de base de datos de H2 (aunque cambiar por un SQLite, PostgreSQL, MySQL, etc es cuestión de configuración). Lo que no he hecho ha sido añadir un campo extra en la tabla de Eventos para determinar la _fecha de creación_ del evento, porque entonces siempre puedes añadir más registros con efecto retroactivo y la misma búsqueda te daría más resultados la segunda vez.
 - No tengo claro si una fuente es una categoría de qué tipo de dato se está captando (e.g. Intensidad, Resistencia, Voltaje) y el valor representa Amperios, Ohmnios, Voltios... O si es un origen de datos. He optado por este segundo caso a la hora de poblar datos de prueba.
 - He supuesto que la consulta de tiemstamps es por rango (desde, hasta), que no es una lista con _exactamente_ los timestamps que tienen que ser
 - El valor supongo que es un número con decimales, y que puede ser negativo
 - Aunque el enunciado dice _aplicación Java_ considero que lo que hace falta es un servidor, que una vez levantado admita persistir eventos, o consultarlos.
 - He querido utilizar el framework de Spring, porque simplifica mucha lógica y te facilita seguir los principios [SOLID](https://es.wikipedia.org/wiki/SOLID)
 - Para hacer eficientes las búsquedas (por timestamp o por valores) los aspectos a tener en cuenta serían:
   - indexar esos campos (así las cláusulas where son más rápidas)
   - escalar la base de datos:
     - horizontal (e.g. separación física de la tabla de eventos por orígen y que sea el motor de base de datos el que haga las consultas en paralelo y mergee los resultados)
     - vertical (utilizar un motor de base de datos o una máquina más potente)
- No me he metido en tema de i18n porque lo considero fuera del alcance, pero en Spring es trivial (como lo sería por ejemplo en Angular)
 
### Enfoque
 - He creado 3 versiones: una apliación nativa de Java, un servidor de Spring, y una aplicación usando la infraestructura de Spring
 - A su vez he creado 2 formas de acceder a los datos: SQL nativo, y JPA de Spring Data. Comparten interfaces comunes y permiten comprender las ventajas de separación de conceptos
 - Las consultas se paginan (para que no te de todos los resultados de golpe)
 - Las versiones de aplicación están hechas en Swing, y la interfaz del servidor web son plantillas rellenadas con Thyemelaf y con Javascript inyectado (por simplicidad)
 - En el servidor web he implementado rutas que devuelvan tanto HTML (navegación web) como JSON (consultas via API)
 - He intentado abstraer la lógica común para poder compartir la mayor parte del comportamiento entre las diferentes versiones, pero he dejado flecos por simplificar:
     - Los mensajes de error cuando fallan las validaciones (las de tipo de dato de las consultas por rango de timestamps o valores) están copiados y pegados (y eso en producción es una pésima práctica, pero es que no hace falta hacer una sobreingeniería para quitar redundancias de dos escenarios que no deben coexistir porque precisamente son complementarios)
     - La creación inicial de la base de datos la hace Spring (así que lo primero que se ejecuta no puede ser la aplicación nativa)
     - Los resultados obtenidos de la base de datos se exponen en la API del servidor web tal cual. Si consideramos que puede haber información sensible o innecesaria, debería haber una clase para cada cosa (no es el caso, pero son cosas a tener en cuenta cuando los programas crecen)
   
### Estructura de carpetas
  - src/main/java/ariadna
    - config: Servicios (de acceso a datos) que se vinculan en base de parámetros de configuración (sólo aplica en Spring)
    - controllers: Puntos de entrada del servidor web
    - models: Clases Java equivalentes a las tablas de la base de datos
    - repositories: Clases que definen cómo se accede a los datos (sólo para JPA, en el servidor web)
    - services: Clases que definen cómo se explotan los repositorios (esos suelen ser _straight forward_...), u otras formas de acceder a la base de datos (e.g. SQL nativo)
    - swing: Interfaz gráfica de la aplicación
    - validations: Comprobaciones de tipos
    - Las propias clases de arranque
   - src/main/resources
     - templates: plantillas HTML del servidor web
     - *.sql: ficheros que se ejecutan al arrancar Spring y no encontrar éste la base de datos creada
     
## Implementación

La definición de la base de datos se hace en src/main/resources/schema.sql, y las clases de src/main/java/ariadna/models representan objetos con los mismos campos que las tablas.

El servicio básico que hay que implementar está en IEventsService:

    public interface IEventsService {	
	    public List<Event> getEventsBetweenTimestamps(Timestamp from, Timestamp to, Pageable pageable);
	    public List<Event> getEventsOfSource(Integer sourceId, Pageable pageable);
	    public List<Event> getEventsBetweenValues(Double min, Double max, Pageable pageable);
	    public long getNumberOfEvents();
    }
    
Tiene básicamente 2 implementaciones (JPA y SQL), si bien la primera son 40 líneas, y muy simples, y la segunda son 90 líneas. Ocurre lo mismo con IPopulateService, pero aquí son menos líneas y no se nota tanto la diferencia. Cabe decir que el AbstractPopulateService se basa en las funciones por implementar para persistir nuevos registros de Eventos, creando timestamps entre 2000-01-01 y 2020-01-01, y valores entre -100 y 100 (ambas han sido una decisión arbitraria).

## Ejecución

 - RawSwingApp se encarga explícitamente de instanciar los servicios necesarios y se los pasa a la interfaz Swing
 - SpringSwingApp crea el ApplicationContext de Spring y después instancia la ventana (la misma que antes, se puede reutilizar por tener inversión de dependencias) y obtiene los servicios que considere Spring que son los adecuados en función de la configuración (e.g. el flag ariadna-data.type)
 - SpringWebApp levanta un servidor en localhost:8080 y mediante un navegador se accede a unas interfaces equivalentes a la versión Swing

Para lanzarlo desde consola, serían de la siguiente forma:

    mvn clean install spring-boot:run -Dstart-class=ariadna.SpringSwingApp
    mvn clean install spring-boot:run -Dstart-class=ariadna.SpringWebApp
