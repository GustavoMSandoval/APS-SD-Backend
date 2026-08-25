# Backend API - Gestão de Descartes

Uma API RESTful desenvolvida em Java com Spring Boot para o gerenciamento de empresas, departamentos e descartes de materiais. O projeto utiliza Spring Security para autenticação baseada em sessão, Spring Data JPA para persistência de dados e é totalmente containerizado com Docker.

## 🚀 Tecnologias Utilizadas

Este projeto foi construído com as seguintes tecnologias e bibliotecas:

* **[Java 21](https://jdk.java.net/21/):** Linguagem de programação.
* **[Spring Boot 3.x]:** Framework java utilizado.
* **[Spring Security]:** Para controle de acesso e autenticação via sessão HTTP.
* **[MySQL]:** Banco de dados relacional.
* **[Docker e Docker Compose]:** Para containerização do ambiente local.
* **[Maven]:** Gerenciador de dependências e build.
* **[Lombok]:** Para redução de código boilerplate.

## 📋 Pré-requisitos

Para rodar o projeto, você precisará apenas ter o Docker instalado na sua máquina:

* [Docker](https://docs.docker.com/get-docker/)
* [Docker Compose](https://docs.docker.com/compose/install/)

## 🔧 Configuração e Execução através do Docker

```bash
git clone https://github.com/GustavoMSandoval/APS-SD-Backend.git
cd APS-SD-Backend 
```

## 🏗️ Estrutura do Projeto

O projeto é dividido em pastas seguindo o modelo de arquitetura em camadas fortemente atrelado ao padrão MVC.

* Config: Arquivos de configurações e de segurança.
* Controllers: Rotas da API como por exemplo: /api/companies/login.
* DTOS: Data transfer objects utilizados para a transferência e conversão de dados.
* Entities: Equivale ao model do MVC onde fica a modelagem de dados e lógica do banco de dados.
* Enums: Tipos de dados especiais para a manipulação de dados presentes nas entities como os campos status e tipos.
* Repositories: Lógica de acesso ao banco de dados, funciona como uma abstração do banco, ajudando a conectar a tabela aos serviços da api.
* Services: Onde a lógica de negócios é desenvolvida e aplicada, provendo as operações principais da aplicação.

## Contéudo dos Arquivos

### Config

**SecurityConfig.java**

```java

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/companies/login", "/api/companies", "/api/companies/logout").permitAll()
                        .anyRequest().authenticated());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

A ideia central deste arquivo é trabalhar com a segurança da aplicação permitindo ou negando ações do usuário, é neste arquivo onde trablhamos a parte de Autorização da aplicação e criptografia de dados.

### Controllers

**DashboardController.java**

```java

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" }, allowCredentials = "true")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestParam(required = false) String departmentName,
            @RequestParam(required = false) DiscardType type,
            @RequestParam(required = false) DiscardStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        Long companyId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        DashboardResponseDTO dashboard = dashboardService.getDashboardData(
                companyId, departmentName, type, status, startDate, endDate);

        return ResponseEntity.ok(dashboard);
    }
}

```

A dashboard funcionará como o principal meio de comunicação e apresentação de dados da apresentação será disponibilizado por meio desta, dados filtrados da maneira que o usuário desejar.

**CompanyController.java**

```java
@RestController
@RequestMapping("/api/companies")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" }, allowCredentials = "true")
public class CompanyController {

    private final CompanyService service;

    public CompanyController(CompanyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CompanyResponseDTO> create(@Valid @RequestBody CompanyRequestDTO request,
            UriComponentsBuilder ucb) {
        CompanyResponseDTO response = service.create(request);
        URI location = ucb.path("/api/companies/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<CompanyResponseDTO> login(
            @Valid @RequestBody CompanyLoginDTO loginRequest,
            HttpServletRequest request,
            HttpServletResponse response) {

        CompanyResponseDTO responseDto = service.authenticate(
                loginRequest.email(), loginRequest.password(), request, response);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        service.logout(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CompanyResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
```

A classe CompanyController funciona como o principal meio de validar as rotas de autenticação e rotas autenticadas do sistema, caso o usuário não esteja autenticado ele não poderá usufruir do sistema.

**DepartmentController.java**

```java
@RestController
@RequestMapping("/api/departments")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class DepartmentController {

    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DepartmentResponseDTO> create(@Valid @RequestBody DepartmentRequestDTO request,
            UriComponentsBuilder ucb) {
        DepartmentResponseDTO response = service.create(request);
        URI location = ucb.path("/api/departments/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DepartmentResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<DepartmentResponseDTO> findByName(@RequestParam("name") String name) {
        return ResponseEntity.ok(service.findByName(name));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
```

Funciona como uma rota tipo CRUD da entity Department, fornecendo rotas de listagem, criação, etc.

**DiscardMaterialController.java**

```java
@RestController
@RequestMapping("/api/discard-materials")
@CrossOrigin(origins = { "http://localhost:3000", "http://localhost:4200" })
public class DiscardMaterialController {

    private final DiscardMaterialService service;

    public DiscardMaterialController(DiscardMaterialService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DiscardMaterialResponseDTO> create(@Valid @RequestBody DiscardMaterialRequestDTO request,
            UriComponentsBuilder ucb) {
        DiscardMaterialResponseDTO response = service.create(request);
        URI location = ucb.path("/api/discard-materials/{id}").buildAndExpand(response.id()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DiscardMaterialResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
```

Funciona como uma rota tipo CRUD da entity DiscardMaterial, fornecendo rotas de listagem, criação, etc.

### Services

**DashboardService.java**

```java
@Service
public class DashboardService {

    private final DiscardMaterialRepository discardMaterialRepository;
    private final DepartmentRepository departmentRepository;

    public DashboardService(
            DiscardMaterialRepository discardMaterialRepository,
            DepartmentRepository departmentRepository) {
        this.discardMaterialRepository = discardMaterialRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(readOnly = true)
    public DashboardResponseDTO getDashboardData(
            Long companyId,
            String departmentName,
            DiscardType type,
            DiscardStatus status,
            LocalDateTime startDate,
            LocalDateTime endDate) {

        Long totalDepartments;
        if (departmentName != null && !departmentName.trim().isEmpty()) {
            totalDepartments = departmentRepository.countByCompanyIdAndNameContainingIgnoreCase(companyId, departmentName);
        } else {
            totalDepartments = departmentRepository.countByCompanyId(companyId);
        }

        Long totalDiscards = discardMaterialRepository.countTotalDiscardsInPeriodByDepartmentName(
                companyId, departmentName, type, status, startDate, endDate
        );

        List<DepartmentDiscardSummaryDTO> discardsByDepartment = 
                discardMaterialRepository.countDiscardsByDepartmentName(
                        companyId, departmentName, type, status, startDate, endDate
                );

        List<DiscardTypeSummaryDTO> discardsByType = 
                discardMaterialRepository.countDiscardsByTypeAndDepartmentName(
                        companyId, departmentName, status, startDate, endDate
                );

        return new DashboardResponseDTO(
                totalDepartments,
                totalDiscards,
                discardsByDepartment,
                discardsByType
        );
    }
}
```

Funciona como o gerenciador de ações que a dashboard pode executar e retornar, a resposta é convertida para DTO e enviada via API para o cliente consumidor do sistema.

**CompanyService.java**

```java
@Service
public class CompanyService {

    private final CompanyRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    public CompanyService(CompanyRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    private CompanyResponseDTO toResponse(Company company) {
        return new CompanyResponseDTO(
                company.getId(),
                company.getCnpj(),
                company.getName(),
                company.getEmail(),
                company.getPhone(),
                company.getCreatedAt(),
                company.getUpdatedAt());
    }

    public CompanyResponseDTO create(CompanyRequestDTO request) {
        Company company = new Company();

        company.setCnpj(request.cnpj());
        company.setName(request.name());
        company.setEmail(request.email());
        company.setPassword(passwordEncoder.encode(request.password()));
        company.setPhone(request.phone());

        return toResponse(repository.save(company));
    }

    public CompanyResponseDTO authenticate(
            String email,
            String rawPassword,
            HttpServletRequest request,
            HttpServletResponse response) {

        Company company = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciais inválidas"));

        if (!passwordEncoder.matches(rawPassword, company.getPassword())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        Authentication auth = new UsernamePasswordAuthenticationToken(
                company.getId(), null, Collections.emptyList());

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        securityContextRepository.saveContext(context, request, response);

        return toResponse(company);
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }

    public List<CompanyResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

A classe CompanyService executa e fornece os serviços de autenticação e criação de Empresa que neste caso funciona como um usuário.

**DepartmentService.java**

```java
@Service
public class DepartmentService {

    private final DepartmentRepository repository;
    private final CompanyRepository companyRepository;

    public DepartmentService(DepartmentRepository repository, CompanyRepository companyRepository) {
        this.repository = repository;
        this.companyRepository = companyRepository;
    }

    private DepartmentResponseDTO toResponse(Department department) {
        return new DepartmentResponseDTO(
                department.getId(),
                department.getName(),
                department.getDescription(),
                department.getResponsibleName(),
                department.getCompany().getId(),
                department.getCreatedAt(),
                department.getUpdatedAt());
    }

    public DepartmentResponseDTO create(DepartmentRequestDTO request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Department department = new Department();
        department.setName(request.name());
        department.setDescription(request.description());
        department.setResponsibleName(request.responsibleName());
        department.setCompany(company);

        return toResponse(repository.save(department));
    }

    public DepartmentResponseDTO findByName(String name) {
        Department department = repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));
        return toResponse(department);
    }

    public List<DepartmentResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

Funciona com operações do tipo CRUD da entity Department, fornecendo funções de listagem, criação, etc.

**DiscardMaterial.java**

```java
@Service
public class DiscardMaterialService {

    private final DiscardMaterialRepository repository;
    private final DepartmentRepository departmentRepository;

    public DiscardMaterialService(DiscardMaterialRepository repository, DepartmentRepository departmentRepository) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
    }

    private DiscardMaterialResponseDTO toResponse(DiscardMaterial discardMaterial) {
        return new DiscardMaterialResponseDTO(
                discardMaterial.getId(),
                discardMaterial.getType(),
                discardMaterial.getDescription(),
                discardMaterial.getDiscardDate(),
                discardMaterial.getEmployeeWhoDiscarded(),
                discardMaterial.getStatus(),
                discardMaterial.getDepartment().getId(),
                discardMaterial.getCreatedAt(),
                discardMaterial.getUpdatedAt());
    }

    public DiscardMaterialResponseDTO create(DiscardMaterialRequestDTO request) {
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        DiscardMaterial discardMaterial = new DiscardMaterial();
        discardMaterial.setType(request.type());
        discardMaterial.setDescription(request.description());
        discardMaterial.setDiscardDate(request.discardDate());
        discardMaterial.setEmployeeWhoDiscarded(request.employeeWhoDiscarded());
        discardMaterial.setStatus(request.status());
        discardMaterial.setDepartment(department);

        return toResponse(repository.save(discardMaterial));
    }

    public List<DiscardMaterialResponseDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
```

Funciona com operações do tipo CRUD da entity DiscardMaterial, fornecendo funções de listagem, criação, etc.