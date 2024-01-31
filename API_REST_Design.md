# API del sistema de préstamos de la biblioteca

Las operaciones que la API debe soportar son las siguientes:
- Registrar un nuevo préstamo.
- Dar de baja un préstamo.
- Obtener un listado de todos los préstamos de un usuario (presentes).
- Obtener un histórico de todos los préstamos de un usuario (pasados).
- Filtrar entre los préstamos por fecha
- Mirar los préstamos que tiene un libro específico

**Recursos identificados:**
- Préstamos (loans): Representa un préstamo de un libro a un usuario

//Al no tener información específica de los usuarios, se van a usar únicamente como atributos en vez de ser su propia entidad



| Método HTTP | URI             | Query Params                                                                                         | Cuerpo de la Petición                                      | Cuerpo de la Respuesta                                                                                                                                | Códigos de Respuesta                                                       |
|-------------|-----------------|------------------------------------------------------------------------------------------------------|------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------|
| POST        | /loans          | N/A                                                                                                  | `{"userId": 123, "bookId": 789, "dueDate": "2023-08-01"}`  | `{"id": 6,"user_id": 123456,"book_id": 1,"initial_date": "2024-01-28","due_date": "2024-02-27","end_date": null,"returned": false}`                   | 201 Created<br/>400 Bad Request<br/>500 Internal Server Error              |
| PATCH       | /loans/{loanId} | N/A                                                                                                  | N/A                                                        | `{"Prestamo devuelto"}`                                                                                                                               | 200 OK<br/>404 Not Found<br/>500 Internal Server Error                     |
| GET         | /loans          | user,book, minInitialDate, maxInitialDate, minDueDate, maxDueDate, minEndDate, maxEndDate, returned  | N/A                                                        | `{"loans": [{"id": 321, "user_id": 12345 "book_id": 789, "initial_date": "2024-01-28","due_date": "2024-02-27","end_date": null,"returned": false}]}` | 200 OK<br/>400 Bad Request<br/>404 Not Found<br/>500 Internal Server Error |



