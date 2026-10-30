# Instruções para gerar o APK

Existem duas formas de obter o APK do Gemini Native: a automática, pelo menu
Ações do GitHub, e a manual, pelo Android Studio.

## Forma automática (menu Ações do GitHub)

O repositório possui uma automação que constrói o APK sozinha a cada
atualização do código.

1. Abra o repositório no GitHub.
2. Clique na aba "Ações" (Actions).
3. Clique na execução mais recente de "Build APK".
4. Role até a seção "Artifacts" (Artefatos) e baixe o arquivo
   GeminiNative-APK. Dentro dele está o app-debug.apk.
5. Para construir manualmente a qualquer momento, selecione "Build APK" na
   barra lateral esquerda e clique em "Run workflow".

O build automático leva alguns minutos. O APK de depuração é assinado com a
chave de teste do Gradle e pode ser instalado normalmente em uso pessoal.

## Forma manual (Android Studio)

### 1. Preparação do ambiente

1. Instale o Android Studio (versão Hedgehog 2023.1.1 ou mais recente). Ele
   já inclui o JDK 17 necessário.
2. Clone o projeto:

   git clone https://github.com/768ull/Google-Gemini-.git
   cd Google-Gemini-

3. Gere o Gradle Wrapper, pois o repositório não inclui o binário:

   gradle wrapper --gradle-version 8.7

   Caso não possua o Gradle instalado, abra a pasta do projeto no Android
   Studio e aceite a correção sugerida pela IDE.

### 2. Opção A: APK de depuração (caminho mais simples)

1. Abra o projeto no Android Studio.
2. No menu, selecione Build, depois Build Bundle(s)/APK(s) e, em seguida,
   Build APK(s).
3. O arquivo estará em:

   app/build/outputs/apk/debug/app-debug.apk

Por linha de comando, o equivalente é:

   ./gradlew assembleDebug

### 3. Opção B: APK assinado de produção (recomendado)

1. No Android Studio, selecione Build e, em seguida,
   Generate Signed Bundle / APK.
2. Escolha APK e clique em Next.
3. Em Key store path, clique em Create new e preencha os dados para gerar um
   novo repositório de chaves (keystore). Guarde a senha e o arquivo .jks em
   local seguro.
4. Selecione o build variant "release", confirme as assinaturas v1 e v2 e
   clique em Finish.
5. O arquivo estará em:

   app/build/outputs/apk/release/app-release.apk

## Instalação no aparelho

1. Ative a opção Instalar apps desconhecidos (ou Fontes desconhecidas) nas
   configurações de segurança do Android.
2. Copie o APK para o dispositivo e toque sobre o arquivo, ou utilize:

   adb install app-debug.apk

## Observação

A assinatura de produção é obrigatória somente para distribuir o aplicativo
fora do ambiente de desenvolvimento. Para uso próprio, o APK de depuração já
atende plenamente.
