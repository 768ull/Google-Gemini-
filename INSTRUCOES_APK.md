# Instruções para gerar o APK

Este documento descreve, passo a passo, como transformar este projeto no
arquivo APK instalável (Gemini Native).

## 1. Preparação do ambiente

1. Instale o Android Studio (versão Hedgehog 2023.1.1 ou mais recente). Ele
   já inclui o JDK 17 necessário.
2. Clone o projeto:

   git clone https://github.com/768ull/Google-Gemini-.git
   cd Google-Gemini-

3. Gere o Gradle Wrapper, pois o repositório não inclui o binário:

   gradle wrapper --gradle-version 8.7

   Caso não possua o Gradle instalado, abra a pasta do projeto no Android
   Studio e aceite a correção sugerida pela IDE, que configurará o Gradle
   automaticamente.

## 2. Opção A: APK de depuração (caminho mais simples)

1. Abra o projeto no Android Studio.
2. No menu, selecione Build, depois Build Bundle(s)/APK(s) e, em seguida,
   Build APK(s).
3. Ao término, clique no link "locate" na notificação. O arquivo estará em:

   app/build/outputs/apk/debug/app-debug.apk

Por linha de comando, o equivalente é:

   ./gradlew assembleDebug

O APK de depuração é adequado para uso pessoal e testes, mas não é
recomendado para distribuição.

## 3. Opção B: APK assinado de produção (recomendado)

1. No Android Studio, selecione Build e, em seguida,
   Generate Signed Bundle / APK.
2. Escolha APK e clique em Next.
3. Em Key store path, clique em Create new e preencha os dados para gerar um
   novo repositório de chaves (keystore). Guarde a senha e o arquivo .jks em
   local seguro; sem eles não será possível atualizar o aplicativo no futuro.
4. Selecione o build variant "release", confirme as assinaturas v1 e v2 e
   clique em Finish.
5. O arquivo estará em:

   app/build/outputs/apk/release/app-release.apk

## 4. Instalação no aparelho

1. Ative a opção Instalar apps desconhecidos (ou Fontes desconhecidas) nas
   configurações de segurança do Android.
2. Copie o APK para o dispositivo e toque sobre o arquivo, ou utilize:

   adb install app-release.apk

## Observação

A assinatura de produção é obrigatória somente para distribuir o aplicativo
fora do ambiente de desenvolvimento. Para uso próprio, o APK de depuração da
Opção A já atende plenamente.
