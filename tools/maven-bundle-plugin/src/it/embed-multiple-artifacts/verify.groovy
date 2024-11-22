new File(basedir, "target/classes/META-INF/MANIFEST.MF").eachLine() { line ->
    if (line.contains("Embed-Dependency") && !(line.contains("commons-io") && line.contains("commons-compress"))) {
        throw new Exception("Failed to embed both")
    }
}
