# Istruzioni di Compilazione - Querqy per OpenSearch 3.4.0

## Prerequisiti

### Java 21
OpenSearch 3.x richiede Java 21. Verifica la versione installata:

```bash
java -version
```

Se non hai Java 21, vedi la sezione [Installazione Java 21](#installazione-java-21) sotto.

### Gradle
Il progetto include il Gradle Wrapper, quindi non è necessario installare Gradle separatamente.

---

## Compilazione con IntelliJ IDEA (Consigliato)

IntelliJ IDEA può scaricare automaticamente Java 21 e gestire tutto il processo di build.

### 1. Apri il progetto
- **File → Open** e seleziona la cartella `querqy-opensearch`
- IntelliJ riconoscerà automaticamente il progetto Gradle

### 2. Configura Java 21 (se non lo hai)
IntelliJ può scaricare Java 21 automaticamente:

1. Vai su **File → Project Structure** (oppure `Cmd+;` su macOS, `Ctrl+Alt+Shift+S` su Windows/Linux)
2. Nella sezione **Project**, clicca sul dropdown **SDK**
3. Seleziona **Add SDK → Download JDK**
4. Scegli:
   - **Version:** 21
   - **Vendor:** Eclipse Temurin (consigliato) oppure Amazon Corretto
5. Clicca **Download**
6. Clicca **Apply** e poi **OK**

### 3. Configura Gradle JVM
1. Vai su **File → Settings** (o **Preferences** su macOS)
2. Naviga a **Build, Execution, Deployment → Build Tools → Gradle**
3. In **Gradle JVM** seleziona il JDK 21 appena scaricato
4. Clicca **Apply** e **OK**

### 4. Compila il progetto
**Opzione A - Dalla toolbar Gradle:**
1. Apri il pannello **Gradle** (di solito sulla destra)
2. Naviga a **opensearch-querqy → Tasks → build**
3. Doppio click su **build** (con test) oppure clicca destro e **Run** con `-x test` per saltare i test

**Opzione B - Dal menu:**
- **Build → Build Project** (`Cmd+F9` / `Ctrl+F9`)

**Opzione C - Dal terminale integrato:**
```bash
./gradlew build -x test
```

### 5. Trova l'output
Il plugin compilato si trova in:
```
build/distributions/opensearch-querqy-1.1.os3.4.0.zip
```

---

## Compilazione da Terminale

### 1. Clona il repository
```bash
git clone https://github.com/GiampaoloGabba/querqy-opensearch.git
cd querqy-opensearch
git checkout claude/opensearch-3.4-compatibility-4EFRP
```

### 2. Imposta JAVA_HOME (se necessario)
```bash
# Esempio con Java installato da IntelliJ (macOS)
export JAVA_HOME=~/Library/Java/JavaVirtualMachines/temurin-21/Contents/Home

# Esempio con SDKMAN!
export JAVA_HOME=~/.sdkman/candidates/java/21.0.5-tem
```

### 3. Compila il plugin
```bash
./gradlew build
```

Per saltare i test (compilazione più veloce):
```bash
./gradlew build -x test
```

### 4. Output
Il plugin compilato si trova in:
```
build/distributions/opensearch-querqy-1.1.os3.4.0.zip
```

---

## Installazione Java 21

### Opzione 1: Tramite IntelliJ IDEA (più semplice)
Vedi la sezione [Compilazione con IntelliJ IDEA](#compilazione-con-intellij-idea-consigliato) sopra.

### Opzione 2: SDKMAN! (consigliato per terminale)
```bash
# Installa SDKMAN!
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Installa Java 21
sdk install java 21.0.5-tem

# Verifica
java -version
```

### Opzione 3: Homebrew (macOS)
```bash
brew install openjdk@21
sudo ln -sfn /opt/homebrew/opt/openjdk@21/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-21.jdk
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
```

### Opzione 4: Package Manager (Linux)
**Ubuntu/Debian:**
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

**Fedora:**
```bash
sudo dnf install java-21-openjdk-devel
```

### Opzione 5: Download manuale
Scarica da [Adoptium Temurin](https://adoptium.net/temurin/releases/?version=21) e configura `JAVA_HOME`.

---

## Installazione Plugin su OpenSearch

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

---

## Comandi Gradle Utili

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

---

## Troubleshooting

### Errore: "Could not resolve org.opensearch.gradle:build-tools"
Assicurati di avere connessione Internet. Gradle deve scaricare le dipendenze da Maven Central.

### Errore: "Unsupported class file major version 65"
Stai usando una versione di Java sbagliata. OpenSearch 3.x richiede Java 21.
- Verifica con `java -version`
- In IntelliJ: controlla **Project Structure → Project SDK** e **Settings → Gradle → Gradle JVM**

### Errore di memoria
Aumenta la memoria per Gradle:
```bash
export GRADLE_OPTS="-Xmx2g"
./gradlew build
```

### IntelliJ non trova le classi OpenSearch
1. **File → Invalidate Caches → Invalidate and Restart**
2. Dopo il riavvio, apri il pannello Gradle e clicca su **Reload All Gradle Projects** (icona refresh)

### Gradle sync fallisce in IntelliJ
1. Assicurati che il Gradle JVM sia impostato su Java 21
2. Prova **File → Sync Project with Gradle Files**

---

## Struttura del Plugin

```
opensearch-querqy-1.1.os3.4.0.zip
├── opensearch-querqy-1.1.os3.4.0.jar
├── plugin-descriptor.properties
├── plugin-security.policy
└── (dipendenze)
```

---

## Riferimenti

- [OpenSearch 3.4.0 Release Notes](https://github.com/opensearch-project/opensearch-build/blob/main/release-notes/opensearch-release-notes-3.4.0.md)
- [Querqy Documentation](https://docs.querqy.org/)
- [OpenSearch Plugin Development](https://opensearch.org/docs/latest/developer-documentation/plugins/)
- [Adoptium Temurin JDK](https://adoptium.net/)
