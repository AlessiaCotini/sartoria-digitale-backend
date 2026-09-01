
---
## `sartoria-digitale-backend/README.md`
```markdown
# Bellariva — Sartoria Digitale (Backend)
API REST + WebSocket per [sartoria-digitale-frontend](../sartoria-digitale-frontend). Spring Boot 4 / Java 25.
## Demo online
- **Backend**: https://sartoria-digitale-backend.onrender.com
- **Frontend**: https://sartoria-digitale-frontend.vercel.app
- **Swagger UI**: https://sartoria-digitale-backend.onrender.com/swagger-ui/index.html
## Stack tecnico
Spring Boot 4.1.0 (Web MVC, Data JPA, Security, Validation, WebSocket), PostgreSQL, JWT (io.jsonwebtoken), springdoc-openapi 3 (Swagger UI), Mailgun (email transazionali), Cloudinary (immagini), Lombok.
## Architettura
### Ruoli e sicurezza
4 ruoli (`CLIENTE`, `SOTTOPOSTO`, `SARTA`, `SUPER_ADMIN`), gerarchia fissa per i tre ruoli gestionali (super admin crea sarte, una sarta crea i propri sottoposti); il cliente si autoregistra. Ogni endpoint sensibile è annotato con `@PreAuthorize` sul ruolo richiesto. Autenticazione stateless via JWT (`Authorization: Bearer <token>`).
### Chat real-time
WebSocket via STOMP su endpoint `/ws` (SockJS fallback), broker semplice su `/topic`, autenticazione JWT verificata in `StompAuthChannelInterceptor` alla fase di CONNECT.
## API principali
| Area | Endpoint | Note |
|---|---|---|
| Auth | `POST /auth/register`, `POST /auth/login` | self-registrazione cliente, login JWT |
| Auth | `POST /auth/richiedi-reset`, `POST /auth/reset-password` | reset password via email |
| Utenti | `GET /utenti/me`, `GET /utenti/clienti` | profilo corrente, ricerca clienti registrati |
| Utenti | `POST /utenti/sarte`, `POST /utenti/sottoposti` | creazione gerarchica ruoli gestionali |
| Misure | `GET /misure/me` | misure corporee del cliente loggato |
| Catalogo | `GET/POST /capi`, `GET /capi/{id}` | capi di abbigliamento |
| Catalogo | `GET/POST /accessori`, `GET /accessori/{id}` | accessori |
| Catalogo | `GET/POST /materiali`, `GET /opzioni`, `GET /opzioni-accessori` | materiali e opzioni configuratore |
| Ordini | `POST /ordini`, `GET /ordini/miei`, `GET /ordini/assegnati`, `GET /ordini` | creazione e liste per ruolo |
| Ordini | `POST /ordini/negozio`, `PATCH /ordini/{id}/assegna`, `PATCH /ordini/{id}/stato`, `PATCH /ordini/{id}/prezzo`, `PATCH /ordini/{id}/fornitore` | gestione ordini lato sarta |
| Ordini | `GET /ordini/magazzino` | vista magazzino |
| Appuntamenti | `POST /appuntamenti`, `POST /appuntamenti/negozio`, `PATCH /appuntamenti/{id}`, `GET /appuntamenti/miei`, `GET /appuntamenti` | prenotazioni cliente/sarta |
| Pagamenti | `GET /pagamenti`, `GET /pagamenti/ordine/{id}`, `PATCH /pagamenti/ordine/{id}/acconto`, `PATCH /pagamenti/ordine/{id}/saldo` | acconto/saldo per ordine |
| Chat | `GET /messaggi/ordine/{id}`, `PATCH /messaggi/ordine/{id}/letti`, `GET /messaggi/non-letti/conteggio`, `GET /messaggi/non-letti/per-ordine` | storico e badge messaggi |
| Contatti | `POST /contatti` | form "scrivici due righe" |
Documentazione interattiva completa su Swagger UI (vedi sopra).
## Avvio in locale
Prerequisiti: Java 25, PostgreSQL, Maven (o il wrapper incluso, `./mvnw` / `.\mvnw.cmd`).
1. Crea `env.properties` nella root (gitignored) con:
   ```properties
   PORT=3027
   DB_URL=jdbc:postgresql://localhost:5432/<nome-db>
   DB_USERNAME=...
   DB_PASSWORD=...
   JWT_SECRET=...
   CLOUDINARY_NAME=...
   CLOUDINARY_APIKEY=...
   CLOUDINARY_SECRET=...
   SUPERADMIN_EMAIL=...
   SUPERADMIN_PASSWORD=...
   MAILGUN_API_KEY=...
   MAILGUN_DOMAIN_NAME=...
   
   
Avvia con ./mvnw spring-boot:run (o dal tuo IDE).


Deploy

Hosting su Render (Web Service, runtime Docker — Render non offre un runtime Java nativo). Il Dockerfile nella root è multi-stage: build con maven:3.9-eclipse-temurin-25, runtime con eclipse-temurin:25-jre.

Database: Render PostgreSQL (free tier), stessa regione del backend — si usa l'hostname interno per la connessione (rete privata Render, gratuita e più veloce dell'URL esterno).

Environment variables da configurare su Render (oltre a quelle di env.properties sopra):

FRONTEND_URL — URL pubblico del frontend deployato (usato nei link delle email di reset password e nel CORS)
PORT — porta su cui Tomcat resta in ascolto
Nota: il servizio è tenuto sveglio da un monitor UptimeRobot che pinga /capi ogni 5 minuti, per evitare lo sleep del piano free di Render.

CORS
Attenzione: ci sono due configurazioni CORS separate nel progetto, entrambe da aggiornare se cambia il dominio del frontend:

security/SecurityConfig.java — CORS per le API REST
configuration/WebSocketConfig.java — CORS per l'endpoint WebSocket /ws (setAllowedOriginPatterns)