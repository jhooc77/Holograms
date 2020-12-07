# Holograms
A Minestom extension that allows easy creation and management of text based Holograms

### Using Holograms

To use Holograms in your extensions, add the Holograms-API module to your build path. Then add Holograms as a dependency in your extension.json file:

```extension.json
"dependencies": [
    "HologramsMinestom"
  ]
```

Hologram creation is made easy with our API. Get a reference to the HologramManager and you're set.

```java
private HologramManager hologramManager;

@Override
public void initialize() {
    HologramPlugin extension = (HologramPlugin) MinecraftServer.getExtensionManager().getExtension("HologramsMinestom");
    this.hologramManager = extension.getHologramManager()
}
```

##### Creating and Modifying Holograms

Once you have the manager reference, you can easily work your way around the APIs offerings:

```java
public void createHologram(String id, Position location, Instance instance) {
    Hologram hologram = new Hologram(id, location, instance);
    hologramManager.addHologram(hologram); // Tells the plugin a new Hologram was added
}
```

Adding lines is easy as well:

```java
public void addTextLine(Hologram hologram, String text) {
    HologramLine line = new TextLine(hologram, text);
    hologram.addLine(line);
}

public void addItemLine(Hologram hologram, ItemStack itemstack) {
    HologramLine line = new ItemLine(hologram, itemstack);
    hologram.addLine(line);
}
```

##### Removing Holograms

You can permanently remove a hologram (incl. persistence) by doing the following:

```java
public void deleteHologram(Hologram hologram) {
    hologramManager.deleteHologram(hologram);
}
```

Or if you want to hide a persistent hologram:

```java
public void hideHologram(Hologram hologram) {
    hologram.despawn();
}
```

And you can show hologram again:

```java
public void showHologram(Hologram hologram) {
    hologram.spawn();
}
```

### In-game commands
The subcommands for this plugin are as follows:

* `/holograms addline <hologramName> <textToAdd>`
* `/holograms create <hologramName> <initialText>`
* `/holograms delete <hologramName>`
* `/holograms info <hologramName>`
* `/holograms insertline <hologramName> <index> <textToAdd>`
* `/holograms list`
* `/holograms move <hologramName> <instance> <x> <y> <z>`
* `/holograms move <hologramName> <x> <y> <z>`
* `/holograms movehere <hologramName>`
* `/holograms near <radius>`
* `/holograms removeline <hologramName> <index>`
* `/holograms refresh`
* `/holograms teleport <hologramName>`
* `/holograms hide <hologramName>`


## License ##
This software is available under the following licenses:

* MIT

Special Thanks To:
-------------

![YourKit-Logo](https://www.yourkit.com/images/yklogo.png)

YourKit supports open source projects with its full-featured Java Profiler. YourKit, LLC is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/) and [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/), innovative and intelligent tools for profiling Java and .NET applications.
