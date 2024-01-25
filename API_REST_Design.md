# API del sistema de préstamos de la biblioteca

Las operaciones que la API debe soportar son las siguientes:
- Registrar un nuevo préstamo.
- Dar de baja un préstamo.
- Obtener un listado de todos los préstamos de un usuario (presentes).
- Obtener un histórico de todos los préstamos de un usuario (pasados).
- Filtrar entre los préstamos por fecha

**Recursos identificados:**
- Préstamos (loans): Representa un préstamo de un libro a un usuario
- Usuario (users): Representa un usuario de la biblioteca.

TODO modifficar los cuerpos de peticion

| Método HTTP | URI                   | Query Params                     | Cuerpo de la Petición                                      | Cuerpo de la Respuesta                                                                 | Códigos de Respuesta                                                                               |
|-------------|-----------------------|----------------------------------|------------------------------------------------------------|----------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
| POST        | /loans                | N/A                              | `{"userId": 123, "bookId": 789, "dueDate": "2023-08-01"}`  | `{"loanId": 321, "userId": 123, "bookId": 789, "dueDate": "2023-08-01"}`               | 201 Created<br/>400 Bad Request<br/>404 Not Found<br/>409 Conflict<br/>500 Internal Server Error   |
| PATCH       | /loans/{loanId}       | N/A                              | N/A                                                        | `{"message": "Loan returned"}`                                                         | 200 OK<br/>404 Not Found<br/>500 Internal Server Error                                             |
| GET         | /users/{userId}/loans | initialDate, loanedDate, endDate | N/A                                                        | `{"loans": [{"loanId": 321, "bookId": 789, "dueDate": "2023-08-01"}]}`                 | 200 OK<br/>400 Bad Request<br/>404 Not Found<br/>500 Internal Server Error                         |



