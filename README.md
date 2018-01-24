# Partly Solid Bicycle rolling over bare bones of LibGDX

  Partly Solid Bicycle rolling over bare bones of LibGDX (or just "Partly Solid Bicycle" or just "PSB") is a game development framework/game engine written in Java. PSB uses [LibGDX](https://github.com/libgdx/libgdx) as platform abstraction layer. PSB tries to be SOLID but it does not try too hard.

  PSB implements [Entity-Component-System (ECS)](https://en.wikipedia.org/wiki/Entity%E2%80%93component%E2%80%93system) pattern.

## Build & run

PSB uses some 3-rd party components included as git submodules so the following commands should be issued before build:

```
git submodule init
git submodule update
```

Android demo project requires Android SDK with build tools of version 23.0.2 so the Android SDK should be installed and the file `local.properties` in root of the project should contain path to Android SDK:

```
sdk.dir=/path/to/sdk
```

*The whole project probably won't build without Android SDK*

When submodules are initialized and Android SDK is installed the demo project is ready to be launched:

```
./gradlew :tools:TP :demo:desktop:run
```

