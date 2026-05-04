API de Gerenciamento de Tarefas (To-Do List)
Stack alvo: Java 17+ (ou 21), Spring Boot 3, PostgreSQL, Flyway, JUnit/Mockito, Swagger (Springdoc) e Docker.

Etapa 1: Setup e Configuração (O "composer create-project")
[X] Acesse o Spring Initializr (start.spring.io) e crie o projeto usando Maven.

[X] Adicione as dependências essenciais: Spring Web, Spring Data JPA, PostgreSQL Driver, Lombok, Validation, Flyway Migration.

[X] Configure o arquivo src/main/resources/application.yml (o equivalente ao .env + config/ do Laravel) com as credenciais do banco de dados local.

Etapa 2: Banco de Dados e Migrations (O "artisan migrate")
Atenção: No Java corporativo, raramente usamos a sincronização automática do Hibernate (ddl-auto=update). Usamos ferramentas de versionamento explícito.

[X] Crie a pasta db/migration dentro de src/main/resources.

[X] Crie o seu primeiro arquivo de migration do Flyway: V1__create_table_tasks.sql.

[X] Escreva o SQL puro para criar a tabela tasks (id UUID ou BIGINT, title, description, status, due_date, created_at, updated_at).

[X] Rode a aplicação para garantir que o Flyway executou a migration no PostgreSQL.

Etapa 3: Domínio e Persistência (O "Eloquent" dividido em dois)
[ ] Crie a classe Entidade Task anotada com @Entity e @Table. Mapeie os atributos para as colunas do banco.

[ ] Use anotações do Lombok (@Getter, @Setter, @NoArgsConstructor, etc.) para evitar o código boilerplate (getters e setters manuais).

[ ] Crie a interface TaskRepository estendendo JpaRepository<Task, Long>.

[ ] Crie um método customizado na interface, como List<Task> findByStatus(String status);. O Spring implementará a query magicamente.

Etapa 4: Lógica de Negócios e DTOs (Os "Services" e "Form Requests")
[ ] Crie as classes DTO (Data Transfer Objects): TaskRequestDTO (para entrada) e TaskResponseDTO (para saída). Dica: Use Java Records, introduzidos no Java 14, são perfeitos para DTOs.

[ ] Adicione anotações de validação no TaskRequestDTO (ex: @NotBlank(message = "Título é obrigatório"), @FutureOrPresent).

[ ] Crie a classe TaskService anotada com @Service.

[ ] Injete o TaskRepository no TaskService via construtor.

[ ] Implemente os métodos de negócio (criar, listar, buscar por ID, atualizar, concluir tarefa, deletar). Faça a conversão entre DTO e Entidade aqui (ou use uma lib como MapStruct, se quiser um desafio extra).

Etapa 5: A Porta de Entrada Web (Os "Controllers")
[ ] Crie a classe TaskController anotada com @RestController e @RequestMapping("/api/tasks").

[ ] Implemente os endpoints (GET, POST, PUT, DELETE), injetando o TaskService.

[ ] Use @Valid no parâmetro do POST/PUT para ativar as validações do DTO (equivalente a injetar um FormRequest no método do Laravel).

[ ] Retorne os dados usando ResponseEntity para controlar corretamente os HTTP Status Codes (200 OK, 201 Created, 204 No Content).

Etapa 6: Tratamento de Erros Global (O "Handler.php")
[ ] Crie uma classe GlobalExceptionHandler anotada com @ControllerAdvice.

[ ] Capture a exceção MethodArgumentNotValidException (quando a validação do DTO falha) e retorne um JSON formatado com os campos que deram erro (Status 400).

[ ] Capture exceções de "Registro não encontrado" (quando buscar uma task por ID que não existe) e retorne um Status 404.

Etapa 7: Documentação com Swagger
[ ] Adicione a dependência springdoc-openapi-starter-webmvc-ui no seu pom.xml.

[ ] Reinicie a aplicação e acesse /swagger-ui.html. Você verá que o Spring mapeou seus endpoints automaticamente (igual o L5-Swagger faz no Laravel, mas com menos esforço de configuração).

[ ] Adicione anotações como @Operation e @ApiResponses no seu Controller para enriquecer a documentação lida pelos recrutadores.

Etapa 8: Testes Automatizados (O "PHPUnit")
[ ] Testes Unitários: Crie a classe TaskServiceTest. Use o JUnit 5 e o Mockito para "mockar" o TaskRepository. Teste as regras de negócio isoladamente (ex: tentar criar uma tarefa e garantir que o repository.save() foi chamado).

[ ] Testes de Integração: Crie a classe TaskControllerTest. Use o @SpringBootTest e o MockMvc para simular requisições HTTP reais batendo nos seus endpoints e verifique os retornos JSON.

Etapa 9: Dockerização (Preparando para a Produção)
[ ] Crie um Dockerfile na raiz do projeto. Utilize o conceito de Multi-stage Build:

Estágio 1: Imagem do Maven para baixar as dependências e compilar o .jar (mvn clean package -DskipTests).

Estágio 2: Imagem do JRE (ex: Eclipse Temurin ou Amazon Corretto) menor e mais leve apenas para rodar o .jar.

[ ] Crie um docker-compose.yml que levante dois containers:

O serviço do banco de dados (PostgreSQL).

O serviço da sua aplicação Spring (dependendo do banco de dados estar pronto).