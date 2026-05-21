#!/usr/bin/env bash
set -euo pipefail

ROOT="$(pwd)"
GRADE="${ROOT}/MoodTownGrade-main"
MENU="${ROOT}/Moodtownmenu-main"
FLAG="${ROOT}/MoodVilleDrapeau-main"

if [ ! -d "$GRADE/src/main/java" ]; then
  echo "Dossier manquant: MoodTownGrade-main/src/main/java"
  exit 1
fi

if [ ! -d "$MENU/src/main/java" ]; then
  echo "Dossier manquant: Moodtownmenu-main/src/main/java"
  exit 1
fi

if [ ! -d "$FLAG/src/main/java" ]; then
  echo "Dossier manquant: MoodVilleDrapeau-main/src/main/java"
  exit 1
fi

echo "== MoodTownSuite : fusion propre =="

mkdir -p src/main/java src/main/resources

# Copie des sources Java.
echo "Copie des sources MoodTownGrade..."
cp -R "$GRADE/src/main/java/"* src/main/java/

echo "Copie des sources MoodTownMenu..."
cp -R "$MENU/src/main/java/"* src/main/java/

echo "Copie des sources MoodTownFlag..."
cp -R "$FLAG/src/main/java/"* src/main/java/

# Ressources : on garde le plugin.yml et config.yml racine déjà fusionnés.
echo "Copie des ressources utiles..."
if [ -d "$GRADE/src/main/resources" ]; then
  find "$GRADE/src/main/resources" -type f ! -name "plugin.yml" ! -name "config.yml" -print0 | while IFS= read -r -d '' file; do
    rel="${file#$GRADE/src/main/resources/}"
    mkdir -p "src/main/resources/$(dirname "$rel")"
    cp "$file" "src/main/resources/$rel"
  done
fi

if [ -d "$MENU/src/main/resources" ]; then
  find "$MENU/src/main/resources" -type f ! -name "plugin.yml" ! -name "config.yml" -print0 | while IFS= read -r -d '' file; do
    rel="${file#$MENU/src/main/resources/}"
    mkdir -p "src/main/resources/$(dirname "$rel")"
    cp "$file" "src/main/resources/$rel"
  done
fi

if [ -d "$FLAG/src/main/resources" ]; then
  find "$FLAG/src/main/resources" -type f ! -name "plugin.yml" ! -name "config.yml" -print0 | while IFS= read -r -d '' file; do
    rel="${file#$FLAG/src/main/resources/}"
    mkdir -p "src/main/resources/$(dirname "$rel")"
    cp "$file" "src/main/resources/$rel"
  done
fi

# Pom propre final.
echo "Création du pom.xml propre..."
cat > pom.xml <<'EOF'
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>fr.moodcraft</groupId>
    <artifactId>MoodTownSuite</artifactId>
    <version>1.0</version>
    <packaging>jar</packaging>

    <name>MoodTownSuite</name>

    <properties>
        <maven.compiler.release>21</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <repositories>
        <repository>
            <id>papermc</id>
            <url>https://repo.papermc.io/repository/maven-public/</url>
        </repository>

        <repository>
            <id>towny</id>
            <url>https://repo.glaremasters.me/repository/towny/</url>
        </repository>

        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>io.papermc.paper</groupId>
            <artifactId>paper-api</artifactId>
            <version>1.21.1-R0.1-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>com.palmergames.bukkit.towny</groupId>
            <artifactId>towny</artifactId>
            <version>0.101.2.0</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>MoodTownSuite</finalName>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <release>21</release>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF

# Suppression des anciens dossiers sources et archives une fois copiés.
echo "Suppression des anciens dossiers sources..."
rm -rf MoodTownGrade-main Moodtownmenu-main MoodVilleDrapeau-main
rm -f MoodTownGrade-main.zip Moodtownmenu-main.zip MoodVilleDrapeau-main.zip

echo "== Fusion terminée =="
echo "Lance maintenant : mvn clean package"
