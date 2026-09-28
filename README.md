# Stock Portfolio

Personal web app to follow Swedish stocks and paper gold, with charts, rule-based buy/sell signals and phone alerts. Runs locally.

## Requirements

- JDK 25
- Docker, running

## Run

```sh
./mvnw spring-boot:run
```

Open http://localhost:8080. Postgres starts automatically in Docker.

## Test

```sh
./mvnw verify
```

Not financial advice: signals are rules applied to past prices, not predictions.
