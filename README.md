# JobTrackr

Suivi de candidatures — projet personnel de remise à niveau technique:  Java/Spring Boot, Angular, TDD, Clean Code, Docker, CI/CD et déploiement AWS.

## Stack

| Couche | Technologies |
|---|---|
| Backend | Java 21, Spring Boot 4.1.1, Spring Data JPA/Hibernate, Spring Security, JWT, PostgreSQL |
| Mapping | MapStruct (génération de mappers à la compilation) |
| Frontend | Angular 18 (standalone), TypeScript, RxJS |
| Tests | JUnit 5, Mockito, Testcontainers, Jasmine/Karma |
| Observabilité | Spring Boot Actuator, Micrometer, Prometheus, Grafana |
| Infra | Docker, Docker Compose, GitHub Actions, AWS (EC2, RDS, Parameter Store) |

## Fonctionnalités

- Inscription / connexion avec authentification par JWT
- Créer, lister (avec filtre par statut), modifier et supprimer une candidature
- Tableau de bord avec le nombre de candidatures par statut (Postulé, Entretien, Refusé, Offre)

## Lancer le projet en local

Prérequis : Docker et Docker Compose installés.

```bash
git clone <url-du-depot>
cd jobtrackr
docker compose up --build
```

- Frontend : http://localhost:4200
- API : http://localhost:8080/api
- Documentation Swagger : http://localhost:8080/swagger-ui.html
- Santé et métriques : http://localhost:8081/actuator/health
- Tableaux de bord Grafana : http://localhost:3000 (identifiants par défaut `admin` / `admin`, à changer avant tout usage au-delà du local)
- Prometheus : http://localhost:9090

Aucune autre configuration n'est nécessaire : le mot de passe de base de données par défaut convient pour un usage local (voir `docker-compose.yml` pour le changer).

## Observabilité

Le backend expose ses métriques via Spring Boot Actuator et Micrometer, au format Prometheus (`/actuator/prometheus`). Le `docker-compose.yml` lance Prometheus (qui scrute ce endpoint toutes les 15 secondes) et Grafana (avec la source de données Prometheus déjà configurée automatiquement).

Une fois les conteneurs démarrés, dans Grafana : Menu → Dashboards → New → Import, et utiliser le tableau de bord communautaire **JVM (Micrometer)**, identifiant `4701` sur grafana.com — il affiche l'utilisation mémoire, les threads, le temps de réponse HTTP et le taux de requêtes par endpoint sans configuration supplémentaire.

Le endpoint `/actuator/*` tourne sur un port séparé (`8081`) du port applicatif (`8080`), une pratique courante pour pouvoir le fermer au public sans toucher à l'API elle-même. Pour un déploiement réel, ce port ne devrait pas être publié sur l'hôte — seul Prometheus, sur le réseau Docker interne, doit pouvoir l'atteindre.

## Lancer en développement, sans Docker

**Backend**
```bash
cd backend
# démarrer un PostgreSQL local, ou lancer seulement le service postgres du compose :
docker compose up postgres -d
mvn spring-boot:run
```

**Frontend**
```bash
cd frontend
npm install
npm start
```

## Lancer les tests

```bash
# Backend : tests unitaires + intégration (Testcontainers démarre un PostgreSQL éphémère,
# Docker doit être lancé)
cd backend
mvn verify

# Frontend
cd frontend
npm test
```

## Structure du dépôt

```
jobtrackr/
├── backend/     API Spring Boot (voir backend/src/main/java/com/jobtrackr)
├── frontend/    Application Angular (voir frontend/src/app)
├── docker-compose.yml
└── .github/workflows/   CI (tests) et CD (build + déploiement AWS)
```

## Déploiement AWS

Le pipeline `.github/workflows/cd.yml` construit les images Docker, les pousse vers Amazon ECR, puis se connecte en SSH à une instance EC2 pour relancer `docker compose`.

Infrastructure minimale nécessaire côté AWS, avant le premier déploiement :

1. **RDS PostgreSQL** (`db.t3.micro`) — groupe de sécurité limité au groupe de sécurité de l'instance EC2 sur le port 5432.
2. **EC2** (`t3.micro`, Amazon Linux 2023) avec Docker et Docker Compose installés, groupe de sécurité ouvrant les ports 80/443 au public et 22 restreint à une IP de confiance.
3. **Secrets GitHub Actions** à configurer (Settings → Secrets and variables → Actions) : `AWS_ACCESS_KEY_ID`, `AWS_SECRET_ACCESS_KEY`, `EC2_HOST`, `EC2_SSH_KEY`.
4. Les secrets applicatifs (mot de passe base de données, `JWT_SECRET`) sont stockés dans **AWS Systems Manager Parameter Store** plutôt qu'en clair sur l'instance.

## Compétences démontrées

| Compétence | Où c'est démontré |
|---|---|
| Injection de dépendances | Constructeur uniquement dans tous les services (`ApplicationService`, `AuthService`...), jamais de champ `@Autowired` |
| TDD | `ApplicationServiceTest` écrit avant l'implémentation finale du service ; historique Git des commits `test:` puis `feat:` |
| Clean Code / SOLID | Découpage par fonctionnalité (`auth/`, `application/`), DTOs séparés des entités JPA, une responsabilité par classe |
| Java 21 / Spring Boot 4.1.1 | Backend complet : sécurité, persistance, API REST |
| Angular | Frontend complet : formulaires réactifs, garde de route, intercepteur HTTP, signals |
| Tests d'intégration réalistes | Testcontainers avec un vrai PostgreSQL plutôt qu'une base en mémoire |
| Mapping DTO/entité | MapStruct — génération à la compilation, aucun mapping manuel |
| Surveillance applicative | Actuator + Micrometer + Prometheus + Grafana, port de gestion séparé |
| CI/CD | `.github/workflows/ci.yml` (tests) et `cd.yml` (déploiement automatisé) |
| Docker | Images multi-étapes pour le backend (Maven → JRE) et le frontend (Node → nginx) |
| AWS | EC2, RDS, Parameter Store — en lien avec la certification AWS Developer Associate en préparation |

## Pistes d'amélioration (si je continue ce projet)

- Migrations de schéma versionnées avec Flyway plutôt que `ddl-auto: update`
- Pagination sur la liste des candidatures
- Rôle ADMIN avec vue d'ensemble multi-utilisateurs

## Note technique — Spring Boot 4 et Jackson 3

Spring Boot 4 fait cohabiter Jackson 2 (`com.fasterxml.jackson`) et Jackson 3 (`tools.jackson`) sur le classpath pendant la période de transition de l'écosystème. Pour éviter toute ambiguïté :

- le JWT est signé avec `jjwt-gson` plutôt que `jjwt-jackson`, donc totalement indépendant de la version de Jackson utilisée par Spring ;
- les tests d'intégration construisent leurs corps de requête JSON avec des text blocks Java plutôt qu'un `ObjectMapper` injecté.

Si une dépendance tierce ajoutée plus tard exige explicitement Jackson 2 (comme springdoc-openapi au moment de l'écriture de ce projet), c'est normal pendant cette phase de transition — Spring Boot gère les deux versions en parallèle.

