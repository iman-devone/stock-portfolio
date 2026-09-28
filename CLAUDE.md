# Stock Portfolio

Personal web app to follow Swedish stocks (Nasdaq Stockholm) and paper gold: charts, rule-based buy/sell signals from technical indicators, and push alerts to the owner's phone. Runs on the owner's laptop at http://localhost:8080 for now.

## Stack

- Java 25 (LTS), Spring Boot 4.1.x, Maven wrapper (`./mvnw`, Maven 3.9.16)
- Thymeleaf pages, plain CSS in `src/main/resources/static`; no separate frontend build
- PostgreSQL 18 in Docker (`compose.yaml`). `spring-boot-docker-compose` starts it on `spring-boot:run` and stops it on shutdown
- Tests: JUnit 5 + Testcontainers 2 (real Postgres, same image as `compose.yaml`)
- CI: GitHub Actions (`.github/workflows/ci.yml`) runs `./mvnw -B -ntp verify` on every push

## Commands

- Build and test: `./mvnw verify` (Docker must be running)
- Run: `./mvnw spring-boot:run`, then open http://localhost:8080
- Health: http://localhost:8080/actuator/health
- Database for tools like IntelliJ: `localhost:15432`, database/user/password all `stockportfolio`

## Change loop

The owner asks for a change in plain words. Then:

1. Make the change and add or update tests.
2. `./mvnw verify` must pass.
3. Restart the app: stop the running `spring-boot:run`, start it again in the background, wait until `/actuator/health` says UP.
4. Tell the owner what to look at on localhost:8080.
5. Commit and push. CI must go green.

## Conventions

- Root package `io.github.imandevone.stockportfolio`; one package per feature (`watchlist`, `prices`, `signals`, `alerts`), `web` for shared pages.
- Spring Boot 4 names: `spring-boot-starter-webmvc` (the `-web` starter is deprecated), per-module test starters (`spring-boot-starter-webmvc-test`), `@AutoConfigureMockMvc` is in `org.springframework.boot.webmvc.test.autoconfigure`. Testcontainers 2 artifacts are `testcontainers-<module>`, classes are in `org.testcontainers.<module>`.
- Constructor injection.
- Secrets (API keys, ntfy topic) come from env vars or the git-ignored `application-local.yaml`, never from committed files. The repo is public.
- Signals are rules applied to past prices, not predictions. Every signal shown or pushed must say which rule fired and on what numbers.

## Decisions so far

- Price data: Avanza has no official API, so don't build on it. Start with Yahoo Finance (unofficial, free, has `.ST` tickers such as `VOLV-B.ST`) behind a `PriceProvider` interface, so a paid feed can replace it if Yahoo gets unreliable.
- Alerts: ntfy (HTTP POST to a topic, pushes to the owner's phone).
- Indicators: ta4j.

## Roadmap

1. [x] Skeleton: home page, Postgres, CI
2. [ ] Watchlist: add and remove stocks and gold
3. [ ] Prices: scheduled fetch and stored history
4. [ ] Charts: candlesticks, volume, moving averages
5. [ ] Signals: RSI, MACD, moving averages, plus the owner's own thresholds
6. [ ] Alerts: push to phone
7. [ ] Always-on: Dockerfile, run on a home server or small VPS
