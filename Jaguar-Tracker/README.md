Jaguar Tracker: Sistema de Gestión de Entrenamiento UAM

1. Introducción y Propósito del Proyecto

Como equipo de liderazgo técnico, presentamos Jaguar Tracker, la solución de software definitiva que actuará como la bitácora oficial de entrenamiento para el gimnasio de la Universidad Americana (UAM). Este proyecto surge de la necesidad de modernizar el registro de actividad física bajo estándares de arquitectura móvil de vanguardia. La plataforma integra el correo institucional no solo como un mecanismo de autenticación robusto, sino como un símbolo de pertenencia a la comunidad universitaria, garantizando un entorno seguro y exclusivo para estudiantes de la UAM.

2. Análisis del Problema y Solución Propuesta

Los puntos críticos resueltos son:

* Rigidez Cronológica: La arquitectura del sistema desacopla el progreso del usuario de los objetos Calendar persistentes, permitiendo una lógica de microciclos agnóstica a la fecha que se adapta a agendas académicas apretadas.
* Estancamiento Físico: La falta de control histórico impide la sobrecarga progresiva. El sistema centraliza datos técnicos para garantizar una progresión de cargas basada en evidencia.
* Gestión de la Fatiga: Jaguar Tracker implementa un seguimiento preciso de la fatiga acumulada, permitiendo ajustes técnicos para evitar el sobreentrenamiento y mejorar la recuperación.

3. Funcionalidades Principales

* Autenticación Institucional:Implementación de seguridad basada en dominios UAM para el envío de códigos de verificación dinámicos.

* Rutinas Flexibles: Gestión de entrenamientos sin fechas rígidas, permitiendo completar volúmenes de entrenamiento según disponibilidad.

* Métricas de Esfuerzo: Registro de intensidad mediante RIR (Repeticiones en Reserva) por defecto, con RPE (Esfuerzo Percibido) como métrica opcional.

* Comunidad Jaguar: Integración con la API de Instagram para el intercambio de progresos y sistema de ranking compartido.

4. Arquitectura del Sistema y Stack Tecnológico

La infraestructura del software se ha diseñado para ser escalable y mantenible, utilizando un paradigma de Programación Orientada a Objetos aprovechando las bondades de Kotlin como data classes para entidades de ejercicio e interfaces para la lógica de equipamiento.

Frontend (Mobile)

* Lenguaje: Kotlin 2.2.10
* UI Framework: Jetpack Compose (Enfoque declarativo).
* Arquitectura: MVVM (Model-View-ViewModel).
* Persistencia: Room Persistence Library para base de datos local.
* Red: Retrofit para consumo de servicios REST.
* Gradle: 9.3.1

Backend

* Framework: Spring Boot 3.2+ (Spring 6.1).
* Base de Datos: PostgreSQL.
* Nombre de la Base de Datos: Jaguar_BD
* Estándar de Comunicación: APIs REST.

Directiva de Desarrollo (Spring 6.1): Siguiendo las mejores prácticas para un código conciso en Kotlin, los desarrolladores NO deben utilizar los parámetros de anotación required = true o defaultValue en los controladores @RequestParam. En su lugar, se deben utilizar los tipos de datos de Kotlin:

* Usar tipos nulables (String?) para definir parámetros opcionales.
* Utilizar la asignación estándar de Kotlin (val maxResults: Int = 100) para definir valores por defecto.

5. Filosofía de Diseño UX/UI

El diseño se fundamenta en Material Design 3, optimizado mediante leyes psicológicas para el contexto de alta exigencia física de un gimnasio:

* Ley de Hick: Reducción sistemática de la carga cognitiva mediante la simplificación de menús de selección de ejercicios.
* Ley de Miller: Los listados de ejercicios se agrupan en "bloques" de máximo 7 elementos para facilitar la retención en la memoria de trabajo.
* Ley de Fitts y Zona del Pulgar: Las acciones críticas (Iniciar Serie, Guardar) están ubicadas en la zona de confort ergonómico. Se prioriza la Precisión Táctil con hit-boxes ampliados para compensar manos sudorosas o temblorosas tras el esfuerzo.
* Ley de Jakob: Uso de convenciones y modelos mentales existentes para eliminar la curva de aprendizaje.
* Test del Mundo Real: Se implementa un Alto Contraste y tipografía de legibilidad absoluta para sobrevivir a la luz solar directa o la iluminación intensa del gimnasio. El sistema omite teclados físicos en favor de Pads Numéricos Contextuales para la entrada de pesos y repeticiones.

6. Equipo de Desarrollo

* Enrique: Diseño UI/UX y Prototipado.
* Andrés: Desarrollo Frontend y Lógica de Aplicación.
* Harry: Desarrollo Backend y Arquitectura de Datos.
* Claudia: QA, Control de Calidad y Validación de Usabilidad.

