# Istruzioni di Compilazione - Querqy per OpenSearch 3.4.0

## Prerequisiti

### 1. Java 21
OpenSearch 3.x richiede Java 21. Verifica la versione installata:

```bash
java -version
```

Se non hai Java 21, installalo:

**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

**macOS (Homebrew):**
```bash
brew install openjdk@21
```

**Windows:**
Scarica da [Adoptium](https://adoptium.net/) o [Oracle](https://www.oracle.com/java/technologies/downloads/)

### 2. Gradle
Il progetto include il Gradle Wrapper, quindi non è necessario installare Gradle separatamente.

## Compilazione

### 1. Clona il repository
```bash
git clone https://github.com/GiampaoloGabba/querqy-opensearch.git
cd querqy-opensearch
git checkout claude/opensearch-3.4-compatibility-4EFRP
```

### 2. Compila il plugin
```bash
./gradlew build
```

Per saltare i test (compilazione più veloce):
```bash
./gradlew build -x test
```

### 3. Output
Il plugin compilato si trova in:
```
build/distributions/opensearch-querqy-1.1.os3.4.0.zip
```

## Installazione su OpenSearch

### 1. Installa il plugin
```bash
# Dalla directory di OpenSearch
bin/opensearch-plugin install file:///percorso/assoluto/opensearch-querqy-1.1.os3.4.0.zip
```

### 2. Riavvia OpenSearch
```bash
# Se usi systemd
sudo systemctl restart opensearch

# Se avvii manualmente
./bin/opensearch
```

### 3. Verifica l'installazione
```bash
curl -X GET "localhost:9200/_cat/plugins?v"
```

Dovresti vedere `opensearch-querqy` nella lista.

## Comandi Utili

| Comando | Descrizione |
|---------|-------------|
| `./gradlew build` | Compila e esegue i test |
| `./gradlew build -x test` | Compila senza test |
| `./gradlew clean` | Pulisce la build |
| `./gradlew compileJava` | Solo compilazione Java |
| `./gradlew test` | Esegue solo i test |
| `./gradlew tasks` | Lista tutti i task disponibili |

## Compilazione per Versione Specifica

Se vuoi compilare per una versione diversa di OpenSearch:
```bash
./gradlew build -Dopensearch.version=3.4.0
```

## Troubleshooting

### Errore: "Could not resolve org.opensearch.gradle:build-tools"
Assicurati di avere connessione Internet. Gradle deve scaricare le dipendenze da Maven Central.

### Errore: "Unsupported class file major version"
Stai usando una versione di Java sbagliata. OpenSearch 3.x richiede Java 21.

### Errore di memoria
Aumenta la memoria per Gradle:
```bash
export GRADLE_OPTS="-Xmx2g"
./gradlew build
```

## Struttura del Plugin

```
opensearch-querqy-1.1.os3.4.0.zip
├── opensearch-querqy-1.1.os3.4.0.jar
├── plugin-descriptor.properties
├── plugin-security.policy
└── (dipendenze)
```

## Riferimenti

- [OpenSearch 3.4.0 Release Notes](https://github.com/opensearch-project/opensearch-build/blob/main/release-notes/opensearch-release-notes-3.4.0.md)
- [Querqy Documentation](https://docs.querqy.org/)
- [OpenSearch Plugin Development](https://opensearch.org/docs/latest/developer-documentation/plugins/)
