# Induviduell inlämnings uppgift som handlar om Bibliotek backend system

Denna pojektet byggs för att bevisa mina kunskaper inom backend arhiktektur. För att följa enklare genom mitt arbete jag har skapat kommits enligt delar i uppgifter.

Projekten handlar om ett enklare bibliotek system med endast backend delen. Biblioteken har funktionalitet av att skapa böcker, författare samt lån. Jag använder mig av SpringBoot versionen 3.0.5. och Java 21. 

## Hur fungerar programmet

#### 1. Vault & Redis

- Programmet kräver att Vault applikation ska vara i gång innan programmet kan startas
- För att vissa Endpoint ska kunna fungera på rätt sätt Redis server ska vara igång också

#### 2. Efter båda Vault och Redis är i gång, programmet kan startas med kommando:

`./gradlew bootRun`

#### 3. Testa Data bas med: 
http://localhost:8080/h2-console

#### 4. Testa endpoints:
http://localhost:8080/swagger-ui.html

#### 5. Logga in:
- För att kunna använda alla endpoints. Man ska authentizera sig med 
{
    "user": "admin"
    "password": "password"
}
- I server responsen ska genereras ett Token.
- Token som är innanför `" "` skriver in i `Authorize`

---

När Vault och Redis är igång och main funktionen körs med `./gradlew bootRun` pragrammet kan testas. Det gör vi med hjälp av två länkar som är nämnda övan. 

Den första öppnar H2 Databas vilken låter dig som användare se vad som finns i data basen och hur olika tabeller ser ut. Samtidigt den kan användas för att utföra SQL queris.

Den andra länken öppnar swagger some är väldigt enkel api kontroller. Hur fungerar swagger? I bilden nedan ser vi olika anrop som användare kan få göra med kort beskriv, innan man kan göra de man behöver endast loggas in.

<img src="./image.png" width="700">

## Prestand Testing med JMeter

### Innan optimering med cashing

<details>
<summary>Data bas fylld med 10.000 böcker och författare </summary>

|Label       | # Samples | Average | Min | Max | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|------------|-----------|---------|-----|-----|-----------|---------|------------|-----------------|-------------|------------|
|Get Books   | 100       | 68      | 53  | 642 | 60.92     | 0.000%  | 6.45786    | 6545.26         | 0.79        | 1037858.0  |
|Get Authors | 100       | 86      | 70  | 595 | 54.60     | 0.000%  | 6.70466    | 9599.04         | 0.83        | 1466057.4  |
|TOTAL       | 200       | 77      | 53  | 642 | 58.56     | 0.000%  | 12.85512   | 15716.87        | 1.58        | 1251957.7  |
</details>

<details>
<summary>Data bas fylld med 50.000 böcker och författare </summary>

| Label       | # Samples | Average | Min | Max  | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------------|-----------|---------|-----|------|-----------|---------|------------|-----------------|-------------|------------|
| Get Books   | 71        | 314     | 280 | 1461 | 138.31    | 0.000%  | 1.39218    | 7235.61         | 0.17        | 5322040.0  |
| Get Authors | 70        | 409     | 364 | 1104 | 91.42     | 0.000%  | 1.42357    | 10498.85        | 0.18        | 7551993.6  |
| TOTAL       | 141       | 361     | 280 | 1461 | 126.68    | 0.000%  | 2.76476    | 17358.34        | 0.34        | 6429109.2  |
</details>

### Efter optimering av Cashing

<details>
<summary>Data bas fylld med 10.000 böcker och författare | Cacheable implementerad</summary>

|Label  | # Samples | Average | Min | Max | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------|-----------|---------|-----|-----|-----------|---------|------------|-----------------|-------------|------------|
|Login  | 100       | 4       | 1   | 191 | 18.74     | 0.000%  | 47.19207   | 23.31           | 10.37       | 505.7      |
|books  | 100       | 9       | 3   | 505 | 49.79     | 0.000%  | 52.77045   | 131.71          | 16.65       | 2555.7     |
|authors| 100       | 5       | 3   | 17  | 1.94      | 0.000%  | 71.78751   | 84.53           | 22.78       | 1205.7     |
|TOTAL  | 300       | 6       | 1   | 505 | 30.82     | 0.000%  | 141.11007  | 196.01          | 40.10       | 1422.4     |
</details>

<details>
<summary>Data bas fylld med 50.000 böcker och författare | Cacheable implementerad</summary>

|Label  | # Samples | Average | Min | Max  | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------|-----------|---------|-----|------|-----------|---------|------------|-----------------|-------------|------------|
|Login  | 100       | 4       | 1   | 151  | 14.75     | 0.000%  | 5.28402    | 2.61            | 1.16        | 505.7      |
|books  | 100       | 126     | 83  | 1885 | 178.62    | 0.000%  | 5.30842    | 27330.75        | 1.67        | 5272132.7  |
|authors| 100       | 58      | 42  | 317  | 30.08     | 0.000%  | 5.87372    | 9922.14         | 1.86        | 1729786.7  |
|TOTAL  | 300       | 63      | 1   | 1885 | 116.18    | 0.000%  | 15.72327   | 35840.18        | 4.47        | 2334141.7  |
</details>




<details>
<summary>Data bas fylld med 50.000 böcker och författare | Cacheable & Pagable implementerad </summary>

|Label  | # Samples | Average | Min | Max | Std. Dev. | Error % | Throughput | Received KB/sec | Sent KB/sec | Avg. Bytes |
|-------|-----------|---------|-----|-----|-----------|---------|------------|-----------------|-------------|------------|
|Login  | 100       | 4       | 1   | 191 | 18.74     | 0.000%  | 47.19207   | 23.31           | 10.37       | 505.7      |
|books  | 100       | 9       | 3   | 505 | 49.79     | 0.000%  | 52.77045   | 131.71          | 16.65       | 2555.7     |
|authors| 100       | 5       | 3   | 17  | 1.94      | 0.000%  | 71.78751   | 84.53           | 22.78       | 1205.7     |
|TOTAL  | 300       | 6       | 1   | 505 | 30.82     | 0.000%  | 141.11007  | 196.01          | 40.10       | 1422.4     |
</details>



